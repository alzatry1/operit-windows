package com.ai.assistance.operit.compat

import android.app.ActivityManager
import android.app.Application
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.PowerManager
import android.os.Vibrator
import android.view.Display
import java.io.File

/**
 * 全局单例：桌面运行环境根。
 * 初始化 ~/.operit/ 目录树，提供 applicationContext 与系统服务单例。
 */
object AppGlobals {
    val appDir: File by lazy {
        File(System.getProperty("user.home"), ".operit").apply { mkdirs() }
    }
    val filesDir: File by lazy { File(appDir, "files").apply { mkdirs() } }
    val cacheDir: File by lazy { File(appDir, "cache").apply { mkdirs() } }
    val prefsDir: File by lazy { File(appDir, "shared_prefs").apply { mkdirs() } }
    val externalDir: File by lazy { File(appDir, "external").apply { mkdirs() } }
    val dataDir: File by lazy { File(appDir, "data").apply { mkdirs() } }
    val databasesDir: File by lazy { File(appDir, "databases").apply { mkdirs() } }
    val noBackupDir: File by lazy { File(appDir, "no_backup").apply { mkdirs() } }
    val obbDir: File by lazy { File(appDir, "obb").apply { mkdirs() } }
    val codeCacheDir: File by lazy { File(appDir, "code_cache").apply { mkdirs() } }

    const val PACKAGE_NAME = "com.ai.assistance.operit"

    val applicationContext: Context by lazy { Application() }

    val assets: AssetManager by lazy { AssetManager() }
    val resources: Resources by lazy { Resources() }
    val packageManager: PackageManager by lazy { PackageManager() }
    val contentResolver: ContentResolver by lazy { ContentResolver(applicationContext) }
    val clipboardManager: ClipboardManager by lazy { ClipboardManager(applicationContext) }
    val connectivityManager: ConnectivityManager by lazy { ConnectivityManager() }
    val powerManager: PowerManager by lazy { PowerManager() }
    val vibrator: Vibrator by lazy { Vibrator() }
    val notificationManager: NotificationManager by lazy { NotificationManager() }
    val activityManager: ActivityManager by lazy { ActivityManager() }
    val audioManager: AudioManager by lazy { AudioManager() }
    val display: Display by lazy { Display() }

    /** 全局 View 单例：androidx.compose.ui.platform.LocalView 的默认值（桌面无真实 View 树）。 */
    val rootView: android.view.View by lazy { android.view.View(applicationContext) }

    private val warnedServices = java.util.Collections.synchronizedSet(mutableSetOf<String>())

    /** Context.getSystemService(String) 的分发表。 */
    fun systemService(name: String): Any? {
        return when (name) {
            Context.CLIPBOARD_SERVICE -> clipboardManager
            Context.NOTIFICATION_SERVICE -> notificationManager
            Context.ACTIVITY_SERVICE -> activityManager
            Context.POWER_SERVICE -> powerManager
            Context.VIBRATOR_SERVICE -> vibrator
            Context.VIBRATOR_MANAGER_SERVICE -> vibrator
            Context.CONNECTIVITY_SERVICE -> connectivityManager
            Context.AUDIO_SERVICE -> audioManager
            Context.WINDOW_SERVICE -> null // B1b: WindowManager
            Context.INPUT_METHOD_SERVICE -> null // B1b: InputMethodManager
            else -> {
                if (warnedServices.add(name)) {
                    org.slf4j.LoggerFactory.getLogger("AppGlobals")
                        .warn("getSystemService($name) 未实现，返回 null")
                }
                null
            }
        }
    }

    /** Context.getSystemService(Class) 的分发表。 */
    @Suppress("UNCHECKED_CAST")
    fun <T> systemService(cls: Class<T>): T? = when (cls) {
        ClipboardManager::class.java -> clipboardManager as T
        NotificationManager::class.java -> notificationManager as T
        ActivityManager::class.java -> activityManager as T
        PowerManager::class.java -> powerManager as T
        Vibrator::class.java -> vibrator as T
        ConnectivityManager::class.java -> connectivityManager as T
        AudioManager::class.java -> audioManager as T
        else -> {
            if (warnedServices.add(cls.name)) {
                org.slf4j.LoggerFactory.getLogger("AppGlobals")
                    .warn("getSystemService(${cls.simpleName}) 未实现，返回 null")
            }
            null
        }
    }
}
