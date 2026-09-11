package android.database

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.os.Handler

/** android.database.CharArrayBuffer。 */
class CharArrayBuffer {
    constructor()
    constructor(size: Int) { data = CharArray(size) }
    constructor(buf: CharArray?) { data = buf }

    var data: CharArray? = null
    var sizeCopied: Int = 0
}

/** android.database.Cursor：完整接口方法集。 */
interface Cursor : java.io.Closeable {
    companion object {
        const val FIELD_TYPE_NULL = 0
        const val FIELD_TYPE_INTEGER = 1
        const val FIELD_TYPE_FLOAT = 2
        const val FIELD_TYPE_STRING = 3
        const val FIELD_TYPE_BLOB = 4
    }

    fun getCount(): Int
    fun getPosition(): Int
    fun move(offset: Int): Boolean
    fun moveToPosition(position: Int): Boolean
    fun moveToFirst(): Boolean
    fun moveToLast(): Boolean
    fun moveToNext(): Boolean
    fun moveToPrevious(): Boolean
    fun isFirst(): Boolean
    fun isLast(): Boolean
    fun isBeforeFirst(): Boolean
    fun isAfterLast(): Boolean
    fun getColumnIndex(columnName: String?): Int
    fun getColumnIndexOrThrow(columnName: String?): Int
    fun getColumnName(columnIndex: Int): String
    fun getColumnNames(): Array<String>
    fun getColumnCount(): Int
    fun getBlob(columnIndex: Int): ByteArray
    fun getString(columnIndex: Int): String?
    fun copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer)
    fun getShort(columnIndex: Int): Short
    fun getInt(columnIndex: Int): Int
    fun getLong(columnIndex: Int): Long
    fun getFloat(columnIndex: Int): Float
    fun getDouble(columnIndex: Int): Double
    fun getType(columnIndex: Int): Int
    fun isNull(columnIndex: Int): Boolean
    fun deactivate()
    fun requery(): Boolean
    override fun close()
    fun isClosed(): Boolean
    fun registerContentObserver(observer: ContentObserver)
    fun unregisterContentObserver(observer: ContentObserver)
    fun registerDataSetObserver(observer: DataSetObserver)
    fun unregisterDataSetObserver(observer: DataSetObserver)
    fun setNotificationUri(cr: ContentResolver, uri: Uri)
    fun getNotificationUri(): Uri?
    fun getNotificationUris(): List<Uri>
    fun getWantsAllOnMoveCalls(): Boolean
    fun setExtras(extras: Bundle)
    fun getExtras(): Bundle
    fun respond(extras: Bundle): Bundle
}

/** android.database.ContentObserver。 */
open class ContentObserver(private val handler: Handler?) {
    open fun deliverSelfNotifications(): Boolean = false
    open fun onChange(selfChange: Boolean) {}
    open fun onChange(selfChange: Boolean, uri: Uri?) = onChange(selfChange)
    open fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) = onChange(selfChange, uri)
    open fun onChange(selfChange: Boolean, uris: Collection<Uri>, flags: Int) = onChange(selfChange, uris.firstOrNull(), flags)

    fun dispatchChange(selfChange: Boolean) {
        val h = handler
        if (h != null) h.post { onChange(selfChange) } else onChange(selfChange)
    }

    fun dispatchChange(selfChange: Boolean, uri: Uri?) {
        val h = handler
        if (h != null) h.post { onChange(selfChange, uri) } else onChange(selfChange, uri)
    }
}

/** android.database.DataSetObserver。 */
open class DataSetObserver {
    open fun onChanged() {}
    open fun onInvalidated() {}
}

/** android.database.DatabaseErrorHandler。 */
fun interface DatabaseErrorHandler {
    fun onCorruption(dbObj: android.database.sqlite.SQLiteDatabase)
}

/** android.database.SQLException。 */
open class SQLException : RuntimeException {
    constructor() : super()
    constructor(error: String?) : super(error)
    constructor(error: String?, cause: Throwable?) : super(error, cause)
}

