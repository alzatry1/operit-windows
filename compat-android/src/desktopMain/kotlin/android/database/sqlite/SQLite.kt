package android.database.sqlite

import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.database.SQLException
import java.io.File

/**
 * android.database.sqlite 轻 stub（B1a 编译级；真正的 SQLite 由 Room/Exposed 等接管，
 * 或后续批次用 JDBC SQLite 实装）。
 */

open class SQLiteException : SQLException {
    constructor() : super()
    constructor(error: String?) : super(error)
    constructor(error: String?, cause: Throwable?) : super(error, cause)
}

class SQLiteDatabaseCorruptException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteConstraintException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteAbortException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

/** android.database.sqlite.SQLiteDatabase。 */
open class SQLiteDatabase : AutoCloseable {

    fun interface CursorFactory {
        fun newCursor(db: SQLiteDatabase, masterQuery: SQLiteCursorDriver?, editTable: String?, query: SQLiteQuery?): Cursor
    }

    interface SQLiteCursorDriver
    interface SQLiteQuery

    var path: String? = null
    private var open = true
    private var version = 0
    private var inTransaction = false

    open fun isOpen(): Boolean = open
    open fun isReadOnly(): Boolean = false
    open fun isDbLockedByCurrentThread(): Boolean = false
    open fun isDbLockedByOtherThreads(): Boolean = false
    open fun getVersion(): Int = version
    open fun setVersion(version: Int) { this.version = version }
    open fun getPageSize(): Long = 4096
    open fun setPageSize(numBytes: Long) {}
    open fun getMaximumSize(): Long = Long.MAX_VALUE
    open fun setMaximumSize(numBytes: Long): Long = numBytes

    open fun beginTransaction() { inTransaction = true }
    open fun beginTransactionNonExclusive() { inTransaction = true }
    open fun beginTransactionWithListener(transactionListener: Any?) { inTransaction = true }
    open fun endTransaction() { inTransaction = false }
    open fun setTransactionSuccessful() {}
    open fun inTransaction(): Boolean = inTransaction
    open fun yieldIfContendedSafely(): Boolean = true
    open fun yieldIfContendedSafely(sleepAfterYieldDelayMillis: Long): Boolean = true

    open override fun close() { open = false }
    open fun isDatabaseIntegrityOk(): Boolean = true

    open fun execSQL(sql: String) {}
    open fun execSQL(sql: String, bindArgs: Array<out Any?>) {}

    open fun rawQuery(sql: String, selectionArgs: Array<out String?>?): Cursor = MatrixCursor(emptyArray())
    open fun rawQuery(sql: String, selectionArgs: Array<out String?>?, cancellationSignal: android.os.CancellationSignal?): Cursor =
        rawQuery(sql, selectionArgs)
    open fun rawQueryWithFactory(
        cursorFactory: CursorFactory?, sql: String, selectionArgs: Array<out String?>?, editTable: String?,
    ): Cursor = rawQuery(sql, selectionArgs)

    open fun query(table: String, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?, groupBy: String?, having: String?, orderBy: String?): Cursor =
        MatrixCursor(columns ?: emptyArray())
    open fun query(table: String, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?, groupBy: String?, having: String?, orderBy: String?, limit: String?): Cursor =
        MatrixCursor(columns ?: emptyArray())
    open fun query(distinct: Boolean, table: String, columns: Array<String>?, selection: String?, selectionArgs: Array<String>?, groupBy: String?, having: String?, orderBy: String?, limit: String?): Cursor =
        MatrixCursor(columns ?: emptyArray())

    open fun insert(table: String, nullColumnHack: String?, values: ContentValues?): Long = -1
    open fun insertOrThrow(table: String, nullColumnHack: String?, values: ContentValues?): Long = -1
    open fun insertWithOnConflict(table: String, nullColumnHack: String?, initialValues: ContentValues?, conflictAlgorithm: Int): Long = -1
    open fun replace(table: String, nullColumnHack: String?, initialValues: ContentValues?): Long = -1
    open fun replaceOrThrow(table: String, nullColumnHack: String?, initialValues: ContentValues?): Long = -1
    open fun update(table: String, values: ContentValues?, whereClause: String?, whereArgs: Array<String>?): Int = 0
    open fun updateWithOnConflict(table: String, values: ContentValues?, whereClause: String?, whereArgs: Array<String>?, conflictAlgorithm: Int): Int = 0
    open fun delete(table: String, whereClause: String?, whereArgs: Array<String>?): Int = 0

    open fun compileStatement(sql: String): SQLiteStatement = SQLiteStatement(sql)
    open fun needUpgrade(newVersion: Int): Boolean = newVersion > version
    open fun getAttachedDbs(): List<android.util.Pair<String, String>>? = emptyList()
    open fun setLocale(locale: java.util.Locale) {}
    open fun setForeignKeyConstraintsEnabled(enable: Boolean) {}
    open fun execPerConnectionSQL(sql: String, bindArgs: Array<out Any?>?) {}
    open fun validateSql(sql: String, cancellationSignal: android.os.CancellationSignal?) {}
    open fun getSyncedTables(): Set<String> = emptySet()
    open fun markTableSyncable(table: String, deletedTable: String) {}
    open fun markTableSyncable(table: String, foreignKey: String, updateTable: String) {}
    open fun enableWriteAheadLogging(): Boolean = true
    open fun disableWriteAheadLogging() {}
    open fun isWriteAheadLoggingEnabled(): Boolean = false
    open fun isOpenReadOnly(): Boolean = false
    open fun reopenReadWrite() {}
    open fun setMaxSqlCacheSize(cacheSize: Int) {}
    open fun setLockingEnabled(lockingEnabled: Boolean) {}
    open fun getLastInsertRowId(): Long = 0
    open fun getLastChangeCount(): Int = 0

