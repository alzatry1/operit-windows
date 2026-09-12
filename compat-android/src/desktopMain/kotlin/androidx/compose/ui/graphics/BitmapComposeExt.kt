package androidx.compose.ui.graphics

/**
 * android.graphics.Bitmap（compat 包壳 org.jetbrains.skia.Bitmap）的 asImageBitmap 扩展。
 * 真实 Compose Desktop 的 asImageBitmap 扩展只认 skia.Bitmap，不认 compat android.graphics.Bitmap；
 * app import androidx.compose.ui.graphics.asImageBitmap 后对 compat Bitmap 调用无候选。
 * 这里委托内部 skiaBitmap 的真实扩展。同模块 internal 可见。——Nova 注
 */
fun android.graphics.Bitmap.asImageBitmap(): ImageBitmap = this.skiaBitmap.asImageBitmap()
