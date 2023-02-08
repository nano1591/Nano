package com.example.nano.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.People
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

data class NanoTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS = listOf(
    NanoTopLevelDestination(
        route = NanoRoute.INBOX,
        selectedIcon = Icons.Default.Inbox,
        unselectedIcon = Icons.Outlined.Inbox,
        iconTextId = R.string.tab_inbox
    ),
    NanoTopLevelDestination(
        route = NanoRoute.ARTICLES,
        selectedIcon = Icons.Default.Article,
        unselectedIcon = Icons.Outlined.Article,
        iconTextId = R.string.tab_article
    ),
    NanoTopLevelDestination(
        route = NanoRoute.DM,
        selectedIcon = Icons.Default.ChatBubbleOutline,
        unselectedIcon = Icons.Outlined.ChatBubbleOutline,
        iconTextId = R.string.tab_inbox
    ),
    NanoTopLevelDestination(
        route = NanoRoute.GROUPS,
        selectedIcon = Icons.Default.People,
        unselectedIcon = Icons.Outlined.People,
        iconTextId = R.string.tab_article
    )
)