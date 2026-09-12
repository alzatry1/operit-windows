package androidx.room

/**
 * androidx.room.withTransaction 桌面端实现（B8x，B12e 修正）。
 *
 * room-ktx 无桌面（KMP）变体；桌面真 Room 2.7 已迁 SQLiteConnection/Transactor，
 * 移除了旧的 `runInTransaction(Callable)`。这里用 Room 2.7 公共扩展
 * `useWriterConnection` + `Transactor.withTransaction(IMMEDIATE)`，均为 androidx.room
 * 包内成员（本文件同包，免 import），语义与 room-ktx 一致。——Nova 注
 */
suspend fun <R> RoomDatabase.withTransaction(block: suspend () -> R): R {
    return useWriterConnection { transactor ->
        transactor.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
            block()
        }
    }
}
