package android.database.sqlite

import android.database.SQLException

/**
 * android.database.sqlite 补充异常与 SQLiteProgram（P3-B2 新增）。
 * 主 SQLiteDatabase/SQLiteOpenHelper/SQLiteStatement 已在 SQLite.kt（B1a）实现。
 */

class SQLiteDiskIOException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteFullException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteCantOpenDatabaseException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteDatabaseLockedException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteTableLockedException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteReadOnlyDatabaseException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteDatatypeMismatchException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteOutOfMemoryException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteMisuseException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteAccessPermException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteDoneException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteBindOrColumnIndexOutOfRangeException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

class SQLiteBlobTooBigException : SQLiteException {
    constructor() : super()
    constructor(error: String?) : super(error)
}

/** android.database.sqlite.SQLiteProgram：绑定参数容器基类。 */
open class SQLiteProgram(private val sql: String?) {
    open fun bindNull(index: Int) {}
    open fun bindLong(index: Int, value: Long) {}
    open fun bindDouble(index: Int, value: Double) {}
    open fun bindString(index: Int, value: String) {}
    open fun bindBlob(index: Int, value: ByteArray) {}
    open fun clearBindings() {}
    open fun close() {}
    @Deprecated("deprecated")
    open fun getSql(): String? = sql
}
