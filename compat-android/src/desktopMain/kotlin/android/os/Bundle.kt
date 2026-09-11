package android.os

import java.io.Serializable

/**
 * android.os.Bundle：HashMap 后端 + 全套类型化存取。
 */
class Bundle : Parcelable, Serializable, Cloneable {
    private val map = LinkedHashMap<String, Any?>()

    constructor()
    constructor(capacity: Int)
    constructor(b: Bundle) { map.putAll(b.map) }
    constructor(loader: ClassLoader?)

    // ---- 通用 ----
    fun size(): Int = map.size
    fun isEmpty(): Boolean = map.isEmpty()
    fun clear() = map.clear()
    fun containsKey(key: String?): Boolean = map.containsKey(key)
    fun remove(key: String?) { map.remove(key) }
    fun keySet(): MutableSet<String> = LinkedHashSet(map.keys)
    fun get(key: String?): Any? = map[key]
    fun putAll(bundle: Bundle) { map.putAll(bundle.map) }
    fun getClassLoader(): ClassLoader = javaClass.classLoader
    fun setClassLoader(loader: ClassLoader?) {}
    fun deepCopy(): Bundle = Bundle(this)

    // ---- 基本类型 ----
    fun putBoolean(key: String?, value: Boolean) { map[key.orEmpty()] = value }
    fun getBoolean(key: String?): Boolean = getBoolean(key, false)
    fun getBoolean(key: String?, def: Boolean): Boolean = (map[key] as? Boolean) ?: def
    fun putByte(key: String?, value: Byte) { map[key.orEmpty()] = value }
    fun getByte(key: String?): Byte = getByte(key, 0)
    fun getByte(key: String?, def: Byte): Byte = (map[key] as? Byte) ?: def
    fun putChar(key: String?, value: Char) { map[key.orEmpty()] = value }
    fun getChar(key: String?): Char = getChar(key, 0.toChar())
    fun getChar(key: String?, def: Char): Char = (map[key] as? Char) ?: def
    fun putShort(key: String?, value: Short) { map[key.orEmpty()] = value }
    fun getShort(key: String?): Short = getShort(key, 0)
    fun getShort(key: String?, def: Short): Short = (map[key] as? Short) ?: def
    fun putInt(key: String?, value: Int) { map[key.orEmpty()] = value }
    fun getInt(key: String?): Int = getInt(key, 0)
    fun getInt(key: String?, def: Int): Int = (map[key] as? Number)?.toInt() ?: def
    fun putLong(key: String?, value: Long) { map[key.orEmpty()] = value }
    fun getLong(key: String?): Long = getLong(key, 0L)
    fun getLong(key: String?, def: Long): Long = (map[key] as? Number)?.toLong() ?: def
    fun putFloat(key: String?, value: Float) { map[key.orEmpty()] = value }
    fun getFloat(key: String?): Float = getFloat(key, 0f)
    fun getFloat(key: String?, def: Float): Float = (map[key] as? Number)?.toFloat() ?: def
    fun putDouble(key: String?, value: Double) { map[key.orEmpty()] = value }
    fun getDouble(key: String?): Double = getDouble(key, 0.0)
    fun getDouble(key: String?, def: Double): Double = (map[key] as? Number)?.toDouble() ?: def
    fun putString(key: String?, value: String?) { map[key.orEmpty()] = value }
    fun getString(key: String?): String? = map[key] as? String
    fun getString(key: String?, def: String): String = (map[key] as? String) ?: def
    fun putCharSequence(key: String?, value: CharSequence?) { map[key.orEmpty()] = value }
    fun getCharSequence(key: String?): CharSequence? = map[key] as? CharSequence
    fun getCharSequence(key: String?, def: CharSequence): CharSequence = (map[key] as? CharSequence) ?: def

    // ---- 数组 ----
    fun putBooleanArray(key: String?, value: BooleanArray?) { map[key.orEmpty()] = value }
    fun getBooleanArray(key: String?): BooleanArray? = map[key] as? BooleanArray
    fun putByteArray(key: String?, value: ByteArray?) { map[key.orEmpty()] = value }
    fun getByteArray(key: String?): ByteArray? = map[key] as? ByteArray
    fun putShortArray(key: String?, value: ShortArray?) { map[key.orEmpty()] = value }
    fun getShortArray(key: String?): ShortArray? = map[key] as? ShortArray
    fun putCharArray(key: String?, value: CharArray?) { map[key.orEmpty()] = value }
    fun getCharArray(key: String?): CharArray? = map[key] as? CharArray
    fun putIntArray(key: String?, value: IntArray?) { map[key.orEmpty()] = value }
    fun getIntArray(key: String?): IntArray? = map[key] as? IntArray
    fun putLongArray(key: String?, value: LongArray?) { map[key.orEmpty()] = value }
    fun getLongArray(key: String?): LongArray? = map[key] as? LongArray
    fun putFloatArray(key: String?, value: FloatArray?) { map[key.orEmpty()] = value }
    fun getFloatArray(key: String?): FloatArray? = map[key] as? FloatArray
    fun putDoubleArray(key: String?, value: DoubleArray?) { map[key.orEmpty()] = value }
    fun getDoubleArray(key: String?): DoubleArray? = map[key] as? DoubleArray
    fun putStringArray(key: String?, value: Array<String>?) { map[key.orEmpty()] = value }
    @Suppress("UNCHECKED_CAST")
    fun getStringArray(key: String?): Array<String>? = map[key] as? Array<String>
    fun putCharSequenceArray(key: String?, value: Array<CharSequence>?) { map[key.orEmpty()] = value }
    @Suppress("UNCHECKED_CAST")
    fun getCharSequenceArray(key: String?): Array<CharSequence>? = map[key] as? Array<CharSequence>

