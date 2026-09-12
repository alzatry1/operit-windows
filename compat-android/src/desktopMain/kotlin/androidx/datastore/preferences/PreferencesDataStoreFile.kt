package androidx.datastore.preferences

import android.content.Context
import java.io.File

/**
 * androidx.datastore.preferences.preferencesDataStoreFile 桌面 shim（B9i）。
 *
 * 真 datastore-preferences 的桌面 jvm 变体（1.1.7）只带了 PreferencesDataStoreDelegateUtils，
 * 这个顶层 Context 扩展没在桌面 jar 里。按真实语义实现：返回应用 data 目录下
 * datastore/<name>.preferences_pb 文件。——Nova 注
 */
fun Context.preferencesDataStoreFile(name: String): File {
    val base = com.ai.assistance.operit.compat.AppGlobals.appDir
    val dir = File(base, "datastore")
    dir.mkdirs()
    return File(dir, "$name.preferences_pb")
}
