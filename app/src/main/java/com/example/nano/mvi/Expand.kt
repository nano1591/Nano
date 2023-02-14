package com.example.nano.mvi

import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

fun <STATE : UIState, SINGLE_EVENT : UISingleEvent> ViewModel.containers(
    initialState: STATE
): Lazy<MutableContainer<STATE, SINGLE_EVENT>> {
    return ContainerLazy(initialState, viewModelScope)
}

class ContainerLazy<STATE : UIState, SINGLE_EVENT : UISingleEvent>(
    initialState: STATE,
    parentScope: CoroutineScope
) : Lazy<MutableContainer<STATE, SINGLE_EVENT>> {
    private var cached: MutableContainer<STATE, SINGLE_EVENT>? = null

    override val value: MutableContainer<STATE, SINGLE_EVENT> = cached
        ?: NanoContainer<STATE, SINGLE_EVENT>(
            initialState = initialState,
            parentScope = parentScope
        ).also { cached = it }

    override fun isInitialized(): Boolean = cached != null
}


fun <T : UIState> Flow<T>.collectState(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    action: StateCollector<T>.() -> Unit
) {
    StateCollector(this@collectState, lifecycleOwner, state).action()
}

class StateCollector<T : UIState>(
    private val flow: Flow<T>,
    private val lifecycleOwner: LifecycleOwner,
    private val state: Lifecycle.State
) {
    fun <P> collectPartial(
        prop1: KProperty1<T, P>,
        action: (P) -> Unit
    ) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(state) {
                flow.map { prop1.get(it) }
                    .distinctUntilChanged()
                    .collect(action)
            }
        }
    }
}

fun <T : UISingleEvent> Flow<T>.collectSingleEvent(
    lifecycleOwner: LifecycleOwner,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    action: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state) {
            this@collectSingleEvent.collect(action)
        }
    }
}