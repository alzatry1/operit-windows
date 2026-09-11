package android.content

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.ai.assistance.operit.compat.AppGlobals
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

/** android.content.SharedPreferences 接口。 */
interface SharedPreferences {
    fun interface OnSharedPreferenceChangeListener {
        fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?)
    }

    val all: Map<String, *>
    fun getString(key: String, defValue: String?): String?
    fun getStringSet(key: String, defValues: Set<String>?): Set<String>?
    fun getInt(key: String, defValue: Int): Int
    fun getLong(key: String, defValue: Long): Long
    fun getFloat(key: String, defValue: Float): Float
    fun getBoolean(key: String, defValue: Boolean): Boolean
    fun contains(key: String): Boolean
    fun edit(): Editor
    fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener?)
    fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener?)

    interface Editor {
        fun putString(key: String, value: String?): Editor
        fun putStringSet(key: String, values: Set<String>?): Editor
        fun putInt(key: String, value: Int): Editor
        fun putLong(key: String, value: Long): Editor
        fun putFloat(key: String, value: Float): Editor
        fun putBoolean(key: String, value: Boolean): Editor
        fun remove(key: String): Editor
        fun clear(): Editor
        fun commit(): Boolean
        fun apply()
    }
}

/**
 * java.util.Properties 持久化实现：~/.operit/shared_prefs/<name>.properties。
 * apply() 异步落盘、commit() 同步；监听回调投递到主 Looper。
 */
internal class SharedPreferencesImpl(private val name: String) : SharedPreferences {

    private val file: File = File(AppGlobals.prefsDir, "$name.properties")
    private val lock = Any()
    private val data = HashMap<String, Any?>()
    private val listeners = CopyOnWriteArrayList<SharedPreferences.OnSharedPreferenceChangeListener>()
    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

    init {
        load()
    }

    private fun load() {
        if (!file.exists()) return
        try {
            val props = Properties()
            FileInputStream(file).use { props.load(it) }
            synchronized(lock) {
                for ((k, v) in props.entries) {
                    val key = k as? String ?: continue
                    val value = v as? String ?: continue
                    data[key] = deserialize(value)
                }
            }
        } catch (e: Exception) {
            Log.e("SharedPreferences", "load $name failed: ${e.message}")
        }
    }

    private fun serialize(value: Any?): String = when (value) {
        null -> "N:"
        is String -> "S:$value"
        is Int -> "I:$value"
        is Long -> "L:$value"
        is Float -> "F:$value"
        is Boolean -> "B:$value"
        is Set<*> -> "T:" + value.filterIsInstance<String>().joinToString(
            separator = ""
        ) { java.util.Base64.getEncoder().encodeToString(it.toByteArray(Charsets.UTF_8)) + "," }
        else -> "S:$value"
    }

    private fun deserialize(raw: String): Any? {
        if (raw.length < 2) return raw
        val payload = raw.substring(2)
        return when (raw.substring(0, 2)) {
            "N:" -> null
            "S:" -> payload
            "I:" -> payload.toIntOrNull()
            "L:" -> payload.toLongOrNull()
            "F:" -> payload.toFloatOrNull()
            "B:" -> payload.toBooleanStrictOrNull()
            "T:" -> payload.split(",").filter { it.isNotEmpty() }
                .map { String(java.util.Base64.getDecoder().decode(it), Charsets.UTF_8) }.toSet()
            else -> raw
        }
    }

    private fun persist() {
        try {
            val props = Properties()
            synchronized(lock) {
                for ((k, v) in data) props.setProperty(k, serialize(v))
            }
            val tmp = File(file.parentFile, "$name.properties.new")
            FileOutputStream(tmp).use { props.store(it, "operit") }
            if (file.exists()) file.delete()
            tmp.renameTo(file)
        } catch (e: Exception) {
            Log.e("SharedPreferences", "persist $name failed: ${e.message}")
        }
    }

