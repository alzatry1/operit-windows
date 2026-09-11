package androidx.activity

import android.util.Log
import java.util.ArrayDeque
import java.util.concurrent.CopyOnWriteArrayList

/** androidx.activity.OnBackPressedCallback。 */
abstract class OnBackPressedCallback(var isEnabled: Boolean) {

    private val cancellables = CopyOnWriteArrayList<Cancellable>()

    abstract fun handleOnBackPressed()

    open fun handleOnBackStarted(backEvent: Any?) {}

    open fun handleOnBackProgressed(backEvent: Any?) {}

    open fun handleOnBackCancelled() {}

    fun remove() {
        cancellables.forEach { it.cancel() }
    }

    internal fun addCancellable(cancellable: Cancellable) {
        cancellables.add(cancellable)
    }

    fun interface Cancellable {
        fun cancel()
    }
}

/**
 * androidx.activity.OnBackPressedDispatcher 桌面版。
 * 栈式管理回调；onBackPressed() 从栈顶找第一个 enabled 的回调执行，否则走 fallback。
 */
class OnBackPressedDispatcher(
    private val fallbackOnBackPressed: Runnable? = null,
) {
    private val callbacks = ArrayDeque<OnBackPressedCallback>()
    private val lock = Any()

    constructor() : this(null)

    fun addCallback(onBackPressedCallback: OnBackPressedCallback) {
        synchronized(lock) { callbacks.addLast(onBackPressedCallback) }
        onBackPressedCallback.addCancellable(OnBackPressedCallback.Cancellable {
            synchronized(lock) { callbacks.remove(onBackPressedCallback) }
        })
    }

    fun addCallback(owner: androidx.lifecycle.LifecycleOwner, onBackPressedCallback: OnBackPressedCallback) {
        addCallback(onBackPressedCallback)
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_DESTROY) {
                onBackPressedCallback.remove()
            }
        }
        owner.lifecycle.addObserver(observer)
        onBackPressedCallback.addCancellable(OnBackPressedCallback.Cancellable {
            owner.lifecycle.removeObserver(observer)
        })
    }

    fun onBackPressed() {
        val callback = synchronized(lock) {
            callbacks.descendingIterator().asSequence().firstOrNull { it.isEnabled }
        }
        if (callback != null) {
            callback.handleOnBackPressed()
        } else {
            Log.d("OnBackPressedDispatcher", "无 enabled 回调，走 fallback")
            fallbackOnBackPressed?.run()
        }
    }

    fun hasEnabledCallbacks(): Boolean =
        synchronized(lock) { callbacks.any { it.isEnabled } }

    fun setOnBackInvokedDispatcher(invoker: Any?) {}
}

/** androidx.activity.OnBackPressedDispatcherOwner。 */
interface OnBackPressedDispatcherOwner {
    val onBackPressedDispatcher: OnBackPressedDispatcher
}
