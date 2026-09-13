package com.ai.assistance.operit

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
    println("Nova-DIAG: main() start")
    val app = OperitApplication()
    AppGlobals.registeredApplication = app
    app.onCreate()
    app.initializeMainApplication()
    println("Nova-DIAG: initializeMainApplication done, entering application{}")

    application {
        println("Nova-DIAG: application{} body")
        val windowState = rememberWindowState(size = DpSize(1280.dp, 800.dp))
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Operit AI",
        ) {
            println("Nova-DIAG: Window content composing")
            OperitApp()
            println("Nova-DIAG: OperitApp composition returned")
        }
    }
    println("Nova-DIAG: application{} exited")
}