    companion object {
        const val OPEN_READONLY = 0x00000001
        const val OPEN_READWRITE = 0x00000000
        const val CREATE_IF_NECESSARY = 0x10000000
        const val NO_LOCALIZED_COLLATORS = 0x00000010
        const val ENABLE_WRITE_AHEAD_LOGGING = 0x00000008
        const val ENABLE_FOREIGN_KEYS = 0x00000080
        const val OPEN_FULLMUTEX = 0x00010000

        const val CONFLICT_NONE = 0
        const val CONFLICT_ROLLBACK = 1
        const val CONFLICT_ABORT = 2
        const val CONFLICT_FAIL = 3
        const val CONFLICT_IGNORE = 4
        const val CONFLICT_REPLACE = 5

        const val SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000

        const val JOURNAL_MODE_DELETE = "DELETE"
        const val JOURNAL_MODE_TRUNCATE = "TRUNCATE"
        const val JOURNAL_MODE_PERSIST = "PERSIST"
        const val JOURNAL_MODE_MEMORY = "MEMORY"
        const val JOURNAL_MODE_WAL = "WAL"
        const val JOURNAL_MODE_OFF = "OFF"

        @JvmStatic
        fun openDatabase(path: String, factory: CursorFactory?, flags: Int): SQLiteDatabase =
            SQLiteDatabase().apply { this.path = path }

        @JvmStatic
        fun openDatabase(path: String, factory: CursorFactory?, flags: Int, errorHandler: android.database.DatabaseErrorHandler?): SQLiteDatabase =
            openDatabase(path, factory, flags)

        @JvmStatic
        fun openOrCreateDatabase(path: String, factory: CursorFactory?): SQLiteDatabase =
            SQLiteDatabase().apply { this.path = path }

        @JvmStatic
        fun openOrCreateDatabase(file: File, factory: CursorFactory?): SQLiteDatabase =
            SQLiteDatabase().apply { this.path = file.absolutePath; file.parentFile?.mkdirs() }

        @JvmStatic
        fun create(factory: CursorFactory?): SQLiteDatabase = SQLiteDatabase()

        @JvmStatic
        fun deleteDatabase(file: File): Boolean {
            var deleted = file.delete()
            deleted = File(file.path + "-journal").delete() or deleted
            deleted = File(file.path + "-shm").delete() or deleted
            deleted = File(file.path + "-wal").delete() or deleted
            return deleted
        }

        @JvmStatic
        fun renameDatabase(file: File, newFile: File): Boolean = file.renameTo(newFile)

        @JvmStatic
        fun releaseMemory(): Int = 0

        @JvmStatic
        fun getJournalMode(path: String): String = JOURNAL_MODE_DELETE
    }
}

/** android.database.sqlite.SQLiteStatement 轻 stub。 */
open class SQLiteStatement(private val sql: String) {
    open fun execute() {}
    open fun executeInsert(): Long = -1
    open fun executeUpdateDelete(): Int = 0
    open fun simpleQueryForLong(): Long = 0
    open fun simpleQueryForString(): String? = null
    open fun simpleQueryForBlob(): ByteArray? = null
    open fun bindNull(index: Int) {}
    open fun bindLong(index: Int, value: Long) {}
    open fun bindDouble(index: Int, value: Double) {}
    open fun bindString(index: Int, value: String) {}
    open fun bindBlob(index: Int, value: ByteArray) {}
    open fun clearBindings() {}
    open fun close() {}
    fun getSql(): String = sql
}

/** android.database.sqlite.SQLiteOpenHelper 轻 stub。 */
abstract class SQLiteOpenHelper(
    private val context: android.content.Context?,
    private val name: String?,
    private val factory: SQLiteDatabase.CursorFactory?,
    private val version: Int,
) {
    constructor(context: android.content.Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int, errorHandler: android.database.DatabaseErrorHandler?) :
        this(context, name, factory, version)

    abstract fun onCreate(db: SQLiteDatabase)
    abstract fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)

    open fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    open fun onOpen(db: SQLiteDatabase) {}
    open fun onConfigure(db: SQLiteDatabase) {}
    open fun onCorruption(db: SQLiteDatabase) {}

    @Volatile private var database: SQLiteDatabase? = null
    private var writeAheadLogging = false

    @Synchronized
    open fun getWritableDatabase(): SQLiteDatabase {
        var db = database
        if (db == null) {
            db = if (name != null) SQLiteDatabase.openOrCreateDatabase(context?.getDatabasePath(name) ?: File(name), factory)
            else SQLiteDatabase.create(factory)
            database = db
            try { onCreate(db) } catch (e: Exception) { android.util.Log.e("SQLiteOpenHelper", "onCreate failed", e) }
        }
        return db
    }

    @Synchronized
    open fun getReadableDatabase(): SQLiteDatabase = getWritableDatabase()

    @Synchronized
    open fun close() {
        database?.close()
        database = null
    }

    open fun getDatabaseName(): String? = name
    open fun setWriteAheadLoggingEnabled(enabled: Boolean) { writeAheadLogging = enabled }
    open fun isWriteAheadLoggingEnabled(): Boolean = writeAheadLogging
    open fun setLookasideConfig(slotSize: Int, slotCount: Int) {}
    open fun setIdleConnectionTimeout(idleTimeoutMs: Long) {}
    open fun setOpenParams(openParams: Any?) {}
}
