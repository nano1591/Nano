package com.example.nano.ui.login

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import com.example.nano.R
import com.example.nano.ui.navigation.NanoContentType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun NanoLoginRoute(
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>
) {
    val userViewModel: NanoUserViewModel = viewModel()
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()

    val showRegisterCard = remember {
        mutableStateOf(false)
    }

    NanoLoginPageRoute(
        contentType = contentType,
        displayFeatures = displayFeatures
    ) {
        NanoLoginContext(
            uiState = uiState,
            toRegister = { showRegisterCard.value = true },
            login = userViewModel::login,
        )
    }

    if (showRegisterCard.value) {
        ModalBottomSheet(
            onDismissRequest = { showRegisterCard.value = false },
            modifier = Modifier.padding(8.dp)
        ) {
            NanoRegisterRoute(
                close = { showRegisterCard.value = false },
                register = userViewModel::register
            )
        }
    }
}


@Composable
fun NanoLoginPageRoute(
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    context: @Composable () -> Unit
) {
    if (contentType == NanoContentType.DUAL_PANE) {
        NanoLoginTwoPane(
            displayFeatures = displayFeatures,
            context = context
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.nano),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .padding(top = 72.dp)
                    .sizeIn(maxWidth = 200.dp)
                    .align(Alignment.TopCenter)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                context()
            }
            Text(
                text = "By Nano.",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun NanoLoginTwoPane(
    displayFeatures: List<DisplayFeature>,
    context: @Composable () -> Unit
) {
    TwoPane(
        first = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nano),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier.fillMaxWidth(0.618F)
                )
            }
        },
        second = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 64.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name).uppercase(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                context()
            }
        },
        strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
        displayFeatures = displayFeatures,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
}
