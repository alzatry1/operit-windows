package ru.noties.jlatexmath

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import org.scilab.forge.jlatexmath.TeXConstants
import org.scilab.forge.jlatexmath.TeXFormula
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * ru.noties.jlatexmath.JLatexMathDrawable 的桌面移植版。
 * Android 原版把 LaTeX 渲染进 Drawable；这里用桌面版 jlatexmath（org.scilab.forge）
 * 真实渲染成位图，再经我们的 Canvas→Skia 桥绘制。——Nova 注
 */
class JLatexMathDrawable private constructor(private val latex: String) : Drawable() {

    private var textSizePx: Float = 24f
    private var paddingPx: Int = 0
    private var backgroundColor: Int = 0
    private var alignMode: Int = ALIGN_LEFT
    private var textColor: Int = 0xFF000000.toInt()

    @Volatile
    private var cachedBitmap: Bitmap? = null

    companion object {
        const val ALIGN_LEFT = 0
        const val ALIGN_CENTER = 1
        const val ALIGN_RIGHT = 2

        @JvmStatic
        fun builder(latex: String): JLatexMathDrawable = JLatexMathDrawable(latex)
    }

    fun textSize(px: Float): JLatexMathDrawable = apply { textSizePx = px; cachedBitmap = null }
    fun padding(px: Int): JLatexMathDrawable = apply { paddingPx = px; cachedBitmap = null }
    fun background(color: Int): JLatexMathDrawable = apply { backgroundColor = color; cachedBitmap = null }
    fun align(align: Int): JLatexMathDrawable = apply { alignMode = align; cachedBitmap = null }
    fun color(color: Int): JLatexMathDrawable = apply { textColor = color; cachedBitmap = null }

    /** 用 jlatexmath 把公式渲染成位图（带缓存）。 */
    private fun renderBitmap(): Bitmap? {
        cachedBitmap?.let { return it }
        return try {
            val formula = TeXFormula(latex)
            val icon = formula.TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(textSizePx / 1.5f) // jlatexmath 的 size 是 pt 量级，粗略换算
                .setFGColor(java.awt.Color(textColor, true))
                .build()
            val awtImage = BufferedImage(
                icon.iconWidth + paddingPx * 2,
                icon.iconHeight + paddingPx * 2,
                BufferedImage.TYPE_INT_ARGB
            )
            val g = awtImage.createGraphics()
            g.color = java.awt.Color(backgroundColor, true)
            g.fillRect(0, 0, awtImage.width, awtImage.height)
            icon.paintIcon(null, g, paddingPx, paddingPx)
            g.dispose()
            val baos = ByteArrayOutputStream()
            ImageIO.write(awtImage, "png", baos)
            val bmp = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size())
            cachedBitmap = bmp
            bmp
        } catch (e: Exception) {
            null
        }
    }

    override val intrinsicWidth: Int
        get() = renderBitmap()?.width ?: 0

    override val intrinsicHeight: Int
        get() = renderBitmap()?.height ?: 0

    override fun draw(canvas: Canvas) {
        val bmp = renderBitmap() ?: return
        canvas.drawBitmap(bmp, bounds.left.toFloat(), bounds.top.toFloat(), null)
    }
}
