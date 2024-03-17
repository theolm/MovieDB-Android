package com.gabrielbmoro.moviedb.core.ui.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private interface ModelViewIntent<in UserIntent : Any, ScreenState : Any> {
    suspend fun setup(): ScreenState? {
        return null
    }

    suspend fun execute(intent: UserIntent): ScreenState {
        throw NotImplementedError("Method not implemented")
    }

    fun defaultEmptyState(): ScreenState
}

abstract class ViewModelMVI<in UserIntent : Any, ScreenState : Any> : ViewModel(),
    ModelViewIntent<UserIntent, ScreenState> {

    private val _uiState = MutableStateFlow(this.defaultEmptyState())
    val uiState = _uiState.stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            setup()?.let { state ->
                _uiState.update { state }
            }
        }
    }

    fun accept(intent: UserIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            val state = execute(intent)
            _uiState.update { state }
        }
    }

    protected fun updateState(state: ScreenState) {
        _uiState.update { state }
    }

    protected fun getState(): ScreenState {
        return _uiState.value
    }

    override suspend fun setup(): ScreenState? {
        return null
    }
}