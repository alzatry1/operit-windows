package com.tom_roush.pdfbox.android

import android.content.Context

/**
 * PDFBoxResourceLoader 垫片（P3-B3）。
 * Android 上用于把 pdfbox 的资源表指向 app assets；桌面 JVM 版 pdfbox 直接从 jar 读资源，
 * init 为 no-op。
 */
object PDFBoxResourceLoader {
    @Volatile
    private var initialized = false

    @JvmStatic
    fun init(context: Context?) {
        initialized = true
    }

    @JvmStatic
    fun isReady(): Boolean = initialized
}
