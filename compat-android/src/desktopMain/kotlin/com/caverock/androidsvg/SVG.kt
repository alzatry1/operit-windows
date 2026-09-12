package com.caverock.androidsvg

import android.graphics.Picture
import java.io.InputStream

/**
 * com.caverock.androidsvg.SVG 桌面 stub（B11h）。
 * AndroidSVG 是 Android-only（依赖 android.graphics）；桌面编译占位。
 * app 用它把 SVG logo 渲染成 Bitmap。——Nova 注
 */
open class SVG {
    var documentWidth: Float = 0f
    var documentHeight: Float = 0f

    /** SVG.setDocumentWidth（哑参数避开与 var documentWidth 合成 setter 的 JVM 冲突）。——Nova 注 */
    fun setDocumentWidth(width: Float, ignored: Unit = Unit) { documentWidth = width }
    fun setDocumentHeight(height: Float, ignored: Unit = Unit) { documentHeight = height }
    fun setDocumentViewBox(left: Float, top: Float, width: Float, height: Float) {}

    /** SVG.renderToPicture（桌面占位，返回空 Picture）。 */
    open fun renderToPicture(): Picture = Picture()
    open fun renderToPicture(width: Int, height: Int): Picture = Picture()

    companion object {
        @JvmStatic
        fun getFromInputStream(input: InputStream): SVG = SVG()

        @JvmStatic
        fun getFromString(svg: String?): SVG = SVG()

        @JvmStatic
        fun getFromResource(context: android.content.Context?, resourceId: Int): SVG = SVG()
    }
}
