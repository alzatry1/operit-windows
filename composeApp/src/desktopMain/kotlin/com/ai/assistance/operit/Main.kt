package com.ai.assistance.operit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.material3.Text
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
 * 初始化链 → 渲染真实 UI 根 OperitApp()（替换原占位 App()）。——Nova 注
 */
fun main() {
    val app = OperitApplication()
    // 先注册为全局 applicationContext，保证 LocalContext 与 OperitApplication.instance 一致
    AppGlobals.registeredApplication = app
    // Application.onCreate：设 instance、AppLogger、JSON、ImageLoader、权限偏好等
    app.onCreate()
    // 主初始化链：AppLogger、偏好管理器、语言、ActivityLifecycleManager、AIMessageManager、
    // 插件注册、WorkManager、记忆自动保存等
    app.initializeMainApplication()

    application {
        val windowState = rememberWindowState(size = DpSize(1280.dp, 800.dp))
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Operit AI",
        ) {
            // 渲染对照实验：亮红测试块（Compose 能跑就一定渲得出）。红了=渲染管线 OK、问题在 OperitApp；黑了=渲染管线问题。——Nova 注
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFFE53935))
            ) {
                Text(
                    "RENDER-TEST-OK 渲染正常",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
