package com.example.nano.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.nano.R

// ui
enum class NanoNavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}

enum class NanoNavigationContentPosition {
    TOP, CENTER
}

enum class NanoContentType {
    SINGLE_PANE, DUAL_PANE
}

// route
object NanoRoute {
    const val INBOX = "Inbox"
    const val ARTICLES = "Articles"
    const val DM = "DirectMessages"
    const val GROUPS = "Groups"
}

data class NanoDestination(
    val route: String,
    val selectedIcon: ImageVector = Icons.Rounded.Login,
    val unselectedIcon: ImageVector = Icons.Outlined.Login,
    val iconTextId: Int = 0
)

val TOP_LEVEL_DESTINATIONS = listOf(
    NanoDestination(
        route = NanoRoute.INBOX,
        selectedIcon = Icons.Rounded.Inbox,
        unselectedIcon = Icons.Outlined.Inbox,
        iconTextId = R.string.tab_inbox
    ),
    NanoDestination(
        route = NanoRoute.ARTICLES,
        selectedIcon = Icons.Rounded.Article,
        unselectedIcon = Icons.Outlined.Article,
        iconTextId = R.string.tab_article
    ),
    NanoDestination(
        route = NanoRoute.DM,
        selectedIcon = Icons.Rounded.Chat,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        iconTextId = R.string.tab_dm
    ),
    NanoDestination(
        route = NanoRoute.GROUPS,
        selectedIcon = Icons.Rounded.People,
        unselectedIcon = Icons.Outlined.People,
        iconTextId = R.string.tab_groups
    )
)