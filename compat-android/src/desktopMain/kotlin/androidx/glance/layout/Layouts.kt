package androidx.glance.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.glance.GlanceModifier

/**
 * androidx.glance.layout 容器与尺寸修饰符桌面 stub。
 * 桌面无 RemoteViews 渲染：容器直接透传 content（保持结构语义），尺寸修饰返回自身。
 */

@Composable
fun Box(
    modifier: GlanceModifier = GlanceModifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    content()
}

@Composable
fun Column(
    modifier: GlanceModifier = GlanceModifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit,
) {
    content()
}

@Composable
fun Row(
    modifier: GlanceModifier = GlanceModifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit,
) {
    content()
}

@Composable
fun Spacer(modifier: GlanceModifier = GlanceModifier) {}

// ---- GlanceModifier 尺寸扩展（stub，返回自身） ----

fun GlanceModifier.width(width: Dp): GlanceModifier = this

fun GlanceModifier.height(height: Dp): GlanceModifier = this

fun GlanceModifier.size(size: Dp): GlanceModifier = this

fun GlanceModifier.size(width: Dp, height: Dp): GlanceModifier = this

fun GlanceModifier.fillMaxSize(): GlanceModifier = this

fun GlanceModifier.fillMaxWidth(): GlanceModifier = this

fun GlanceModifier.fillMaxHeight(): GlanceModifier = this

fun GlanceModifier.padding(all: Dp): GlanceModifier = this

fun GlanceModifier.padding(horizontal: Dp, vertical: Dp): GlanceModifier = this

fun GlanceModifier.padding(start: Dp, top: Dp, end: Dp, bottom: Dp): GlanceModifier = this
