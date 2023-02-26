package com.example.nano.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nano.data.Email
import com.example.nano.data.dao.Api
import com.example.nano.data.dao.DB
import com.example.nano.data.dao.SP
import com.example.nano.ui.utils.emitData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class NanoHomeState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class NanoHomeViewModel @Inject constructor(
    private val api: Api,
    private val db: DB,
    private val sp: SP
) : ViewModel() {
    private val _uiState = MutableStateFlow(NanoHomeState())
    val uiState: StateFlow<NanoHomeState> = _uiState

    init {
        initial()
    }

    @OptIn(FlowPreview::class)
    private fun initial() {
        viewModelScope.launch {
            _uiState.emit(_uiState.value.copy(loading = true))
            db.email().getAll().collect {
                _uiState.emitData { copy(emails = it, loading = false) }
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
            db.email().insert(email)
        }
    }

    fun delete(email: Email) {
        viewModelScope.launch {
            db.email().delete(email)
        }
    }

    fun view(email: Email) {
        viewModelScope.launch {
            db.email().insert(email.copy(viewAt = Date()))
        }
    }

    fun onMsgShowed() {
        viewModelScope.launch { _uiState.emitData { copy(error = "") } }
    }
}
