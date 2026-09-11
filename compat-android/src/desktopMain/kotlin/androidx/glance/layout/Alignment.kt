package androidx.glance.layout

/**
 * androidx.glance.layout.Alignment 桌面 stub。
 * 结构与真实 API 一致：双向对齐常量 + Vertical/Horizontal 子接口。
 * 注意初始化顺序：Vertical/Horizontal 实例必须先于双向组合常量声明。
 */
interface Alignment {

    interface Horizontal : Alignment

    interface Vertical : Alignment

    companion object {
        val Top: Vertical = VerticalAlignmentImpl("Top")
        val CenterVertically: Vertical = VerticalAlignmentImpl("CenterVertically")
        val Bottom: Vertical = VerticalAlignmentImpl("Bottom")

        val Start: Horizontal = HorizontalAlignmentImpl("Start")
        val CenterHorizontally: Horizontal = HorizontalAlignmentImpl("CenterHorizontally")
        val End: Horizontal = HorizontalAlignmentImpl("End")

        val TopStart: Alignment = BiAlignment(Top, Start)
        val TopCenter: Alignment = BiAlignment(Top, CenterHorizontally)
        val TopEnd: Alignment = BiAlignment(Top, End)
        val CenterStart: Alignment = BiAlignment(CenterVertically, Start)
        val Center: Alignment = BiAlignment(CenterVertically, CenterHorizontally)
        val CenterEnd: Alignment = BiAlignment(CenterVertically, End)
        val BottomStart: Alignment = BiAlignment(Bottom, Start)
        val BottomCenter: Alignment = BiAlignment(Bottom, CenterHorizontally)
        val BottomEnd: Alignment = BiAlignment(Bottom, End)
    }
}

private class BiAlignment(val vertical: Alignment.Vertical, val horizontal: Alignment.Horizontal) : Alignment {
    override fun toString(): String = "BiAlignment($vertical, $horizontal)"
}

private class VerticalAlignmentImpl(private val name: String) : Alignment.Vertical {
    override fun toString(): String = name
}

private class HorizontalAlignmentImpl(private val name: String) : Alignment.Horizontal {
    override fun toString(): String = name
}
