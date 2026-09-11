package android.util

/**
 * android.util 容器家族桌面实现。
 */

/** android.util.Pair（注意与 kotlin.Pair 区分：字段名 first/second 相同）。 */
class Pair<F, S>(@JvmField val first: F, @JvmField val second: S) {
    override fun equals(other: Any?): Boolean =
        other is Pair<*, *> && first == other.first && second == other.second
    override fun hashCode(): Int = (first?.hashCode() ?: 0) * 31 + (second?.hashCode() ?: 0)
    override fun toString(): String = "Pair{$first $second}"

    companion object {
        @JvmStatic fun <A, B> create(a: A, b: B): Pair<A, B> = Pair(a, b)
    }
}

/** android.util.LruCache：LinkedHashMap(accessOrder) 实现，可被 object 表达式继承覆写 sizeOf/entryRemoved/create。 */
open class LruCache<K, V>(maxSize: Int) {
    private val map = LinkedHashMap<K, V>(0, 0.75f, true)
    private var maxSize: Int = if (maxSize > 0) maxSize else throw IllegalArgumentException("maxSize <= 0")
    private var size = 0
    private var putCount = 0
    private var createCount = 0
    private var evictionCount = 0
    private var hitCount = 0
    private var missCount = 0

    /** 单条目大小，默认 1。AOSP 语义：可覆写返回字节数等。 */
    protected open fun sizeOf(key: K, value: V): Int = 1

    /** 条目被逐出/移除时回调。 */
    protected open fun entryRemoved(evicted: Boolean, key: K, oldValue: V, newValue: V?) {}

    /** miss 时创建，默认 null。 */
    protected open fun create(key: K): V? = null

    fun resize(maxSize: Int) {
        if (maxSize <= 0) throw IllegalArgumentException("maxSize <= 0")
        synchronized(this) { this.maxSize = maxSize }
        trimToSize(maxSize)
    }

    fun get(key: K): V? {
        synchronized(this) {
            val v = map[key]
            if (v != null) { hitCount++; return v }
            missCount++
        }
        val created = create(key) ?: return null
        synchronized(this) {
            createCount++
            val prev = map.put(key, created)
            if (prev != null) map.put(key, prev) else size += safeSizeOf(key, created)
            if (prev != null) entryRemoved(false, key, created, prev)
        }
        trimToSize(maxSize)
        return created
    }

    fun put(key: K, value: V): V? {
        val previous: V?
        synchronized(this) {
            putCount++
            size += safeSizeOf(key, value)
            previous = map.put(key, value)
            if (previous != null) size -= safeSizeOf(key, previous)
        }
        if (previous != null) entryRemoved(false, key, previous, value)
        trimToSize(maxSize)
        return previous
    }

    fun remove(key: K): V? {
        val previous: V?
        synchronized(this) {
            previous = map.remove(key)
            if (previous != null) size -= safeSizeOf(key, previous)
        }
        if (previous != null) entryRemoved(false, key, previous, null)
        return previous
    }

    fun evictAll() = trimToSize(-1)

    fun trimToSize(maxSize: Int) {
        while (true) {
            var key: K? = null
            var value: V? = null
            synchronized(this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    size = 0 // 防御：sizeOf 覆写异常导致失衡时复位
                    return
                }
                if (size <= maxSize) return
                val eldest = map.entries.firstOrNull() ?: return
                key = eldest.key
                value = eldest.value
                map.remove(key)
                size -= safeSizeOf(key!!, value!!)
                evictionCount++
            }
            entryRemoved(true, key!!, value!!, null)
        }
    }

    private fun safeSizeOf(key: K, value: V): Int {
        val result = sizeOf(key, value)
        if (result < 0) throw IllegalStateException("Negative size: $key=$value")
        return result
    }

    @Synchronized fun size(): Int = size
    @Synchronized fun maxSize(): Int = maxSize
    @Synchronized fun snapshot(): Map<K, V> = LinkedHashMap(map)
    @Synchronized fun putCount(): Int = putCount
    @Synchronized fun createCount(): Int = createCount
    @Synchronized fun evictionCount(): Int = evictionCount
    @Synchronized fun hitCount(): Int = hitCount
    @Synchronized fun missCount(): Int = missCount
    @Synchronized override fun toString(): String {
        val accesses = hitCount + missCount
        val hitPercent = if (accesses != 0) 100 * hitCount / accesses else 0
        return "LruCache[maxSize=$maxSize,hits=$hitCount,misses=$missCount,hitRate=$hitPercent%]"
    }
}

/** android.util.SparseArray：int→E 有序映射。 */
open class SparseArray<E>(initialCapacity: Int = 10) : Cloneable {
    private val keys = ArrayList<Int>(initialCapacity)
    private val values = ArrayList<E?>(initialCapacity)

