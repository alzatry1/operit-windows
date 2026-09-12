package androidx.concurrent.futures

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * com.google.common.util.concurrent.ListenableFuture 桌面极简版（B11a）。
 * app 用 future.await()（kotlinx-coroutines 的 ListenableFuture 扩展）；桌面提供一个
 * 立即可完成的实现。——Nova 注
 */
open class ListenableFuture<T>(private val value: T) {
    open fun get(): T = value
    open fun isDone(): Boolean = true
    open fun isCancelled(): Boolean = false
}

/** kotlinx-coroutines 的 ListenableFuture.await() 桌面等价物。 */
suspend fun <T> ListenableFuture<T>.await(): T = suspendCancellableCoroutine { cont ->
    cont.resume(get())
}
