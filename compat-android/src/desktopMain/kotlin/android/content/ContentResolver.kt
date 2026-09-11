package android.content

import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ConcurrentHashMap

/**
 * android.content.ContentResolver 桌面版。
 * openInputStream 支持 file://、content://（尽力映射到 ~/.operit/external）、裸路径、http(s)://。
 */
open class ContentResolver(private val context: Context) {

    companion object {
        const val SCHEME_CONTENT = "content"
        const val SCHEME_FILE = "file"
        const val SCHEME_ANDROID_RESOURCE = "android.resource"
        const val CURSOR_ITEM_BASE_TYPE = "vnd.android.cursor.item"
        const val CURSOR_DIR_BASE_TYPE = "vnd.android.cursor.dir"
        const val SYNC_EXTRAS_ACCOUNT = "account"
        const val SYNC_EXTRAS_MANUAL = "force"
        const val SYNC_OBSERVER_TYPE_ACTIVE = 4
        const val SYNC_OBSERVER_TYPE_STATUS = 8
        const val SYNC_OBSERVER_TYPE_PENDING = 2
        const val QUERY_ARG_LIMIT = "android:query-arg-limit"
        const val QUERY_ARG_OFFSET = "android:query-arg-offset"
        const val QUERY_ARG_SORT_ORDER = "android:query-arg-sortOrder"
        const val QUERY_ARG_SQL_SELECTION = "android:query-arg-sql-selection"
        const val QUERY_ARG_SQL_SELECTION_ARGS = "android:query-arg-sql-selectionArgs"
        const val QUERY_ARG_SQL_SORT_ORDER = "android:query-arg-sql-sortOrder"

        /** content:// 持久化授权登记（桌面版：内存 Set，授权即通过）。 */
        private val persistedPermissions = ConcurrentHashMap.newKeySet<String>()
    }

    /** 把 Uri 尽力解析为本地文件。 */
    protected fun resolveFile(uri: Uri): File? {
        return when (uri.scheme) {
            SCHEME_FILE, "file" -> uri.path?.let { File(it) }
            null -> if (uri.toString().startsWith("/")) File(uri.toString()) else File(uri.toString())
            SCHEME_CONTENT -> {
                // 我们的 DocumentsContract URI 路径形态: /tree/<id>/document/<docId>
                // 把 docId 形如 primary:foo/bar 映射到 external 目录
                val docId = uri.lastPathSegment
                if (docId != null && docId.contains(":")) {
                    val rel = docId.substringAfter(':').replace("%3A", "/").replace("%2F", "/")
                    val decoded = Uri.decode(rel)
                    val f = File(android.os.Environment.getExternalStorageDirectory(), decoded)
                    if (f.exists()) return f
                }
                // 回退：authority 是 file provider 时尝试 path 段
                uri.path?.let { p ->
                    val f = File(p)
                    if (f.exists()) return f
                }
                null
            }
            else -> null
        }
    }

    @Throws(FileNotFoundException::class)
    open fun openInputStream(uri: Uri): InputStream? {
        return when (uri.scheme) {
            "http", "https" -> java.net.URL(uri.toString()).openStream()
            else -> {
                val f = resolveFile(uri)
                if (f != null && f.exists()) FileInputStream(f)
                else if (uri.scheme == null && File(uri.toString()).exists()) FileInputStream(File(uri.toString()))
                else throw FileNotFoundException("无法打开: $uri")
            }
        }
    }

    @Throws(FileNotFoundException::class)
    open fun openOutputStream(uri: Uri): OutputStream? = openOutputStream(uri, "w")

    @Throws(FileNotFoundException::class)
    open fun openOutputStream(uri: Uri, mode: String): OutputStream? {
        val f = resolveFile(uri) ?: throw FileNotFoundException("无法打开输出: $uri")
        f.parentFile?.mkdirs()
        return FileOutputStream(f, mode.contains("a"))
    }

    @Throws(FileNotFoundException::class)
    open fun openFileDescriptor(uri: Uri, mode: String): ParcelFileDescriptor? =
        openFileDescriptor(uri, mode, null)

    @Throws(FileNotFoundException::class)
    open fun openFileDescriptor(uri: Uri, mode: String, cancellationSignal: CancellationSignal?): ParcelFileDescriptor? {
        val f = resolveFile(uri) ?: throw FileNotFoundException("无法打开 fd: $uri")
        return ParcelFileDescriptor.open(f, ParcelFileDescriptor.parseMode(mode))
    }

    open fun openAssetFileDescriptor(uri: Uri, mode: String): android.content.res.AssetFileDescriptor? =
        openAssetFileDescriptor(uri, mode, null)

    open fun openAssetFileDescriptor(
        uri: Uri, mode: String, cancellationSignal: CancellationSignal?,
    ): android.content.res.AssetFileDescriptor? {
        val pfd = openFileDescriptor(uri, mode, cancellationSignal) ?: return null
        return android.content.res.AssetFileDescriptor(pfd, 0, android.content.res.AssetFileDescriptor.UNKNOWN_LENGTH)
    }

    open fun openTypedAssetFileDescriptor(
        uri: Uri, mimeTypeFilter: String, opts: Bundle?,
    ): android.content.res.AssetFileDescriptor? = openAssetFileDescriptor(uri, "r")

    open fun openTypedAssetFileDescriptor(
        uri: Uri, mimeTypeFilter: String, opts: Bundle?, signal: CancellationSignal?,
    ): android.content.res.AssetFileDescriptor? = openAssetFileDescriptor(uri, "r", signal)

    /** query：file/content 返回 DISPLAY_NAME/SIZE 等基础列。 */
    open fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? = query(uri, projection, selection, selectionArgs, sortOrder, null)

