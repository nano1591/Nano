package com.example.nano.ui.viewModel

import androidx.lifecycle.ViewModel
import com.example.nano.data.Email
import com.example.nano.mvi.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

class NanoHomeViewModel : ViewModel() {
    val mvi by nanoMvi<NanoHomeState, NanoHomeEvent, NanoHomeIntent, NanoHomeIntentDelegate>(
        initialState = NanoHomeState.initial,
        delegateIntent = { delegateIntent() }
    ) {
        return@nanoMvi when (it) {
            is NanoHomeIntentDelegate.Progress.Dismiss -> NanoHomeEvent.Msg("关闭")
            else -> null
        }
    }

    @OptIn(FlowPreview::class)
    private fun Flow<NanoHomeIntent>.delegateIntent(): Flow<NanoHomeIntentDelegate> = merge(
        filterIsInstance<NanoHomeIntent.Progress>().flatMapConcat {
            // Do the processing here
            // intent -> partialChange
            flowOf(NanoHomeIntentDelegate.Progress.Loading)
        },
        filterIsInstance<NanoHomeIntent.ProgressDismiss>().flatMapConcat {
            flowOf(NanoHomeIntentDelegate.Progress.Dismiss)
        }
    )
}

data class NanoHomeState(
    val emails: List<Email> = emptyList(),
    val selectedEmail: Email? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
) : UiState {
    companion object {
        val initial = NanoHomeState()
    }
}

sealed class NanoHomeIntent : UiIntent {
    object Progress : NanoHomeIntent()
    object ProgressDismiss : NanoHomeIntent()

}

sealed class NanoHomeEvent : UiEvent {
    data class Msg(val msg: String) : NanoHomeEvent()
}

sealed class NanoHomeIntentDelegate : UiIntentDelegate<NanoHomeState> {
    sealed class Progress : NanoHomeIntentDelegate() {
        override fun invoke(oldState: NanoHomeState): NanoHomeState = when (this) {
            is Loading -> oldState.copy(loading = true)
            is Dismiss -> oldState.copy(loading = false)
        }

        object Loading : Progress()
        object Dismiss : Progress()
    }
}
