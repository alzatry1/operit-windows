package com.ai.assistance.operit.util.streamnative

object NativeXmlSplitter {

    init {
        // 桌面端无 streamnative 本地库：loadLibrary 失败由 NativeMarkdownSplitter 统一判定，这里静默。——Nova 注
        try {
            System.loadLibrary("streamnative")
        } catch (_: Throwable) {
        }
    }

    private external fun nativeSplitXmlSegments(content: String): IntArray

    fun splitXmlTag(content: String): List<List<String>> {
        // 本地库不可用时回退纯 Kotlin XML 分割。——Nova 注
        if (!NativeMarkdownSplitter.nativeAvailable) return pureKotlinSplitXmlTag(content)

        val results = mutableListOf<List<String>>()

        val segments = nativeSplitXmlSegments(content)
        if (segments.isEmpty()) return results

        var i = 0
        while (i + 2 < segments.size) {
            val type = segments[i]
            val start = segments[i + 1]
            val end = segments[i + 2]
            i += 3

            if (start < 0 || end < 0 || start > end || end > content.length) continue

            val chunk = content.substring(start, end)
            if (type == 1) {
                val tagNameMatch = Regex("<([A-Za-z][A-Za-z0-9_]*)[\\s>]").find(chunk)
                val tagName = tagNameMatch?.groupValues?.getOrNull(1) ?: "unknown"
                results.add(listOf(tagName, chunk))
            } else {
                if (chunk.isNotBlank()) {
                    results.add(listOf("text", chunk))
                }
            }
        }

        return results
    }

    /**
     * 纯 Kotlin 兜底的 XML 标签分割：把 <tag>...</tag> 或 <tag/> 块从纯文本里拆出来。
     * 本地库不可用时保证 app 不崩、结构化内容能解析（精度略低于 native）。——Nova 注
     */
    private fun pureKotlinSplitXmlTag(content: String): List<List<String>> {
        val results = mutableListOf<List<String>>()
        if (content.isEmpty()) return results
        // 匹配成对标签或自闭合标签
        val tagPattern =
            Regex("<([A-Za-z][A-Za-z0-9_]*)[^>]*?/>|<([A-Za-z][A-Za-z0-9_]*)[^>]*>.*?</\\2>", RegexOption.DOT_MATCHES_ALL)
        var lastIndex = 0
        for (match in tagPattern.findAll(content)) {
            if (match.range.first > lastIndex) {
                val text = content.substring(lastIndex, match.range.first)
                if (text.isNotBlank()) results.add(listOf("text", text))
            }
            val tagName = match.groupValues[1].ifEmpty { match.groupValues[2] }.ifEmpty { "unknown" }
            results.add(listOf(tagName, match.value))
            lastIndex = match.range.last + 1
        }
        if (lastIndex < content.length) {
            val text = content.substring(lastIndex)
            if (text.isNotBlank()) results.add(listOf("text", text))
        }
        return results
    }
}
