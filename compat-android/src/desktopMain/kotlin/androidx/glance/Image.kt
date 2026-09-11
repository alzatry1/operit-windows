package androidx.glance

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable

/**
 * androidx.glance.ImageProvider / Image 桌面 stub。
 * ImageProvider 包装资源 id / Bitmap / Drawable（与真实 API 的构造面一致）。
 */
class ImageProvider {
    val resId: Int?
    val bitmap: Bitmap?
    val drawable: Drawable?

    constructor(resId: Int) {
        this.resId = resId
        this.bitmap = null
        this.drawable = null
    }

    constructor(bitmap: Bitmap) {
        this.resId = null
        this.bitmap = bitmap
        this.drawable = null
    }

    constructor(drawable: Drawable) {
        this.resId = null
        this.bitmap = null
        this.drawable = drawable
    }
}

/** androidx.glance.Image：桌面无小部件渲染，空实现。 */
@Composable
fun Image(provider: ImageProvider, contentDescription: String?, modifier: GlanceModifier = GlanceModifier) {
}
