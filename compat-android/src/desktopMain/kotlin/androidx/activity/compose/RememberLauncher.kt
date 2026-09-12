package androidx.activity.compose

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.concurrent.atomic.AtomicInteger

/**
 * androidx.activity.compose.LocalActivityResultRegistryOwner。
 * 桌面默认提供全局 registry（权限类立即授予，其余回调取消值）。
 */
val LocalActivityResultRegistryOwner = staticCompositionLocalOf<ActivityResultRegistryOwner?> { null }

private val fallbackRegistryOwner = object : ActivityResultRegistryOwner {
    override val activityResultRegistry: ActivityResultRegistry = object : ActivityResultRegistry() {
        override fun <I, O> onLaunch(
            requestCode: Int,
            contract: ActivityResultContract<I, O>,
            input: I,
            options: androidx.core.app.ActivityOptionsCompat?,
        ) {
            android.util.Log.w(
                "ActivityResultRegistry",
                "fallback onLaunch(${contract.javaClass.simpleName})，回调取消值",
            )
            dispatchResult(requestCode, android.app.Activity.RESULT_CANCELED, null)
        }
    }
}

/**
 * androidx.activity.compose.ManagedActivityResultLauncher。
 */
class ManagedActivityResultLauncher<I, O>(
    private val launcher: ActivityResultLauncher<I>,
    override val contract: ActivityResultContract<I, O>,
) : ActivityResultLauncher<I>() {
    override fun launch(input: I, options: androidx.core.app.ActivityOptionsCompat?) {
        launcher.launch(input, options)
    }

    override fun unregister() {
        launcher.unregister()
    }
}

private val launcherKeyCounter = AtomicInteger(0)

/**
 * androidx.activity.compose.rememberLauncherForActivityResult 桌面版。
 * 注册到当前 ActivityResultRegistryOwner（无则全局 fallback）。
 */
@Composable
fun <I, O> rememberLauncherForActivityResult(
    contract: ActivityResultContract<I, O>,
    onResult: (O) -> Unit,
): ManagedActivityResultLauncher<I, O> {
    val registry =
        (LocalActivityResultRegistryOwner.current ?: fallbackRegistryOwner).activityResultRegistry
    val currentOnResult = androidx.compose.runtime.rememberUpdatedState(onResult)
    val key = remember { "compose_launcher_${launcherKeyCounter.getAndIncrement()}" }
    val launcher = remember(registry, contract) {
        registry.register(key, contract) { result -> currentOnResult.value(result) }
    }
    DisposableEffect(launcher) {
        onDispose { /* 保留注册，与 Android 行为一致（launcher 随 composition 存活） */ }
    }
    return remember(launcher) { ManagedActivityResultLauncher(launcher, contract) }
}
