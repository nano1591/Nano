package com.example.nano.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nano.data.Email
import com.example.nano.mvi.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NanoHomeViewModel : ViewModel() {
    private val _nanoHomeIntent = MutableSharedFlow<NanoHomeIntent>()

    val uiState =
        _nanoHomeIntent
            .toNewsStateFlow() // 将事件流转换成状态流
            .flowOn(Dispatchers.IO) // 异步地进行变换操作
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                NanoHomeUIState(error = "0000")
            ) // 将流转换成共享流以供界面订阅

    fun dispatch(intent: NanoHomeIntent) {
        viewModelScope.launch { _nanoHomeIntent.emit(intent) }
    }

    @OptIn(FlowPreview::class)
    private fun Flow<NanoHomeIntent>.toNewsStateFlow(): Flow<NanoHomeUIState> = merge(
        // 加载首页事件处理
        filterIsInstance<NanoHomeIntent.ShowMsg>()
            .flatMapConcat {
                it.show(it)
            },
        filterIsInstance<NanoHomeIntent.OnMsgShow>()
            .flatMapConcat {
                it.dismissMsg()
            },
    )

    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NanoHomeViewModel() as T
            }
        }
    }
}

data class NanoHomeUIState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
) : UIState

sealed class NanoHomeIntent {
    data class ShowMsg(val msg: String) : NanoHomeIntent() {
        fun show(intent: ShowMsg) = flowOf(NanoHomeUIState().copy(error = intent.msg))
    }

    object OnMsgShow : NanoHomeIntent() {
        fun dismissMsg() = flowOf(NanoHomeUIState().copy(error = null))
    }
}