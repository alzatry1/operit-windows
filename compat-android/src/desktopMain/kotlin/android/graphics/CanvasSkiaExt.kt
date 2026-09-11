package android.graphics

/**
 * 桥接：app 代码在 Compose 的 `canvas.nativeCanvas`（桌面=org.jetbrains.skia.Canvas）
 * 上直接调 android.graphics 的 drawText。这里给 Skia Canvas 加扩展，
 * 包壳成我们的 android.graphics.Canvas 复用真实文本渲染。——Nova 注
 */

private fun org.jetbrains.skia.Canvas.asAndroidCanvas(): Canvas {
    val c = Canvas()
    c.skiaCanvas = this
    return c
}

fun org.jetbrains.skia.Canvas.drawText(text: String, x: Float, y: Float, paint: Paint) {
    asAndroidCanvas().drawText(text, x, y, paint)
}

fun org.jetbrains.skia.Canvas.drawText(text: CharSequence, start: Int, end: Int, x: Float, y: Float, paint: Paint) {
    asAndroidCanvas().drawText(text, start, end, x, y, paint)
}

fun org.jetbrains.skia.Canvas.drawText(text: CharArray, index: Int, count: Int, x: Float, y: Float, paint: Paint) {
    asAndroidCanvas().drawText(text, index, count, x, y, paint)
}
