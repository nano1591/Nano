package com.example.nano.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "NavigationRail[TOP]")
@Composable
fun PreviewNanoNavigationRailTOP() {
    NanoNavigationRail(
        selectedDestination = NanoRoute.INBOX,
        navigationContentPosition = NanoNavigationContentPosition.TOP,
        navigateToTopLevelDestination = {}
    )
}

@Preview(name = "NavigationRail[CENTER]")
@Composable
fun PreviewNanoNavigationRailCENTER() {
    NanoNavigationRail(
        selectedDestination = NanoRoute.INBOX,
        navigationContentPosition = NanoNavigationContentPosition.CENTER,
        navigateToTopLevelDestination = {}
    )
}

@Preview(name = "PreviewNanoBottomNavigationBar")
@Composable
fun PreviewNanoBottomNavigationBar() {
    NanoBottomNavigationBar(
        selectedDestination = NanoRoute.INBOX,
        navigateToTopLevelDestination = {}
    )
}

@Preview(name = "PermanentNavigationDrawerContent[TOP]")
@Composable
fun PreviewPermanentNavigationDrawerContentTOP() {
    PermanentNavigationDrawerContent(
        selectedDestination = NanoRoute.INBOX,
        navigationContentPosition = NanoNavigationContentPosition.TOP,
        navigateToTopLevelDestination = {}
    )
}

@Preview(name = "PermanentNavigationDrawerContent[CENTER]")
@Composable
fun PreviewPermanentNavigationDrawerContentCENTER() {
    PermanentNavigationDrawerContent(
        selectedDestination = NanoRoute.INBOX,
        navigationContentPosition = NanoNavigationContentPosition.CENTER,
        navigateToTopLevelDestination = {}
    )
}

@Preview(name = "ModalNavigationDrawerContent[TOP]")
@Composable
fun PreviewModalNavigationDrawerContentTOP() {
    ModalNavigationDrawerContent(
        selectedDestination = NanoRoute.INBOX,
        navigationContentPosition = NanoNavigationContentPosition.TOP,
        navigateToTopLevelDestination = {}
    )
}

@Preview(name = "ModalNavigationDrawerContent[CENTER]")
@Composable
fun PreviewModalNavigationDrawerContentCENTER() {
    ModalNavigationDrawerContent(
        selectedDestination = NanoRoute.INBOX,
        navigationContentPosition = NanoNavigationContentPosition.CENTER,
        navigateToTopLevelDestination = {}
    )
}