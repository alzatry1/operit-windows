package androidx.core.view

import android.view.View
import android.view.Window

/**
 * androidx.core.view.WindowCompat 桌面版。
 */
object WindowCompat {

    @JvmStatic
    fun setDecorFitsSystemWindows(window: Window, decorFitsSystemWindows: Boolean) {
        window.decorFitsSystemWindows = decorFitsSystemWindows
    }

    @JvmStatic
    fun getInsetsController(window: Window, view: View): WindowInsetsControllerCompat =
        WindowInsetsControllerCompat(window, view)

    @JvmStatic
    fun setShowWhenLocked(window: Window, showWhenLocked: Boolean) {}

    @JvmStatic
    fun setTurnScreenOn(window: Window, turnScreenOn: Boolean) {}
}