    open fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?, cancellationSignal: CancellationSignal?,
    ): Cursor? {
        val f = resolveFile(uri) ?: return null
        if (!f.exists()) return null
        val cols = projection ?: arrayOf(
            OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE,
            "document_id", "mime_type", "last_modified", "flags",
        )
        val cursor = MatrixCursor(cols)
        val row = cursor.newRow()
        for (col in cols) {
            when (col) {
                OpenableColumns.DISPLAY_NAME -> row.add(f.name)
                OpenableColumns.SIZE -> row.add(if (f.isFile) f.length() else 0L)
                "document_id" -> row.add(f.absolutePath)
                "mime_type" -> row.add(getType(Uri.fromFile(f)) ?: "application/octet-stream")
                "last_modified" -> row.add(f.lastModified())
                "flags" -> row.add(2 or 4)
                else -> row.add(null)
            }
        }
        return cursor
    }

    open fun getType(uri: Uri): String? {
        val path = uri.path ?: uri.lastPathSegment ?: return null
        val ext = path.substringAfterLast('.', "").lowercase()
        return MIME_BY_EXT[ext] ?: if (ext.isEmpty()) null else "application/octet-stream"
    }

    open fun insert(url: Uri, values: ContentValues?): Uri? {
        Log.w("ContentResolver", "insert($url) 桌面版不支持，返回 null")
        return null
    }

    open fun bulkInsert(url: Uri, values: Array<out ContentValues>): Int {
        var count = 0
        for (v in values) if (insert(url, v) != null) count++
        return count
    }

    open fun delete(url: Uri, where: String?, selectionArgs: Array<String>?): Int {
        val f = resolveFile(url) ?: return 0
        return if (f.deleteRecursively()) 1 else 0
    }

    open fun update(uri: Uri, values: ContentValues?, where: String?, selectionArgs: Array<String>?): Int = 0

    open fun takePersistableUriPermission(uri: Uri, modeFlags: Int) {
        persistedPermissions += uri.toString()
        Log.d("ContentResolver", "takePersistableUriPermission($uri) 登记")
    }

    open fun releasePersistableUriPermission(uri: Uri, modeFlags: Int) {
        persistedPermissions.remove(uri.toString())
    }

    open fun getPersistedUriPermissions(): List<UriPermission> =
        persistedPermissions.map { UriPermission(Uri.parse(it), Intent.FLAG_GRANT_READ_URI_PERMISSION, 0L) }

    open fun getOutgoingPersistedUriPermissions(): List<UriPermission> = emptyList()

    open fun canonicalize(url: Uri): Uri = url
    open fun uncanonicalize(url: Uri): Uri = url

    open fun acquireContentProviderClient(name: String): ContentProviderClient? = null
    open fun acquireUnstableContentProviderClient(name: String): ContentProviderClient? = null

    open fun registerContentObserver(uri: Uri, notifyForDescendents: Boolean, observer: android.database.ContentObserver) {}
    open fun unregisterContentObserver(observer: android.database.ContentObserver) {}
    open fun notifyChange(uri: Uri?, observer: android.database.ContentObserver?) {}
    open fun notifyChange(uri: Uri?, observer: android.database.ContentObserver?, syncToNetwork: Boolean) {}

    open fun startSync(uri: Uri?, extras: Bundle?) {}
    open fun requestSync(account: Any?, authority: String?, extras: Bundle?) {}

    open fun call(uri: Uri, method: String, arg: String?, extras: Bundle?): Bundle? = null
    open fun call(authority: String, method: String, arg: String?, extras: Bundle?): Bundle? = null
    open fun refresh(url: Uri, args: Bundle?, cancellationSignal: CancellationSignal?): Boolean = false

    open fun getStreamTypes(url: Uri, mimeTypeFilter: String): Array<String>? = null

    open fun hasReadUriPermission(uri: Uri, pid: Int, uid: Int): Boolean = true
    open fun hasWriteUriPermission(uri: Uri, pid: Int, uid: Int): Boolean = true

    open fun loadThumbnail(uri: Uri, size: android.util.Size, signal: CancellationSignal?): android.graphics.Bitmap? = null

    open fun wrap(original: ContentProvider?): ContentProviderClient? = null

}

private val MIME_BY_EXT: Map<String, String> = mapOf(
            "jpg" to "image/jpeg", "jpeg" to "image/jpeg", "png" to "image/png",
            "gif" to "image/gif", "webp" to "image/webp", "bmp" to "image/bmp",
            "svg" to "image/svg+xml", "ico" to "image/x-icon",
            "mp3" to "audio/mpeg", "wav" to "audio/wav", "ogg" to "audio/ogg",
            "flac" to "audio/flac", "m4a" to "audio/mp4", "aac" to "audio/aac",
            "mp4" to "video/mp4", "mkv" to "video/x-matroska", "avi" to "video/x-msvideo",
            "mov" to "video/quicktime", "webm" to "video/webm", "3gp" to "video/3gpp",
            "txt" to "text/plain", "md" to "text/markdown", "html" to "text/html",
            "htm" to "text/html", "css" to "text/css", "js" to "application/javascript",
            "json" to "application/json", "xml" to "text/xml", "csv" to "text/csv",
            "pdf" to "application/pdf", "zip" to "application/zip",
            "tar" to "application/x-tar", "gz" to "application/gzip",
            "7z" to "application/x-7z-compressed", "rar" to "application/vnd.rar",
            "doc" to "application/msword",
            "docx" to "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "xls" to "application/vnd.ms-excel",
            "xlsx" to "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "ppt" to "application/vnd.ms-powerpoint",
            "pptx" to "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "apk" to "application/vnd.android.package-archive",
            "wasm" to "application/wasm",
)
