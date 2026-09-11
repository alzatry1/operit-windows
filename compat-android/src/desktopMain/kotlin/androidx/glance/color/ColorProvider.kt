package androidx.glance.color

/**
 * androidx.glance.color 桌面 stub。
 * 真实 glance 里 color.ColorProvider 是 unit.ColorProvider 的旧名别名——同构 typealias 复刻，
 * 两个包的 import 路径解析到同一个类。
 */
typealias ColorProvider = androidx.glance.unit.ColorProvider

/**
 * androidx.glance.color.ColorProviders 最小占位（GlanceTheme.colors 的返回类型）。
 * 桌面无小部件主题体系，仅保存传入值。
 */
class ColorProviders(val light: ColorProvider? = null, val dark: ColorProvider? = null)
