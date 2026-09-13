package com.ai.assistance.operit.util.streamnative

import com.ai.assistance.operit.util.markdown.MarkdownNodeStable
import com.ai.assistance.operit.util.markdown.MarkdownProcessorType

private fun Int.toMarkdownTypeOrNull(): MarkdownProcessorType? =
    MarkdownProcessorType.entries.getOrNull(this)

private fun IntArray.toInlineStableNodes(content: String): List<MarkdownNodeStable> {
    val nodes = ArrayList<MarkdownNodeStable>(size / 3)
    var index = 0

    while (index + 2 < size) {
        val typeOrdinal = this[index]
        val start = this[index + 1]
        val end = this[index + 2]
        index += 3

        if (typeOrdinal < 0 || start < 0 || end < start || end > content.length) {
            continue
        }

        val type = typeOrdinal.toMarkdownTypeOrNull() ?: MarkdownProcessorType.PLAIN_TEXT
        val nodeContent =
            if (type == MarkdownProcessorType.HTML_BREAK) {
                "\n"
            } else {
                content.substring(start, end)
            }
        nodes +=
            MarkdownNodeStable(
                type = type,
                content = nodeContent,
                children = emptyList()
            )
    }

    return nodes
}

object NativeMarkdownSplitter {

    /**
     * 桌面端没有 streamnative 本地库（.dll/.so 未为桌面打包）：loadLibrary 失败时不崩，
     * 置 nativeAvailable=false，后续解析回退纯 Kotlin 实现。——Nova 注
     */
    val nativeAvailable: Boolean =
        try {
            System.loadLibrary("streamnative")
            true
        } catch (e: Throwable) {
            System.err.println("Nova-Native: streamnative 本地库不可用，markdown 解析回退纯 Kotlin：${e.message}")
            false
        }

    private external fun nativeCreateBlockSession(): Long
    private external fun nativeCreateInlineSession(): Long
    private external fun nativeDestroySession(handle: Long)
    private external fun nativePush(handle: Long, chunk: String): IntArray

    class Session internal constructor(
        private val handle: Long,
    ) {
        fun push(chunk: String): IntArray = nativePush(handle, chunk)
        fun destroy() = nativeDestroySession(handle)
    }

    fun createBlockSession(): Session {
        if (!nativeAvailable) throw UnsupportedOperationException("streamnative 本地库不可用（桌面端未打包）")
        return Session(nativeCreateBlockSession())
    }
    fun createInlineSession(): Session {
        if (!nativeAvailable) throw UnsupportedOperationException("streamnative 本地库不可用（桌面端未打包）")
        return Session(nativeCreateInlineSession())
    }

    fun parseInlineToStableNodes(content: String): List<MarkdownNodeStable> {
        if (content.isEmpty()) return emptyList()

        // 本地库不可用时回退纯 Kotlin inline 解析。——Nova 注
        if (!nativeAvailable) return pureKotlinParseInline(content)

        val session = createInlineSession()
        return try {
            session.push(content).toInlineStableNodes(content)
        } finally {
            session.destroy()
        }
    }

    /**
     * 纯 Kotlin 兜底的 inline markdown 解析器。
     * 处理常见 inline 标记：行内代码/粗体/斜体/删除线/行内公式/链接/图片/换行。
     * 本地库不可用时保证 app 不崩、markdown 能渲染（精度略低于 native，但功能可用）。——Nova 注
     */
    private fun pureKotlinParseInline(content: String): List<MarkdownNodeStable> {
        val nodes = mutableListOf<MarkdownNodeStable>()
        // 优先级：行内代码 > 图片 > 链接 > 粗体 > 删除线 > 行内公式 > 斜体 > 换行
        val pattern = Regex(
            "(`[^`\\n]+`)" +                                  // 1 inline code
            "|(!\\[[^\\]\\n]*\\]\\([^)\\n]*\\))" +            // 2 image
            "|(\\[[^\\]\\n]+\\]\\([^)\\n]*\\))" +             // 3 link
            "|(\\*\\*[^*\\n]+\\*\\*)" +                       // 4 bold **
            "|(__[^_\\n]+__)" +                               // 5 bold __
            "|(~~[^~\\n]+~~)" +                               // 6 strikethrough
            "|(\\$[^$\\n]+\\$)" +                             // 7 inline latex
            "|(\\*[^*\\n]+\\*)" +                             // 8 italic *
            "|(_[^_\\n]+_)" +                                 // 9 italic _
            "|(<br\\s*/?>)",                                  // 10 html break
            RegexOption.MULTILINE
        )

        var lastIndex = 0
        fun addPlain(text: String) {
            if (text.isNotEmpty()) {
                nodes += MarkdownNodeStable(MarkdownProcessorType.PLAIN_TEXT, text, emptyList())
            }
        }

        for (match in pattern.findAll(content)) {
            if (match.range.first > lastIndex) addPlain(content.substring(lastIndex, match.range.first))
            val text = match.value
            val (type, nodeContent) = when {
                match.groups[1] != null -> MarkdownProcessorType.INLINE_CODE to text.removeSurrounding("`")
                match.groups[2] != null -> MarkdownProcessorType.IMAGE to text
                match.groups[3] != null -> MarkdownProcessorType.LINK to text
                match.groups[4] != null -> MarkdownProcessorType.BOLD to text.removeSurrounding("**")
                match.groups[5] != null -> MarkdownProcessorType.BOLD to text.removeSurrounding("__")
                match.groups[6] != null -> MarkdownProcessorType.STRIKETHROUGH to text.removeSurrounding("~~")
                match.groups[7] != null -> MarkdownProcessorType.INLINE_LATEX to text.removeSurrounding("$")
                match.groups[8] != null -> MarkdownProcessorType.ITALIC to text.removeSurrounding("*")
                match.groups[9] != null -> MarkdownProcessorType.ITALIC to text.removeSurrounding("_")
                match.groups[10] != null -> MarkdownProcessorType.HTML_BREAK to "\n"
                else -> MarkdownProcessorType.PLAIN_TEXT to text
            }
            nodes += MarkdownNodeStable(type, nodeContent, emptyList())
            lastIndex = match.range.last + 1
        }
        if (lastIndex < content.length) addPlain(content.substring(lastIndex))
        return nodes
    }
}
