package androidx.room

import java.util.concurrent.Callable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

/**
 * androidx.room.withTransaction 桌面端实现（B8x）。
 *
 * room-ktx 无桌面（KMP）变体，真 room-runtime 也不含此挂起扩展。
 * 这里委托 RoomDatabase 公开的 `runInTransaction(Callable)`：把 suspend 块包成
 * Callable 在真实数据库事务里执行，语义与 Android room-ktx 一致。——Nova 注
 */
suspend fun <R> RoomDatabase.withTransaction(block: suspend () -> R): R {
    val callable = Callable<R> {
        runBlocking(Dispatchers.IO) { block() }
    }
    return this.runInTransaction(callable)
}
