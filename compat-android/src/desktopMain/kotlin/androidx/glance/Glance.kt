package androidx.glance

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * androidx.glance 桌面垫片：Glance 是 Android 主屏小组件框架，桌面没有小组件。
 * 这里提供编译形状，让 widget 代码可编译（运行时为 no-op）。——Nova 注
 */

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.BINARY)
annotation class ExperimentalGlanceApi

interface GlanceId

/** GlanceModifier：Glance 用自己的修饰符（不是 Compose Modifier）。 */
class GlanceModifier {
    companion object : GlanceModifier()
}

// ---- 修饰符扩展（返回自身，编译形状）----
fun GlanceModifier.background(color: Color): GlanceModifier = this
fun GlanceModifier.background(colorProvider: androidx.glance.unit.ColorProvider): GlanceModifier = this
fun GlanceModifier.padding(all: Dp): GlanceModifier = this
fun GlanceModifier.padding(horizontal: Dp = Dp(0f), vertical: Dp = Dp(0f)): GlanceModifier = this
fun GlanceModifier.padding(start: Dp = Dp(0f), top: Dp = Dp(0f), end: Dp = Dp(0f), bottom: Dp = Dp(0f)): GlanceModifier = this
fun GlanceModifier.fillMaxSize(): GlanceModifier = this
fun GlanceModifier.fillMaxWidth(): GlanceModifier = this
fun GlanceModifier.fillMaxHeight(): GlanceModifier = this
fun GlanceModifier.height(height: Dp): GlanceModifier = this
fun GlanceModifier.width(width: Dp): GlanceModifier = this
fun GlanceModifier.size(size: Dp): GlanceModifier = this
fun GlanceModifier.size(width: Dp, height: Dp): GlanceModifier = this
fun GlanceModifier.clickable(onClick: androidx.glance.action.Action): GlanceModifier = this
fun GlanceModifier.clickable(block: () -> Unit): GlanceModifier = this
fun GlanceModifier.cornerRadius(radius: Dp): GlanceModifier = this

/** ImageProvider：Glance 的图片来源包装。 */
class ImageProvider {
    constructor(resId: Int)
    constructor(bitmap: android.graphics.Bitmap)
    constructor(bytes: ByteArray)
}

/** Glance 主题。 */
object GlanceTheme {
    val colors: Any get() = Unit
}

/** Glance Image 可组合函数。 */
@Composable
fun Image(provider: ImageProvider, contentDescription: String?, modifier: GlanceModifier = GlanceModifier, contentScale: Any? = null) {}
