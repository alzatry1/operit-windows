package com.ai.assistance.operit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.ai.assistance.operit.compat.AppGlobals
import com.ai.assistance.operit.core.application.OperitApplication
import com.ai.assistance.operit.ui.main.OperitApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Windows 桌面入口。
 *
 * 关键：先开 Compose 窗口（显示加载屏），把 OperitApplication 的重型初始化挪到后台协程。
 * 之前 initializeMainApplication() 直接在 main 主线程跑，卡死（很可能卡在 Coil3 全局图片加载器
 * 初始化）导致永远到不了 Compose 窗口，表现为"装得上但打不开界面/黑屏"。——Nova 注
 */
fun main() = application {
    val windowState = rememberWindowState(size = DpSize(1280.dp, 800.dp))
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Operit AI",
    ) {
        var initDone by remember { mutableStateOf(false) }
        var initError by remember { mutableStateOf<String?>(null) }

        // 后台协程跑重型初始化，不阻塞窗口打开。——Nova 注
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                try {
                    println("Nova-DIAG: background init start")
                    val app = OperitApplication()
                    AppGlobals.registeredApplication = app
                    app.onCreate()
                    app.initializeMainApplication()
                    println("Nova-DIAG: background init done")
                    initDone = true
                } catch (e: Throwable) {
                    println("Nova-DIAG: background init FAILED: ${e.javaClass.name}: ${e.message}")
                    e.printStackTrace()
                    initError = "${e.javaClass.name}: ${e.message}"
                }
            }
        }

        when {
            initError != null -> InitErrorScreen(initError!!)
            initDone -> OperitApp()
            else -> InitLoadingScreen()
        }
    }
}

/** 初始化加载屏：窗口先开出来，避免黑屏。——Nova 注 */
@Composable
private fun InitLoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
        Text(
            "Operit AI 正在初始化…",
            modifier = Modifier.align(Alignment.Center).padding(top = 80.dp)
        )
    }
}

/** 初始化失败屏：把错误显示在屏幕上，便于诊断。——Nova 注 */
@Composable
private fun InitErrorScreen(err: String) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.TopStart) {
        Text("初始化失败:\n$err")
    }
}
