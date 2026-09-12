package android.provider

import android.content.ContentResolver
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.File
import java.io.FileNotFoundException

/** android.provider.DocumentsContract：URI 构建/解析 + 尽力文件化操作。 */
object DocumentsContract {

    object Document {
        const val COLUMN_DOCUMENT_ID = "document_id"
        const val COLUMN_MIME_TYPE = "mime_type"
        const val COLUMN_DISPLAY_NAME = "display_name"
        const val COLUMN_SUMMARY = "summary"
        const val COLUMN_LAST_MODIFIED = "last_modified"
        const val COLUMN_ICON = "icon"
        const val COLUMN_FLAGS = "flags"
        const val COLUMN_SIZE = "size"

        const val MIME_TYPE_DIR = "vnd.android.document/directory"
        const val MIME_TYPE_NONE = "application/octet-stream"

        const val FLAG_VIRTUAL_DOCUMENT = 512
        const val FLAG_SUPPORTS_COPY = 128
        const val FLAG_SUPPORTS_MOVE = 16
        const val FLAG_SUPPORTS_DELETE = 4
        const val FLAG_SUPPORTS_WRITE = 2
        const val FLAG_SUPPORTS_THUMBNAIL = 1
        const val FLAG_SUPPORTS_RENAME = 8
        const val FLAG_SUPPORTS_REMOVE = 64
        const val FLAG_DIR_SUPPORTS_CREATE = 8
        const val FLAG_SUPPORTS_METADATA = 16384
        const val FLAG_PARTIAL = 8192
        const val FLAG_SUPPORTS_EDIT = 4096
        const val FLAG_SUPPORTS_SETTINGS = 2048
        const val FLAG_WEB_LINKABLE = 1024
        const val FLAG_DIR_PREFERS_GRID = 16
    }

    object Root {
        const val COLUMN_ROOT_ID = "root_id"
        const val COLUMN_FLAGS = "flags"
        const val COLUMN_ICON = "icon"
        const val COLUMN_TITLE = "title"
        const val COLUMN_SUMMARY = "summary"
        const val COLUMN_DOCUMENT_ID = "document_id"
        const val COLUMN_AVAILABLE_BYTES = "available_bytes"
        const val COLUMN_CAPACITY_BYTES = "capacity_bytes"
        const val COLUMN_MIME_TYPES = "mime_types"
        const val COLUMN_QUOTA_BYTES = "quota_bytes"

        const val FLAG_LOCAL_ONLY = 1
        const val FLAG_SUPPORTS_CREATE = 2
        const val FLAG_SUPPORTS_RECENTS = 4
        const val FLAG_SUPPORTS_SEARCH = 8
        const val FLAG_SUPPORTS_IS_CHILD = 16
        const val FLAG_SUPPORTS_EJECT = 32
    }

    const val EXTRA_INITIAL_URI = "android.provider.extra.INITIAL_URI"
    const val EXTRA_PROMPT = "android.provider.extra.PROMPT"
    const val EXTRA_INFO = "android.provider.extra.INFO"
    const val EXTRA_LOADING = "android.provider.extra.LOADING"
    const val EXTRA_ERROR = "android.provider.extra.ERROR"
    const val EXTRA_OPTIONS = "android.provider.extra.OPTIONS"
    const val EXTRA_ORIENTATION = "android.provider.extra.ORIENTATION"
    const val EXTRA_PACKAGE_NAME = "android.provider.extra.PACKAGE_NAME"
    const val EXTRA_RESULT = "android.provider.extra.RESULT"
    const val EXTRA_SHOW_CREATE_FILES = "android.provider.extra.SHOW_CREATE_FILES"

    const val ACTION_MANAGE_DOCUMENT = "android.provider.action.MANAGE_DOCUMENT"
    const val ACTION_OPEN_DOCUMENT_TREE = "android.intent.action.OPEN_DOCUMENT_TREE"
    const val ACTION_OPEN_DOCUMENT = "android.intent.action.OPEN_DOCUMENT"
    const val ACTION_BROWSE = "android.provider.action.BROWSE"
    const val ACTION_BROWSE_DOCUMENT_ROOT = "android.provider.action.BROWSE_DOCUMENT_ROOT"

    const val PROVIDER_INTERFACE = "android.content.action.DOCUMENTS_PROVIDER"

    // ---- URI 构建 ----
    @JvmStatic fun buildRootsUri(authority: String): Uri = Uri.parse("content://$authority/roots")

    @JvmStatic fun buildRootUri(authority: String, rootId: String): Uri =
        Uri.parse("content://$authority/roots/${Uri.encode(rootId)}")

    @JvmStatic fun buildRecentDocumentsUri(authority: String, rootId: String): Uri =
        Uri.parse("content://$authority/roots/${Uri.encode(rootId)}/recent")

    @JvmStatic fun buildTreeDocumentUri(authority: String, documentId: String): Uri =
        Uri.parse("content://$authority/tree/${Uri.encode(documentId)}")

