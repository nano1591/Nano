package com.example.nano.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nano.data.dao.Api
import com.example.nano.data.dao.DB
import com.example.nano.data.dao.SP
import com.example.nano.ui.utils.emitData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NanoUserUiState(
    val isLogin: Boolean = false,
    val loading: Boolean = false,
    val userAcct: String = "",
    val userPwd: String = ""
)

@HiltViewModel
class NanoUserViewModel @Inject constructor(
    private val api: Api,
    private val db: DB,
    private val sp: SP
) : ViewModel() {
    private val _uiState = MutableStateFlow(NanoUserUiState())
    val uiState: StateFlow<NanoUserUiState> = _uiState

    init {
        viewModelScope.launch {
            sp.token.collect {
                _uiState.emitData {
                    copy(isLogin = it.isNotEmpty())
                }
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            api.register(
                acct = "email",
                name = "name",
                pwd = "pwd",
                avatar = "avatar"
            )
        }
    }

    fun login(acct: String, pwd: String) {
        viewModelScope.launch {
            api.login(acct, pwd)
        }
    }
}