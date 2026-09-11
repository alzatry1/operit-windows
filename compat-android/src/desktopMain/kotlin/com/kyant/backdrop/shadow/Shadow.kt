package com.kyant.backdrop.shadow

import androidx.compose.ui.graphics.Color

/** com.kyant.backdrop.shadow.Shadow 垫片。 */
class Shadow(val color: Color, val offsetX: Float = 0f, val offsetY: Float = 0f, val blurRadius: Float = 0f) {
    companion object {
        val Plain: Shadow = Shadow(Color.Unspecified)
    }
}
