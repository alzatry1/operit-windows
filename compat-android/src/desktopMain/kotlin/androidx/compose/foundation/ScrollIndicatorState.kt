package androidx.compose.foundation

/**
 * androidx.compose.foundation.ScrollIndicatorState 桌面垫片。
 * 滚动指示器状态（新版 foundation 提供；CMP 1.9 的 foundation 尚无此接口，app 的 fork 版 LazyListState 引用它）。——Nova 注
 */
interface ScrollIndicatorState {
    /** 当前滚动偏移（px）。 */
    val scrollOffset: Int

    /** 内容总大小（px）。 */
    val contentSize: Int

    /** 视口大小（px）。 */
    val viewportSize: Int

    /** 最大可滚动偏移。 */
    val maximumScrollOffset: Int get() = (contentSize - viewportSize).coerceAtLeast(0)
}
