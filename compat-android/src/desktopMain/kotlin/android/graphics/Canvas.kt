package android.graphics

import android.util.Log

/**
 * android.graphics.Canvas：包壳 org.jetbrains.skia.Canvas。
 */
open class Canvas {
    internal var skiaCanvas: org.jetbrains.skia.Canvas? = null
    internal var bitmap: Bitmap? = null
    private var saveDepth = 0

    constructor()

    constructor(bitmap: Bitmap) {
        this.bitmap = bitmap
        this.skiaCanvas = try {
            org.jetbrains.skia.Canvas(bitmap.skiaBitmap, org.jetbrains.skia.SurfaceProps(org.jetbrains.skia.PixelGeometry.UNKNOWN))
        } catch (e: Throwable) {
            Log.w("Canvas", "创建 skia Canvas 失败: ${e.message}")
            null
        }
    }

    // ---- 尺寸 ----
    open val width: Int get() = bitmap?.width ?: 0
    open val height: Int get() = bitmap?.height ?: 0
    open val density: Int get() = bitmap?.density ?: android.util.DisplayMetrics.DENSITY_DEFAULT
    open val maximumBitmapWidth: Int get() = 32766
    open val maximumBitmapHeight: Int get() = 32766
    open val isHardwareAccelerated: Boolean get() = false
    open val isOpaque: Boolean get() = false
    open val clipBounds: Rect get() = Rect(0, 0, width, height)

    // ---- 清屏/整绘 ----
    open fun drawColor(color: Int) {
        try { skiaCanvas?.clear(color) } catch (e: Throwable) { logFail("drawColor", e) }
    }

    open fun drawColor(color: Int, mode: PorterDuff.Mode) {
        val p = Paint().apply { this.color = color; xfermode = PorterDuffXfermode(mode) }
        drawPaint(p)
    }

    open fun drawColor(color: Int, mode: BlendMode) = drawColor(color)

    open fun drawRGB(r: Int, g: Int, b: Int) = drawColor(Color.rgb(r, g, b))

    open fun drawARGB(a: Int, r: Int, g: Int, b: Int) = drawColor(Color.argb(a, r, g, b))

    open fun drawPaint(paint: Paint) {
        try { skiaCanvas?.drawPaint(paint.toSkia()) } catch (e: Throwable) { logFail("drawPaint", e) }
    }

    // ---- 图元 ----
    open fun drawPoint(x: Float, y: Float, paint: Paint) {
        try { skiaCanvas?.drawPoint(x, y, paint.toSkia()) } catch (e: Throwable) { logFail("drawPoint", e) }
    }

    open fun drawPoints(pts: FloatArray, offset: Int, count: Int, paint: Paint) {
        try {
            val sub = pts.copyOfRange(offset, offset + count)
            skiaCanvas?.drawPoints(sub, paint.toSkia())
        } catch (e: Throwable) { logFail("drawPoints", e) }
    }

    open fun drawPoints(pts: FloatArray, paint: Paint) = drawPoints(pts, 0, pts.size, paint)

    open fun drawLine(startX: Float, startY: Float, stopX: Float, stopY: Float, paint: Paint) {
        try { skiaCanvas?.drawLine(startX, startY, stopX, stopY, paint.toSkia()) } catch (e: Throwable) { logFail("drawLine", e) }
    }

    open fun drawLines(pts: FloatArray, offset: Int, count: Int, paint: Paint) {
        try {
            val sub = pts.copyOfRange(offset, offset + count)
            skiaCanvas?.drawLines(sub, paint.toSkia())
        } catch (e: Throwable) { logFail("drawLines", e) }
    }

    open fun drawLines(pts: FloatArray, paint: Paint) = drawLines(pts, 0, pts.size, paint)

    open fun drawRect(rect: Rect, paint: Paint) =
        drawRect(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat(), paint)

    open fun drawRect(r: RectF, paint: Paint) = drawRect(r.left, r.top, r.right, r.bottom, paint)

