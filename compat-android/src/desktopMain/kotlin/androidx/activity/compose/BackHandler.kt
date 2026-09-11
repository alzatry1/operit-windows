package androidx.activity.compose

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * androidx.activity.compose.setContent 桌面版。
 * 真实渲染由桌面入口（Main.kt 的 application { Window { } }）驱动；
 * 这里只把 composable 记录到 ComponentActivity.contentView，供入口读取。
 */
fun ComponentActivity.setContent(content: @Composable () -> Unit) {
    @Suppress("UNCHECKED_CAST")
    contentView = content as () -> Unit
}

fun ComponentActivity.setContent(parent: Any?, content: @Composable () -> Unit) {
    setContent(content)
}

/**
 * androidx.activity.compose.LocalOnBackPressedDispatcherOwner。
 * 桌面默认提供全局单例 dispatcher（Main.kt 可在 CompositionLocalProvider 里覆盖为 Activity 的）。
 */
val LocalOnBackPressedDispatcherOwner = staticCompositionLocalOf<OnBackPressedDispatcherOwner?> { null }

private val fallbackBackDispatcherOwner = object : OnBackPressedDispatcherOwner {
    override val onBackPressedDispatcher = androidx.activity.OnBackPressedDispatcher()
}

/**
 * androidx.activity.compose.BackHandler 桌面版。
 * CMP 1.9 desktop 的 androidx.compose.ui.backhandler 包只有内部类（无公开 BackHandler
 * composable，已验证 ui-desktop-1.9.0.jar），因此按 Android 语义自实现：
 * 挂到 LocalOnBackPressedDispatcherOwner 的 OnBackPressedDispatcher，由桌面入口把
 * Esc/返回键事件转发给 dispatcher.onBackPressed()。
 */
@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)
    val dispatcher =
        (LocalOnBackPressedDispatcherOwner.current ?: fallbackBackDispatcherOwner).onBackPressedDispatcher
    DisposableEffect(dispatcher, enabled) {
        val callback = object : androidx.activity.OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
        dispatcher.addCallback(callback)
        onDispose { callback.remove() }
    }
}
