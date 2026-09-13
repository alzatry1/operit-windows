package androidx.compose.material3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * androidx.compose.material3 的 adaptive / 新版组件桌面 stub（B9d）。
 *
 * 桌面 CMP material3-desktop:1.8.2 未提供 WideNavigationRail / ModalWideNavigationRail /
 * ShortNavigationBar(Item) / TimePickerDialog / VerticalDragHandle 等组件，
 * composedsl 渲染器按真实签名引用它们。这里按渲染器实际用到的命名参数给最小实现：
 * 结构上用 Row/Column 占位，保证 @Composable 作用域正确、签名可解析。——Nova 注
 */

@Composable
fun WideNavigationRail(
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    arrangement: Arrangement.Vertical? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        if (header != null) header()
        if (content != null) content()
    }
}

@Composable
fun ModalWideNavigationRail(
    modifier: Modifier = Modifier,
    hideOnCollapse: Boolean = false,
    expandedHeaderTopPadding: Dp = 0.dp,
    shape: Shape? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    tonalElevation: Dp = 0.dp,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    arrangement: Arrangement.Vertical? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        if (header != null) header()
        if (content != null) content()
    }
}

@Composable
fun ShortNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color? = null,
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(modifier = modifier) {
        if (content != null) content()
    }
}

@Composable
fun ShortNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: (@Composable () -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    Column(modifier = modifier) {
        if (icon != null) icon()
        if (label != null) label()
    }
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    dismissButton: (@Composable () -> Unit)? = null,
    shape: Shape? = null,
    containerColor: Color = Color.Unspecified,
    title: (@Composable () -> Unit)? = null,
    modeToggleButton: (@Composable () -> Unit)? = null,
    /** content 带 ColumnScope receiver（app 在 content lambda 里调 ColumnScope 扩展如 columnComposeDslModifierResolver）。——Nova 注 */
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    // 桌面端时间选择对话框占位：仅渲染内容槽，不弹真实系统对话框
    if (content != null) Column { content() }
}

@Composable
fun WideNavigationRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: (@Composable () -> Unit)? = null,
    railExpanded: Boolean = false,
) {
    Column(modifier = modifier) {
        if (icon != null) icon()
        if (label != null) label()
    }
}

@Composable
fun VerticalDragHandle(
    modifier: Modifier = Modifier,
    contentColor: Color? = null,
) {
    // 桌面端拖拽手柄占位（composedsl 生成的可拖拽分栏用）
    Row(modifier = modifier) {}
}
