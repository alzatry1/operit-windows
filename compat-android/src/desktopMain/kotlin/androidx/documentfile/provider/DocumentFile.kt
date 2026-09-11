package androidx.documentfile.provider

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File

/**
 * androidx.documentfile.provider.DocumentFile 桌面版。
 * 包壳 java.io.File（tree uri 映射到 ~/.operit/external/documents/...）。
 */
abstract class DocumentFile {

    abstract val uri: Uri

    abstract val name: String?

    abstract val type: String?

    abstract val isDirectory: Boolean

    abstract val isFile: Boolean

    abstract val isVirtual: Boolean

    abstract fun lastModified(): Long

    abstract fun length(): Long

    abstract fun canRead(): Boolean

    abstract fun canWrite(): Boolean

    abstract fun delete(): Boolean

    abstract fun exists(): Boolean

    abstract fun listFiles(): Array<DocumentFile>

    abstract fun createFile(mimeType: String, displayName: String): DocumentFile?

    abstract fun createDirectory(displayName: String): DocumentFile?

    abstract fun renameTo(displayName: String): Boolean

    abstract fun getParentFile(): DocumentFile?

    abstract fun findFile(displayName: String): DocumentFile?

    companion object {
        @JvmStatic
        fun fromFile(file: File): DocumentFile = RawDocumentFile(file)

        @JvmStatic
        fun fromTreeUri(context: Context, treeUri: Uri): DocumentFile? {
            val docId = treeUri.lastPathSegment ?: return null
            val decoded = Uri.decode(docId)
            // SAF tree uri: primary:Documents/xxx → ~/.operit/external/documents/xxx
            val rel = decoded.removePrefix("primary:").removePrefix("home:")
            val root = File(
                com.ai.assistance.operit.compat.AppGlobals.externalDir,
                "documents/$rel",
            ).apply { mkdirs() }
            return TreeDocumentFile(root, root)
        }

        @JvmStatic
        fun fromSingleUri(context: Context, singleUri: Uri): DocumentFile? {
            val path = singleUri.path ?: return null
            return RawDocumentFile(File(path))
        }

        @JvmStatic
        fun isDocumentUri(context: Context, uri: Uri?): Boolean = uri?.scheme == "content"
    }
}

/** 包壳 java.io.File。 */
internal open class RawDocumentFile(internal val file: File) : DocumentFile() {

    override val uri: Uri get() = Uri.fromFile(file)

    override val name: String? get() = file.name

    override val type: String?
        get() = if (file.isDirectory) null else guessMimeType(file.name)

    override val isDirectory: Boolean get() = file.isDirectory

    override val isFile: Boolean get() = file.isFile

    override val isVirtual: Boolean get() = false

    override fun lastModified(): Long = file.lastModified()

    override fun length(): Long = file.length()

    override fun canRead(): Boolean = file.canRead()

    override fun canWrite(): Boolean = file.canWrite()

    override fun delete(): Boolean = file.deleteRecursively()

    override fun exists(): Boolean = file.exists()

    override fun listFiles(): Array<DocumentFile> =
        (file.listFiles() ?: emptyArray()).map { RawDocumentFile(it) }.toTypedArray()

    override fun createFile(mimeType: String, displayName: String): DocumentFile? {
        val target = File(file, displayName)
        return try {
            if (target.createNewFile() || target.exists()) RawDocumentFile(target) else null
        } catch (e: Throwable) {
            Log.w("DocumentFile", "createFile 失败: $displayName", e)
            null
        }
    }

    override fun createDirectory(displayName: String): DocumentFile? {
        val target = File(file, displayName)
        return if (target.mkdirs() || target.isDirectory) RawDocumentFile(target) else null
    }

    override fun renameTo(displayName: String): Boolean =
        file.renameTo(File(file.parentFile, displayName))

    override fun getParentFile(): DocumentFile? =
        file.parentFile?.let { RawDocumentFile(it) }

    override fun findFile(displayName: String): DocumentFile? =
        listFiles().firstOrNull { it.name == displayName }

    protected fun guessMimeType(name: String): String {
        val ext = name.substringAfterLast('.', "").lowercase()
        return when (ext) {
            "txt" -> "text/plain"
            "json" -> "application/json"
            "xml" -> "application/xml"
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            "mp3" -> "audio/mpeg"
            "mp4" -> "video/mp4"
            "zip" -> "application/zip"
            "pdf" -> "application/pdf"
            "md" -> "text/markdown"
            "" -> "application/octet-stream"
            else -> "application/octet-stream"
        }
    }
}

/** SAF tree 包壳：root 目录 + 相对路径文件。 */
internal class TreeDocumentFile(
    private val root: File,
    file: File,
) : RawDocumentFile(file) {

    override fun listFiles(): Array<DocumentFile> =
        (file.listFiles() ?: emptyArray()).map { TreeDocumentFile(root, it) }.toTypedArray()

    override fun createFile(mimeType: String, displayName: String): DocumentFile? {
        val target = File(file, displayName)
        return try {
            if (target.createNewFile() || target.exists()) TreeDocumentFile(root, target) else null
        } catch (e: Throwable) {
            Log.w("DocumentFile", "TreeDocumentFile.createFile 失败: $displayName", e)
            null
        }
    }

    override fun createDirectory(displayName: String): DocumentFile? {
        val target = File(file, displayName)
        return if (target.mkdirs() || target.isDirectory) TreeDocumentFile(root, target) else null
    }

    override fun getParentFile(): DocumentFile? {
        val parent = file.parentFile ?: return null
        if (parent == root.parentFile) return null
        return TreeDocumentFile(root, parent)
    }

    override fun findFile(displayName: String): DocumentFile? =
        listFiles().firstOrNull { it.name == displayName }
}
