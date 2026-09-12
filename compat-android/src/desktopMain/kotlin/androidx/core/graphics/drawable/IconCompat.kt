package androidx.core.graphics.drawable

import android.graphics.Bitmap

/**
 * androidx.core.graphics.drawable.IconCompat 桌面 stub（B10z）。
 * app 用它给通知/快捷方式构造图标；桌面仅存类型与位图。——Nova 注
 */
class IconCompat {
    var type: Int = TYPE_UNKNOWN
        private set
    var bitmap: Bitmap? = null
        private set
    var resId: Int = 0
        private set

    companion object {
        const val TYPE_UNKNOWN = 0
        const val TYPE_BITMAP = 1
        const val TYPE_RESOURCE = 2
        const val TYPE_URI = 4

        @JvmStatic
        fun createWithBitmap(bitmap: Bitmap?): IconCompat = IconCompat().apply {
            type = TYPE_BITMAP
            this.bitmap = bitmap
        }

        @JvmStatic
        fun createWithResource(context: android.content.Context?, resId: Int): IconCompat = IconCompat().apply {
            type = TYPE_RESOURCE
            this.resId = resId
        }

        @JvmStatic
        fun createWithContentUri(uri: String?): IconCompat = IconCompat().apply {
            type = TYPE_URI
        }
    }
}
