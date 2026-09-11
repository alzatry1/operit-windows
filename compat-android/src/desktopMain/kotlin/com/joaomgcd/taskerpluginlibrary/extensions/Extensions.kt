package com.joaomgcd.taskerpluginlibrary.extensions

import android.content.Context
import android.util.Log

/**
 * taskerpluginlibrary 扩展（P3-B2 新增，编译级 stub）。
 * 桌面无 Tasker 宿主：requestQuery 记日志后直接返回。
 */

/** 向 Tasker 插件发送 query 请求（桌面 no-op）。 */
fun <T : Any> Class<T>.requestQuery(context: Context, update: Any? = null) {
    Log.d("TaskerPlugin", "requestQuery(${this.simpleName}) no-op on desktop")
}