    @JvmStatic fun buildDocumentUri(authority: String, documentId: String): Uri =
        Uri.parse("content://$authority/document/${Uri.encode(documentId)}")

    @JvmStatic fun buildDocumentUriUsingTree(treeUri: Uri, documentId: String): Uri {
        val base = treeUri.toString().substringAfter("/tree/").substringBefore("/document/")
        return Uri.parse("${treeUri.toString().substringBefore("/tree/")}/tree/$base/document/${Uri.encode(documentId)}")
    }

    @JvmStatic fun buildChildDocumentsUri(authority: String, parentDocumentId: String): Uri =
        Uri.parse("content://$authority/document/${Uri.encode(parentDocumentId)}/children")

    @JvmStatic fun buildChildDocumentsUriUsingTree(treeUri: Uri, parentDocumentId: String): Uri {
        val base = treeUri.toString().substringAfter("/tree/").substringBefore("/document/")
        return Uri.parse("${treeUri.toString().substringBefore("/tree/")}/tree/$base/document/${Uri.encode(parentDocumentId)}/children")
    }

    @JvmStatic fun buildSearchDocumentsUri(authority: String, rootId: String, query: String): Uri =
        Uri.parse("content://$authority/roots/${Uri.encode(rootId)}/search?query=${Uri.encode(query)}")

    // ---- URI 解析 ----
    @JvmStatic fun getDocumentId(documentUri: Uri): String = documentUri.lastPathSegment ?: ""

    @JvmStatic fun getTreeDocumentId(documentUri: Uri): String {
        val s = documentUri.toString()
        return if ("/tree/" in s) s.substringAfter("/tree/").substringBefore("/document/").let { Uri.decode(it) }
        else documentUri.lastPathSegment ?: ""
    }

    @JvmStatic fun getRootId(rootUri: Uri): String = rootUri.lastPathSegment ?: ""

    @JvmStatic fun getSearchDocumentsQuery(searchUri: Uri): String? = searchUri.getQueryParameter("query")

    @JvmStatic fun isDocumentUri(context: android.content.Context?, uri: Uri?): Boolean =
        uri != null && uri.scheme == "content" && uri.toString().contains("/document/")

    @JvmStatic fun isTreeUri(uri: Uri?): Boolean =
        uri != null && uri.scheme == "content" && uri.toString().contains("/tree/")

    @JvmStatic fun isRootsUri(context: android.content.Context?, uri: Uri?): Boolean =
        uri != null && uri.toString().contains("/roots")

    // ---- 文件化映射 ----
    internal fun documentIdToFile(documentId: String): File {
        val rel = if (documentId.contains(":")) documentId.substringAfter(':') else documentId
        return File(android.os.Environment.getExternalStorageDirectory(), Uri.decode(rel))
    }

    internal fun uriToFile(uri: Uri): File = documentIdToFile(getDocumentId(uri))

    // ---- 文档操作 ----
    @JvmStatic
    fun createDocument(resolver: ContentResolver, parentDocumentUri: Uri, mimeType: String, displayName: String): Uri? {
        return try {
            val parent = uriToFile(parentDocumentUri)
            if (!parent.exists()) parent.mkdirs()
            val target = File(parent, displayName)
            if (mimeType == Document.MIME_TYPE_DIR) target.mkdirs()
            else if (!target.exists()) target.createNewFile()
            val docId = "primary:${parentDocumentUri.lastPathSegment?.substringAfter(':') ?: ""}/$displayName"
            buildDocumentUriUsingTree(parentDocumentUri, docId)
        } catch (e: Exception) {
            Log.w("DocumentsContract", "createDocument 失败: ${e.message}")
            null
        }
    }

    @JvmStatic
    fun deleteDocument(resolver: ContentResolver, documentUri: Uri): Boolean {
        return try {
            uriToFile(documentUri).deleteRecursively()
        } catch (e: Exception) {
            Log.w("DocumentsContract", "deleteDocument 失败: ${e.message}")
            false
        }
    }

    @JvmStatic
    fun removeDocument(resolver: ContentResolver, documentUri: Uri, parentDocumentUri: Uri): Boolean =
        deleteDocument(resolver, documentUri)

    @JvmStatic
    fun renameDocument(resolver: ContentResolver, documentUri: Uri, displayName: String): Uri? {
        return try {
            val src = uriToFile(documentUri)
            val dst = File(src.parentFile, displayName)
            if (src.renameTo(dst)) {
                val docId = "primary:${dst.absolutePath.removePrefix(android.os.Environment.getExternalStorageDirectory().absolutePath + "/")}"
                buildDocumentUri(documentUri.authority ?: "", docId)
            } else null
        } catch (e: Exception) {
            Log.w("DocumentsContract", "renameDocument 失败: ${e.message}")
            null
        }
    }

