package com.gabrielbmoro.moviedb.details.domain.usecases

import com.gabrielbmoro.moviedb.repository.MoviesRepository
import com.gabrielbmoro.moviedb.repository.model.VideoStream
import com.google.common.truth.Truth
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetTrailersUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetTrailersUseCase

    @Before
    fun before() {
        repository = mockk()
        useCase = GetTrailersUseCase(repository)
    }

    @Test
    fun `should be able to get a valid trailer - existing valid trailer`() {
        runTest {
            // arrange
            val movieId = 123L
            every { repository.getVideoStreams(movieId) }.returns(
                flow {
                    emit(
                        listOf(
                            VideoStream.mockVideoStreamNotTrailer(),
                            VideoStream.mockVideoStreamTrailer(),
                            VideoStream.mockVideoStreamNotYouTube()
                        )
                    )
                }
            )

            // act
            val result = useCase(movieId).firstOrNull()

            // assert
            Truth.assertThat(result).isEqualTo(VideoStream.mockVideoStreamTrailer())
        }
    }

    @Test
    fun `should not be able to get a valid trailer - no valid youtube trailer`() {
        runTest {
            // arrange
            val movieId = 123L
            every { repository.getVideoStreams(movieId) }.returns(
                flow {
                    emit(
                        listOf(
                            VideoStream.mockVideoStreamNotYouTube()
                        )
                    )
                }
            )

            // act
            val result = useCase(movieId).firstOrNull()

            // assert
            Truth.assertThat(result).isNull()
        }
    }

    @Test
    fun `should not be able to get a valid trailer - no valid trailer`() {
        runTest {
            // arrange
            val movieId = 123L
            every { repository.getVideoStreams(movieId) }.returns(
                flow {
                    emit(
                        listOf(
                            VideoStream.mockVideoStreamNotTrailer()
                        )
                    )
                }
            )
            // act
            val result = useCase(movieId).firstOrNull()

            // assert
            Truth.assertThat(result).isNull()
        }
    }

    @Test
    fun `should be able to get a valid trailer - passing one time by repository`() {
        runTest {
            // arrange
            val movieId = 123L
            every { repository.getVideoStreams(movieId) }.returns(
                flow {
                    emit(
                        listOf(
                            VideoStream.mockVideoStreamNotTrailer(),
                            VideoStream.mockVideoStreamTrailer(),
                            VideoStream.mockVideoStreamNotYouTube()
                        )
                    )
                }
            )

            // act
            useCase(movieId)

            // assert
            coVerify(exactly = 1) { repository.getVideoStreams(movieId) }
        }
    }
}
