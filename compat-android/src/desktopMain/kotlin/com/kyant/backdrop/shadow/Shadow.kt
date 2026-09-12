package com.kyant.backdrop.shadow

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** com.kyant.backdrop.shadow.Shadow 垫片。真库 radius 为 Dp。——Nova 注 */
class Shadow(
    val radius: Dp = 0.dp,
    val color: Color = Color.Unspecified,
    val offsetX: Dp = 0.dp,
    val offsetY: Dp = 0.dp,
) {
    companion object {
        val Plain: Shadow = Shadow()
    }
}
