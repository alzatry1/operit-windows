package androidx.glance.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.glance.GlanceModifier
import androidx.glance.unit.ColorProvider

/**
 * androidx.glance.text 桌面 stub：FontWeight / TextStyle / Text。
 * FontWeight 不引用 androidx.compose.ui.text.font.FontWeight（包不同），
 * 复刻真实 glance 的独立轻量实现（权重整数包装）。
 */
class FontWeight private constructor(val weight: Int) {
    companion object {
        val Thin = FontWeight(100)
        val ExtraLight = FontWeight(200)
        val Light = FontWeight(300)
        val Normal = FontWeight(400)
        val Medium = FontWeight(500)
        val SemiBold = FontWeight(600)
        val Bold = FontWeight(700)
        val ExtraBold = FontWeight(800)
        val Black = FontWeight(900)
    }
}

/** androidx.glance.text.TextStyle。 */
class TextStyle(
    val color: ColorProvider? = null,
    val fontSize: TextUnit? = null,
    val fontWeight: FontWeight? = null,
)

/** androidx.glance.text.Text：桌面无小部件渲染，空实现。 */
@Composable
fun Text(text: String, modifier: GlanceModifier = GlanceModifier, style: TextStyle = TextStyle()) {
}
