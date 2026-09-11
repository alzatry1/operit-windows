package androidx.glance.layout

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier

/** androidx.glance.layout 垫片（Glance 的布局可组合，编译形状）。——Nova 注 */

object Alignment {
    class Horizontal private constructor()
    class Vertical private constructor()
    companion object {
        val Top: Vertical = Vertical()
        val Bottom: Vertical = Vertical()
        val CenterVertically: Vertical = Vertical()
        val Start: Horizontal = Horizontal()
        val End: Horizontal = Horizontal()
        val CenterHorizontally: Horizontal = Horizontal()
        val Center: Pair<Horizontal, Vertical> = Horizontal() to Vertical()
    }
}

@Composable
fun Box(modifier: GlanceModifier = GlanceModifier, contentAlignment: Any? = null, content: @Composable () -> Unit = {}) { content() }

@Composable
fun Column(
    modifier: GlanceModifier = GlanceModifier,
    verticalAlignment: Any? = null,
    horizontalAlignment: Any? = null,
    content: @Composable () -> Unit = {},
) { content() }

@Composable
fun Row(
    modifier: GlanceModifier = GlanceModifier,
    horizontalAlignment: Any? = null,
    verticalAlignment: Any? = null,
    content: @Composable () -> Unit = {},
) { content() }

@Composable
fun Spacer(modifier: GlanceModifier = GlanceModifier) {}
