package com.example.nano.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

fun <S : UiState, E : UiEvent, I : UiIntent, D : UiIntentDelegate<S>> ViewModel.nanoMvi(
    initialState: S,
    delegateIntent: Flow<I>.() -> Flow<D>,
    started: SharingStarted = SharingStarted.Eagerly,
    filterUiEvent: (p: D) -> E?
): Lazy<NanoMvi<S, E, I>> {
    return NanoMviDelegate(initialState, delegateIntent, filterUiEvent, viewModelScope, started)
}

class NanoMviDelegate<S : UiState, E : UiEvent, I : UiIntent, D : UiIntentDelegate<S>>(
    initialState: S,
    delegateIntent: Flow<I>.() -> Flow<D>,
    private val filterUiEvent: (p: D) -> E?,
    parentScope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly
) : Lazy<NanoMvi<S, E, I>> {
    private val _uiIntent = MutableSharedFlow<I>()
    private val _uiEvent = Channel<E>()

    private var cached: NanoMvi<S, E, I>? = null

    override val value: NanoMvi<S, E, I> = cached ?: NanoMviWrapper(
        uiState = _uiIntent
            .delegateIntent() // intent -> delegate
            .onEach { p -> filterUiEvent(p)?.let { _uiEvent.send(it) } } // delegate(event) -> event
            .scan(initialState) { oldState, delegate -> delegate.invoke(oldState) } // delegate -> state
            .flowOn(Dispatchers.IO)
            .stateIn(parentScope, started, initialState),
        uiEvent = _uiEvent.receiveAsFlow(),
        uiIntent = _uiIntent,
        parentScope = parentScope
    ).also { cached = it }

    override fun isInitialized(): Boolean = cached != null
}