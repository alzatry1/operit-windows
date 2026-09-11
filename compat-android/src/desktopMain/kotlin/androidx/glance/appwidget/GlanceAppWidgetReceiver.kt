package androidx.glance.appwidget

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * androidx.glance.appwidget.GlanceAppWidgetReceiver 桌面 stub。
 * 桌面无系统广播源：onReceive/onUpdate 等仅记日志，子类可安全覆写并回调 super。
 */
abstract class GlanceAppWidgetReceiver : BroadcastReceiver() {

    abstract val glanceAppWidget: GlanceAppWidget

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("GlanceAppWidgetReceiver", "onReceive(${intent.action}) 桌面无小部件广播，忽略")
    }

    open fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("GlanceAppWidgetReceiver", "onUpdate(${appWidgetIds.size} ids) 桌面无小部件宿主，忽略")
    }

    open fun onDeleted(context: Context, appWidgetIds: IntArray) {}

    open fun onEnabled(context: Context) {}

    open fun onDisabled(context: Context) {}
}
