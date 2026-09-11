package com.tom_roush.pdfbox.text

import com.tom_roush.pdfbox.pdmodel.PDDocument

/** 门面对象：包装 org.apache.pdfbox.text.PDFTextStripper（见 PDDocument 说明）。 */
class PDFTextStripper {
    private val real = org.apache.pdfbox.text.PDFTextStripper()

    fun getText(document: PDDocument): String = real.getText(document.real)
}