/** android.database.StaleDataException。 */
open class StaleDataException : RuntimeException {
    constructor() : super()
    constructor(description: String?) : super(description)
}

/** android.database.CursorIndexOutOfBoundsException。 */
class CursorIndexOutOfBoundsException : IndexOutOfBoundsException {
    constructor() : super()
    constructor(message: String?) : super(message)
}

/** android.database.AbstractCursor：移动/观察者/生命周期基建。 */
abstract class AbstractCursor : Cursor {
    protected var mPos = -1
    protected var mClosed = false
    private val contentObservers = java.util.concurrent.CopyOnWriteArrayList<ContentObserver>()
    private val dataSetObservers = java.util.concurrent.CopyOnWriteArrayList<DataSetObserver>()
    private var notificationUri: Uri? = null
    private var notifyUris: List<Uri> = emptyList()
    private var extras: Bundle = Bundle.EMPTY

    protected val mRowIdColumnIndex = -1

    override fun getCount(): Int = getCountInternal()

    protected abstract fun getCountInternal(): Int

    override fun getPosition(): Int = mPos

    override fun moveToPosition(position: Int): Boolean {
        val count = getCount()
        if (position >= count) { mPos = count; return false }
        if (position < 0) { mPos = -1; return false }
        mPos = position
        return true
    }

    override fun move(offset: Int): Boolean = moveToPosition(mPos + offset)
    override fun moveToFirst(): Boolean = moveToPosition(0)
    override fun moveToLast(): Boolean = moveToPosition(getCount() - 1)
    override fun moveToNext(): Boolean = moveToPosition(mPos + 1)
    override fun moveToPrevious(): Boolean = moveToPosition(mPos - 1)
    override fun isFirst(): Boolean = mPos == 0 && getCount() != 0
    override fun isLast(): Boolean = getCount() != 0 && mPos == getCount() - 1
    override fun isBeforeFirst(): Boolean = if (getCount() == 0) true else mPos == -1
    override fun isAfterLast(): Boolean = if (getCount() == 0) true else mPos == getCount()

    override fun getColumnIndexOrThrow(columnName: String?): Int {
        val index = getColumnIndex(columnName)
        if (index >= 0) return index
        throw IllegalArgumentException("column '$columnName' does not exist")
    }

    override fun getColumnIndex(columnName: String?): Int {
        val names = getColumnNames()
        if (columnName != null) {
            for (i in names.indices) if (names[i].equals(columnName, ignoreCase = true)) return i
        }
        return -1
    }

    override fun getColumnCount(): Int = getColumnNames().size
    override fun getColumnName(columnIndex: Int): String = getColumnNames()[columnIndex]

    override fun copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer) {
        val s = getString(columnIndex) ?: ""
        val data = buffer.data
        if (data == null || data.size < s.length) buffer.data = s.toCharArray()
        else s.toCharArray().copyInto(data)
        buffer.sizeCopied = s.length
    }

    override fun deactivate() {}
    override fun requery(): Boolean = true

    override fun close() { mClosed = true }
    override fun isClosed(): Boolean = mClosed

    override fun registerContentObserver(observer: ContentObserver) {
        if (!contentObservers.contains(observer)) contentObservers.add(observer)
    }
    override fun unregisterContentObserver(observer: ContentObserver) { contentObservers.remove(observer) }
    override fun registerDataSetObserver(observer: DataSetObserver) {
        if (!dataSetObservers.contains(observer)) dataSetObservers.add(observer)
    }
    override fun unregisterDataSetObserver(observer: DataSetObserver) { dataSetObservers.remove(observer) }

    override fun setNotificationUri(cr: ContentResolver, uri: Uri) { notificationUri = uri }
    override fun getNotificationUri(): Uri? = notificationUri
    override fun getNotificationUris(): List<Uri> = if (notifyUris.isEmpty() && notificationUri != null) listOf(notificationUri!!) else notifyUris
    override fun getWantsAllOnMoveCalls(): Boolean = false
    override fun setExtras(extras: Bundle) { this.extras = extras }
    override fun getExtras(): Bundle = extras
    override fun respond(extras: Bundle): Bundle = Bundle.EMPTY

    protected fun checkPosition() {
        if (mPos < 0 || mPos >= getCount()) throw CursorIndexOutOfBoundsException("position=$mPos count=${getCount()}")
    }

    protected open fun onMove(oldPosition: Int, newPosition: Int): Boolean = true

    override fun getType(columnIndex: Int): Int = Cursor.FIELD_TYPE_STRING
}

