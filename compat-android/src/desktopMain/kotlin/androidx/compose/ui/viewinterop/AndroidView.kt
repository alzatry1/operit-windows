package androidx.compose.ui.viewinterop

import android.content.Context
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.slf4j.LoggerFactory

/**
 * androidx.compose.ui.viewinterop.AndroidView 桌面垫片。
 * CMP Desktop 不提供 AndroidView（无 Android View 宿主）：渲染一个空 Box 占位，
 * factory/update 均不调用。同一 factory 类型只记一次日志。
 */

private val androidViewWarned = java.util.Collections.synchronizedSet(mutableSetOf<String>())

@Composable
fun <T : View> AndroidView(
    factory: (Context) -> T,
    modifier: Modifier = Modifier,
    update: (T) -> Unit = {},
    onRelease: (T) -> Unit = {},
) {
    remember {
        if (androidViewWarned.add("AndroidView")) {
            LoggerFactory.getLogger("AndroidView")
                .warn("桌面端 AndroidView 渲染为空占位 Box（factory/update 不执行）")
        }
        true
    }
    Box(modifier)
}
