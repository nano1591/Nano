package com.example.nano.mvi

import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Keep
// UI状态帧
interface UIState

@Keep
// 单次UI事件
interface UISingleEvent

/**
 * 状态容器。包含UI状态和单次事件，如果不包含单词事件，则使用【Nothing】
 */
interface Container<STATE : UIState, SINGLE_EVENT : UISingleEvent> {
    // ui状态流
    val uiStateFlow: StateFlow<STATE>

    // 单次事件流
    val singleEventFlow: Flow<SINGLE_EVENT>
}

/**
 * 对内提供的可修改容器
 */
interface MutableContainer<STATE : UIState, SINGLE_EVENT : UISingleEvent> :
    Container<STATE, SINGLE_EVENT> {
    // 更新状态
    fun updateState(action: STATE.() -> STATE)

    // 发送事件
    fun dispatchEvent(event: SINGLE_EVENT)
}

internal class NanoContainer<STATE : UIState, SINGLE_EVENT : UISingleEvent>(
    initialState: STATE,
    private val parentScope: CoroutineScope
) : MutableContainer<STATE, SINGLE_EVENT> {
    private val _internalStateFlow = MutableStateFlow(initialState)

    private val _internalSingleEventSharedFlow = MutableSharedFlow<SINGLE_EVENT>()

    override val uiStateFlow: StateFlow<STATE>
        get() = _internalStateFlow

    override val singleEventFlow: Flow<SINGLE_EVENT>
        get() = _internalSingleEventSharedFlow

    override fun updateState(action: STATE.() -> STATE) {
        _internalStateFlow.update { action(_internalStateFlow.value) }
    }

    override fun dispatchEvent(event: SINGLE_EVENT) {
        parentScope.launch {
            _internalSingleEventSharedFlow.emit(event)
        }
    }
}

