package com.ai.assistance.operit.res

import java.util.Locale
import java.util.Properties
import java.util.concurrent.ConcurrentHashMap

/**
 * R 资源运行时：按系统 Locale 解析字符串表。
 * 数据来自 classloader: res-strings/<tag>.properties（由 tools/generate_r.py 生成）。
 */
object Strings {
    private val index: Map<Int, String> by lazy { loadIndex() }
    private val tables = ConcurrentHashMap<String, Properties>()
    var localeOverride: Locale? = null
        @Synchronized set(value) {
            field = value
            tables.clear()
        }

    private fun loadIndex(): Map<Int, String> {
        val props = Properties()
        val stream = javaClass.classLoader?.getResourceAsStream("res-strings/_index.properties")
            ?: return emptyMap()
        stream.reader(Charsets.UTF_8).use { props.load(it) }
        return props.entries.mapNotNull { (k, v) ->
            (k as? String)?.toIntOrNull()?.let { it to (v as String) }
        }.toMap()
    }

    /** Locale 解析链：localeOverride/系统默认 → 精确 tag → 仅语言 → default */
    private fun resolveTag(): String {
        val locale = localeOverride ?: Locale.getDefault()
        val lang = locale.language.lowercase()
        val country = locale.country.uppercase()
        val candidates = mutableListOf<String>()
        if (country.isNotEmpty()) candidates += "$lang-r$country"
        candidates += lang
        candidates += "default"
        for (tag in candidates) {
            val normalized = tag.lowercase().replace("_", "-").replace("-r", "-r")
            // 文件名形如 pt-rBR.properties：language 小写 + -r + COUNTRY 大写
            val fileTag = if (tag.contains("-r")) {
                val (l, c) = tag.split("-r")
                "${l}-r${c.uppercase()}"
            } else tag
            if (hasTable(fileTag)) return fileTag
        }
        return "default"
    }

    private fun hasTable(tag: String): Boolean =
        javaClass.classLoader?.getResourceAsStream("res-strings/$tag.properties") != null

    private fun table(): Properties {
        val tag = resolveTag()
        return tables.getOrPut(tag) {
            val props = Properties()
            // 先铺 default 兜底，再覆盖目标语言
            if (tag != "default") {
                javaClass.classLoader?.getResourceAsStream("res-strings/default.properties")
                    ?.reader(Charsets.UTF_8)?.use { props.load(it) }
            }
            javaClass.classLoader?.getResourceAsStream("res-strings/$tag.properties")
                ?.reader(Charsets.UTF_8)?.use { props.load(it) }
            props
        }
    }

    /** android.R.string.* 的 id → key 映射（android/R.kt，900000 段）。 */
    private val androidNames: Map<Int, String> = mapOf(
        900001 to "__android_ok",
        900002 to "__android_cancel",
        900003 to "__android_copy",
        900004 to "__android_selectAll"
    )

    private fun nameOf(id: Int): String? = index[id]?.substringAfter(':') ?: androidNames[id]

    @JvmStatic
    fun get(id: Int): String {
        val name = nameOf(id) ?: return "<missing:$id>"
        return table().getProperty(name) ?: "<missing:$name>"
    }

    @JvmStatic
    fun getString(id: Int, vararg args: Any?): String {
        val raw = get(id)
        if (raw.startsWith("<missing:")) return raw
        return try {
            String.format(raw, *args)
        } catch (e: Exception) {
            raw
        }
    }

    @JvmStatic
    fun getArray(id: Int): Array<String> {
        val name = nameOf(id) ?: return emptyArray()
        val t = table()
        val count = t.getProperty("$name._count")?.toIntOrNull() ?: return emptyArray()
        return Array(count) { i -> t.getProperty("$name.$i") ?: "" }
    }

    /** JVM 简化复数规则：count==1 → one；count==0 且有 zero → zero；其余 → other */
    @JvmStatic
    fun getQuantityString(id: Int, count: Int, vararg args: Any?): String {
        val name = nameOf(id) ?: return "<missing:$id>"
        val t = table()
        val key = when {
            count == 1 && t.containsKey("$name.one") -> "$name.one"
            count == 0 && t.containsKey("$name.zero") -> "$name.zero"
            t.containsKey("$name.other") -> "$name.other"
            else -> return "<missing:$name>"
        }
        val raw = t.getProperty(key)
        return try {
            String.format(raw, *args)
        } catch (e: Exception) {
            raw
        }
    }
}

/** R.color 解析：0xAARRGGBB Int。 */
object Colors {
    private val table: Map<Int, Int> by lazy {
        val props = Properties()
        javaClass.classLoader?.getResourceAsStream("res-colors.properties")
            ?.reader(Charsets.UTF_8)?.use { props.load(it) }
        props.entries.mapNotNull { (k, v) ->
            val id = (k as? String)?.toIntOrNull() ?: return@mapNotNull null
            val hex = (v as? String)?.removePrefix("0x")?.toLongOrNull(16) ?: return@mapNotNull null
            id to hex.toInt()
        }.toMap()
    }

    @JvmStatic
    fun getColor(id: Int): Int = table[id] ?: 0xFF000000.toInt()
}

/** R.drawable/mipmap/xml/layout → 资源文件路径。 */
object ResFiles {
    private val table: Map<Int, String> by lazy {
        val props = Properties()
        javaClass.classLoader?.getResourceAsStream("res-files.properties")
            ?.reader(Charsets.UTF_8)?.use { props.load(it) }
        props.entries.mapNotNull { (k, v) ->
            (k as? String)?.toIntOrNull()?.let { it to (v as String) }
        }.toMap()
    }

    @JvmStatic
    fun getPath(id: Int): String? = table[id]

    @JvmStatic
    fun openStream(id: Int): java.io.InputStream? {
        val path = getPath(id) ?: return null
        return javaClass.classLoader?.getResourceAsStream(path)
    }
}