    open fun drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        try {
            skiaCanvas?.drawRect(org.jetbrains.skia.Rect.makeLTRB(left, top, right, bottom), paint.toSkia())
        } catch (e: Throwable) { logFail("drawRect", e) }
    }

    open fun drawRoundRect(rect: RectF, rx: Float, ry: Float, paint: Paint) =
        drawRoundRect(rect.left, rect.top, rect.right, rect.bottom, rx, ry, paint)

    open fun drawRoundRect(left: Float, top: Float, right: Float, bottom: Float, rx: Float, ry: Float, paint: Paint) {
        try {
            skiaCanvas?.drawRRect(
                org.jetbrains.skia.RRect.makeComplexLTRB(
                    left, top, right, bottom, floatArrayOf(rx, ry, rx, ry, rx, ry, rx, ry),
                ),
                paint.toSkia(),
            )
        } catch (e: Throwable) { logFail("drawRoundRect", e) }
    }

    open fun drawOval(oval: RectF, paint: Paint) = drawOval(oval.left, oval.top, oval.right, oval.bottom, paint)

    open fun drawOval(left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {
        try {
            skiaCanvas?.drawOval(org.jetbrains.skia.Rect.makeLTRB(left, top, right, bottom), paint.toSkia())
        } catch (e: Throwable) { logFail("drawOval", e) }
    }

    open fun drawCircle(cx: Float, cy: Float, radius: Float, paint: Paint) {
        try { skiaCanvas?.drawCircle(cx, cy, radius, paint.toSkia()) } catch (e: Throwable) { logFail("drawCircle", e) }
    }

    open fun drawArc(oval: RectF, startAngle: Float, sweepAngle: Float, useCenter: Boolean, paint: Paint) =
        drawArc(oval.left, oval.top, oval.right, oval.bottom, startAngle, sweepAngle, useCenter, paint)

    open fun drawArc(
        left: Float, top: Float, right: Float, bottom: Float,
        startAngle: Float, sweepAngle: Float, useCenter: Boolean, paint: Paint,
    ) {
        try {
            skiaCanvas?.drawArc(left, top, right, bottom, startAngle, sweepAngle, useCenter, paint.toSkia())
        } catch (e: Throwable) { logFail("drawArc", e) }
    }

    open fun drawPath(path: Path, paint: Paint) {
        try { skiaCanvas?.drawPath(path.skiaPath, paint.toSkia()) } catch (e: Throwable) { logFail("drawPath", e) }
    }

    open fun drawVertices(mode: VertexMode, vertexCount: Int, verts: FloatArray, vertOffset: Int, texs: FloatArray?, texOffset: Int, colors: IntArray?, colorOffset: Int, indices: ShortArray?, indexOffset: Int, indexCount: Int, paint: Paint) {
        logOnce("drawVertices 未实现")
    }

    // ---- 位图 ----
    private fun drawBitmapInternal(bmp: Bitmap, src: Rect?, dst: Rect, paint: Paint?) {
        val c = skiaCanvas ?: return
        try {
            val image = org.jetbrains.skia.Image.makeFromBitmap(bmp.skiaBitmap)
            val srcR = src?.let {
                org.jetbrains.skia.Rect.makeLTRB(it.left.toFloat(), it.top.toFloat(), it.right.toFloat(), it.bottom.toFloat())
            } ?: org.jetbrains.skia.Rect.makeLTRB(0f, 0f, bmp.width.toFloat(), bmp.height.toFloat())
            val dstR = org.jetbrains.skia.Rect.makeLTRB(
                dst.left.toFloat(), dst.top.toFloat(), dst.right.toFloat(), dst.bottom.toFloat(),
            )
            val sampling = if (paint?.isFilterBitmap == true) org.jetbrains.skia.SamplingMode.LINEAR
            else org.jetbrains.skia.SamplingMode.DEFAULT
            c.drawImageRect(image, srcR, dstR, sampling, paint?.toSkia(), true)
        } catch (e: Throwable) { logFail("drawBitmap", e) }
    }

    open fun drawBitmap(bitmap: Bitmap, left: Float, top: Float, paint: Paint?) =
        drawBitmapInternal(bitmap, null, Rect(left.toInt(), top.toInt(), (left + bitmap.width).toInt(), (top + bitmap.height).toInt()), paint)

    open fun drawBitmap(bitmap: Bitmap, src: Rect?, dst: Rect, paint: Paint?) = drawBitmapInternal(bitmap, src, dst, paint)

    open fun drawBitmap(bitmap: Bitmap, src: Rect?, dst: RectF, paint: Paint?) {
        val r = Rect()
        dst.round(r)
        drawBitmapInternal(bitmap, src, r, paint)
    }

    open fun drawBitmap(bitmap: Bitmap, matrix: Matrix, paint: Paint?) {
        val c = skiaCanvas ?: return
        c.save()
        try {
            c.concat(matrix.toSkia())
            drawBitmapInternal(bitmap, null, Rect(0, 0, bitmap.width, bitmap.height), paint)
        } finally {
            c.restore()
        }
    }

    open fun drawBitmap(colors: IntArray, offset: Int, stride: Int, x: Float, y: Float, width: Int, height: Int, hasAlpha: Boolean, paint: Paint?) {
        try {
            val bmp = Bitmap.createBitmap(colors, offset, stride, width, height, Bitmap.Config.ARGB_8888)
            drawBitmap(bmp, x, y, paint)
        } catch (e: Throwable) { logFail("drawBitmap(colors)", e) }
    }

    open fun drawBitmapMesh(bitmap: Bitmap, meshWidth: Int, meshHeight: Int, verts: FloatArray, vertOffset: Int, colors: IntArray?, colorOffset: Int, paint: Paint?) {
        drawBitmap(bitmap, 0f, 0f, paint)
    }

    // ---- 文本 ----
    open fun drawText(text: String, x: Float, y: Float, paint: Paint) {
        drawTextInternal(text, x, y, paint)
    }

    open fun drawText(text: String, start: Int, end: Int, x: Float, y: Float, paint: Paint) {
        if (start in 0..end && end <= text.length) drawTextInternal(text.substring(start, end), x, y, paint)
    }

    open fun drawText(text: CharSequence, start: Int, end: Int, x: Float, y: Float, paint: Paint) =
        drawTextInternal(text.subSequence(start, end).toString(), x, y, paint)

    open fun drawText(text: CharArray, index: Int, count: Int, x: Float, y: Float, paint: Paint) =
        drawTextInternal(String(text, index, count), x, y, paint)

    private fun drawTextInternal(text: String, x: Float, y: Float, paint: Paint) {
        val c = skiaCanvas ?: return
        try {
            val font = paint.toSkiaFont()
            val skiaPaint = paint.toSkia()
            var startX = when (paint.textAlign) {
                Paint.Align.LEFT -> x
                Paint.Align.CENTER -> x - paint.measureText(text) / 2
                Paint.Align.RIGHT -> x - paint.measureText(text)
            }
            if (paint.letterSpacing != 0f) {
                val spacing = paint.letterSpacing * paint.textSize
                for (ch in text) {
                    c.drawString(ch.toString(), startX, y, font, skiaPaint)
                    startX += font.measureTextWidth(ch.toString(), skiaPaint) + spacing
                }
            } else {
                c.drawString(text, startX, y, font, skiaPaint)
            }
        } catch (e: Throwable) { logFail("drawText", e) }
    }

    open fun drawPosText(text: String, pos: FloatArray, paint: Paint) {
        // 逐字符定位绘制
        for (i in text.indices) {
            val px = pos.getOrNull(i * 2) ?: continue
            val py = pos.getOrNull(i * 2 + 1) ?: continue
            drawTextInternal(text[i].toString(), px, py, paint)
        }
    }

    open fun drawTextOnPath(text: String, path: Path, hOffset: Float, vOffset: Float, paint: Paint) {
        logOnce("drawTextOnPath 未实现（沿路径文字）")
    }

    open fun drawTextOnPath(text: CharArray, index: Int, count: Int, path: Path, hOffset: Float, vOffset: Float, paint: Paint) {
        drawTextOnPath(String(text, index, count), path, hOffset, vOffset, paint)
    }

    // ---- 变换 ----
    open fun translate(dx: Float, dy: Float) { skiaCanvas?.translate(dx, dy) }
    open fun scale(sx: Float, sy: Float) { skiaCanvas?.scale(sx, sy) }
    open fun scale(sx: Float, sy: Float, px: Float, py: Float) {
        skiaCanvas?.translate(px, py); skiaCanvas?.scale(sx, sy); skiaCanvas?.translate(-px, -py)
    }
    open fun rotate(degrees: Float) { skiaCanvas?.rotate(degrees) }
    open fun rotate(degrees: Float, px: Float, py: Float) {
        skiaCanvas?.translate(px, py); skiaCanvas?.rotate(degrees); skiaCanvas?.translate(-px, -py)
    }
    open fun skew(sx: Float, sy: Float) { skiaCanvas?.skew(sx, sy) }
    open fun concat(matrix: Matrix?) { matrix?.let { skiaCanvas?.concat(it.toSkia()) } }
    open fun setMatrix(matrix: Matrix?) { matrix?.let { skiaCanvas?.setMatrix(it.toSkia()) } }

    open fun save(): Int {
        val count = try { skiaCanvas?.save() ?: saveDepth } catch (e: Throwable) { saveDepth }
        saveDepth++
        return count
    }

    open fun saveLayer(bounds: RectF?, paint: Paint?): Int {
        val count = try { skiaCanvas?.saveLayer(bounds?.let { org.jetbrains.skia.Rect.makeLTRB(it.left, it.top, it.right, it.bottom) }, paint?.toSkia()) ?: saveDepth } catch (e: Throwable) { saveDepth }
        saveDepth++
        return count
    }

    open fun saveLayer(left: Float, top: Float, right: Float, bottom: Float, paint: Paint?): Int =
        saveLayer(RectF(left, top, right, bottom), paint)

    open fun saveLayerAlpha(bounds: RectF?, alpha: Int): Int {
        val p = Paint().apply { this.alpha = alpha }
        return saveLayer(bounds, p)
    }

    open fun saveLayerAlpha(left: Float, top: Float, right: Float, bottom: Float, alpha: Int): Int =
        saveLayerAlpha(RectF(left, top, right, bottom), alpha)

    open fun restore() {
        try { skiaCanvas?.restore() } catch (e: Throwable) { logFail("restore", e) }
        if (saveDepth > 0) saveDepth--
    }

    open fun restoreToCount(saveCount: Int) {
        while (saveDepth > saveCount) restore()
    }

    open fun getSaveCount(): Int = saveDepth

    // ---- 裁剪 ----
    open fun clipRect(rect: RectF): Boolean {
        return try {
            skiaCanvas?.clipRect(org.jetbrains.skia.Rect.makeLTRB(rect.left, rect.top, rect.right, rect.bottom))
            true
        } catch (e: Throwable) { false }
    }

    open fun clipRect(rect: Rect): Boolean = clipRect(RectF(rect))

    open fun clipRect(rect: RectF, op: Region.Op): Boolean =
        clipRect(rect)

    open fun clipRect(rect: Rect, op: Region.Op): Boolean = clipRect(RectF(rect))

    open fun clipRect(left: Float, top: Float, right: Float, bottom: Float): Boolean =
        clipRect(RectF(left, top, right, bottom))

    open fun clipRect(left: Float, top: Float, right: Float, bottom: Float, op: Region.Op): Boolean =
        clipRect(RectF(left, top, right, bottom))

    open fun clipRect(left: Int, top: Int, right: Int, bottom: Int): Boolean =
        clipRect(Rect(left, top, right, bottom))

    open fun clipOutRect(rect: Rect): Boolean = clipRect(rect)
    open fun clipOutRect(rect: RectF): Boolean = clipRect(rect)
    open fun clipOutRect(left: Int, top: Int, right: Int, bottom: Int): Boolean = clipRect(left, top, right, bottom)
    open fun clipOutRect(left: Float, top: Float, right: Float, bottom: Float): Boolean = clipRect(left, top, right, bottom)

    open fun clipPath(path: Path): Boolean {
        return try {
            skiaCanvas?.clipPath(path.skiaPath)
            true
        } catch (e: Throwable) { false }
    }

    open fun clipPath(path: Path, op: Region.Op): Boolean = clipPath(path)

    open fun clipOutPath(path: Path): Boolean = clipPath(path)

    // ---- 查询 ----
    open fun quickReject(rect: RectF, type: EdgeType): Boolean = false
    open fun quickReject(path: Path, type: EdgeType): Boolean = false
    open fun quickReject(left: Float, top: Float, right: Float, bottom: Float, type: EdgeType): Boolean = false
    open fun quickReject(rect: RectF): Boolean = false
    open fun quickReject(path: Path): Boolean = false
    open fun getClipBounds(bounds: Rect): Boolean {
        bounds.set(0, 0, width, height)
        return true
    }

    open fun getMatrix(): Matrix = Matrix()
    open fun getMatrix(matrix: Matrix) { matrix.reset() }

    open fun release() { skiaCanvas = null }

    enum class EdgeType { BW, AA }
    enum class VertexMode { TRIANGLES, TRIANGLE_STRIP, TRIANGLE_FAN }

    private fun logFail(op: String, e: Throwable) {
        Log.w("Canvas", "$op 失败: ${e.message}")
    }

    private var loggedOnce = false
    private fun logOnce(msg: String) {
        if (!loggedOnce) { loggedOnce = true; Log.w("Canvas", msg) }
    }
}
