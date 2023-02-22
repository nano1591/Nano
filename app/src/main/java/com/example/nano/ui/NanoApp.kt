package com.example.nano.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import com.example.nano.ui.navigation.*
import kotlinx.coroutines.launch
import com.example.nano.R
import com.example.nano.ui.home.NanoHomeRoute
import com.example.nano.ui.login.NanoLoginRoute
import com.example.nano.ui.login.NanoUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NanoApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>
) {
    val navigationType: NanoNavigationType
    val contentType: NanoContentType

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NanoNavigationType.BOTTOM_NAVIGATION
            contentType = NanoContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = NanoNavigationType.NAVIGATION_RAIL
            contentType = NanoContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = NanoNavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = NanoContentType.DUAL_PANE
        }
        else -> {
            navigationType = NanoNavigationType.BOTTOM_NAVIGATION
            contentType = NanoContentType.SINGLE_PANE
        }
    }

    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            NanoNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            NanoNavigationContentPosition.CENTER
        }
        else -> {
            NanoNavigationContentPosition.TOP
        }
    }

    val userViewModel: NanoUserViewModel = viewModel(
        factory = NanoUserViewModel.provideFactory(NanoApplication.accountRepository)
    )
    val uiState by userViewModel.uiState.collectAsStateWithLifecycle()

//    if (uiState.isLogin) {
//        NanoNavigationWrapper(
//            navigationType = navigationType,
//            contentType = contentType,
//            displayFeatures = displayFeatures,
//            navigationContentPosition = navigationContentPosition
//        )
//    } else {
    NanoLoginRoute(
        contentType = contentType,
        displayFeatures = displayFeatures
    )
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NanoNavigationWrapper(
    navigationType: NanoNavigationType,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: NanoNavigationContentPosition
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NanoNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: NanoRoute.INBOX

    val closeDetailScreen: () -> Unit = {}
    val navigateToDetail: (Long, NanoContentType) -> Unit = { _, _ -> }
    val onFABClicked: () -> Unit = {}

    if (navigationType == NanoNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                onFABClicked = onFABClicked
            )
        }) {
            NanoAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    onFABClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    navigateToTopLevelDestination = { destination ->
                        navigationActions.navigateTo(destination)
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            NanoAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                onFABClicked = onFABClicked
            ) {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    }
}

@Composable
fun NanoAppContent(
    modifier: Modifier = Modifier,
    navigationType: NanoNavigationType,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: NanoNavigationContentPosition,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (NanoTopLevelDestination) -> Unit,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
    onFABClicked: () -> Unit = {},
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == NanoNavigationType.NAVIGATION_RAIL) {
            NanoNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            NanoNavHost(
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationType = navigationType,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                onFABClicked = onFABClicked,
                modifier = Modifier.weight(1f),
            )
            AnimatedVisibility(visible = navigationType == NanoNavigationType.BOTTOM_NAVIGATION) {
                NanoBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@Composable
private fun NanoNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: NanoNavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
    onFABClicked: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NanoRoute.INBOX,
    ) {
        composable(NanoRoute.INBOX) {
            NanoHomeRoute(
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationType = navigationType,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                onFABClicked = onFABClicked
            )
        }
        composable(NanoRoute.ARTICLES) {
            Text(text = stringResource(id = R.string.tab_article))
        }
        composable(NanoRoute.DM) {
            Text(text = stringResource(id = R.string.tab_dm))
        }
        composable(NanoRoute.GROUPS) {
            Text(text = stringResource(id = R.string.tab_groups))
        }
    }
}
