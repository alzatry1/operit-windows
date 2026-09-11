package androidx.glance.appwidget.action

import android.content.ComponentName
import android.content.Intent
import androidx.glance.action.Action

/**
 * androidx.glance.appwidget.action.actionStartActivity 桌面 stub。
 * 构造一个标记性 Action（桌面无小部件点击分发，Action 永不触发）。
 */

internal class StartActivityIntentAction(val intent: Intent) : Action

internal class StartActivityComponentAction(val component: ComponentName) : Action

fun actionStartActivity(intent: Intent): Action = StartActivityIntentAction(intent)

fun actionStartActivity(component: ComponentName): Action = StartActivityComponentAction(component)
