package com.iyxan23.zipalignjava

import java.io.OutputStream
import java.io.RandomAccessFile

/**
 * com.iyxan23.zipalignjava.ZipAlign 桌面 stub（B11h）。
 * zipalign-java 是纯 JVM 库但坐标/桌面适配未验证，先提供编译占位。
 * 真实的 APK 对齐（zipalign）功能后续接真库或验证坐标后实装。——Nova 注
 */
object ZipAlign {
    /**
     * ZipAlign.alignZip：把输入 zip 对齐后写出。桌面占位：直接透传拷贝
     * （不做对齐；对齐对桌面侧 APK 逆向分析流程非关键路径）。
     */
    @JvmStatic
    fun alignZip(raf: RandomAccessFile, out: OutputStream, alignment: Int, bufferSize: Int) {
        raf.seek(0)
        val buf = ByteArray(bufferSize.coerceAtLeast(4096))
        while (true) {
            val n = raf.read(buf)
            if (n <= 0) break
            out.write(buf, 0, n)
        }
        out.flush()
    }
}
