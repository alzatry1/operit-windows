package android.util

import java.io.FilterInputStream
import java.io.FilterOutputStream
import java.io.InputStream
import java.io.OutputStream

/**
 * android.util.Base64 桌面实现：委托 java.util.Base64。
 * flags 语义与 AOSP 对齐（DEFAULT/NO_WRAP/NO_PADDING/CRLF/URL_SAFE/NO_CLOSE）。
 */
object Base64 {
    const val DEFAULT = 0
    const val NO_PADDING = 1
    const val NO_WRAP = 2
    const val CRLF = 4
    const val URL_SAFE = 8
    const val NO_CLOSE = 16

    private fun encoder(flags: Int): java.util.Base64.Encoder {
        var e = when {
            flags and URL_SAFE != 0 -> java.util.Base64.getUrlEncoder()
            flags and CRLF != 0 -> java.util.Base64.getMimeEncoder()
            else -> java.util.Base64.getEncoder() // basic 不换行，等价 NO_WRAP 默认
        }
        if (flags and NO_PADDING != 0) e = e.withoutPadding()
        return e
    }

    private fun decoder(flags: Int): java.util.Base64.Decoder =
        if (flags and URL_SAFE != 0) java.util.Base64.getUrlDecoder()
        else java.util.Base64.getMimeDecoder() // mime decoder 忽略非字母表字符，最宽容

    @JvmStatic
    fun encode(input: ByteArray, flags: Int): ByteArray = encoder(flags).encode(input)

    @JvmStatic
    fun encode(input: ByteArray, offset: Int, len: Int, flags: Int): ByteArray =
        encoder(flags).encode(input.copyOfRange(offset, offset + len))

    @JvmStatic
    fun encodeToString(input: ByteArray, flags: Int): String = encoder(flags).encodeToString(input)

    @JvmStatic
    fun encodeToString(input: ByteArray, offset: Int, len: Int, flags: Int): String =
        encoder(flags).encodeToString(input.copyOfRange(offset, offset + len))

    @JvmStatic
    fun decode(str: String, flags: Int): ByteArray = decoder(flags).decode(str)

    @JvmStatic
    fun decode(input: ByteArray, flags: Int): ByteArray = decoder(flags).decode(input)

    @JvmStatic
    fun decode(input: ByteArray, offset: Int, len: Int, flags: Int): ByteArray =
        decoder(flags).decode(input.copyOfRange(offset, offset + len))
}

/** AOSP Base64InputStream：默认解码输入流。 */
class Base64InputStream @JvmOverloads constructor(
    private val source: InputStream,
    private val flags: Int = Base64.DEFAULT,
) : FilterInputStream(source) {
    private val decoded: ByteArray by lazy {
        val raw = source.readBytes()
        try { Base64.decode(raw, flags) } catch (e: IllegalArgumentException) { raw }
    }
    private var pos = 0

    override fun read(): Int = if (pos < decoded.size) decoded[pos++].toInt() and 0xFF else -1

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        if (pos >= decoded.size) return -1
        val n = minOf(len, decoded.size - pos)
        System.arraycopy(decoded, pos, b, off, n)
        pos += n
        return n
    }

    override fun available(): Int = decoded.size - pos
}

/** AOSP Base64OutputStream：写出时编码。 */
class Base64OutputStream @JvmOverloads constructor(
    private val sink: OutputStream,
    private val flags: Int = Base64.DEFAULT,
) : FilterOutputStream(sink) {
    private val buffer = java.io.ByteArrayOutputStream()

    override fun write(b: Int) { buffer.write(b) }
    override fun write(b: ByteArray, off: Int, len: Int) { buffer.write(b, off, len) }

    override fun close() {
        val encoded = Base64.encode(buffer.toByteArray(), flags)
        sink.write(encoded)
        if (flags and Base64.NO_CLOSE == 0) sink.close() else sink.flush()
        super.close()
    }
}
