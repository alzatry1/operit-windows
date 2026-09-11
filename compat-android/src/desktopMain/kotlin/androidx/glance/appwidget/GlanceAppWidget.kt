package androidx.glance.appwidget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId

/**
 * androidx.glance.appwidget.GlanceAppWidget 桌面 stub。
 * 桌面无 AppWidget 宿主：provideContent/updateAll 只记日志，不渲染、不持久化。
 */
abstract class GlanceAppWidget {

    abstract suspend fun provideGlance(context: Context, id: GlanceId)

    open suspend fun onDelete(context: Context, glanceId: GlanceId) {}
}

/**
 * androidx.glance.appwidget.provideContent：真实实现是 GlanceAppWidget 的顶层 suspend 扩展，
 * 只能在 provideGlance 内部调用。桌面 stub：接收内容即弃（绝不调用 content——避免误触渲染）。
 */
suspend fun GlanceAppWidget.provideContent(content: @Composable () -> Unit) {
    Log.d("GlanceAppWidget", "provideContent(${javaClass.simpleName}) 桌面无小部件宿主，内容丢弃")
}

/** androidx.glance.appwidget.updateAll：桌面 stub，记日志。 */
suspend fun GlanceAppWidget.updateAll(context: Context) {
    Log.i("GlanceAppWidget", "updateAll(${javaClass.simpleName}) 桌面无小部件宿主，跳过")
}
