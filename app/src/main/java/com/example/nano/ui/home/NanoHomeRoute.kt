package com.example.nano.ui.home

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    val homeViewModel: NanoHomeViewModel = viewModel(
        factory = NanoHomeViewModel.provideFactory()
    )
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    NanoHome(
        uiState = uiState,
        dispatch = homeViewModel::dispatch,
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
    TextButton(onClick = { dispatch(NanoHomeIntent.ShowMsg("1234")) }) {
        Text(text = "show")
    }

    if (!uiState.error.isNullOrEmpty()) {
        Toast.makeText(LocalContext.current, uiState.error, Toast.LENGTH_SHORT).show()
        dispatch(NanoHomeIntent.OnMsgShow)
    }
}