package com.example.nano.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nano.data.model.Email
import com.example.nano.data.repository.EmailRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class NanoHomeState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
) {
    companion object {
        val initial = NanoHomeState()
    }
}

class NanoHomeViewModel(
    private val emailRepository: EmailRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NanoHomeState.initial)
    val uiState: StateFlow<NanoHomeState> = _uiState

    init {
        initial()
    }

    @OptIn(FlowPreview::class)
    private fun initial() {
        viewModelScope.launch {
            _uiState.emit(_uiState.value.copy(loading = true))
            emailRepository.allEmailFromDB().collect {
                _uiState.emit(_uiState.value.copy(emails = it.emailList, loading = false))
            }
//            flowOf(emailRepository.allEmailFromDB())
//                .flattenMerge()
//                .transformWhile {
//                    emit(it.emailList)
//                    !it.isLocalData
//                }
//                .flowOn(Dispatchers.IO)
//                .catch{err ->
//                    _uiState.emit(_uiState.value.copy(loading = false, error = err.toString()))
//                }
//                .collect {
//                    _uiState.emit(_uiState.value.copy(emails = it, loading = false))
//                }
        }
    }

    fun add(email: Email) {
        viewModelScope.launch {
            emailRepository.add(email)
        }
    }

    fun delete(email: Email) {
        viewModelScope.launch {
            emailRepository.delete(email)
        }
    }

    fun view(email: Email) {
        viewModelScope.launch {
            emailRepository.add(email.copy(viewAt = Date()))
        }
    }

    fun onMsgShowed() {
        viewModelScope.launch { _uiState.emit(_uiState.value.copy(error = "")) }
    }

    companion object {
        fun provideFactory(
            emailRepository: EmailRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NanoHomeViewModel(emailRepository) as T
            }
        }
    }
}