    open fun get(key: Int): E? = get(key, null)

    open fun get(key: Int, valueIfKeyNotFound: E?): E? {
        val i = indexOfKey(key)
        return if (i >= 0) values[i] else valueIfKeyNotFound
    }

    open fun put(key: Int, value: E?) {
        val i = indexOfKey(key)
        if (i >= 0) {
            values[i] = value
        } else {
            val ins = -i - 1
            keys.add(ins, key)
            values.add(ins, value)
        }
    }

    open fun append(key: Int, value: E?) {
        if (keys.isNotEmpty() && keys.last() >= key) { put(key, value); return }
        keys.add(key)
        values.add(value)
    }

    open fun delete(key: Int) = remove(key)

    open fun remove(key: Int) {
        val i = indexOfKey(key)
        if (i >= 0) removeAt(i)
    }

    open fun remove(key: Int, value: Any?): Boolean {
        val i = indexOfKey(key)
        if (i >= 0 && values[i] == value) { removeAt(i); return true }
        return false
    }

    open fun removeAt(index: Int) {
        if (index in 0 until keys.size) {
            keys.removeAt(index)
            values.removeAt(index)
        }
    }

    open fun removeAtRange(index: Int, size: Int) {
        repeat(minOf(size, keys.size - index)) { removeAt(index) }
    }

    open fun clear() { keys.clear(); values.clear() }

    open fun size(): Int = keys.size

    open fun isEmpty(): Boolean = keys.isEmpty()

    open fun keyAt(index: Int): Int = keys[index]

    open fun valueAt(index: Int): E? = values[index]

    open fun setValueAt(index: Int, value: E?) { values[index] = value }

    open fun indexOfKey(key: Int): Int {
        var lo = 0; var hi = keys.size - 1
        while (lo <= hi) {
            val mid = (lo + hi) ushr 1
            val midVal = keys[mid]
            when {
                midVal < key -> lo = mid + 1
                midVal > key -> hi = mid - 1
                else -> return mid
            }
        }
        return -(lo + 1)
    }

    open fun indexOfValue(value: E?): Int = values.indexOf(value)

    open fun indexOfValueByValue(value: E?): Int =
        values.indexOfFirst { it == value }

    open fun containsKey(key: Int): Boolean = indexOfKey(key) >= 0

    open fun containsValue(value: E?): Boolean = values.contains(value)

    public override fun clone(): SparseArray<E> {
        val c = SparseArray<E>(keys.size)
        c.keys.addAll(keys); c.values.addAll(values)
        return c
    }

    override fun toString(): String =
        keys.indices.joinToString(", ", "{", "}") { "${keys[it]}=${values[it]}" }
}

/** android.util.LongSparseArray。 */
open class LongSparseArray<E>(initialCapacity: Int = 10) : Cloneable {
    private val keys = ArrayList<Long>(initialCapacity)
    private val values = ArrayList<E?>(initialCapacity)

    open fun get(key: Long): E? = get(key, null)
    open fun get(key: Long, valueIfKeyNotFound: E?): E? {
        val i = indexOfKey(key)
        return if (i >= 0) values[i] else valueIfKeyNotFound
    }
    open fun put(key: Long, value: E?) {
        val i = indexOfKey(key)
        if (i >= 0) values[i] = value else { val ins = -i - 1; keys.add(ins, key); values.add(ins, value) }
    }
    open fun append(key: Long, value: E?) {
        if (keys.isNotEmpty() && keys.last() >= key) { put(key, value); return }
        keys.add(key); values.add(value)
    }
    open fun delete(key: Long) = remove(key)
    open fun remove(key: Long) { val i = indexOfKey(key); if (i >= 0) removeAt(i) }
    open fun removeAt(index: Int) {
        if (index in 0 until keys.size) { keys.removeAt(index); values.removeAt(index) }
    }
    open fun clear() { keys.clear(); values.clear() }
    open fun size(): Int = keys.size
    open fun isEmpty(): Boolean = keys.isEmpty()
    open fun keyAt(index: Int): Long = keys[index]
    open fun valueAt(index: Int): E? = values[index]
    open fun setValueAt(index: Int, value: E?) { values[index] = value }
    open fun indexOfKey(key: Long): Int {
        var lo = 0; var hi = keys.size - 1
        while (lo <= hi) {
            val mid = (lo + hi) ushr 1
            val midVal = keys[mid]
            when {
                midVal < key -> lo = mid + 1
                midVal > key -> hi = mid - 1
                else -> return mid
            }
        }
        return -(lo + 1)
    }
    open fun indexOfValue(value: E?): Int = values.indexOf(value)
    open fun containsKey(key: Long): Boolean = indexOfKey(key) >= 0
    open fun containsValue(value: E?): Boolean = values.contains(value)

