package com.ai.assistance.operit.compat

import android.graphics.Paint

/**
 * org.jetbrains.skia.Canvas 的 android.graphics.Canvas 风格桥接扩展。
 *
 * 桌面 CMP 里 Compose 的 canvas.nativeCanvas 是 org.jetbrains.skia.Canvas，
 * 但 app 的绘制代码（为 Android 写的）把它当 android.graphics.Canvas 用，
 * 调 drawText/getClipBounds/saveLayerAlpha/clipRect 等。这些扩展把它桥接到 Skia。
 * ——Nova 注
 */

/** drawText(text, x, y, android.graphics.Paint) → Skia。 */
fun org.jetbrains.skia.Canvas.drawText(text: String, x: Float, y: Float, paint: Paint) {
    drawString(text, x, y, paint.toSkiaFont(), paint.toSkia())
}

/** getClipBounds(android.graphics.Rect)：Skia Canvas 桌面不提供精确 clip bounds，给一个覆盖画布的大矩形（markdown 可见性优化逻辑照样成立）。 */
fun org.jetbrains.skia.Canvas.getClipBounds(bounds: android.graphics.Rect): Boolean {
    bounds.left = 0
    bounds.top = 0
    bounds.right = 100000
    bounds.bottom = 100000
    return true
}

/** saveLayerAlpha(left, top, right, bottom, alpha)。 */
fun org.jetbrains.skia.Canvas.saveLayerAlpha(left: Float, top: Float, right: Float, bottom: Float, alpha: Int): Int {
    val p = org.jetbrains.skia.Paint().apply { this.alpha = alpha }
    saveLayer(org.jetbrains.skia.Rect.makeLTRB(left, top, right, bottom), p)
    return 0
}

/** clipRect(left, top, right, bottom) 浮点便捷（Skia 原生需 ClipMode）。 */
fun org.jetbrains.skia.Canvas.clipRect(left: Float, top: Float, right: Float, bottom: Float) {
    clipRect(org.jetbrains.skia.Rect.makeLTRB(left, top, right, bottom), org.jetbrains.skia.ClipMode.INTERSECT, true)
}
