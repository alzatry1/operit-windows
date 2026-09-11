package android.util

import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.ConcurrentHashMap

/**
 * android.util.Log 桌面实现：转发到 slf4j。
 * 运行期由 composeApp 的 logback 绑定负责输出。
 */
object Log {
    const val VERBOSE = 2
    const val DEBUG = 3
    const val INFO = 4
    const val WARN = 5
    const val ERROR = 6
    const val ASSERT = 7

    private val loggers = ConcurrentHashMap<String, org.slf4j.Logger>()

    private fun logger(tag: String?): org.slf4j.Logger =
        loggers.getOrPut(tag ?: "android") { LoggerFactory.getLogger(tag ?: "android") }

    @JvmStatic fun v(tag: String?, msg: String): Int { logger(tag).trace(msg); return 0 }
    @JvmStatic fun v(tag: String?, msg: String?, tr: Throwable?): Int { logger(tag).trace(msg, tr); return 0 }
    @JvmStatic fun d(tag: String?, msg: String): Int { logger(tag).debug(msg); return 0 }
    @JvmStatic fun d(tag: String?, msg: String?, tr: Throwable?): Int { logger(tag).debug(msg, tr); return 0 }
    @JvmStatic fun i(tag: String?, msg: String): Int { logger(tag).info(msg); return 0 }
    @JvmStatic fun i(tag: String?, msg: String?, tr: Throwable?): Int { logger(tag).info(msg, tr); return 0 }
    @JvmStatic fun w(tag: String?, msg: String): Int { logger(tag).warn(msg); return 0 }
    @JvmStatic fun w(tag: String?, msg: String?, tr: Throwable?): Int { logger(tag).warn(msg, tr); return 0 }
    @JvmStatic fun w(tag: String?, tr: Throwable): Int { logger(tag).warn("", tr); return 0 }
    @JvmStatic fun e(tag: String?, msg: String): Int { logger(tag).error(msg); return 0 }
    @JvmStatic fun e(tag: String?, msg: String?, tr: Throwable?): Int { logger(tag).error(msg, tr); return 0 }
    @JvmStatic fun wtf(tag: String?, msg: String?): Int { logger(tag).error("FATAL: $msg"); return 0 }
    @JvmStatic fun wtf(tag: String?, tr: Throwable?): Int { logger(tag).error("FATAL", tr); return 0 }
    @JvmStatic fun wtf(tag: String?, msg: String?, tr: Throwable?): Int { logger(tag).error("FATAL: $msg", tr); return 0 }

    @JvmStatic
    fun println(priority: Int, tag: String?, msg: String): Int {
        val l = logger(tag)
        when (priority) {
            VERBOSE -> l.trace(msg)
            DEBUG -> l.debug(msg)
            INFO -> l.info(msg)
            WARN -> l.warn(msg)
            ERROR, ASSERT -> l.error(msg)
            else -> l.debug(msg)
        }
        return 0
    }

    @JvmStatic fun isLoggable(tag: String?, level: Int): Boolean = true

    @JvmStatic
    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) return ""
        val sw = StringWriter()
        tr.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }
}

/** AOSP android.util.AndroidException。 */
open class AndroidException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}
