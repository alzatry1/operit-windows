package android.appwidget

import android.content.ComponentName
import android.content.Context

/**
 * android.appwidget.AppWidgetManager 最小垫片（B1b-2 新建）。
 * 存在原因：androidx.glance.appwidget.GlanceAppWidgetReceiver.onUpdate 的签名
 * 依赖此类型；应用代码也引用了 INVALID_APPWIDGET_ID / EXTRA_APPWIDGET_ID 常量。
 * 桌面无小部件宿主，方法全部返回空/no-op。
 */
open class AppWidgetManager {

    open fun getAppWidgetIds(provider: ComponentName): IntArray = IntArray(0)

    open fun getAppWidgetInfo(appWidgetId: Int): Any? = null

    open fun updateAppWidget(appWidgetIds: IntArray, views: Any?) {}

    open fun updateAppWidget(appWidgetId: Int, views: Any?) {}

    open fun updateAppWidget(provider: ComponentName, views: Any?) {}

    companion object {
        const val ACTION_APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE"
        const val ACTION_APPWIDGET_DELETED = "android.appwidget.action.APPWIDGET_DELETED"
        const val ACTION_APPWIDGET_ENABLED = "android.appwidget.action.APPWIDGET_ENABLED"
        const val ACTION_APPWIDGET_DISABLED = "android.appwidget.action.APPWIDGET_DISABLED"
        const val ACTION_APPWIDGET_OPTIONS_CHANGED = "android.appwidget.action.APPWIDGET_UPDATE_OPTIONS"
        const val ACTION_APPWIDGET_RESTORED = "android.appwidget.action.APPWIDGET_RESTORED"
        const val EXTRA_APPWIDGET_ID = "appWidgetId"
        const val EXTRA_APPWIDGET_IDS = "appWidgetIds"
        const val EXTRA_APPWIDGET_OPTIONS = "appWidgetOptions"
        const val EXTRA_APPWIDGET_PROVIDER = "appWidgetProvider"
        const val INVALID_APPWIDGET_ID = 0

        @JvmStatic
        fun getInstance(context: Context): AppWidgetManager = AppWidgetManager()
    }
}
