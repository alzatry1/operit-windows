package androidx.compose.ui.input.pointer

import androidx.compose.ui.Modifier

/**
 * androidx.compose.ui.input.pointer.pointerInteropFilter 的桌面垫片。
 * 该 API 用于把 Android MotionEvent 透传给 Android View；桌面无此概念，no-op。——Nova 注
 */
fun Modifier.pointerInteropFilter(
    requestDisallowInterceptTouchEvent: Any? = null,
    onTouchEvent: (android.view.MotionEvent) -> Boolean,
): Modifier = this
