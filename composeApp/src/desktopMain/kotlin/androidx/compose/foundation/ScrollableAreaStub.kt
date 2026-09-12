package androidx.compose.foundation

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

/**
 * Modifier.scrollableArea 桌面 no-op stub。
 * scrollableArea 是 Compose foundation 的 Android 内部修饰符，CMP desktop 1.9.0 未提供；
 * 桌面端用不到 beyond-bounds 滚动区域，no-op 透传 Modifier。——Nova 注
 */
fun Modifier.scrollableArea(
    state: ScrollableState,
    orientation: Orientation,
    enabled: Boolean = true,
    reverseScrolling: Boolean = false,
    flingBehavior: FlingBehavior? = null,
    interactionSource: MutableInteractionSource? = null,
    overscrollEffect: OverscrollEffect? = null,
): Modifier = this
