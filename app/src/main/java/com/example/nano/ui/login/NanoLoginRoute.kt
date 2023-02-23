package com.example.nano.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import com.example.nano.R
import com.example.nano.ui.NanoApplication
import com.example.nano.ui.components.PasswordTextField
import com.example.nano.ui.navigation.NanoContentType
import com.example.nano.ui.utils.PasswordState
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane

@Composable
fun NanoLoginRoute(
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>
) {
    val userViewModel: NanoUserViewModel = viewModel(
        factory = NanoUserViewModel.provideFactory(NanoApplication.accountRepository)
    )
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()

    if (contentType == NanoContentType.DUAL_PANE) {
        NanoLoginTwoPane(
            displayFeatures = displayFeatures
        ) {
            NanoLogin(
                uiState = uiState,
                register = userViewModel::register,
                login = userViewModel::logout
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1F))
            Image(
                painter = painterResource(id = R.drawable.nano),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.fillMaxWidth(0.369F)
            )
            Spacer(modifier = Modifier.weight(1F))
            Column(
                modifier = Modifier
                    .weight(3F)
                    .fillMaxHeight()
                    .sizeIn(maxWidth = 400.dp)
            ) {
                NanoLogin(
                    uiState = uiState,
                    register = userViewModel::register,
                    login = userViewModel::logout
                )
            }
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = "By Nano.",
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun NanoLoginTwoPane(
    displayFeatures: List<DisplayFeature>,
    content: @Composable () -> Unit
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
                    .sizeIn(maxWidth = 400.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name).uppercase(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                content()
            }
        },
        strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
        displayFeatures = displayFeatures,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NanoLogin(
    uiState: NanoUserUiState,
    register: () -> Unit,
    login: () -> Unit
) {
    var acct by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(uiState.userAcct, TextRange(6, 15)))
    }
    val passwordFocusRequest = remember { FocusRequester() }
    OutlinedTextField(
        value = acct,
        onValueChange = { acct = it },
        label = { Text(text = stringResource(id = R.string.user_account)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    val passwordState = remember { PasswordState() }
    PasswordTextField(
        label = stringResource(id = R.string.user_password),
        passwordState = passwordState,
        imeAction = ImeAction.Next,
        modifier = Modifier.focusRequester(passwordFocusRequest)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(
            onClick = { register() }, modifier = Modifier
                .weight(1F)
                .padding(4.dp)
        ) {
            Text(text = stringResource(id = R.string.user_register))
        }
        Button(
            onClick = { login() },
            enabled = (acct.text.isNotEmpty() && !passwordState.isValid),
            colors = ButtonDefaults.buttonColors(),
            modifier = Modifier
                .weight(2F)
                .padding(4.dp),
        ) {
            Text(text = stringResource(id = R.string.user_login))
        }
    }
}