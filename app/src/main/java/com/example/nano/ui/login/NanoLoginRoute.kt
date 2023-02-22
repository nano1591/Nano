package com.example.nano.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import com.example.nano.ui.NanoApplication
import com.example.nano.ui.navigation.NanoContentType

@Composable
fun NanoLoginRoute(
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>
) {
    val userViewModel: NanoUserViewModel = viewModel(
        factory = NanoUserViewModel.provideFactory(NanoApplication.accountRepository)
    )
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()
    NanoLogin(
        uiState = uiState,
        register = userViewModel::register,
        logout = userViewModel::logout
    )
}

@Composable
fun NanoLogin(
    uiState: NanoUserUiState,
    register: () -> Unit,
    logout: () -> Unit
) {
    AnimatedVisibility(visible = !uiState.isLogin) {
        TextButton(onClick = register) {
            Text(text = "register")
        }
    }
    AnimatedVisibility(visible = uiState.isLogin) {
        TextButton(onClick = logout) {
            Text(text = "hello")
        }
    }
}