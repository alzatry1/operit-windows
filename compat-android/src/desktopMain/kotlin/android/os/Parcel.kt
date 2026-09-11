package android.os

import android.util.Log
import java.io.Serializable

/**
 * android.os.Parcelable/Parcel 桌面实现。
 * Parcelable 接口带默认方法：让 @Parcelize 标记的类在无编译器插件时也能编译。
 * Parcel 是内存内顺序读写缓冲（足够进程内传递），marshall 走 Java 序列化兜底。
 */

interface Parcelable {
    fun describeContents(): Int = 0
    fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val CONTENTS_FILE_DESCRIPTOR = 1
        const val PARCELABLE_WRITE_RETURN_VALUE = 1
    }

    interface Creator<T> {
        fun createFromParcel(source: Parcel): T
        fun newArray(size: Int): Array<T?>
    }

    interface ClassLoaderCreator<T> : Creator<T> {
        fun createFromParcel(source: Parcel, loader: ClassLoader?): T
    }
}

class Parcel private constructor() {
    private val data = ArrayList<Any?>()
    private var pos = 0

    fun writeInt(v: Int) { data += v }
    fun readInt(): Int = (data.getOrNull(pos++) as? Number)?.toInt() ?: 0
    fun readLong(): Long = (data.getOrNull(pos++) as? Number)?.toLong() ?: 0L
    fun writeLong(v: Long) { data += v }
    fun writeFloat(v: Float) { data += v }
    fun readFloat(): Float = (data.getOrNull(pos++) as? Number)?.toFloat() ?: 0f
    fun writeDouble(v: Double) { data += v }
    fun readDouble(): Double = (data.getOrNull(pos++) as? Number)?.toDouble() ?: 0.0
    fun writeByte(v: Byte) { data += v }
    fun readByte(): Byte = (data.getOrNull(pos++) as? Number)?.toByte() ?: 0
    fun writeString(s: String?) { data += s }
    fun readString(): String? = data.getOrNull(pos++) as? String
    fun writeBoolean(b: Boolean) { data += b }
    fun readBoolean(): Boolean {
        val v = data.getOrNull(pos++)
        return when (v) { is Boolean -> v; is Number -> v.toInt() != 0; else -> false }
    }
    fun writeValue(v: Any?) { data += v }
    fun readValue(loader: ClassLoader?): Any? = data.getOrNull(pos++)
    fun writeSerializable(s: Serializable?) { data += s }
    fun readSerializable(): Serializable? = data.getOrNull(pos++) as? Serializable
    fun writeParcelable(p: Parcelable?, flags: Int) { data += p }
    @Suppress("UNCHECKED_CAST")
    fun <T : Parcelable?> readParcelable(loader: ClassLoader?): T? = data.getOrNull(pos++) as? T
    fun writeBundle(b: Bundle?) { data += b }
    fun readBundle(): Bundle? = data.getOrNull(pos++) as? Bundle
    fun readBundle(loader: ClassLoader?): Bundle? = readBundle()
    fun writeStrongBinder(b: IBinder?) { data += b }
    fun readStrongBinder(): IBinder? = data.getOrNull(pos++) as? IBinder
    fun writeStrongInterface(i: IInterface?) { data += i }
    fun writeBinderList(l: List<IBinder?>?) { data += l }
    fun writeStringList(l: List<String?>?) { data += l }
    @Suppress("UNCHECKED_CAST")
    fun readStringList(l: MutableList<String>) { (data.getOrNull(pos++) as? List<*>)?.filterIsInstance<String>()?.let(l::addAll) }
    fun writeStringArray(a: Array<String?>?) { data += a }
    @Suppress("UNCHECKED_CAST")
    fun createStringArray(): Array<String?>? = data.getOrNull(pos++) as? Array<String?>
    fun readStringArray(a: Array<String?>) {}
    fun writeIntArray(a: IntArray?) { data += a }
    fun createIntArray(): IntArray? = data.getOrNull(pos++) as? IntArray
    fun readIntArray(a: IntArray) {}
    fun writeLongArray(a: LongArray?) { data += a }
    fun createLongArray(): LongArray? = data.getOrNull(pos++) as? LongArray
    fun writeFloatArray(a: FloatArray?) { data += a }
    fun createFloatArray(): FloatArray? = data.getOrNull(pos++) as? FloatArray
    fun writeDoubleArray(a: DoubleArray?) { data += a }
    fun createDoubleArray(): DoubleArray? = data.getOrNull(pos++) as? DoubleArray
    fun writeBooleanArray(a: BooleanArray?) { data += a }
    fun createBooleanArray(): BooleanArray? = data.getOrNull(pos++) as? BooleanArray
    fun writeByteArray(b: ByteArray?) { data += b }
    fun writeByteArray(b: ByteArray?, off: Int, len: Int) { data += b?.copyOfRange(off, off + len) }
    fun createByteArray(): ByteArray? = data.getOrNull(pos++) as? ByteArray
    fun readByteArray(b: ByteArray) {}
    fun writeCharSequence(cs: CharSequence?) { data += cs }
    fun readCharSequence(): CharSequence? = data.getOrNull(pos++) as? CharSequence
    fun writeCharArray(a: CharArray?) { data += a }
    fun createCharArray(): CharArray? = data.getOrNull(pos++) as? CharArray
    fun writeFileDescriptor(fd: java.io.FileDescriptor?) { data += fd }
    fun readFileDescriptor(): ParcelFileDescriptor? = data.getOrNull(pos++) as? ParcelFileDescriptor
    fun hasFileDescriptors(): Boolean = false
    fun writeException(e: Exception) { data += e }
    fun readException() { (data.getOrNull(pos++) as? Exception)?.let { throw it } }
    fun enforceInterface(name: String) {}

    fun marshall(): ByteArray {
        val baos = java.io.ByteArrayOutputStream()
        java.io.ObjectOutputStream(baos).use { oos ->
            oos.writeInt(data.size)
            for (item in data) {
                if (item is Serializable) oos.writeObject(item) else oos.writeObject(item?.toString())
            }
        }
        return baos.toByteArray()
    }

    fun unmarshall(bytes: ByteArray, offset: Int, length: Int) {
        data.clear(); pos = 0
        try {
            java.io.ObjectInputStream(java.io.ByteArrayInputStream(bytes, offset, length)).use { ois ->
                val n = ois.readInt()
                repeat(n) { data += ois.readObject() }
            }
        } catch (e: Exception) {
            Log.w("Parcel", "unmarshall failed: ${e.message}")
        }
    }

    fun setDataPosition(pos: Int) { this.pos = pos }
    fun dataPosition(): Int = pos
    fun dataSize(): Int = data.size
    fun dataAvail(): Int = data.size - pos
    fun dataCapacity(): Int = Int.MAX_VALUE
    fun setDataCapacity(size: Int) {}
    fun recycle() { data.clear(); pos = 0 }
    fun appendFrom(parcel: Parcel, offset: Int, length: Int) {
        data.addAll(parcel.data.subList(offset, (offset + length).coerceAtMost(parcel.data.size)))
    }

    companion object {
        @JvmStatic fun obtain(): Parcel = Parcel()
        @JvmField val STRING_CREATOR = object : Parcelable.Creator<String> {
            override fun createFromParcel(source: Parcel): String = source.readString() ?: ""
            override fun newArray(size: Int): Array<String?> = arrayOfNulls(size)
        }
    }
}
