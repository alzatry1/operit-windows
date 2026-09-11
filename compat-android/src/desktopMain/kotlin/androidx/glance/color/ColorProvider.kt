package androidx.glance.color

import androidx.compose.ui.graphics.Color

/** androidx.glance.color.ColorProvider 垫片。——Nova 注 */
class ColorProvider(val day: Color, val night: Color = day) {
    constructor(resId: Int) : this(Color.Unspecified)
}

/** 常用构造扩展。 */
fun ColorProvider(dayNightResId: Int, other: Int): ColorProvider = ColorProvider(Color.Unspecified)
