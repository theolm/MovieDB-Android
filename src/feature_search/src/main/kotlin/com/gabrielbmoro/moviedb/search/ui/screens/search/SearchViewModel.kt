package com.gabrielbmoro.moviedb.search.ui.screens.search

import androidx.compose.ui.text.input.TextFieldValue
import com.gabrielbmoro.moviedb.core.ui.mvi.ViewModelMVI
import com.gabrielbmoro.moviedb.domain.usecases.SearchMovieUseCase

class SearchViewModel(
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModelMVI<SearchUserIntent, SearchUIState>() {
    override fun defaultEmptyState() = SearchUIState(TextFieldValue(""))

    override suspend fun execute(intent: SearchUserIntent): SearchUIState {
        return when (intent) {
            is SearchUserIntent.SearchBy -> {
                getState().copy(
                    results = searchMovieUseCase.execute(
                        SearchMovieUseCase.Params(
                            query = intent.query.text
                        )
                    )
                )
            }

            is SearchUserIntent.ClearSearchField -> {
                getState().copy(
                    searchQuery = TextFieldValue("")
                )
            }

            is SearchUserIntent.SearchInputFieldChanged -> {
                getState().copy(
                    searchQuery = intent.query
                )
            }
        }
    }
}