package com.ai.assistance.operit.data.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

/**
 * Room KMP 2.8 已移除 RoomDatabase.openHelper（改用 SQLiteConnection 驱动模型）。
 * app 侧备份/查看器代码仍使用 openHelper.writableDatabase 形态，此处以扩展属性桥接。
 *
 * 运行期说明：SupportSQLiteDatabase 的完整实现需要把 Room 的 SQLiteConnection
 * 包壳回旧 API（工作量在后续批次）；当前 writableDatabase 访问会抛出并记录，
 * 仅保证编译绿与启动链路完整。
 */
val RoomDatabase.openHelper: SupportSQLiteOpenHelper
    get() = StubSupportSQLiteOpenHelper

private object StubSupportSQLiteOpenHelper : SupportSQLiteOpenHelper {
    override val databaseName: String? = null

    override val writableDatabase: SupportSQLiteDatabase
        get() = throw UnsupportedOperationException(
            "Room KMP 桌面端无 SupportSQLiteOpenHelper；openHelper 桥接待实装"
        )

    override val readableDatabase: SupportSQLiteDatabase
        get() = writableDatabase

    override fun setWriteAheadLoggingEnabled(enabled: Boolean) {}

    override fun setOpenParams(openParams: Any?) {}

    override fun close() {}
}
