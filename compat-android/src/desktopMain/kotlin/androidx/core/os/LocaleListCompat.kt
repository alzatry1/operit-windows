package androidx.core.os

import java.util.Locale

/**
 * androidx.core.os.LocaleListCompat 桌面版：包装 android.os.LocaleList。
 */
class LocaleListCompat private constructor(
    private val delegate: android.os.LocaleList,
) : Iterable<Locale> {

    fun get(index: Int): Locale? = if (index in 0 until size()) delegate.get(index) else null

    fun size(): Int = delegate.size()

    fun isEmpty(): Boolean = delegate.isEmpty()

    fun toLanguageTags(): String = delegate.toLanguageTags()

    fun getFirstMatch(supportedLanguages: Array<String>): Locale? {
        for (lang in supportedLanguages) {
            val wanted = Locale.forLanguageTag(lang)
            for (l in delegate) {
                if (l.language == wanted.language) return l
            }
        }
        return null
    }

    /** 返回平台包装对象（桌面即 android.os.LocaleList）。 */
    fun unwrap(): android.os.LocaleList = delegate

    override fun iterator(): Iterator<Locale> = delegate.iterator()

    override fun equals(other: Any?): Boolean =
        other is LocaleListCompat && other.delegate == delegate

    override fun hashCode(): Int = delegate.hashCode()

    override fun toString(): String = delegate.toString()

    companion object {
        @JvmField
        val EMPTY: LocaleListCompat = LocaleListCompat(android.os.LocaleList.EMPTY)

        @JvmStatic
        fun getDefault(): LocaleListCompat = LocaleListCompat(android.os.LocaleList.getDefault())

        @JvmStatic
        fun getAdjustedDefault(): LocaleListCompat = getDefault()

        @JvmStatic
        fun create(vararg localeList: Locale): LocaleListCompat =
            LocaleListCompat(android.os.LocaleList.of(*localeList))

        @JvmStatic
        fun forLanguageTags(list: String?): LocaleListCompat =
            LocaleListCompat(android.os.LocaleList.forLanguageTags(list))

        @JvmStatic
        fun matchesLanguageAndScript(supported: Locale, desired: Locale): Boolean =
            supported.language == desired.language

        @JvmStatic
        fun wrap(localeList: Any?): LocaleListCompat? =
            (localeList as? android.os.LocaleList)?.let { LocaleListCompat(it) }
    }
}
