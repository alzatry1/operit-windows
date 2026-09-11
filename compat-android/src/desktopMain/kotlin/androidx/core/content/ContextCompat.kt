package androidx.core.content

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File
import java.util.concurrent.Executor

/**
 * androidx.core.content.ContextCompat 桌面版。
 */
object ContextCompat {

    @JvmStatic
    fun getColor(context: Context, id: Int): Int = context.resources.getColor(id, null)

    @JvmStatic
    fun checkSelfPermission(context: Context, permission: String): Int =
        PackageManager.PERMISSION_GRANTED

    @JvmStatic
    fun <T : Any> getSystemService(context: Context, serviceClass: Class<T>): T? =
        context.getSystemService(serviceClass)

    @JvmStatic
    fun startActivity(context: Context, intent: Intent, options: Bundle?) {
        context.startActivity(intent, options)
    }

    @JvmStatic
    fun startActivities(context: Context, intents: Array<out Intent>): Boolean {
        context.startActivities(intents)
        return true
    }

    @JvmStatic
    fun startActivities(context: Context, intents: Array<out Intent>, options: Bundle?): Boolean {
        context.startActivities(intents)
        return true
    }

    @JvmStatic
    fun getExternalFilesDirs(context: Context, type: String?): Array<File?> =
        context.getExternalFilesDirs(type)

    @JvmStatic
    fun getExternalCacheDirs(context: Context): Array<File?> = context.getExternalCacheDirs()

    @JvmStatic
    fun createDeviceProtectedStorageContext(context: Context): Context =
        context.createDeviceProtectedStorageContext()

    @JvmStatic
    fun getDrawable(context: Context, id: Int): Drawable? = context.getDrawable(id)

    @JvmStatic
    fun getColorStateList(context: Context, id: Int): ColorStateList? =
        context.getColorStateList(id)

    @JvmStatic
    fun startForegroundService(context: Context, intent: Intent) {
        context.startForegroundService(intent)
    }

    @JvmStatic
    fun getMainExecutor(context: Context): Executor = context.mainExecutor

    @JvmStatic
    fun getFilesDir(context: Context): File = context.filesDir

    @JvmStatic
    fun getCacheDir(context: Context): File = context.cacheDir

    @JvmStatic
    fun getDataDir(context: Context): File = context.dataDir

    @JvmStatic
    fun getNoBackupFilesDir(context: Context): File = context.noBackupFilesDir

    @JvmStatic
    fun getObbDirs(context: Context): Array<File> = context.getObbDirs()

    @JvmStatic
    fun getCodeCacheDir(context: Context): File = context.codeCacheDir

    @JvmStatic
    fun registerReceiver(
        context: Context, receiver: android.content.BroadcastReceiver?,
        filter: android.content.IntentFilter, flags: Int,
    ): Intent? = context.registerReceiver(receiver, filter, flags)

    @JvmStatic
    fun createAttributionContext(context: Context, attributionTag: String?): Context =
        context.createAttributionContext(attributionTag)
}

/**
 * androidx.core.content.FileProvider 桌面版。
 * getUriForFile 直接映射 file:// scheme；content:// 授权体系在桌面无意义。
 */
open class FileProvider : android.content.ContentProvider() {

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): android.database.Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: android.content.ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun update(
        uri: Uri, values: android.content.ContentValues?,
        selection: String?, selectionArgs: Array<String>?,
    ): Int = 0

    override fun openFile(uri: Uri, mode: String): android.os.ParcelFileDescriptor? {
        val path = uri.path ?: return null
        return try {
            android.os.ParcelFileDescriptor.open(
                File(path),
                if (mode.contains("w")) android.os.ParcelFileDescriptor.MODE_READ_WRITE
                else android.os.ParcelFileDescriptor.MODE_READ_ONLY,
            )
        } catch (e: Throwable) {
            Log.w("FileProvider", "openFile 失败: $path", e)
            null
        }
    }

    companion object {
        @JvmStatic
        fun getUriForFile(context: Context, authority: String, file: File): Uri =
            Uri.fromFile(file)

        @JvmStatic
        fun getUriForFile(context: Context, authority: String, file: File, displayName: String): Uri =
            Uri.fromFile(file)

        @JvmStatic
        fun parseMetaData(context: Context, componentName: ComponentName): Any? = null
    }
}
