package com.gabrielbmoro.moviedb.search.ui.screens.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.text.input.TextFieldValue
import com.gabrielbmoro.moviedb.repository.model.Movie
import com.gabrielbmoro.moviedb.search.domain.SearchMovieUseCase
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var searchMovieUseCase: SearchMovieUseCase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        searchMovieUseCase = mockk()
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should be able to search for a movie - founded movie`() = runTest {
        // arrange
        val resultFlow: Flow<List<Movie>> = flow {
            emit(listOf(Movie.mockChuckNorrisVsVandammeMovie()))
        }
        every { searchMovieUseCase(any()) }.returns(resultFlow)
        val searchQuery = TextFieldValue("Chuck Nor")

        val viewModel = SearchViewModel(searchMovieUseCase)

        // act
        viewModel.onSearchQueryChanged(searchQuery)

        delay(500L)

        // assert
        Truth.assertThat(viewModel.uiState.value.results)
            .contains(Movie.mockChuckNorrisVsVandammeMovie())
        Truth.assertThat(viewModel.uiState.value.searchQuery).isEqualTo(
            searchQuery
        )
    }

    @Test
    fun `should be able to search for a movie - empty list`() = runTest {
        // arrange
        val resultFlow: Flow<List<Movie>> = flow { emit(emptyList()) }
        every { searchMovieUseCase(any()) }.returns(resultFlow)
        val searchQuery = TextFieldValue("Ice ag")

        val viewModel = SearchViewModel(searchMovieUseCase)

        // act
        viewModel.onSearchQueryChanged(searchQuery)

        delay(500L)

        // assert
        Truth.assertThat(viewModel.uiState.value.results).isEmpty()
        Truth.assertThat(viewModel.uiState.value.searchQuery).isEqualTo(
            searchQuery
        )
    }

    @Test
    fun `should be able to reset the search field - empty search`() = runTest {
        // arrange
        val resultFlow: Flow<List<Movie>> = flow { emit(emptyList()) }
        every { searchMovieUseCase(any()) }.returns(resultFlow)
        val viewModel = SearchViewModel(searchMovieUseCase)
        viewModel.onSearchQueryChanged(TextFieldValue("Test"))
        delay(500)

        // act
        viewModel.onClearSearchQuery()
        delay(500)

        // assert
        Truth.assertThat(viewModel.uiState.value.searchQuery.text).isEmpty()
        Truth.assertThat(viewModel.uiState.value.results).isNull()
    }
}