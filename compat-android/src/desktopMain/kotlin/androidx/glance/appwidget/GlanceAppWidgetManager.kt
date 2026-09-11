package androidx.glance.appwidget

import android.content.Context
import androidx.glance.GlanceId

/**
 * androidx.glance.appwidget.GlanceAppWidgetManager 桌面 stub。
 * 桌面无小部件实例：id 查询恒返回 0、id 列表恒为空。
 */
class GlanceAppWidgetManager(private val context: Context) {

    fun getAppWidgetId(glanceId: GlanceId): Int = 0

    suspend fun getGlanceIds(): List<GlanceId> = emptyList()

    suspend fun <T : GlanceAppWidget> getGlanceIds(receiver: Class<T>): List<GlanceId> = emptyList()
}
