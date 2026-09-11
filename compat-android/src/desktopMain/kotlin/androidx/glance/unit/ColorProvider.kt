package androidx.glance.unit

import androidx.compose.ui.graphics.Color

/**
 * androidx.glance.unit.ColorProvider 桌面版。
 * 包装 androidx.compose.ui.graphics.Color；day/night 构造复刻真实 API 的昼夜双值语义
 *（桌面无小部件主题求值，两个值都仅保存不消费）。
 */
class ColorProvider {
    val day: Color
    val night: Color

    constructor(color: Color) {
        this.day = color
        this.night = color
    }

    constructor(day: Color, night: Color) {
        this.day = day
        this.night = night
    }

    constructor(color: Int) : this(Color(color))

    val color: Color
        get() = day
}