    // ---- Parcelable / Serializable / Bundle ----
    fun putParcelable(key: String?, value: Parcelable?) { map[key.orEmpty()] = value }
    @Suppress("UNCHECKED_CAST")
    fun <T : Parcelable?> getParcelable(key: String?): T? = map[key] as? T
    @Suppress("UNCHECKED_CAST")
    fun <T> getParcelable(key: String?, clazz: Class<T>): T? =
        map[key]?.let { if (clazz.isInstance(it)) it as T else null }
    fun putParcelableArray(key: String?, value: Array<out Parcelable>?) { map[key.orEmpty()] = value }
    @Suppress("UNCHECKED_CAST")
    fun <T : Parcelable?> getParcelableArray(key: String?): Array<T>? = map[key] as? Array<T>
    @Suppress("UNCHECKED_CAST")
    fun <T> getParcelableArray(key: String?, clazz: Class<T>): Array<T>? = map[key] as? Array<T>
    fun putSerializable(key: String?, value: Serializable?) { map[key.orEmpty()] = value }
    fun getSerializable(key: String?): Serializable? = map[key] as? Serializable
    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable?> getSerializable(key: String?, clazz: Class<T>): T? =
        map[key]?.let { if (clazz.isInstance(it)) it as T else null }
    fun putBundle(key: String?, value: Bundle?) { map[key.orEmpty()] = value }
    fun getBundle(key: String?): Bundle? = map[key] as? Bundle

    // ---- ArrayList ----
    fun putStringArrayList(key: String?, value: ArrayList<String>?) { map[key.orEmpty()] = value }
    @Suppress("UNCHECKED_CAST")
    fun getStringArrayList(key: String?): ArrayList<String>? = map[key] as? ArrayList<String>
    fun putIntegerArrayList(key: String?, value: ArrayList<Int>?) { map[key.orEmpty()] = value }
    @Suppress("UNCHECKED_CAST")
    fun getIntegerArrayList(key: String?): ArrayList<Int>? = map[key] as? ArrayList<Int>
    fun putCharSequenceArrayList(key: String?, value: ArrayList<CharSequence>?) { map[key.orEmpty()] = value }
    @Suppress("UNCHECKED_CAST")
    fun getCharSequenceArrayList(key: String?): ArrayList<CharSequence>? = map[key] as? ArrayList<CharSequence>
    fun putParcelableArrayList(key: String?, value: ArrayList<out Parcelable>?) { map[key.orEmpty()] = value }
    @Suppress("UNCHECKED_CAST")
    fun <T : Parcelable?> getParcelableArrayList(key: String?): ArrayList<T>? = map[key] as? ArrayList<T>
    @Suppress("UNCHECKED_CAST")
    fun <T> getParcelableArrayList(key: String?, clazz: Class<T>): ArrayList<T>? =
        (map[key] as? List<*>)?.filterIsInstance(clazz)?.let { ArrayList(it) }

    // ---- IBinder ----
    fun putBinder(key: String?, value: IBinder?) { map[key.orEmpty()] = value }
    fun getBinder(key: String?): IBinder? = map[key] as? IBinder

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeValue(LinkedHashMap(map)) }
    fun readFromParcel(parcel: Parcel) {
        @Suppress("UNCHECKED_CAST")
        (parcel.readValue(null) as? Map<String, Any?>)?.let { map.clear(); map.putAll(it) }
    }

    public override fun clone(): Bundle = Bundle(this)

    @Synchronized override fun toString(): String = "Bundle[$map]"

    override fun equals(other: Any?): Boolean = other is Bundle && other.map == map
    override fun hashCode(): Int = map.hashCode()

    companion object {
        @JvmField val EMPTY: Bundle = Bundle()

        @JvmField val CREATOR: Parcelable.Creator<Bundle> = object : Parcelable.Creator<Bundle> {
            override fun createFromParcel(source: Parcel): Bundle = Bundle().also { it.readFromParcel(source) }
            override fun newArray(size: Int): Array<Bundle?> = arrayOfNulls(size)
        }
    }
}
