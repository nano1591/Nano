package com.example.nano.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nano.data.Email
import com.example.nano.mvi.NanoMviWrapper
import com.example.nano.mvi.UiIntent
import com.example.nano.mvi.UiState
import kotlinx.coroutines.flow.SharingStarted

class NanoHomeViewModel : ViewModel() {
    val nanoHomeMvi = NanoMviWrapper<NanoHomeUIState, NanoHomeIntent>(
        NanoHomeUIState(error = "0000", loading = true),
        viewModelScope,
        SharingStarted.Eagerly
    )
}

data class NanoHomeUIState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
) : UiState

sealed class NanoHomeIntent : UiIntent<NanoHomeUIState> {
    data class ShowMsg(val msg: String) : NanoHomeIntent() {
        override suspend fun invoke(prev: NanoHomeUIState): NanoHomeUIState {
            return prev.copy(error = this.msg)
        }
    }

    object OnMsgShow : NanoHomeIntent() {
        override suspend fun invoke(prev: NanoHomeUIState): NanoHomeUIState {
            return prev.copy(error = null)
        }
    }

    object DismissProgress : NanoHomeIntent() {
        override suspend fun invoke(prev: NanoHomeUIState): NanoHomeUIState {
            return prev.copy(loading = false)
        }
    }

    object ShowProgress : NanoHomeIntent() {
        override suspend fun invoke(prev: NanoHomeUIState): NanoHomeUIState {
            return prev.copy(loading = true)
        }
    }
}