    @JvmStatic
    fun copyDocument(resolver: ContentResolver, sourceDocumentUri: Uri, targetParentDocumentUri: Uri): Uri? {
        return try {
            val src = uriToFile(sourceDocumentUri)
            val dstDir = uriToFile(targetParentDocumentUri)
            dstDir.mkdirs()
            val dst = File(dstDir, src.name)
            src.copyRecursively(dst, overwrite = true)
            createDocument(resolver, targetParentDocumentUri, if (src.isDirectory) Document.MIME_TYPE_DIR else "application/octet-stream", src.name)
        } catch (e: Exception) {
            Log.w("DocumentsContract", "copyDocument 失败: ${e.message}")
            null
        }
    }

    @JvmStatic
    fun moveDocument(
        resolver: ContentResolver, sourceDocumentUri: Uri,
        sourceParentDocumentUri: Uri, targetParentDocumentUri: Uri,
    ): Uri? {
        return try {
            val src = uriToFile(sourceDocumentUri)
            val dstDir = uriToFile(targetParentDocumentUri)
            dstDir.mkdirs()
            val dst = File(dstDir, src.name)
            if (src.renameTo(dst)) {
                buildDocumentUriUsingTree(targetParentDocumentUri, "primary:${dst.name}")
            } else null
        } catch (e: Exception) {
            Log.w("DocumentsContract", "moveDocument 失败: ${e.message}")
            null
        }
    }

    @JvmStatic
    fun getDocumentThumbnail(resolver: ContentResolver, documentUri: Uri, size: Point, signal: CancellationSignal?): android.graphics.Bitmap? = null

    @JvmStatic fun isChildDocument(resolver: ContentResolver, parentDocumentUri: Uri, documentUri: Uri): Boolean = false
    @JvmStatic fun getDocumentType(documentUri: Uri): String = "application/octet-stream"
}

/** android.provider.DocumentsProvider：子类（Operit 的两个 DocumentsProvider）的基类。 */
abstract class DocumentsProvider : ContentProvider() {

    abstract fun queryRoots(projection: Array<out String>?): Cursor

    abstract fun queryChildDocuments(
        parentDocumentId: String, projection: Array<out String>?, sortOrder: String?,
    ): Cursor

    open fun queryChildDocuments(
        parentDocumentId: String, projection: Array<out String>?, queryArgs: Bundle?,
    ): Cursor = queryChildDocuments(parentDocumentId, projection, null as String?)

    abstract fun queryDocument(documentId: String, projection: Array<out String>?): Cursor

    @Throws(FileNotFoundException::class)
    abstract fun openDocument(
        documentId: String, mode: String, signal: CancellationSignal?,
    ): ParcelFileDescriptor

    // ---- 可选覆写 ----
    open fun queryRecentDocuments(rootId: String, projection: Array<String>?): Cursor =
        throw UnsupportedOperationException("Recent documents not supported")

    open fun queryRecentDocuments(rootId: String, projection: Array<String>?, queryArgs: Bundle?): Cursor =
        queryRecentDocuments(rootId, projection)

    open fun querySearchDocuments(rootId: String, query: String, projection: Array<String>?): Cursor =
        throw UnsupportedOperationException("Search not supported")

    @Throws(FileNotFoundException::class)
    open fun createDocument(parentDocumentId: String, mimeType: String, displayName: String): String? =
        throw FileNotFoundException("Create not supported")

    @Throws(FileNotFoundException::class)
    open fun renameDocument(documentId: String, displayName: String): String? =
        throw FileNotFoundException("Rename not supported")

    @Throws(FileNotFoundException::class)
    open fun deleteDocument(documentId: String): Unit =
        throw FileNotFoundException("Delete not supported")

    @Throws(FileNotFoundException::class)
    open fun removeDocument(documentId: String, parentDocumentId: String): Unit =
        throw FileNotFoundException("Remove not supported")

    @Throws(FileNotFoundException::class)
    open fun copyDocument(sourceDocumentId: String, targetParentDocumentId: String): String =
        throw FileNotFoundException("Copy not supported")

    @Throws(FileNotFoundException::class)
    open fun moveDocument(
        sourceDocumentId: String, sourceParentDocumentId: String, targetParentDocumentId: String,
    ): String? = throw FileNotFoundException("Move not supported")

    open fun isChildDocument(parentDocumentId: String, documentId: String): Boolean = false

    @Throws(FileNotFoundException::class)
    open fun openDocumentThumbnail(documentId: String, sizeHint: Point, signal: CancellationSignal?): Any? =
        throw FileNotFoundException("Thumbnail not supported")

    open fun findDocumentPath(parentDocumentId: String?, documentId: String): Any? = null

    @Throws(FileNotFoundException::class)
    open fun openTypedDocument(documentId: String, mimeTypeFilter: String, opts: Bundle?, signal: CancellationSignal?): Any? =
        throw FileNotFoundException("Typed document not supported")

    // ---- ContentProvider 契约：把 URI 调用路由到 documentId 版本 ----
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    companion object {
        const val QUERY_ARG_CURSOR = "android:query-arg-cursor"
        const val QUERY_ARG_LIMIT = "android:query-arg-limit"
        const val QUERY_ARG_OFFSET = "android:query-arg-offset"
        const val QUERY_ARG_SHOW_TABS = "android:query-arg-showTabs"
    }
}
