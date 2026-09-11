package com.ai.assistance.operit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Operit AI — Windows 移植版入口 Composable。
 * 占位实现：随着 Android 版 UI 逐层迁移，这里将被替换为
 * 与原项目 OperitApp.kt 完全一致的导航根。
 */
@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                Text("Operit AI · Windows 移植骨架已就绪")
            }
        }
    }
}
