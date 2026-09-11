package androidx.activity

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * androidx.activity.ComponentActivity 桌面版。
 * extends android.app.Activity（B1b-1 新建），实现 LifecycleOwner /
 * OnBackPressedDispatcherOwner / ActivityResultRegistryOwner / ViewModelStoreOwner。
 *
 * 桌面单窗口模型：生命周期由桌面入口（Main.kt/perform* 门面）驱动；
 * ActivityResultRegistry 的 onLaunch 对权限请求立即 grant，其余记日志并回调取消值。
 */
open class ComponentActivity : Activity(),
    LifecycleOwner,
    ViewModelStoreOwner,
    OnBackPressedDispatcherOwner,
    ActivityResultRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private val store = ViewModelStore()
    private val backDispatcher = OnBackPressedDispatcher { finish() }
    private val resultRegistry = DesktopActivityResultRegistry()

    /** androidx.activity.compose.setContent 记录的 composable；桌面由入口驱动渲染，这里只存。 */
    var contentView: (() -> Unit)? = null

    override val lifecycle: Lifecycle get() = lifecycleRegistry

    override val viewModelStore: ViewModelStore get() = store

    override val onBackPressedDispatcher: OnBackPressedDispatcher get() = backDispatcher

    override val activityResultRegistry: ActivityResultRegistry get() = resultRegistry

    // ---- 生命周期钩子：同步 LifecycleRegistry 状态 ----
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun onResume() {
        super.onResume()
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun onPause() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        super.onPause()
    }

    override fun onStop() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        super.onStop()
    }

    override fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        store.clear()
        super.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        backDispatcher.onBackPressed()
    }

    /**
     * 桌面版权限请求：立即授予并异步回调（语义与 Activity.requestPermissions 一致，
     * 但走 ActivityResultRegistry 风格日志）。
     */
    override fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        Log.i("ComponentActivity", "requestPermissions(${permissions.joinToString()}) → 桌面全部授予")
        super.requestPermissions(permissions, requestCode)
    }

    /** 桌面 ActivityResultRegistry：权限请求立即 grant，其余回调"取消"值。 */
    private inner class DesktopActivityResultRegistry : ActivityResultRegistry() {
        override fun <I, O> onLaunch(
            requestCode: Int,
            contract: ActivityResultContract<I, O>,
            input: I,
            options: ActivityOptionsCompat?,
        ) {
            when (contract) {
                is androidx.activity.result.contract.ActivityResultContracts.RequestPermission -> {
                    Log.i("ComponentActivity", "RequestPermission($input) → 桌面授予")
                    val intent = Intent().putExtra(
                        androidx.activity.result.contract.ActivityResultContracts.RequestPermission
                            .EXTRA_PERMISSION_GRANT_RESULTS,
                        intArrayOf(android.content.pm.PackageManager.PERMISSION_GRANTED),
                    )
                    dispatchResult(requestCode, RESULT_OK, intent)
                }
                is androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions -> {
                    Log.i("ComponentActivity", "RequestMultiplePermissions → 桌面全部授予")
                    val perms = (input as? Array<*>)?.filterIsInstance<String>()?.toTypedArray() ?: emptyArray()
                    val intent = Intent()
                        .putExtra(
                            androidx.activity.result.contract.ActivityResultContracts.RequestPermission
                                .EXTRA_PERMISSIONS, perms,
                        )
                        .putExtra(
                            androidx.activity.result.contract.ActivityResultContracts.RequestPermission
                                .EXTRA_PERMISSION_GRANT_RESULTS,
                            IntArray(perms.size) { android.content.pm.PackageManager.PERMISSION_GRANTED },
                        )
                    dispatchResult(requestCode, RESULT_OK, intent)
                }
                else -> {
                    Log.w(
                        "ComponentActivity",
                        "onLaunch(${contract.javaClass.simpleName}) 桌面无系统选择器，回调取消值",
                    )
                    dispatchResult(requestCode, RESULT_CANCELED, null)
                }
            }
        }
    }
}
