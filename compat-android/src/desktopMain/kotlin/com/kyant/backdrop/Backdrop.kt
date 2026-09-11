package com.kyant.backdrop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * com.kyant.backdrop 液态玻璃特效库的桌面编译级垫片。
 * Android 上通过渲染层捕获实现玻璃拟态；桌面版 v1 降级为普通绘制（不玻璃），
 * 视觉实装见 MIGRATION-PLAN P7。——Nova 注
 */

/** 背景层数据载体。 */
class Backdrop {
    // 真实实现记录被采样背景；垫片为占位。
}

/** Backdrop 作用域：承载 highlight/shadow/effects 配置。 */
class BackdropScope internal constructor() {
    var shape: Shape? = null
    var highlight: Any? = null
    var shadow: Any? = null
    var effectsBlock: Any? = null
}

/** effects 配置块作用域。 */
class BackdropEffectsScope {
    /** 供 vibrancy/blur/lens 等扩展注册。 */
    internal val applied = mutableListOf<Any>()
}

/**
 * Modifier.drawBackdrop：真实实现把背景采样+特效应用到本组件。
 * 垫片：恒等（不附加特效），保证编译与布局不变。
 */
fun Modifier.drawBackdrop(
    backdrop: Backdrop,
    shape: () -> Shape,
    highlight: (() -> Any?)? = null,
    shadow: (() -> Any?)? = null,
    effects: BackdropEffectsScope.() -> Unit = {},
    layerBlock: (BackdropScope.() -> Unit)? = null,
    onDrawBehind: (DrawScope.() -> Unit)? = null,
    onDrawSurface: (DrawScope.() -> Unit)? = null,
): Modifier = this
