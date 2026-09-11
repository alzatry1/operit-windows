package com.tom_roush.pdfbox.pdmodel

import java.io.Closeable
import java.io.File

/**
 * com.tom_roush:pdfbox-android 的桌面门面（P3-B3）。
 * pdfbox-android 是 AAR 工件无法在桌面 JVM 使用；这里以真实 org.apache.pdfbox 3.x 为后端，
 * 保持 app 侧 2.x 风格调用（PDDocument.load / PDType1Font.HELVETICA）零修改编译。
 */
class PDDocument private constructor(internal val real: org.apache.pdfbox.pdmodel.PDDocument) :
    Closeable {

    constructor() : this(org.apache.pdfbox.pdmodel.PDDocument())

    val isEncrypted: Boolean
        get() = real.isEncrypted

    val numberOfPages: Int
        get() = real.numberOfPages

    fun addPage(page: PDPage) = real.addPage(page.real)

    fun setAllSecurityToBeRemoved(remove: Boolean) {
        real.setAllSecurityToBeRemoved(remove)
    }

    fun save(file: File) = real.save(file)

    override fun close() = real.close()

    companion object {
        @JvmStatic
        fun load(file: File): PDDocument =
            PDDocument(org.apache.pdfbox.Loader.loadPDF(file))
    }
}
