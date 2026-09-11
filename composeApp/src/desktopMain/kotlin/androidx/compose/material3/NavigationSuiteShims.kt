package androidx.compose.material3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * material3 导航套件（NavigationSuite）新版组件的桌面垫片。
 * CMP 1.9 的 material3 在 androidx.compose.material3 路径下没有这三个组件
 * （它们在更新的 material3 / adaptive-navigation-suite 里）。这里提供编译形状 + 合理渲染。
 * ——Nova 注
 */

@Composable
fun ModalWideNavigationRail(
    modifier: Modifier = Modifier,
    hideOnCollapse: Boolean = false,
    header: @Composable (() -> Unit)? = null,
    colors: Any? = null,
    content: @Composable () -> Unit = {},
) {
    Column(modifier = modifier.fillMaxHeight()) {
        if (header != null) Box { header() }
        Column { content() }
    }
}

@Composable
fun ShortNavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    content: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) { content() }
}

@Composable
fun RowScope.ShortNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    Column(
        modifier = modifier
            .weight(1f)
            .selectable(selected = selected, enabled = enabled, onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        icon()
        if (label != null && (alwaysShowLabel || selected)) label()
    }
}
