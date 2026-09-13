package com.ai.assistance.operit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.ai.assistance.operit.compat.AppGlobals
import com.ai.assistance.operit.core.application.OperitApplication
import com.ai.assistance.operit.ui.main.OperitApp

/**
 * Windows 桌面入口。
 * 桌面端没有 Android 系统按 manifest 自动实例化 Application 的机制，这里手动完成：
 * 实例化 OperitApplication → 注册为全局 applicationContext → 跑 onCreate/initializeMainApplication
 * 初始化链 → 渲染真实 UI 根 OperitApp()。——Nova 注
 */
fun main() {
    val app = OperitApplication()
    // 先注册为全局 applicationContext，保证 LocalContext 与 OperitApplication.instance 一致
    AppGlobals.registeredApplication = app
    app.onCreate()
    app.initializeMainApplication()

    application {
        val windowState = rememberWindowState(size = DpSize(1280.dp, 800.dp))
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Operit AI",
        ) {
            OperitAppWithErrorSurface()
        }
    }
}

/**
 * 错误边界：OperitApp 组合若抛异常，把错误显示在屏幕上（黑屏 → 可见报错），同时打日志。——Nova 注
 */
@Composable
private fun OperitAppWithErrorSurface() {
    val composeError = remember { mutableStateOf<String?>(null) }
    val err = composeError.value
    if (err != null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1E1E1E))) {
            Text(
                "组合出错:\n$err",
                color = Color(0xFFFF5252),
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
            )
        }
    } else {
        try {
            OperitApp()
        } catch (e: Throwable) {
            e.printStackTrace()
            composeError.value =
                "${e.javaClass.name}: ${e.message}\n" +
                    e.stackTrace.take(10).joinToString("\n") { it.toString() }
        }
    }
}
