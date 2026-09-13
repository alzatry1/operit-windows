package com.ai.assistance.operit.util

import android.content.Context

/**
 * 桌面端全局异常处理器。
 * Android 原版会弹 CrashReportActivity + exitProcess(1)；桌面端没有崩溃页 Activity，
 * exitProcess 会导致整个 app 闪退。这里改成：只把异常打到日志（stdout/AppLogger，便于诊断），
 * 不杀进程、不弹崩溃页，让 app 保持存活。——Nova 注
 */
class GlobalExceptionHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        // 打到 stdout（CI 冒烟日志能抓到）+ AppLogger，便于定位真异常。——Nova 注
        println("Nova-CRASH: 未捕获异常 on thread=${thread.name}: ${ex.javaClass.name}: ${ex.message}")
        ex.printStackTrace()
        try {
            AppLogger.e("GlobalExceptionHandler", "未捕获异常 on thread=${thread.name}", ex)
        } catch (_: Throwable) {
            // AppLogger 未初始化时忽略
        }
        // 桌面端不 exitProcess、不弹崩溃页——让 app 保持存活，由调用方/界面自行降级。——Nova 注
    }
}
