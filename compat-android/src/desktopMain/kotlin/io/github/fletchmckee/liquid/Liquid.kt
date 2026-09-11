package io.github.fletchmckee.liquid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * io.github.fletchmckee.liquid 液态玻璃库的桌面编译级垫片。
 * 桌面 v1 不实际渲染液化效果，布局与可组合结构保持不变。——Nova 注
 */

/** 液态效果状态载体。 */
class LiquidState {
    internal var shape: Shape? = null
    internal var frost: Dp = 0.dp
    internal var curve: Float = 0f
    internal var refractionHeight: Float = 0f
    internal var refractionAmount: Float = 0f
}

/** liquid 配置块作用域（app 代码里设置 shape/frost/curve 等）。 */
class LiquidScope internal constructor(internal val state: LiquidState) {
    var shape: Shape?
        get() = state.shape
        set(v) { state.shape = v }
    var frost: Dp
        get() = state.frost
        set(v) { state.frost = v }
    var curve: Float
        get() = state.curve
        set(v) { state.curve = v }
    var refractionHeight: Float
        get() = state.refractionHeight
        set(v) { state.refractionHeight = v }
    var refractionAmount: Float
        get() = state.refractionAmount
        set(v) { state.refractionAmount = v }
}

@Composable
fun rememberLiquidState(): LiquidState = remember { LiquidState() }

/** Modifier.liquid：垫片恒等。 */
fun Modifier.liquid(state: LiquidState, block: LiquidScope.() -> Unit): Modifier {
    LiquidScope(state).block()
    return this
}

/** Modifier.liquefiable：垫片恒等。 */
fun Modifier.liquefiable(state: LiquidState): Modifier = this
