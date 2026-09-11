package androidx.core.content

import android.content.SharedPreferences

/**
 * androidx.core.content.SharedPreferencesKt 桌面版（ktx 扩展）。
 * 与真实实现一致：默认 apply() 异步落盘，commit=true 时同步提交。
 */
inline fun SharedPreferences.edit(commit: Boolean = false, action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.action()
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}
