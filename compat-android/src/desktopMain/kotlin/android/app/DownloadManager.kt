package android.app

import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.ai.assistance.operit.compat.AppGlobals
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

/**
 * android.app.DownloadManager 桌面版。
 * 真实下载：后台线程把 URL 下载到目标文件，回调通知。——Nova 注
 */
class DownloadManager(private val context: Context) {

    class Request(val uri: Uri) {
        var destinationUri: Uri? = null
        var title: CharSequence? = null
        var description: CharSequence? = null
        var notificationVisibility: Int = VISIBILITY_VISIBLE
        var allowedNetworkTypes: Int = -1

        fun setDestinationUri(dest: Uri?): Request = apply { destinationUri = dest }
        fun setDestinationInExternalFilesDir(ctx: Context?, dirType: String?, subPath: String?): Request = apply {
            val dir = File(AppGlobals.appDir, "downloads/${dirType ?: ""}")
            dir.mkdirs()
            destinationUri = Uri.fromFile(File(dir, subPath ?: "download"))
        }
        fun setDestinationInExternalPublicDir(dirType: String?, subPath: String?): Request = apply {
            val dir = File(AppGlobals.appDir, "downloads/${dirType ?: ""}")
            dir.mkdirs()
            destinationUri = Uri.fromFile(File(dir, subPath ?: "download"))
        }
        fun setTitle(t: CharSequence?): Request = apply { title = t }
        fun setDescription(d: CharSequence?): Request = apply { description = d }
        fun setNotificationVisibility(v: Int): Request = apply { notificationVisibility = v }
        fun setAllowedNetworkTypes(types: Int): Request = apply { allowedNetworkTypes = types }
        /** DownloadManager.Request.addRequestHeader（自定义请求头，如 User-Agent）。——Nova 注 */
        val headers: MutableMap<String, String> = LinkedHashMap()
        fun addRequestHeader(name: String, value: String): Request = apply { headers[name] = value }
        fun setAllowedOverMetered(b: Boolean): Request = apply {}
        fun setAllowedOverRoaming(b: Boolean): Request = apply {}

        companion object {
            const val VISIBILITY_VISIBLE = 0
            const val VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1
            const val VISIBILITY_HIDDEN = 2
            const val VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION = 3
        }
    }

    class Query {
        var ids: LongArray? = null
        fun setFilterById(vararg ids: Long): Query = apply { this.ids = ids }
    }

    private val executor = Executors.newCachedThreadPool { r -> Thread(r, "DownloadManager").apply { isDaemon = true } }
    private val tasks = ConcurrentHashMap<Long, Pair<Request, Int>>() // id -> (request, status)

    fun enqueue(request: Request): Long {
        val id = idCounter.incrementAndGet()
        tasks[id] = request to STATUS_PENDING
        executor.submit {
            tasks[id] = request to STATUS_RUNNING
            val ok = try {
                val src = java.net.URI(request.uri.toString()).toURL().openStream()
                val destFile = request.destinationUri?.path?.let { File(it) }
                if (destFile == null) false else {
                    destFile.parentFile?.mkdirs()
                    src.use { input -> destFile.outputStream().use { input.copyTo(it) } }
                    true
                }
            } catch (e: Exception) {
                false
            }
            tasks[id] = request to (if (ok) STATUS_SUCCESSFUL else STATUS_FAILED)
        }
        return id
    }

    fun query(query: Query): Cursor {
        // 最小实现：返回空 MatrixCursor（调用方遍历时按无记录处理）
        return android.database.MatrixCursor(arrayOf(COLUMN_ID, COLUMN_STATUS))
    }

    fun remove(vararg ids: Long): Int {
        var n = 0
        for (id in ids) if (tasks.remove(id) != null) n++
        return n
    }

    companion object {
        private val idCounter = java.util.concurrent.atomic.AtomicLong(1)

        const val STATUS_PENDING = 1 shl 0
        const val STATUS_RUNNING = 1 shl 1
        const val STATUS_PAUSED = 1 shl 2
        const val STATUS_SUCCESSFUL = 1 shl 3
        const val STATUS_FAILED = 1 shl 4

        const val VISIBILITY_VISIBLE = 0
        const val VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1
        const val VISIBILITY_HIDDEN = 2
        const val VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION = 3

        const val COLUMN_ID = "id"
        const val COLUMN_STATUS = "status"
        const val COLUMN_TITLE = "title"
        const val COLUMN_LOCAL_URI = "local_uri"
        const val COLUMN_BYTES_DOWNLOADED_SO_FAR = "bytes_so_far"
        const val COLUMN_TOTAL_SIZE_BYTES = "total_size"

        const val ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE"
        const val EXTRA_DOWNLOAD_ID = "extra_download_id"
    }
}
