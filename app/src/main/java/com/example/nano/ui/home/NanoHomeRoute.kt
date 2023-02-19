package com.example.nano.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import com.example.nano.ui.navigation.NanoContentType
import com.example.nano.ui.navigation.NanoNavigationType
import com.example.nano.ui.viewModel.NanoHomeEvent
import com.example.nano.ui.viewModel.NanoHomeIntent
import com.example.nano.ui.viewModel.NanoHomeState
import com.example.nano.ui.viewModel.NanoHomeViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun NanoHomeRoute(
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: NanoNavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
    onFABClicked: () -> Unit
) {
    val homeViewModel: NanoHomeViewModel = viewModel()
    val uiState by homeViewModel.mvi.uiState.collectAsStateWithLifecycle()
    NanoHome(
        uiState = uiState,
        uiEvent = homeViewModel.mvi.uiEvent,
        dispatch = homeViewModel.mvi::dispatch,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationType = navigationType,
        closeDetailScreen = closeDetailScreen,
        navigateToDetail = navigateToDetail,
        onFABClicked = onFABClicked
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NanoHome(
    uiState: NanoHomeState,
    uiEvent: Flow<NanoHomeEvent>,
    dispatch: (intent: NanoHomeIntent) -> Unit,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: NanoNavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
    onFABClicked: () -> Unit
) {
    rememberCoroutineScope().launch {
        uiEvent.collect {
            when (it) {
                is NanoHomeEvent.Msg -> {
                    Log.e("NanoHome", it.msg)
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
                TextButton(onClick = { dispatch(NanoHomeIntent.ProgressDismiss) }) {
                    Text(text = "dismiss")
                }
            }
        }
        AnimatedVisibility(visible = !uiState.loading) {
            TextButton(onClick = { dispatch(NanoHomeIntent.Progress) }) {
                Text(text = "show progress")
            }
        }
    }
}