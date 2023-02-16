package com.example.nano.ui.home

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import com.example.nano.ui.navigation.NanoContentType
import com.example.nano.ui.navigation.NanoNavigationType
import com.example.nano.ui.viewModel.NanoHomeIntent
import com.example.nano.ui.viewModel.NanoHomeUIState
import com.example.nano.ui.viewModel.NanoHomeViewModel

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
    val uiState by homeViewModel.nanoHomeMvi.uiState.collectAsStateWithLifecycle()
    NanoHome(
        uiState = uiState,
        dispatch = homeViewModel.nanoHomeMvi::dispatch,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationType = navigationType,
        closeDetailScreen = closeDetailScreen,
        navigateToDetail = navigateToDetail,
        onFABClicked = onFABClicked
    )
}

@Composable
fun NanoHome(
    uiState: NanoHomeUIState,
    dispatch: (intent: NanoHomeIntent) -> Unit,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: NanoNavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
    onFABClicked: () -> Unit
) {

    if (!uiState.error.isNullOrEmpty()) {
        Toast.makeText(LocalContext.current, uiState.error, Toast.LENGTH_SHORT).show()
        dispatch(NanoHomeIntent.OnMsgShow)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
                TextButton(onClick = { dispatch(NanoHomeIntent.DismissProgress) }) {
                    Text(text = "dismiss")
                }
            }
        }
        AnimatedVisibility(visible = !uiState.loading) {
            TextButton(onClick = { dispatch(NanoHomeIntent.ShowProgress) }) {
                Text(text = "show progress")
            }
        }
    }
}