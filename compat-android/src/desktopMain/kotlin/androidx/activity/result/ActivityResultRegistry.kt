package androidx.activity.result

import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityOptionsCompat
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * androidx.activity.result.ActivityResultRegistry 桌面版。
 * 维护 key→(contract, callback) 映射；桌面无系统 activity 栈，
 * dispatchResult 直接解析 intent 并回调。
 */
abstract class ActivityResultRegistry {

    private val keyToRc = ConcurrentHashMap<String, Int>()
    private val rcToKey = ConcurrentHashMap<Int, String>()
    private val callbacks = ConcurrentHashMap<String, ActivityResultCallback<Any?>>()
    private val contracts = ConcurrentHashMap<String, ActivityResultContract<Any?, Any?>>()
    private val rcGenerator = AtomicInteger(0x00010000)

    abstract fun <I, O> onLaunch(
        requestCode: Int,
        contract: ActivityResultContract<I, O>,
        input: I,
        options: ActivityOptionsCompat?,
    )

    @Suppress("UNCHECKED_CAST")
    open fun <I, O> register(
        key: String,
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>,
    ): ActivityResultLauncher<I> {
        val rc = keyToRc.getOrPut(key) { rcGenerator.getAndIncrement() }
        rcToKey[rc] = key
        callbacks[key] = callback as ActivityResultCallback<Any?>
        contracts[key] = contract as ActivityResultContract<Any?, Any?>
        Log.d("ActivityResultRegistry", "register($key, rc=$rc)")
        return object : ActivityResultLauncher<I>() {
            override fun launch(input: I, options: ActivityOptionsCompat?) {
                onLaunch(rc, contract, input, options)
            }

            override fun unregister() {
                this@ActivityResultRegistry.unregister(key)
            }

            override val contract: ActivityResultContract<I, *>
                get() = contract
        }
    }

    open fun unregister(key: String) {
        val rc = keyToRc.remove(key)
        if (rc != null) rcToKey.remove(rc)
        callbacks.remove(key)
        contracts.remove(key)
        Log.d("ActivityResultRegistry", "unregister($key)")
    }

    @Suppress("UNCHECKED_CAST")
    open fun dispatchResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        val key = rcToKey[requestCode] ?: return false
        val callback = callbacks[key] ?: return false
        val contract = contracts[key] ?: return false
        return try {
            callback.onActivityResult(contract.parseResult(resultCode, data))
            true
        } catch (t: Throwable) {
            Log.e("ActivityResultRegistry", "dispatchResult 解析失败", t)
            false
        }
    }

    open fun dispatchResult(requestCode: Int, result: Any?): Boolean {
        val key = rcToKey[requestCode] ?: return false
        val callback = callbacks[key] ?: return false
        return try {
            callback.onActivityResult(result)
            true
        } catch (t: Throwable) {
            Log.e("ActivityResultRegistry", "dispatchResult 回调失败", t)
            false
        }
    }
}

/** androidx.activity.result.ActivityResultRegistryOwner。 */
interface ActivityResultRegistryOwner {
    val activityResultRegistry: ActivityResultRegistry
}
