package com.example.nano.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.nano.data.Email
import com.example.nano.mvi.Container
import com.example.nano.mvi.UISingleEvent
import com.example.nano.mvi.UIState
import com.example.nano.mvi.containers

class NanoHomeViewModel : ViewModel() {
    private val _container by containers<NanoHomeUIState, NanoHomeEvent>(NanoHomeUIState())

    val container: Container<NanoHomeUIState, NanoHomeEvent> = _container

    fun updateLoadingMode(mode: Boolean) {
        _container.updateState { copy(loading = mode) }
    }
}

data class NanoHomeUIState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
) : UIState

sealed class NanoHomeEvent : UISingleEvent {
    data class LoadingEvent(val mode: Boolean) : NanoHomeEvent()
}

