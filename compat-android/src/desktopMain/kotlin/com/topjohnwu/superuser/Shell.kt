package com.topjohnwu.superuser

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * com.topjohnwu.superuser（libsu）桌面功能等价实现（P3-B2 新增）。
 * 桌面无 root 概念：Shell.cmd(...).exec() 直接用 java.lang.ProcessBuilder 执行命令
 * （Windows 走 cmd /c，其余走 sh -c），真实返回 out/err/code。
 */

/** libsu CallbackList：add 时触发 onAddElement 回调。 */
open class CallbackList<E> : ArrayList<E> {
    constructor() : super()
    constructor(c: Collection<E>) : super(c)

    open fun onAddElement(e: E) {}

    override fun add(element: E): Boolean {
        onAddElement(element)
        return super.add(element)
    }
}

/** libsu Shell。 */
class Shell private constructor() {

    val isRoot: Boolean get() = false
    val isAlive: Boolean get() = true

    /** libsu Shell.Result。 */
    class Result(
        val out: List<String>,
        val err: List<String>,
        val code: Int,
    ) {
        val isSuccess: Boolean get() = code == 0

        override fun toString(): String = "Shell.Result(code=$code, out=${out.size} lines, err=${err.size} lines)"
    }

    /** libsu Shell.Job：可执行命令任务。 */
    class Job internal constructor(private val commands: Array<out String>) {
        private var stdout: List<String>? = null
        private var stderr: List<String>? = null

        fun to(out: List<String>?): Job {
            this.stdout = out
            return this
        }

        fun to(out: List<String>?, err: List<String>?): Job {
            this.stdout = out
            this.stderr = err
            return this
        }

        /** 同步执行并返回 Result。 */
        fun exec(): Result {
            val outLines = mutableListOf<String>()
            val errLines = mutableListOf<String>()
            val code = runProcess(commands, outLines, errLines)
            stdout?.let { if (it is MutableList<String>) it.addAll(outLines) }
            stderr?.let { if (it is MutableList<String>) it.addAll(errLines) }
            return Result(outLines, errLines, code)
        }

        /** 提交异步执行，返回 Future（libsu v6 语义）。 */
        fun enqueue(): Future<Result> = executor.submit(Callable { exec() })

        fun submit() {
            executor.submit { exec() }
        }

        fun submit(callback: ((Result) -> Unit)?) {
            executor.submit { callback?.invoke(exec()) }
        }
    }

    /** libsu Shell.Builder。 */
    class Builder internal constructor() {
        private var flags = 0
        private var timeoutSeconds = 20L

        fun setFlags(flags: Int): Builder {
            this.flags = flags
            return this
        }

        fun setTimeout(timeout: Long): Builder {
            this.timeoutSeconds = timeout
            return this
        }

        fun build(): Shell = Shell()

        companion object {
            @JvmStatic
            fun create(): Builder = Builder()
        }
    }

    companion object {
        const val FLAG_REDIRECT_STDERR = 1
        const val FLAG_MOUNT_MASTER = 2
        const val FLAG_NON_ROOT_SHELL = 4

        @JvmField
        var enableVerboseLogging: Boolean = false

        private val executor: ExecutorService = Executors.newCachedThreadPool { r ->
            Thread(r, "libsu-compat").apply { isDaemon = true }
        }

        private val defaultShell by lazy { Shell() }
        private var defaultBuilder: Builder = Builder()

        @JvmStatic
        fun getShell(): Shell = defaultShell

        @JvmStatic
        fun getShell(callback: (Shell) -> Unit) {
            callback(defaultShell)
        }

        @JvmStatic
        fun setDefaultBuilder(builder: Builder) {
            defaultBuilder = builder
        }

        @JvmStatic
        fun isAppGrantedRoot(): Boolean? = false

        @JvmStatic
        fun cmd(vararg commands: String): Job = Job(commands)

        @JvmStatic
        fun cmd(commands: List<String>): Job = Job(commands.toTypedArray())

        @JvmStatic
        fun sh(vararg commands: String): Job = Job(commands)

        @JvmStatic
        fun su(vararg commands: String): Job = Job(commands)

        /** 真实进程执行：Windows 走 cmd /c，其余走 sh -c。 */
        internal fun runProcess(commands: Array<out String>, out: MutableList<String>, err: MutableList<String>): Int {
            val script = commands.joinToString("\n")
            val isWindows = System.getProperty("os.name").lowercase().contains("windows")
            val pb = if (isWindows) ProcessBuilder("cmd", "/c", script) else ProcessBuilder("sh", "-c", script)
            return try {
                val process = pb.start()
                // 并发读取两个流，避免缓冲区死锁
                val outQueue = ConcurrentLinkedQueue<String>()
                val errQueue = ConcurrentLinkedQueue<String>()
                val outThread = Thread {
                    BufferedReader(InputStreamReader(process.inputStream)).forEachLine { outQueue.add(it) }
                }.apply { isDaemon = true; start() }
                val errThread = Thread {
                    BufferedReader(InputStreamReader(process.errorStream)).forEachLine { errQueue.add(it) }
                }.apply { isDaemon = true; start() }
                val code = process.waitFor()
                outThread.join(2000)
                errThread.join(2000)
                out.addAll(outQueue)
                err.addAll(errQueue)
                code
            } catch (e: Exception) {
                err.add("shell exec failed: ${e.message}")
                -1
            }
        }
    }
}

/** libsu ShellUtils。 */
object ShellUtils {
    @JvmStatic
    fun fastCmd(vararg commands: String): String =
        Shell.cmd(*commands).exec().out.joinToString("\n")

    @JvmStatic
    fun fastCmdResult(vararg commands: String): Boolean =
        Shell.cmd(*commands).exec().isSuccess
}
