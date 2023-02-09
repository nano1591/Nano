package com.example.nano.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.nano.data.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NanoHomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NanoHomeUIState())
    val uiState: StateFlow<NanoHomeUIState> = _uiState
}

data class NanoHomeUIState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
