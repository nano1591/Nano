package com.example.nano.ui.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountTree
import androidx.compose.material.icons.rounded.Reply
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import com.example.nano.R
import com.example.nano.ui.navigation.NanoContentType
import com.example.nano.ui.navigation.NanoDestination
import com.example.nano.ui.navigation.NanoNavigationActions
import com.example.nano.ui.navigation.NanoRoute
import com.example.nano.ui.utils.TextFieldState
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

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NanoNavigationActions(navController)
    }

    NanoLoginNavHost(
        navController = navController,
        contentType = contentType,
        displayFeatures = displayFeatures,
        uiState = uiState,
        navigateToRegister = {
            navigationActions.navigateTo(NanoDestination(route = NanoRoute.REGISTER))
        },
        goBack = {
            navigationActions.navigateTo(NanoDestination(route = NanoRoute.REGISTER))
        },
        login = userViewModel::login
    )
}

@Composable
fun NanoLoginNavHost(
    navController: NavHostController,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    uiState: NanoUserUiState,
    navigateToRegister: () -> Unit,
    goBack: () -> Unit,
    login: (String, String) -> Unit
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = NanoRoute.LOGIN
    ) {
        composable(NanoRoute.LOGIN) {
            NanoLoginRoute(
                contentType = contentType,
                displayFeatures = displayFeatures,
                uiState = uiState,
                navigateToRegister = navigateToRegister,
                login = login
            )
        }
        composable(NanoRoute.REGISTER) {

        }
    }
}

@Composable
fun NanoLoginRoute(
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    uiState: NanoUserUiState,
    navigateToRegister: () -> Unit,
    login: (String, String) -> Unit
) {
    if (contentType == NanoContentType.DUAL_PANE) {
        NanoLoginTwoPane(
            displayFeatures = displayFeatures,
            uiState = uiState,
            navigateToRegister = navigateToRegister,
            login = login
        )
    } else {
        NanoLoginOnePane(
            uiState = uiState,
            navigateToRegister = navigateToRegister,
            login = login
        )
    }
}

@Composable
fun NanoLoginOnePane(
    uiState: NanoUserUiState,
    navigateToRegister: () -> Unit,
    login: (String, String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp),
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
                .sizeIn(maxWidth = 400.dp)
                .align(Alignment.Center)
        ) {
            NanoLogin(
                uiState = uiState,
                navigateToRegister = navigateToRegister,
                login = login
            )
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

@Composable
fun NanoLoginTwoPane(
    displayFeatures: List<DisplayFeature>,
    uiState: NanoUserUiState,
    navigateToRegister: () -> Unit,
    login: (String, String) -> Unit
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
                    .padding(end = 64.dp)
                    .sizeIn(maxWidth = 400.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.app_name).uppercase(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                NanoLogin(
                    uiState = uiState,
                    navigateToRegister = navigateToRegister,
                    login = login
                )
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
    navigateToRegister: () -> Unit,
    login: (String, String) -> Unit
) {
    var acct by remember {
        TextFieldState(uiState.userAcct)
    }
    var pwd by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(uiState.userPwd, TextRange(6, 15)))
    }
    OutlinedTextField(
        value = acct,
        onValueChange = { acct = it },
        label = { Text(text = stringResource(id = R.string.user_account)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
        value = pwd,
        onValueChange = { pwd = it },
        label = { Text(text = stringResource(id = R.string.user_password)) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation('\u2B50'),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(
            onClick = { navigateToRegister() },
            modifier = Modifier
                .weight(1F)
                .alignByBaseline()
        ) {
            Text(text = stringResource(id = R.string.user_register))
        }
        ElevatedButton(
            onClick = { login("1", "2") },
            enabled = (acct.text.isNotEmpty() && pwd.text.isNotEmpty()),
            modifier = Modifier
                .weight(1.617F)
                .alignByBaseline(),
            contentPadding = PaddingValues(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.user_login)
            )
        }
    }
}