package com.example.nano.ui.login

import androidx.compose.runtime.Composable
import com.example.nano.ui.navigation.NanoContentType

@Composable
fun NanoRegisterRoute(
    contentType: NanoContentType,
    uiState: NanoUserUiState,
    goBack: () -> Unit,
    register: (String, String, String, String) -> Unit
) {
    /**
     * TODO
     * 横屏模式下 表现为弹窗，竖屏模式下，直接切换页面
     * 点击注册 成功后返回到登录页面
     */
}