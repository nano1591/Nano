package com.example.nano.mvi

import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Keep
// UI状态帧
interface UiState

@Keep
// 单次UI事件
interface UiIntent<S : UiState> {
    suspend fun invoke(prev: S): S
}

interface NanoMvi<S : UiState, I : UiIntent<S>> {
    val uiState: StateFlow<S>
    fun dispatch(intent: I)
}

class NanoMviWrapper<S : UiState, I : UiIntent<S>>(
    initialState: S,
    private val parentScope: CoroutineScope,
    started: SharingStarted = SharingStarted.Eagerly
) : NanoMvi<S, I> {
    private val uiIntent = MutableSharedFlow<I>()
    override lateinit var uiState: StateFlow<S>

    override fun dispatch(intent: I) {
        parentScope.launch { uiIntent.emit(intent) }
    }

    init {
        uiState = uiIntent
            .map { it.invoke(this.uiState.value) }
            .flowOn(Dispatchers.IO)
            .stateIn(parentScope, started, initialState)
    }
}


//inline fun <S: UiState, reified I: UiIntent<S>> createNanoMvi(
//    initialState: S,
//    parentScope: CoroutineScope,
//    started: SharingStarted = SharingStarted.Eagerly
//): NanoMvi<S, I> {
//    val uiIntent = MutableSharedFlow<I>()
//    val uiState = uiIntent
//        .map { it.invoke() }
//        .flowOn(Dispatchers.IO)
//        .stateIn(parentScope, started, initialState)
//
//    return NanoMviWrapper(uiState, uiIntent, parentScope)
//}
//
//private class NanoMviWrapper<S: UiState, I: UiIntent<S>>(
//    override val uiState: StateFlow<S>,
//    private val uiIntent: MutableSharedFlow<I>,
//    private val parentScope: CoroutineScope
//): NanoMvi<S, I> {
//    override fun dispatch(intent: I) {
//        parentScope.launch { uiIntent.emit(intent) }
//    }
//}
