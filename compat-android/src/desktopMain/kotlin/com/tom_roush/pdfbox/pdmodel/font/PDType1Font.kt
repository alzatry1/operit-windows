package com.tom_roush.pdfbox.pdmodel.font

import org.apache.pdfbox.pdmodel.font.Standard14Fonts

/**
 * 门面对象：包装 org.apache.pdfbox.pdmodel.font.PDType1Font（见 PDDocument 说明）。
 * pdfbox 3.x 移除了 2.x 的静态字体常量，这里以 Standard14Fonts 重建。
 */
class PDType1Font private constructor(internal val real: org.apache.pdfbox.pdmodel.font.PDType1Font) {
    companion object {
        @JvmField
        val HELVETICA =
            PDType1Font(org.apache.pdfbox.pdmodel.font.PDType1Font(Standard14Fonts.FontName.HELVETICA))

        @JvmField
        val HELVETICA_BOLD =
            PDType1Font(org.apache.pdfbox.pdmodel.font.PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD))
    }
}
