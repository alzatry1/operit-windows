package androidx.core.net

import android.net.Uri
import java.io.File

/**
 * androidx.core.net.UriKt 桌面版。
 * 仅支持 file:// scheme（与真实实现一致：非 file scheme 抛 IllegalArgumentException）。
 */
fun Uri.toFile(): File {
    require("file" == scheme) { "Uri lacks 'file' scheme: $this" }
    return File(path ?: "")
}
