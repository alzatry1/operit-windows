package androidx.glance.unit

import androidx.compose.ui.graphics.Color

/**
 * androidx.glance.unit.ColorProvider 垫片（旧路径，已废弃，转发到 color 包）。——Nova 注
 */
@Deprecated("迁移到 androidx.glance.color.ColorProvider")
class ColorProvider(val day: Color, val night: Color = day) {
    constructor(resId: Int) : this(Color.Unspecified)
}
