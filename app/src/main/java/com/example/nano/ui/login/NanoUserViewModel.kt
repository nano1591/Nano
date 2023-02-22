package com.example.nano.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nano.data.repository.AccountRepository
import com.example.nano.ui.utils.emitData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NanoUserViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NanoUserUiState())
    val uiState: StateFlow<NanoUserUiState> = _uiState

    init {
        viewModelScope.launch {
            accountRepository.isLogin.collect {
                _uiState.emitData {
                    copy(isLogin = it)
                }
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            accountRepository.register(
                email = "email",
                name = "name",
                pwd = "pwd",
                avatar = "avatar"
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            accountRepository.logout()
        }
    }

    companion object {
        fun provideFactory(
            accountRepository: AccountRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NanoUserViewModel(accountRepository) as T
            }
        }
    }
}

data class NanoUserUiState(
    val isLogin: Boolean = false,
    val loading: Boolean = false
)