/** android.database.MatrixCursor：内存行游标，完全可用。 */
open class MatrixCursor : AbstractCursor {
    private val columnNames: Array<out String>
    private val rows = ArrayList<Array<Any?>>()

    constructor(columnNames: Array<out String>, initialCapacity: Int = 1) {
        this.columnNames = columnNames
    }

    constructor(columnNames: Array<out String>) : this(columnNames, 1)

    override fun getCountInternal(): Int = rows.size
    @Suppress("UNCHECKED_CAST")
    override fun getColumnNames(): Array<String> = columnNames as Array<String>

    private fun value(columnIndex: Int): Any? {
        checkPosition()
        return rows[mPos][columnIndex]
    }

    override fun getString(columnIndex: Int): String? = value(columnIndex)?.toString()
    override fun getShort(columnIndex: Int): Short = (value(columnIndex) as? Number)?.toShort() ?: 0
    override fun getInt(columnIndex: Int): Int = (value(columnIndex) as? Number)?.toInt()
        ?: value(columnIndex)?.toString()?.toIntOrNull() ?: 0
    override fun getLong(columnIndex: Int): Long = (value(columnIndex) as? Number)?.toLong()
        ?: value(columnIndex)?.toString()?.toLongOrNull() ?: 0L
    override fun getFloat(columnIndex: Int): Float = (value(columnIndex) as? Number)?.toFloat() ?: 0f
    override fun getDouble(columnIndex: Int): Double = (value(columnIndex) as? Number)?.toDouble() ?: 0.0
    override fun getBlob(columnIndex: Int): ByteArray = (value(columnIndex) as? ByteArray) ?: ByteArray(0)
    override fun isNull(columnIndex: Int): Boolean = value(columnIndex) == null

    override fun getType(columnIndex: Int): Int = when (value(columnIndex)) {
        null -> Cursor.FIELD_TYPE_NULL
        is ByteArray -> Cursor.FIELD_TYPE_BLOB
        is Float, is Double -> Cursor.FIELD_TYPE_FLOAT
        is Number -> Cursor.FIELD_TYPE_INTEGER
        else -> Cursor.FIELD_TYPE_STRING
    }

    fun newRow(): RowBuilder {
        return RowBuilder(rows.size)
    }

    fun addRow(columnValues: Iterable<Any?>) {
        val arr = columnValues.toList().toTypedArray()
        require(arr.size == columnNames.size) { "columnValues.size ${arr.size} != columnNames.size ${columnNames.size}" }
        rows.add(arr)
    }

    fun addRow(columnValues: Array<Any?>) {
        require(columnValues.size == columnNames.size) { "columnValues.size ${columnValues.size} != columnNames.size ${columnNames.size}" }
        rows.add(columnValues.copyOf())
    }

    inner class RowBuilder(private val index: Int) {
        private val row = arrayOfNulls<Any?>(columnNames.size)
        private var next = 0

        fun add(columnValue: Any?): RowBuilder {
            row[next++] = columnValue
            if (next == columnNames.size) finish()
            return this
        }

        fun add(columnName: String, columnValue: Any?): RowBuilder {
            val i = columnNames.indexOf(columnName)
            require(i >= 0) { "unknown column $columnName" }
            row[i] = columnValue
            return this
        }

        fun finish() {
            while (rows.size <= index) rows.add(arrayOfNulls(columnNames.size))
            rows[index] = row
        }
    }
}

