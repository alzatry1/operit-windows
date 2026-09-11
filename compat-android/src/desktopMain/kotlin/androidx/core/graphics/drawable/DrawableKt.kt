package androidx.core.graphics.drawable

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

/**
 * androidx.core.graphics.drawable.toBitmap 扩展：真实绘制。
 * 用 Bitmap.createBitmap + Canvas.drawDrawable 语义（draw 到 canvas）。
 */
@JvmOverloads
fun Drawable.toBitmap(
    width: Int = intrinsicWidth,
    height: Int = intrinsicHeight,
    config: Bitmap.Config? = null,
): Bitmap {
    val w = if (width > 0) width else 1
    val h = if (height > 0) height else 1
    val bitmap = Bitmap.createBitmap(w, h, config ?: Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    setBounds(0, 0, w, h)
    draw(canvas)
    return bitmap
}
