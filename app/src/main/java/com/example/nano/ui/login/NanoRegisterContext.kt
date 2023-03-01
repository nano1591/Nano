package com.example.nano.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nano.R
import com.example.nano.ui.components.PasswordInput
import com.example.nano.ui.components.TextInput
import com.example.nano.ui.utils.TextFieldState

@Composable
fun NanoRegisterContext(
    acct: TextFieldState,
    name: TextFieldState,
    pwd: TextFieldState,
    confirmPwd: TextFieldState
) {
    TextInput(
        label = stringResource(id = R.string.user_account),
        state = acct,
        showErrorOnFirst = true,
        imeAction = ImeAction.Next
    )
    TextInput(
        label = stringResource(id = R.string.user_name),
        state = name,
        showErrorOnFirst = true,
        imeAction = ImeAction.Next
    )
    PasswordInput(
        label = stringResource(id = R.string.user_password),
        state = pwd,
        showErrorOnFirst = true,
        imeAction = ImeAction.Next
    )
    PasswordInput(
        label = stringResource(id = R.string.user_confirm_pwd),
        state = confirmPwd,
        showErrorOnFirst = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NanoRegisterFullDialog(
    acct: TextFieldState,
    name: TextFieldState,
    pwd: TextFieldState,
    confirmPwd: TextFieldState,
    register: (String, String, String, String) -> Unit,
    goBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.user_register)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.dismiss)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { register(acct.text, name.text, pwd.text, "") },
                        enabled = acct.isValid && name.isValid && pwd.isValid && confirmPwd.isValid
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Done,
                            contentDescription = stringResource(id = R.string.user_register)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NanoRegisterContext(
                acct = acct,
                name = name,
                pwd = pwd,
                confirmPwd = confirmPwd
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NanoRegisterDialog(
    acct: TextFieldState,
    name: TextFieldState,
    pwd: TextFieldState,
    confirmPwd: TextFieldState,
    register: (String, String, String, String) -> Unit,
    goBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = goBack,
        modifier = Modifier.padding(56.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.user_register)
                        )
                    },
                    actions = {
                        IconButton(onClick = { goBack() }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.dismiss)
                            )
                        }
                    }
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NanoRegisterContext(
                        acct = acct,
                        name = name,
                        pwd = pwd,
                        confirmPwd = confirmPwd
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
                ) {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(id = R.string.dismiss),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = { register(acct.text, name.text, pwd.text, "") },
                        enabled = acct.isValid && name.isValid && pwd.isValid && confirmPwd.isValid
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Done,
                            contentDescription = stringResource(id = R.string.user_register),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun A() {
    Row(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
    ) {
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(id = R.string.dismiss),
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Outlined.Done,
                contentDescription = stringResource(id = R.string.user_register),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}