    public override fun clone(): LongSparseArray<E> {
        val c = LongSparseArray<E>(keys.size)
        c.keys.addAll(keys); c.values.addAll(values)
        return c
    }
}

/** android.util.ArrayMap：LinkedHashMap 超集 + 索引访问。 */
open class ArrayMap<K, V> : LinkedHashMap<K, V> {
    constructor() : super()
    constructor(capacity: Int) : super(capacity)
    constructor(map: Map<out K, V>) : super(map)

    open fun keyAt(index: Int): K? = if (index in 0 until size) keys.elementAt(index) else null
    open fun valueAt(index: Int): V? = if (index in 0 until size) values.elementAt(index) else null
    open fun append(key: K, value: V) { put(key, value) }
    open fun removeAt(index: Int): V? {
        val k = keyAt(index) ?: return null
        return remove(k)
    }
    open fun setValueAt(index: Int, value: V): V? {
        val k = keyAt(index) ?: return null
        return put(k, value)
    }
    open fun indexOfKey(key: K?): Int = keys.indexOf(key)
    open fun containsAll(collection: Collection<*>): Boolean = keys.containsAll(collection)
    open fun erase() = clear()
    open fun validate() {}
}

/** android.util.ArraySet：LinkedHashSet 超集 + 索引访问。 */
open class ArraySet<E> : LinkedHashSet<E> {
    constructor() : super()
    constructor(capacity: Int) : super(capacity)
    constructor(set: Collection<E>) : super(set)

    open fun valueAt(index: Int): E? = if (index in 0 until size) elementAt(index) else null
    open fun removeAt(index: Int): E? {
        val v = valueAt(index) ?: return null
        remove(v)
        return v
    }
    open fun indexOfElement(value: E?): Int {
        var i = 0
        for (e in this) { if (e == value) return i; i++ }
        return -1
    }
}

/** android.util.Size：不可变 int 尺寸。 */
class Size(private val width: Int, private val height: Int) : java.io.Serializable {
    fun getWidth(): Int = width
    fun getHeight(): Int = height
    override fun equals(other: Any?): Boolean =
        other is Size && other.width == width && other.height == height
    override fun hashCode(): Int = width xor (height shl 16 or (height ushr 16))
    override fun toString(): String = "${width}x$height"

    companion object {
        @JvmStatic fun parseSize(string: String): Size {
            val sep = string.indexOf('*').let { if (it < 0) string.indexOf('x') else it }
            if (sep < 0) throw IllegalArgumentException("Invalid size: $string")
            return Size(string.substring(0, sep).trim().toInt(), string.substring(sep + 1).trim().toInt())
        }
    }
}

/** android.util.SizeF。 */
class SizeF(private val width: Float, private val height: Float) : java.io.Serializable {
    fun getWidth(): Float = width
    fun getHeight(): Float = height
    override fun equals(other: Any?): Boolean =
        other is SizeF && other.width == width && other.height == height
    override fun hashCode(): Int = width.toBits() xor height.toBits()
    override fun toString(): String = "${width}x$height"
}

/** android.util.Range。 */
class Range<T : Comparable<T>>(val lower: T, val upper: T) {
    init {
        if (lower > upper) throw IllegalArgumentException("lower ($lower) must be <= upper ($upper)")
    }
    fun contains(value: T): Boolean = value >= lower && value <= upper
    fun contains(range: Range<T>): Boolean = range.lower >= lower && range.upper <= upper
    fun clamp(value: T): T = when {
        value < lower -> lower
        value > upper -> upper
        else -> value
    }
    fun intersect(range: Range<T>): Range<T> {
        val newLower = maxOf(lower, range.lower)
        val newUpper = minOf(upper, range.upper)
        return if (newLower <= newUpper) Range(newLower, newUpper) else throw IllegalArgumentException("no intersection")
    }
    fun extend(range: Range<T>): Range<T> = Range(minOf(lower, range.lower), maxOf(upper, range.upper))
    fun extend(value: T): Range<T> = Range(minOf(lower, value), maxOf(upper, value))
    override fun equals(other: Any?): Boolean =
        other is Range<*> && lower == other.lower && upper == other.upper
    override fun hashCode(): Int = lower.hashCode() * 31 + upper.hashCode()
    override fun toString(): String = "[$lower, $upper]"

    companion object {
        @JvmStatic fun <T : Comparable<T>> create(lower: T, upper: T): Range<T> = Range(lower, upper)
    }
}
