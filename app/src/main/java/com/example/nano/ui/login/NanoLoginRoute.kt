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
import com.example.nano.ui.utils.TextFieldState
import com.example.nano.ui.utils.Validators
import com.example.nano.ui.utils.slideInBottomEnd
import com.example.nano.ui.utils.slideOutBottomEnd
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@SuppressLint("RememberReturnType")
@Composable
fun NanoLoginRoute(
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>
) {
    val userViewModel: NanoUserViewModel = viewModel()
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()

    val showRegister = remember {
        mutableStateOf(false)
    }

    NanoLoginPageRoute(
        contentType = contentType,
        displayFeatures = displayFeatures
    ) {
        NanoLoginContext(
            uiState = uiState,
            toRegister = { showRegister.value = true },
            login = userViewModel::login,
        )
    }

    AnimatedVisibility(
        visible = showRegister.value,
        enter = slideInBottomEnd(),
        exit = slideOutBottomEnd()
    ) {
        NanoRegisterPageRoute(
            contentType = contentType,
            register = userViewModel::register
        ) {
            showRegister.value = false
        }
    }
}

@Composable
fun NanoRegisterPageRoute(
    contentType: NanoContentType,
    register: (String, String, String, String) -> Unit,
    goBack: () -> Unit
) {
    val acct = remember {
        TextFieldState(
            "",
            Validators()
                .required()
                .minLength(6)
                .maxLength(12)
                .word()
                .merge()
        )
    }
    val name = remember {
        TextFieldState(
            "",
            Validators()
                .required()
                .minLength(2)
                .maxLength(12)
                .word()
                .merge()
        )
    }
    val pwd = remember {
        TextFieldState(
            "",
            Validators()
                .required()
                .minLength(6)
                .maxLength(16)
                .word()
                .merge()
        )
    }
    val confirmPwd = remember {
        TextFieldState("", Validators().equal(pwd).merge())
    }

    // TODO 点击×的时候，检查有没有输入值，有的话提示丢弃弹窗
    if (contentType == NanoContentType.SINGLE_PANE) {
        NanoRegisterFullDialog(
            acct = acct,
            name = name,
            pwd = pwd,
            confirmPwd = confirmPwd,
            register = register,
            goBack = goBack
        )
    } else {
        NanoRegisterDialog(
            acct = acct,
            name = name,
            pwd = pwd,
            confirmPwd = confirmPwd,
            register = register,
            goBack = goBack
        )
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
