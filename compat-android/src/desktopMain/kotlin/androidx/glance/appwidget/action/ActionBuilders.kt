package androidx.glance.appwidget.action

import android.content.Intent
import androidx.glance.action.Action

/** androidx.glance.appwidget.action 垫片。——Nova 注 */
fun actionStartActivity(intent: Intent): Action = object : Action {}
fun <T : android.app.Activity> actionStartActivity(activity: Class<T>): Action = object : Action {}
fun actionStartActivity(componentName: android.content.ComponentName): Action = object : Action {}
inline fun <reified T> actionRunCallback(): Action = object : Action {}
