package com.example.nano.ui.login

import android.annotation.SuppressLint
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
            navigationActions.navigateTo(NanoDestination(route = NanoRoute.LOGIN))
        },
        login = userViewModel::login,
        register = userViewModel::register
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
    login: (String, String) -> Unit,
    register: (String, String, String, String) -> Unit
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = NanoRoute.LOGIN
    ) {
        composable(NanoRoute.LOGIN) {
            NanoLoginPageRoute(
                contentType = contentType,
                displayFeatures = displayFeatures
            ) {
                NanoLoginContext(
                    uiState = uiState,
                    navigateToRegister = navigateToRegister,
                    login = login
                )
            }
        }
        composable(NanoRoute.REGISTER) {
            NanoRegisterRoute(
                contentType = contentType,
                uiState = uiState,
                goBack = goBack,
                register = register
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
