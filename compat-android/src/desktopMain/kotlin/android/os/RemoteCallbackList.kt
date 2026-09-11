package android.os

import java.util.concurrent.CopyOnWriteArrayList

/**
 * android.os.RemoteCallbackList 桌面版。
 * 桌面无跨进程回调分发；退化为线程安全的回调列表，广播语义保持
 *（beginBroadcast/getBroadcastItem/finishBroadcast 顺序与 AOSP 一致）。
 */
open class RemoteCallbackList<E : IInterface> {
    private val callbacks = CopyOnWriteArrayList<E>()
    @Volatile private var killed = false
    private var broadcasting = false

    open fun register(callback: E?): Boolean {
        if (callback == null || killed) return false
        return callbacks.add(callback)
    }

    open fun unregister(callback: E?): Boolean {
        if (callback == null) return false
        return callbacks.remove(callback)
    }

    fun kill() {
        killed = true
        callbacks.clear()
    }

    fun beginBroadcast(): Int {
        broadcasting = true
        return callbacks.size
    }

    fun getBroadcastItem(index: Int): E = callbacks[index]

    fun finishBroadcast() {
        broadcasting = false
    }

    open fun onCallbackDied(callback: E) {}
}
