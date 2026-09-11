package com.tom_roush.pdfbox.pdmodel

import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import java.io.Closeable

/** 门面对象：包装 org.apache.pdfbox.pdmodel.PDPageContentStream（见 PDDocument 说明）。 */
class PDPageContentStream(document: PDDocument, page: PDPage) : Closeable {
    internal val real =
        org.apache.pdfbox.pdmodel.PDPageContentStream(document.real, page.real)

    fun beginText() = real.beginText()
    fun endText() = real.endText()
    fun setFont(font: PDType1Font, fontSize: Float) = real.setFont(font.real, fontSize)
    fun setLeading(leading: Float) = real.setLeading(leading)
    fun newLineAtOffset(tx: Float, ty: Float) = real.newLineAtOffset(tx, ty)
    fun newLine() = real.newLine()
    fun showText(text: String?) = real.showText(text ?: "")

    override fun close() = real.close()
}
