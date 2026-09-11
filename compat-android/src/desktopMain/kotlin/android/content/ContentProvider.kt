package android.content

import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.FileNotFoundException

/**
 * android.content.ContentProvider 桌面版。
 * 与 AOSP 不同：核心 CRUD 方法提供默认实现（非抽象），降低子类负担。
 */
abstract class ContentProvider : ContentInterface {
    /** 真实 Android 的 getContext()；Kotlin 属性形态，attachInfo 时注入。 */
    var context: Context? = null
        private set

    private var readPermission: String? = null
    private var writePermission: String? = null

    fun requireContext(): Context = context ?: throw IllegalStateException("ContentProvider not attached")

    fun attachInfo(context: Context?, info: android.content.pm.ProviderInfo?) {
        this.context = context
        onCreate()
    }

    fun attachInfoForTesting(context: Context, info: android.content.pm.ProviderInfo?) = attachInfo(context, info)

    open fun onCreate(): Boolean = true

    // ---- 查询 ----
    abstract fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor?

    open fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?, cancellationSignal: CancellationSignal?,
    ): Cursor? = query(uri, projection, selection, selectionArgs, sortOrder)

    open fun query(
        uri: Uri, projection: Array<String>?, queryArgs: Bundle?,
        cancellationSignal: CancellationSignal?,
    ): Cursor? = query(uri, projection, null, null, null, cancellationSignal)

    abstract fun getType(uri: Uri): String?

    open fun getStreamTypes(uri: Uri, mimeTypeFilter: String): Array<String>? = null

    abstract fun insert(uri: Uri, values: ContentValues?): Uri?

    open fun insert(uri: Uri, values: ContentValues?, extras: Bundle?): Uri? = insert(uri, values)

    open fun bulkInsert(uri: Uri, values: Array<out ContentValues>): Int {
        var count = 0
        for (v in values) if (insert(uri, v) != null) count++
        return count
    }

    abstract fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int

    open fun delete(uri: Uri, extras: Bundle?): Int = delete(uri, null, null)

    abstract fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int

    open fun update(uri: Uri, values: ContentValues?, extras: Bundle?): Int = update(uri, values, null, null)

    // ---- 文件 ----
    @Throws(FileNotFoundException::class)
    open fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val path = uri.path ?: throw FileNotFoundException(uri.toString())
        return ParcelFileDescriptor.open(java.io.File(path), ParcelFileDescriptor.parseMode(mode))
    }

    @Throws(FileNotFoundException::class)
    open fun openFile(uri: Uri, mode: String, signal: CancellationSignal?): ParcelFileDescriptor? =
        openFile(uri, mode)

    @Throws(FileNotFoundException::class)
    open fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        val pfd = openFile(uri, mode) ?: return null
        return AssetFileDescriptor(pfd, 0, AssetFileDescriptor.UNKNOWN_LENGTH)
    }

    @Throws(FileNotFoundException::class)
    open fun openAssetFile(uri: Uri, mode: String, signal: CancellationSignal?): AssetFileDescriptor? =
        openAssetFile(uri, mode)

    open fun call(method: String, arg: String?, extras: Bundle?): Bundle? = null
    open fun call(authority: String, method: String, arg: String?, extras: Bundle?): Bundle? = call(method, arg, extras)

    // ---- 权限/生命周期 ----
    fun getReadPermission(): String? = readPermission
    fun setReadPermission(permission: String?) { readPermission = permission }
    fun getWritePermission(): String? = writePermission
    fun setWritePermission(permission: String?) { writePermission = permission }
    fun setPathPermissions(perms: Array<out Any>?) {}

    open fun onLowMemory() {}
    open fun onTrimMemory(level: Int) {}
    open fun shutdown() {}
    open fun onConfigurationChanged(newConfig: android.content.res.Configuration) {}

    fun setAttributionTag(tag: String?) {}

    companion object {
        @JvmStatic
        fun coerceToLocalContentProvider(uri: Uri): ContentProvider? = null
    }
}

/** 标记接口：AOSP 的 ContentInterface 方法集已在 ContentProvider 中给出默认实现。 */
interface ContentInterface

/** android.content.ContentProviderClient。 */
class ContentProviderClient : AutoCloseable {
    fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? = null

    fun getType(uri: Uri): String? = null
    fun insert(url: Uri, initialValues: ContentValues?): Uri? = null
    fun delete(url: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    fun update(url: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
    fun bulkInsert(url: Uri, initialValues: Array<out ContentValues>): Int = 0

    fun openFile(url: Uri, mode: String): ParcelFileDescriptor? = null
    fun openFile(url: Uri, mode: String, signal: CancellationSignal?): ParcelFileDescriptor? = null
    fun openAssetFile(url: Uri, mode: String): AssetFileDescriptor? = null

    fun release(): Boolean = true
    override fun close() {}
    fun getLocalContentProvider(): ContentProvider? = null
    fun call(method: String, arg: String?, extras: Bundle?): Bundle? = null
    fun applyBatch(operations: ArrayList<ContentProviderOperation>): Array<Any> = emptyArray()
}

/** android.content.OperationApplicationException。 */
class OperationApplicationException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}
