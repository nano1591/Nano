package com.example.nano.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.nano.R
import com.example.nano.ui.components.PasswordInput
import com.example.nano.ui.components.TextInput
import com.example.nano.ui.utils.TextFieldState
import com.example.nano.ui.utils.Validators

// TODO 转屏时 状态消失， 且error提示一直在（状态问题？）
@Composable
fun NanoLoginContext(
    uiState: NanoUserUiState,
    toRegister: () -> Unit,
    login: (String, String) -> Unit
) {
    val acct = remember {
        TextFieldState(
            uiState.userAcct,
            Validators()
                .required()
                .minLength(6)
                .maxLength(12)
                .word()
                .merge()
        )
    }
    val pwd = remember {
        TextFieldState(
            uiState.userPwd,
            Validators()
                .required()
                .minLength(6)
                .maxLength(16)
                .word()
                .merge()
        )
    }
    TextInput(
        label = stringResource(id = R.string.user_account),
        state = acct,
        imeAction = ImeAction.Next
    )
    Spacer(modifier = Modifier.height(16.dp))
    PasswordInput(
        label = stringResource(id = R.string.user_password),
        state = pwd
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        TextButton(
            onClick = { toRegister() },
        ) {
            Text(text = stringResource(id = R.string.user_register))
        }
        Button(
            onClick = { login(acct.text, pwd.text) },
            enabled = acct.isValid && pwd.isValid,
        ) {
            Text(text = stringResource(id = R.string.user_login))
        }
    }
}