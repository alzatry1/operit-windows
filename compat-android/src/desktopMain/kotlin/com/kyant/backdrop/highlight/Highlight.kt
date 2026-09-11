package com.kyant.backdrop.highlight

import androidx.compose.ui.graphics.Color

/** com.kyant.backdrop.highlight.Highlight 垫片。 */
class Highlight(val color: Color, val width: Float = 0f, val blurRadius: Float = 0f) {
    companion object {
        val Plain: Highlight = Highlight(Color.Unspecified)
    }
}
