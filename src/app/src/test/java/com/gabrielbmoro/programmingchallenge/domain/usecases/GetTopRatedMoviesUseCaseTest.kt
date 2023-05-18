package com.gabrielbmoro.programmingchallenge.domain.usecases

import com.gabrielbmoro.programmingchallenge.domain.model.DataOrException
import com.gabrielbmoro.programmingchallenge.domain.model.Page
import com.gabrielbmoro.programmingchallenge.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetTopRatedMoviesUseCaseTest {

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetTopRatedMoviesUseCase

    @Before
    fun before() {
        repository = mockk()
        useCase = GetTopRatedMoviesUseCase(repository)
    }

    @Test
    fun `should be able to get all top rated movies`() {
        // arrange
        val topRatedMovies = Page.mockPageWithWhiteDragonMovieOnly()

        coEvery { repository.getTopRatedMovies(1) }.returns(
            DataOrException(topRatedMovies)
        )

        runTest {
            // act
            useCase(1)

            // assert
            coVerify(exactly = 1) { repository.getTopRatedMovies(1) }
        }
    }
}