/** android.database.CursorWrapper。 */
open class CursorWrapper(protected val mCursor: Cursor) : Cursor {
    override fun getCount(): Int = mCursor.getCount()
    override fun getPosition(): Int = mCursor.getPosition()
    override fun move(offset: Int): Boolean = mCursor.move(offset)
    override fun moveToPosition(position: Int): Boolean = mCursor.moveToPosition(position)
    override fun moveToFirst(): Boolean = mCursor.moveToFirst()
    override fun moveToLast(): Boolean = mCursor.moveToLast()
    override fun moveToNext(): Boolean = mCursor.moveToNext()
    override fun moveToPrevious(): Boolean = mCursor.moveToPrevious()
    override fun isFirst(): Boolean = mCursor.isFirst()
    override fun isLast(): Boolean = mCursor.isLast()
    override fun isBeforeFirst(): Boolean = mCursor.isBeforeFirst()
    override fun isAfterLast(): Boolean = mCursor.isAfterLast()
    override fun getColumnIndex(columnName: String?): Int = mCursor.getColumnIndex(columnName)
    override fun getColumnIndexOrThrow(columnName: String?): Int = mCursor.getColumnIndexOrThrow(columnName)
    override fun getColumnName(columnIndex: Int): String = mCursor.getColumnName(columnIndex)
    override fun getColumnNames(): Array<String> = mCursor.getColumnNames()
    override fun getColumnCount(): Int = mCursor.getColumnCount()
    override fun getBlob(columnIndex: Int): ByteArray = mCursor.getBlob(columnIndex)
    override fun getString(columnIndex: Int): String? = mCursor.getString(columnIndex)
    override fun copyStringToBuffer(columnIndex: Int, buffer: CharArrayBuffer) = mCursor.copyStringToBuffer(columnIndex, buffer)
    override fun getShort(columnIndex: Int): Short = mCursor.getShort(columnIndex)
    override fun getInt(columnIndex: Int): Int = mCursor.getInt(columnIndex)
    override fun getLong(columnIndex: Int): Long = mCursor.getLong(columnIndex)
    override fun getFloat(columnIndex: Int): Float = mCursor.getFloat(columnIndex)
    override fun getDouble(columnIndex: Int): Double = mCursor.getDouble(columnIndex)
    override fun getType(columnIndex: Int): Int = mCursor.getType(columnIndex)
    override fun isNull(columnIndex: Int): Boolean = mCursor.isNull(columnIndex)
    override fun deactivate() = mCursor.deactivate()
    override fun requery(): Boolean = mCursor.requery()
    override fun close() = mCursor.close()
    override fun isClosed(): Boolean = mCursor.isClosed()
    override fun registerContentObserver(observer: ContentObserver) = mCursor.registerContentObserver(observer)
    override fun unregisterContentObserver(observer: ContentObserver) = mCursor.unregisterContentObserver(observer)
    override fun registerDataSetObserver(observer: DataSetObserver) = mCursor.registerDataSetObserver(observer)
    override fun unregisterDataSetObserver(observer: DataSetObserver) = mCursor.unregisterDataSetObserver(observer)
    override fun setNotificationUri(cr: ContentResolver, uri: Uri) = mCursor.setNotificationUri(cr, uri)
    override fun getNotificationUri(): Uri? = mCursor.getNotificationUri()
    override fun getNotificationUris(): List<Uri> = mCursor.getNotificationUris()
    override fun getWantsAllOnMoveCalls(): Boolean = mCursor.getWantsAllOnMoveCalls()
    override fun setExtras(extras: Bundle) = mCursor.setExtras(extras)
    override fun getExtras(): Bundle = mCursor.getExtras()
    override fun respond(extras: Bundle): Bundle = mCursor.respond(extras)
    fun getWrappedCursor(): Cursor = mCursor
}
