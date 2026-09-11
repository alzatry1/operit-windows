package androidx.glance.appwidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier

/** androidx.glance.appwidget 垫片（桌面无小组件，编译形状）。——Nova 注 */

open class GlanceAppWidget {
    open suspend fun provideGlance(context: Context, id: GlanceId) {}
    open suspend fun update(context: Context, glanceId: GlanceId) {}
    open fun updateAll(context: Context) {}
}

open class GlanceAppWidgetReceiver : android.content.BroadcastReceiver() {
    open val glanceAppWidget: GlanceAppWidget get() = GlanceAppWidget()
    override fun onReceive(context: Context, intent: android.content.Intent) {}
}

class GlanceAppWidgetManager(context: Context) {
    fun getGlanceIds(provider: android.content.ComponentName): IntArray = intArrayOf()
    fun <T> getAppWidgetState(glanceId: GlanceId, stateDefinition: Any?, glanceId2: Any?): T? = null
    fun updateAppWidgetState(glanceId: GlanceId, state: Any) {}
    companion object {
        fun getInstance(context: Context): GlanceAppWidgetManager = GlanceAppWidgetManager(context)
    }
}

/** provideContent：GlanceAppWidget 里声明内容。 */
suspend fun GlanceAppWidget.provideContent(content: @Composable () -> Unit) {}

fun GlanceAppWidget.updateAll(context: Context) {}
