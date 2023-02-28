package com.example.nano.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nano.R
import com.example.nano.ui.components.PasswordInput
import com.example.nano.ui.components.TextInput
import com.example.nano.ui.theme.NanoTheme
import com.example.nano.ui.utils.TextFieldState
import com.example.nano.ui.utils.Validators

@Preview(showBackground = true)
@Composable
fun P() {
    NanoTheme {
        NanoRegisterRoute(
            close = {},
            register = { _, _, _, _ -> }
        )
    }
}

@Composable
fun NanoRegisterRoute(
    close: () -> Unit,
    register: (String, String, String, String) -> Unit
) {
    NanoRegisterScaffold(
        goBack = close,
        register = register
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NanoRegisterScaffold(
    goBack: () -> Unit,
    register: (String, String, String, String) -> Unit
) {
    val acct = remember {
        TextFieldState(
            "",
            Validators()
                .required()
                .minLength(6)
                .maxLength(12)
                .word()
                .merge()
        )
    }
    val name = remember {
        TextFieldState(
            "",
            Validators()
                .required()
                .minLength(2)
                .maxLength(12)
                .word()
                .merge()
        )
    }
    val pwd = remember {
        TextFieldState(
            "",
            Validators()
                .required()
                .minLength(6)
                .maxLength(16)
                .word()
                .merge()
        )
    }
    val confirmPwd = remember {
        TextFieldState("", Validators().equal(pwd).merge())
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surface),
        topBar = {
            CenterAlignedTopAppBar(
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
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
    }
}
