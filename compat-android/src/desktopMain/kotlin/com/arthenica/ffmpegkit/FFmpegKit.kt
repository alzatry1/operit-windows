package com.arthenica.ffmpegkit

import android.util.Log
import java.util.concurrent.TimeUnit

/**
 * com.arthenica.ffmpegkit 桌面垫片（P3-B4）。
 * 与纯编译 stub 不同：桌面环境若装有 ffmpeg/ffprobe 二进制则真实执行
 *（ProcessBuilder，FFmpegKit 风格引号切分），否则返回失败会话。
 */

/** ffmpegkit 日志条目。 */
open class Log(val id: Long, val level: Int, val message: String?)

/** 会话完成/日志/统计回调。 */
fun interface FFmpegSessionCompleteCallback { fun apply(session: FFmpegSession) }
fun interface LogCallback { fun apply(log: Log) }
fun interface StatisticsCallback { fun apply(statistics: Statistics) }
open class Statistics

/** 返回码。 */
open class ReturnCode(val value: Int) {
    companion object {
        const val SUCCESS = 0
        const val CANCEL = 255

        @JvmStatic fun isSuccess(returnCode: ReturnCode?): Boolean = returnCode?.value == SUCCESS
        @JvmStatic fun isCancel(returnCode: ReturnCode?): Boolean = returnCode?.value == CANCEL
        @JvmStatic fun isValueError(returnCode: ReturnCode?): Boolean =
            !isSuccess(returnCode) && !isCancel(returnCode)
    }
}

/** 会话输出。returnCode 非空（对齐真实 FFmpegKit 的平台类型语义）。 */
open class FFmpegSession(
    val returnCode: ReturnCode,
    val output: String?,
    val allLogs: List<Log> = emptyList(),
    val failStackTrace: String? = null,
    val startTime: Long = 0L,
    val endTime: Long = 0L
)

/** ffprobe 媒体信息会话。 */
open class MediaInformationSession(
    val mediaInformation: MediaInformation?,
    val returnCode: ReturnCode? = null
)

/** 媒体信息。 */
open class MediaInformation(
    val duration: String? = null,
    val format: String? = null,
    val bitrate: String? = null,
    val size: String? = null,
    val filename: String? = null,
    val streams: List<StreamInformation> = emptyList(),
    val allProperties: org.json.JSONObject? = null
)

/** 流信息（视频/音频轨）。 */
open class StreamInformation(
    val index: String? = null,
    val type: String? = null,
    val codec: String? = null,
    val width: String? = null,
    val height: String? = null,
    val sampleRate: String? = null,
    val channels: String? = null,
    val allProperties: org.json.JSONObject? = null
)

/** FFmpegKit 主入口。 */
object FFmpegKit {
    private const val TAG = "FFmpegKit"
    private const val TIMEOUT_SECONDS = 300L

    /** 同步执行 ffmpeg 命令。桌面：调用系统 ffmpeg；无二进制则失败会话。 */
    @JvmStatic
    fun execute(command: String?): FFmpegSession = runProcess("ffmpeg", command)

    @JvmStatic
    fun executeAsync(command: String?, completeCallback: FFmpegSessionCompleteCallback?): FFmpegSession {
        val session = execute(command)
        completeCallback?.apply(session)
        return session
    }

    @JvmStatic
    fun executeAsync(
        command: String?,
        completeCallback: FFmpegSessionCompleteCallback?,
        logCallback: LogCallback?,
        statisticsCallback: StatisticsCallback?
    ): FFmpegSession = executeAsync(command, completeCallback)

    @JvmStatic
    fun cancel() {}

    @JvmStatic
    fun cancel(sessionId: Long) {}

    internal fun runProcess(binary: String, command: String?): FFmpegSession {
        if (command.isNullOrBlank()) {
            return FFmpegSession(ReturnCode(1), null, failStackTrace = "empty command")
        }
        return try {
            val args = splitArgs(command)
            val pb = ProcessBuilder(listOf(binary) + args)
            pb.redirectErrorStream(true)
            val process = pb.start()
            val output = process.inputStream.bufferedReader().readText()
            if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroyForcibly()
                return FFmpegSession(ReturnCode(1), output, failStackTrace = "timeout")
            }
            FFmpegSession(ReturnCode(process.exitValue()), output)
        } catch (e: Exception) {
            Log.w(TAG, "$binary 不可用或执行失败: ${e.message}")
            FFmpegSession(ReturnCode(1), null, failStackTrace = e.message)
        }
    }

    /** FFmpegKit 风格参数切分：支持单/双引号包裹与反斜杠转义。 */
    internal fun splitArgs(command: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inSingle = false
        var inDouble = false
        var escaped = false
        for (c in command) {
            when {
                escaped -> { current.append(c); escaped = false }
                c == '\\' && !inSingle -> escaped = true
                c == '\'' && !inDouble -> inSingle = !inSingle
                c == '"' && !inSingle -> inDouble = !inDouble
                c.isWhitespace() && !inSingle && !inDouble -> {
                    if (current.isNotEmpty()) { result.add(current.toString()); current.clear() }
                }
                else -> current.append(c)
            }
        }
        if (current.isNotEmpty()) result.add(current.toString())
        return result
    }
}

/** FFprobeKit 主入口。 */
object FFprobeKit {
    @JvmStatic
    fun getMediaInformation(path: String?): MediaInformationSession {
        val session = FFmpegKit.runProcess(
            "ffprobe",
            "-v quiet -print_format json -show_format -show_streams ${'"'}$path${'"'}"
        )
        if (!ReturnCode.isSuccess(session.returnCode)) {
            return MediaInformationSession(null, session.returnCode)
        }
        return MediaInformationSession(parseMediaInformation(session.output), session.returnCode)
    }

    private fun parseMediaInformation(json: String?): MediaInformation? {
        if (json.isNullOrBlank()) return null
        fun find(key: String, inText: String = json): String? =
            Regex(""""$key"\s*:\s*"?([^",}\n]+)"?""").find(inText)?.groupValues?.get(1)?.trim()
        val streams = Regex("""\{[^{}]*"codec_type"[^{}]*\}""").findAll(json).map { m ->
            val s = m.value
            StreamInformation(
                index = find("index", s),
                type = find("codec_type", s),
                codec = find("codec_name", s),
                width = find("width", s),
                height = find("height", s)
            )
        }.toList()
        return MediaInformation(
            duration = find("duration"),
            format = find("format_name"),
            bitrate = find("bit_rate"),
            size = find("size"),
            filename = find("filename"),
            streams = streams
        )
    }
}

/** FFmpegKitConfig：版本信息查询。 */
object FFmpegKitConfig {
    @JvmStatic fun getVersion(): String = FFmpegKit.execute("-version").output
        ?.lineSequence()?.firstOrNull()?.removePrefix("ffmpeg version")?.trim()
        ?: "unavailable"

    @JvmStatic fun getBuildDate(): String = "desktop-stub"

    @JvmStatic fun enableLogCallback(callback: LogCallback?) {}
    @JvmStatic fun enableStatisticsCallback(callback: StatisticsCallback?) {}
}
