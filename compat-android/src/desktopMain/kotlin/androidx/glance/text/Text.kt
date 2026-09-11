package androidx.glance.text

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier

/** androidx.glance.text 垫片。——Nova 注 */

enum class FontWeight { Thin, ExtraLight, Light, Normal, Medium, SemiBold, Bold, ExtraBold, Black }

class TextStyle(
    val color: Color? = null,
    val fontSize: androidx.compose.ui.unit.TextUnit? = null,
    val fontWeight: FontWeight? = null,
    val textAlign: Any? = null,
)

@Composable
fun Text(text: String, modifier: GlanceModifier = GlanceModifier, style: TextStyle? = null, maxLines: Int = Int.MAX_VALUE) {}
