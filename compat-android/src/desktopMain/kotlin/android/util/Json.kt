package android.util

import java.io.Closeable
import java.io.Flushable
import java.io.IOException
import java.io.Reader
import java.io.Writer

/**
 * android.util.Json* 桌面实现：委托 com.google.gson.stream（AOSP 的 JsonReader 本就源自 gson）。
 * gson 的 MalformedJsonException 会转换为本包的同名异常。
 */

enum class JsonToken {
    BEGIN_ARRAY, END_ARRAY, BEGIN_OBJECT, END_OBJECT,
    NAME, STRING, NUMBER, BOOLEAN, NULL, END_DOCUMENT;

    companion object {
        internal fun from(g: com.google.gson.stream.JsonToken): JsonToken = when (g) {
            com.google.gson.stream.JsonToken.BEGIN_ARRAY -> BEGIN_ARRAY
            com.google.gson.stream.JsonToken.END_ARRAY -> END_ARRAY
            com.google.gson.stream.JsonToken.BEGIN_OBJECT -> BEGIN_OBJECT
            com.google.gson.stream.JsonToken.END_OBJECT -> END_OBJECT
            com.google.gson.stream.JsonToken.NAME -> NAME
            com.google.gson.stream.JsonToken.STRING -> STRING
            com.google.gson.stream.JsonToken.NUMBER -> NUMBER
            com.google.gson.stream.JsonToken.BOOLEAN -> BOOLEAN
            com.google.gson.stream.JsonToken.NULL -> NULL
            com.google.gson.stream.JsonToken.END_DOCUMENT -> END_DOCUMENT
        }
    }
}

class MalformedJsonException : IOException {
    constructor(message: String?) : super(message)
    constructor(message: String?, throwable: Throwable?) : super(message, throwable)
    constructor(throwable: Throwable?) : super(throwable)
}

private inline fun <T> jsonGuard(block: () -> T): T {
    try {
        return block()
    } catch (e: com.google.gson.stream.MalformedJsonException) {
        throw MalformedJsonException(e.message, e)
    } catch (e: NumberFormatException) {
        throw MalformedJsonException(e.message, e)
    }
}

class JsonReader(`in`: Reader) : Closeable {
    private val g = com.google.gson.stream.JsonReader(`in`)

    var isLenient: Boolean
        get() = g.isLenient
        set(value) { g.isLenient = value }

    @Throws(IOException::class) fun beginArray() = jsonGuard { g.beginArray() }
    @Throws(IOException::class) fun endArray() = jsonGuard { g.endArray() }
    @Throws(IOException::class) fun beginObject() = jsonGuard { g.beginObject() }
    @Throws(IOException::class) fun endObject() = jsonGuard { g.endObject() }
    @Throws(IOException::class) fun hasNext(): Boolean = jsonGuard { g.hasNext() }
    @Throws(IOException::class) fun peek(): JsonToken = jsonGuard { JsonToken.from(g.peek()) }
    @Throws(IOException::class) fun nextName(): String = jsonGuard { g.nextName() }
    @Throws(IOException::class) fun nextString(): String = jsonGuard { g.nextString() }
    @Throws(IOException::class) fun nextBoolean(): Boolean = jsonGuard { g.nextBoolean() }
    @Throws(IOException::class) fun nextNull() = jsonGuard { g.nextNull() }
    @Throws(IOException::class) fun nextDouble(): Double = jsonGuard { g.nextDouble() }
    @Throws(IOException::class) fun nextLong(): Long = jsonGuard { g.nextLong() }
    @Throws(IOException::class) fun nextInt(): Int = jsonGuard { g.nextInt() }
    @Throws(IOException::class) fun skipValue() = jsonGuard { g.skipValue() }
    // gson 2.10.1 尚无 promoteNameToValue()，暂缺（AOSP API 21+ 很少用）
    @Throws(IOException::class) override fun close() = g.close()
    fun getPath(): String = g.path
    override fun toString(): String = g.toString()
}

class JsonWriter(out: Writer) : Closeable, Flushable {
    private val g = com.google.gson.stream.JsonWriter(out)

    var isLenient: Boolean
        get() = g.isLenient
        set(value) { g.isLenient = value }

    var serializeNulls: Boolean
        get() = g.serializeNulls
        set(value) { g.serializeNulls = value }

    @Throws(IOException::class) fun beginArray(): JsonWriter = jsonGuard { g.beginArray() }.let { this }
    @Throws(IOException::class) fun endArray(): JsonWriter = jsonGuard { g.endArray() }.let { this }
    @Throws(IOException::class) fun beginObject(): JsonWriter = jsonGuard { g.beginObject() }.let { this }
    @Throws(IOException::class) fun endObject(): JsonWriter = jsonGuard { g.endObject() }.let { this }
    @Throws(IOException::class) fun name(name: String): JsonWriter = jsonGuard { g.name(name) }.let { this }
    @Throws(IOException::class) fun value(value: String?): JsonWriter = jsonGuard { g.value(value) }.let { this }
    @Throws(IOException::class) fun value(value: Boolean): JsonWriter = jsonGuard { g.value(value) }.let { this }
    @Throws(IOException::class) fun value(value: Boolean?): JsonWriter = jsonGuard { g.value(value) }.let { this }
    @Throws(IOException::class) fun value(value: Double): JsonWriter = jsonGuard { g.value(value) }.let { this }
    @Throws(IOException::class) fun value(value: Long): JsonWriter = jsonGuard { g.value(value) }.let { this }
    @Throws(IOException::class) fun value(value: Number?): JsonWriter = jsonGuard { g.value(value) }.let { this }
    @Throws(IOException::class) fun nullValue(): JsonWriter = jsonGuard { g.nullValue() }.let { this }
    @Throws(IOException::class) fun jsonValue(value: String?): JsonWriter = jsonGuard { g.jsonValue(value) }.let { this }
    fun setIndent(indent: String) { g.setIndent(indent) }
    @Throws(IOException::class) override fun flush() = g.flush()
    @Throws(IOException::class) override fun close() = g.close()
}
