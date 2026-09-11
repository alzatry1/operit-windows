package androidx.glance

import androidx.glance.unit.ColorProvider

/**
 * androidx.glance.GlanceModifier 桌面 stub。
 * 桌面无 RemoteViews 小部件宿主，修饰符仅作为不可变标记存在（链式调用返回自身）。
 * 伴生对象继承本类，复刻真实 API 中 `GlanceModifier` 既当类型又当默认实例的用法。
 * 注意：必须 open——final 类不能被伴生对象继承。
 */
open class GlanceModifier {
    companion object : GlanceModifier()
}

/** androidx.glance.background：背景色修饰（stub，丢弃并返回自身）。 */
fun GlanceModifier.background(colorProvider: ColorProvider): GlanceModifier = this
