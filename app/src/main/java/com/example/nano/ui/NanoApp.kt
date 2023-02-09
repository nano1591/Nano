package com.example.nano.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import com.example.nano.ui.navigation.*
import com.example.nano.ui.viewModel.NanoHomeUIState
import kotlinx.coroutines.launch
import com.example.nano.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NanoApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    nanoHomeUIState: NanoHomeUIState,
    closeDetailScreen: () -> Unit = {},
    navigateToDetail: (Long, NanoContentType) -> Unit = { _, _ -> }
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

    NanoNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        nanoHomeUIState = nanoHomeUIState,
        closeDetailScreen = closeDetailScreen,
        navigateToDetail = navigateToDetail
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NanoNavigationWrapper(
    navigationType: NanoNavigationType,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: NanoNavigationContentPosition,
    nanoHomeUIState: NanoHomeUIState,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit
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

    if (navigationType == NanoNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
            )
        }) {
            NanoAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                nanoHomeUIState = nanoHomeUIState,
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
                    navigateToTopLevelDestination = navigationActions::navigateTo,
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
                nanoHomeUIState = nanoHomeUIState,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail
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
    nanoHomeUIState: NanoHomeUIState,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (NanoTopLevelDestination) -> Unit,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
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
                nanoHomeUIState = nanoHomeUIState,
                navigationType = navigationType,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
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
    navController: NavHostController,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    nanoHomeUIState: NanoHomeUIState,
    navigationType: NanoNavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = NanoRoute.INBOX,
    ) {
        composable(NanoRoute.INBOX) {
            Text(text = stringResource(id = R.string.tab_inbox))
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
