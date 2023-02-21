package com.example.nano.ui.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.window.layout.DisplayFeature
import com.example.nano.data.model.Email
import com.example.nano.data.model.MailboxType
import com.example.nano.ui.NanoApplication
import com.example.nano.ui.navigation.NanoContentType
import com.example.nano.ui.navigation.NanoNavigationType
import com.example.nano.ui.viewModel.NanoHomeState
import com.example.nano.ui.viewModel.NanoHomeViewModel
import java.util.*

@Composable
fun NanoHomeRoute(
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: NanoNavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
    onFABClicked: () -> Unit
) {
    val homeViewModel: NanoHomeViewModel = viewModel(
        factory = NanoHomeViewModel.provideFactory(NanoApplication.emailRepository)
    )
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    NanoHome(
        uiState = uiState,
        add = homeViewModel::add,
        delete = homeViewModel::delete,
        view = homeViewModel::view,
        onMsgShowed = homeViewModel::onMsgShowed,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationType = navigationType,
        closeDetailScreen = closeDetailScreen,
        navigateToDetail = navigateToDetail,
        onFABClicked = onFABClicked
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NanoHome(
    uiState: NanoHomeState,
    add: (Email) -> Unit,
    delete: (Email) -> Unit,
    view: (Email) -> Unit,
    onMsgShowed: () -> Unit,
    contentType: NanoContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: NanoNavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, NanoContentType) -> Unit,
    onFABClicked: () -> Unit
) {
    var eid by remember { mutableStateOf(1) }
    if (!uiState.error.isNullOrEmpty()) {
        Toast.makeText(LocalContext.current, uiState.error, Toast.LENGTH_SHORT).show()
        onMsgShowed()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        }
        AnimatedVisibility(visible = uiState.emails.isNotEmpty()) {
            Column {
                uiState.emails.map {
                    Text(
                        text = it.toString(),
                        modifier = Modifier
                            .background(
                                if (it.viewAt !== null) MaterialTheme.colorScheme.inverseOnSurface
                                else MaterialTheme.colorScheme.primaryContainer
                            )
                            .clickable { view(it) })
                    TextButton(onClick = { delete(it) }) {
                        Text(text = "delete")
                    }
                }
            }
        }
        TextButton(onClick = {
            add(
                Email(
                    eid = eid,
                    sender = 1,
                    recipients = listOf(1, 2),
                    subject = "subject",
                    body = "body",
                    attachments = emptyList(),
                    isImportant = false,
                    isStarred = false,
                    mailbox = MailboxType.INBOX,
                    createdAt = Date(),
                    threads = emptyList()
                )
            )
            eid = ++eid
        }) {
            Text(text = "add")
        }

    }
}