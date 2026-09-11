package androidx.compose.ui.tooling.preview

/**
 * androidx.compose.ui.tooling.preview.Preview 桌面垫片。
 * 桌面 CMP 的 @Preview 在 androidx.compose.desktop.ui.tooling.preview.Preview；
 * app 写的是 common 路径 androidx.compose.ui.tooling.preview.Preview。
 * 这是纯设计时注解（无运行时行为），垫片安全。——Nova 注
 */
@MustBeDocumented
@Repeatable
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Preview(
    val name: String = "",
    val group: String = "",
    @Deprecated("apiLevel 已废弃") val apiLevel: Int = -1,
    val widthDp: Int = -1,
    val heightDp: Int = -1,
    val locale: String = "",
    val fontScale: Float = 1f,
    val showSystemUi: Boolean = false,
    val showBackground: Boolean = false,
    val backgroundColor: Long = 0,
    val uiMode: Int = 0,
    val device: String = "",
    val wallpaper: Int = -1,
    val showDecorations: Boolean = false,
)
