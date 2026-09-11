package androidx.glance

import androidx.compose.runtime.Composable
import androidx.glance.color.ColorProviders

/**
 * androidx.glance 核心杂项桌面 stub：GlanceId / ExperimentalGlanceApi / GlanceTheme。
 * 桌面无小部件宿主，所有 Glance composable 只做结构占位、不产生真实渲染。
 */

/** androidx.glance.GlanceId：真实实现是接口，标记一个小部件实例。 */
interface GlanceId

/** androidx.glance.ExperimentalGlanceApi。 */
@RequiresOptIn(message = "This Glance API is experimental and may change in the future.")
annotation class ExperimentalGlanceApi

/**
 * androidx.glance.GlanceTheme：真实实现里同名的 object 与 @Composable 顶层函数并存。
 * colors 属性在桌面返回空占位。
 */
object GlanceTheme {
    val colors: ColorProviders
        @Composable
        get() = ColorProviders()
}

/** 顶层 @Composable GlanceTheme：桌面仅透传 content（不建立真实 CompositionLocal 主题）。 */
@Composable
fun GlanceTheme(colors: ColorProviders = GlanceTheme.colors, content: @Composable () -> Unit) {
    content()
}
