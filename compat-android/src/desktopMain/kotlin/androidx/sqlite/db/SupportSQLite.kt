package androidx.sqlite.db

import android.content.ContentValues
import android.database.Cursor
import java.io.Closeable
import java.util.Locale

/**
 * androidx.sqlite.db 垫片（P3-B2 新增）。
 * room-runtime-jvm 2.8.4 的 RoomDatabase.openHelper / Migration 体系引用本包类型；
 * composeApp 的 Migration 代码以 SupportSQLiteDatabase.execSQL 执行建表 SQL。
 * JVM 签名与真实 androidx.sqlite:sqlite(-framework) 工件对齐（接口 + getX 方法名）。
 */

interface SupportSQLiteDatabase : Closeable {

    fun execSQL(sql: String)

    fun execSQL(sql: String, bindArgs: Array<out Any?>)

    fun query(query: String): Cursor

    fun query(query: String, bindArgs: Array<out Any?>?): Cursor

    fun query(query: SupportSQLiteQuery): Cursor

    fun insert(table: String, conflictAlgorithm: Int, values: ContentValues): Long

    fun update(table: String, conflictAlgorithm: Int, values: ContentValues, whereClause: String?, whereArgs: Array<out Any?>?): Int

    fun delete(table: String, whereClause: String?, whereArgs: Array<out Any?>?): Int

    fun beginTransaction()

    fun endTransaction()

    fun setTransactionSuccessful()

    fun inTransaction(): Boolean

    fun isOpen(): Boolean

    fun isReadOnly(): Boolean

    fun isDbLockedByCurrentThread(): Boolean

    fun getPath(): String?

    fun getVersion(): Int

    fun setVersion(version: Int)

    fun getMaximumSize(): Long

    fun setMaximumSize(numBytes: Long): Long

    fun getPageSize(): Long

    fun setPageSize(numBytes: Long)

    fun compileStatement(sql: String): SupportSQLiteStatement

    fun getAttachedDbs(): List<android.util.Pair<String, String>>?

    fun isDatabaseIntegrityOk(): Boolean

    fun needUpgrade(newVersion: Int): Boolean

    fun setLocale(locale: Locale)

    fun setMaxSqlCacheSize(cacheSize: Int)

    fun setForeignKeyConstraintsEnabled(enable: Boolean)

    fun enableWriteAheadLogging(): Boolean

    fun disableWriteAheadLogging()

    fun isWriteAheadLoggingEnabled(): Boolean

    fun yieldIfContendedSafely(): Boolean

    fun yieldIfContendedSafely(sleepAfterYieldDelayMillis: Long): Boolean

    override fun close()
}

interface SupportSQLiteProgram : Closeable {
    fun bindNull(index: Int)
    fun bindLong(index: Int, value: Long)
    fun bindDouble(index: Int, value: Double)
    fun bindString(index: Int, value: String)
    fun bindBlob(index: Int, value: ByteArray)
    fun clearBindings()
    override fun close()
}

interface SupportSQLiteStatement : SupportSQLiteProgram {
    fun execute()
    fun executeUpdateDelete(): Int
    fun executeInsert(): Long
    fun simpleQueryForLong(): Long
    fun simpleQueryForString(): String?
}

interface SupportSQLiteQuery {
    val sql: String
    val argCount: Int
    fun bindTo(statement: SupportSQLiteProgram)
}

interface SupportSQLiteOpenHelper : Closeable {

    val databaseName: String?

    val writableDatabase: SupportSQLiteDatabase

    val readableDatabase: SupportSQLiteDatabase

    fun setWriteAheadLoggingEnabled(enabled: Boolean)

    fun setOpenParams(openParams: Any?)

    interface Factory {
        fun create(configuration: Any?): SupportSQLiteOpenHelper
    }

    override fun close()
}
