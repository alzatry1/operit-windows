package com.kyant.backdrop.highlight

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** com.kyant.backdrop.highlight.Highlight 垫片。真库 width/blurRadius 为 Dp、有 alpha。——Nova 注 */
class Highlight(
    val width: Dp = 0.dp,
    val blurRadius: Dp = 0.dp,
    val alpha: Float = 1f,
    val color: Color = Color.Unspecified,
) {
    companion object {
        val Plain: Highlight = Highlight()
    }
}
