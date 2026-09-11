package androidx.compose.ui.res

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ai.assistance.operit.res.ResFiles
import com.ai.assistance.operit.res.Strings

/**
 * androidx.compose.ui.res.* 的桌面垫片。
 * Android 上这些 API 由 framework 提供；CMP Desktop 缺失，这里补齐，
 * 让迁移代码里的 stringResource(R.string.xxx) 等调用零修改工作。
 */

@Composable
fun stringResource(id: Int): String = Strings.get(id)

@Composable
fun stringResource(id: Int, vararg formatArgs: Any): String = Strings.getString(id, *formatArgs)

@Composable
fun pluralStringResource(id: Int, count: Int): String = Strings.getQuantityString(id, count)

@Composable
fun pluralStringResource(id: Int, count: Int, vararg formatArgs: Any): String =
    Strings.getQuantityString(id, count, *formatArgs)

private val warnedPaths = java.util.Collections.synchronizedSet(mutableSetOf<String>())

@Composable
fun painterResource(id: Int): Painter {
    val path = ResFiles.getPath(id)
    return remember(id) {
        if (path == null) {
            ColorPainter(Color.Transparent)
        } else {
            try {
                if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg") ||
                    path.endsWith(".webp") || path.endsWith(".gif") || path.endsWith(".bmp")
                ) {
                    val bytes = ResFiles.openStream(id)?.use { it.readBytes() }
                    if (bytes != null) {
                        val image = org.jetbrains.skia.Image.makeFromEncoded(bytes)
                        BitmapPainter(image.asImageBitmap())
                    } else {
                        ColorPainter(Color.Transparent)
                    }
                } else {
                    // Vector XML 等暂不支持，透明占位
                    if (warnedPaths.add(path)) {
                        org.slf4j.LoggerFactory.getLogger("PainterResource")
                            .warn("painterResource: 暂不支持的资源格式 $path，使用透明占位")
                    }
                    ColorPainter(Color.Transparent)
                }
            } catch (e: Exception) {
                org.slf4j.LoggerFactory.getLogger("PainterResource")
                    .warn("painterResource: 解码失败 $path: ${e.message}")
                ColorPainter(Color.Transparent)
            }
        }
    }
}

@Composable
fun dimensionResource(id: Int): Dp {
    if (warnedPaths.add("dimen:$id")) {
        org.slf4j.LoggerFactory.getLogger("DimensionResource")
            .warn("dimensionResource($id): dimen 体系未实现，返回 0.dp")
    }
    return 0.dp
}

@Composable
fun integerResource(id: Int): Int = 0

@Composable
fun booleanResource(id: Int): Boolean = false

@Composable
fun colorResource(id: Int): Color = Color(com.ai.assistance.operit.res.Colors.getColor(id))
