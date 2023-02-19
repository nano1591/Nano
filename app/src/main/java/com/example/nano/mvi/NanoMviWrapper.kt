package com.example.nano.mvi

import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Keep
// vm -> state -> ui
interface UiState

@Keep
// vm -> event -> ui
interface UiEvent

@Keep
// ui -> intent -> vm
interface UiIntent

interface UiIntentDelegate<S : UiState> {
    fun invoke(oldState: S): S
}

interface NanoMvi<S : UiState, E : UiEvent, I : UiIntent> {
    val uiState: StateFlow<S>
    val uiEvent: Flow<E>
    fun dispatch(intent: I)
}

class NanoMviWrapper<S : UiState, E : UiEvent, I : UiIntent>(
    override val uiState: StateFlow<S>,
    override val uiEvent: Flow<E>,
    private val uiIntent: MutableSharedFlow<I>,
    private val parentScope: CoroutineScope
) : NanoMvi<S, E, I> {
    override fun dispatch(intent: I) {
        parentScope.launch { uiIntent.emit(intent) }
    }
}