    override val all: Map<String, *>
        get() = synchronized(lock) { HashMap(data) }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValue(key: String, defValue: T): T {
        synchronized(lock) {
            val v = data[key] ?: return defValue
            @Suppress("UNCHECKED_CAST")
            return when (defValue) {
                is Int -> (v as? Number)?.toInt() as? T ?: defValue
                is Long -> (v as? Number)?.toLong() as? T ?: defValue
                is Float -> (v as? Number)?.toFloat() as? T ?: defValue
                else -> v as? T ?: defValue
            }
        }
    }

    override fun getString(key: String, defValue: String?): String? {
        synchronized(lock) { return (data[key] as? String) ?: defValue }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? {
        synchronized(lock) { return (data[key] as? Set<String>) ?: defValues }
    }

    override fun getInt(key: String, defValue: Int): Int = getValue(key, defValue)
    override fun getLong(key: String, defValue: Long): Long = getValue(key, defValue)
    override fun getFloat(key: String, defValue: Float): Float = getValue(key, defValue)
    override fun getBoolean(key: String, defValue: Boolean): Boolean = getValue(key, defValue)

    override fun contains(key: String): Boolean = synchronized(lock) { data.containsKey(key) }

    override fun edit(): SharedPreferences.Editor = EditorImpl()

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?,
    ) {
        if (listener != null && !listeners.contains(listener)) listeners.add(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?,
    ) {
        listeners.remove(listener)
    }

    private fun notifyListeners(keys: List<String>) {
        if (listeners.isEmpty() || keys.isEmpty()) return
        mainHandler.post {
            for (key in keys) {
                for (l in listeners) {
                    try { l.onSharedPreferenceChanged(this, key) } catch (t: Throwable) {
                        Log.e("SharedPreferences", "listener failed", t)
                    }
                }
            }
        }
    }

    inner class EditorImpl : SharedPreferences.Editor {
        private val mutations = LinkedHashMap<String, Any?>()
        private val removals = HashSet<String>()
        private var clearAll = false

        @Synchronized
        private fun put(key: String, value: Any?): SharedPreferences.Editor {
            mutations[key] = value
            removals.remove(key)
            return this
        }

        override fun putString(key: String, value: String?): SharedPreferences.Editor = put(key, value)
        override fun putStringSet(key: String, values: Set<String>?): SharedPreferences.Editor = put(key, values)
        override fun putInt(key: String, value: Int): SharedPreferences.Editor = put(key, value)
        override fun putLong(key: String, value: Long): SharedPreferences.Editor = put(key, value)
        override fun putFloat(key: String, value: Float): SharedPreferences.Editor = put(key, value)
        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor = put(key, value)

        @Synchronized
        override fun remove(key: String): SharedPreferences.Editor {
            mutations.remove(key)
            removals.add(key)
            return this
        }

        @Synchronized
        override fun clear(): SharedPreferences.Editor {
            clearAll = true
            mutations.clear()
            removals.clear()
            return this
        }

        /** 应用编辑到内存，返回变更的 key 列表。 */
        @Synchronized
        private fun applyToMemory(): List<String> {
            val changed = ArrayList<String>()
            synchronized(lock) {
                if (clearAll) { changed.addAll(data.keys); data.clear() }
                for (k in removals) { if (data.remove(k) != null) changed.add(k) }
                for ((k, v) in mutations) {
                    if (v == null) { if (data.remove(k) != null) changed.add(k) }
                    else if (data[k] != v) { data[k] = v; changed.add(k) }
                }
            }
            return changed
        }

        override fun apply() {
            val changed = applyToMemory()
            diskExecutor.execute { persist() }
            notifyListeners(changed)
        }

        override fun commit(): Boolean {
            val changed = applyToMemory()
            persist()
            notifyListeners(changed)
            return true
        }
    }

    companion object {
        private val instances = ConcurrentHashMap<String, SharedPreferencesImpl>()
        private val diskExecutor = Executors.newSingleThreadExecutor { r ->
            Thread(r, "sharedprefs-io").apply { isDaemon = true }
        }

        fun getInstance(name: String): SharedPreferencesImpl =
            instances.getOrPut(name) { SharedPreferencesImpl(name) }
    }
}
