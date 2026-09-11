package androidx.core.graphics

/**
 * androidx.core.graphics.Insets 桌面版：不可变四元组。
 */
class Insets private constructor(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
) {
    override fun equals(other: Any?): Boolean =
        other is Insets && left == other.left && top == other.top &&
            right == other.right && bottom == other.bottom

    override fun hashCode(): Int = ((left * 31 + top) * 31 + right) * 31 + bottom

    override fun toString(): String = "Insets{left=$left, top=$top, right=$right, bottom=$bottom}"

    companion object {
        @JvmField val NONE = Insets(0, 0, 0, 0)

        @JvmStatic
        fun of(left: Int, top: Int, right: Int, bottom: Int): Insets = Insets(left, top, right, bottom)

        @JvmStatic
        fun of(r: android.graphics.Rect): Insets = of(r.left, r.top, r.right, r.bottom)

        @JvmStatic
        fun add(a: Insets, b: Insets): Insets =
            of(a.left + b.left, a.top + b.top, a.right + b.right, a.bottom + b.bottom)

        @JvmStatic
        fun subtract(a: Insets, b: Insets): Insets =
            of(a.left - b.left, a.top - b.top, a.right - b.right, a.bottom - b.bottom)

        @JvmStatic
        fun max(a: Insets, b: Insets): Insets =
            of(
                maxOf(a.left, b.left), maxOf(a.top, b.top),
                maxOf(a.right, b.right), maxOf(a.bottom, b.bottom),
            )

        @JvmStatic
        fun min(a: Insets, b: Insets): Insets =
            of(
                minOf(a.left, b.left), minOf(a.top, b.top),
                minOf(a.right, b.right), minOf(a.bottom, b.bottom),
            )

        @JvmStatic
        fun toCompatInsets(insets: Any?): Insets = NONE
    }
}
