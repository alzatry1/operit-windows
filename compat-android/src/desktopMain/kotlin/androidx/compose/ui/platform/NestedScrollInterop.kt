package androidx.compose.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection

/**
 * androidx.compose.ui.platform.rememberNestedScrollInteropConnection 的桌面垫片。
 * CMP Desktop 没有这个 API（它是 Android View 互操作用）。桌面无 Android View 树，
 * 返回一个 no-op 的 NestedScrollConnection。——Nova 注
 */
@Composable
fun rememberNestedScrollInteropConnection(): NestedScrollConnection {
    return remember { object : NestedScrollConnection {} }
}
