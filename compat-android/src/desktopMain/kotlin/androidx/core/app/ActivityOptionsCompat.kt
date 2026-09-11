package androidx.core.app

import android.os.Bundle

/**
 * androidx.core.app.ActivityOptionsCompat 轻 stub。
 * 桌面无转场动画；仅承载 toBundle() 语义供 ActivityResultLauncher.launch 签名使用。
 */
open class ActivityOptionsCompat {

    open fun toBundle(): Bundle? = null

    companion object {
        @JvmStatic
        fun makeBasic(): ActivityOptionsCompat = ActivityOptionsCompat()

        @JvmStatic
        fun makeSceneTransitionAnimation(activity: android.app.Activity): ActivityOptionsCompat =
            ActivityOptionsCompat()

        @JvmStatic
        fun makeCustomAnimation(
            context: android.content.Context, enterResId: Int, exitResId: Int,
        ): ActivityOptionsCompat = ActivityOptionsCompat()

        @JvmStatic
        fun makeScaleUpAnimation(
            source: android.view.View, startX: Int, startY: Int, startWidth: Int, startHeight: Int,
        ): ActivityOptionsCompat = ActivityOptionsCompat()

        @JvmStatic
        fun makeClipRevealAnimation(
            source: android.view.View, startX: Int, startY: Int, width: Int, height: Int,
        ): ActivityOptionsCompat = ActivityOptionsCompat()

        @JvmStatic
        fun makeTaskLaunchBehind(): ActivityOptionsCompat = ActivityOptionsCompat()
    }
}
