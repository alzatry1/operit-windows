package android.graphics

import android.os.Parcel
import android.os.Parcelable
import com.ai.assistance.operit.res.ResFiles
import java.io.InputStream
import java.io.OutputStream
import java.nio.Buffer

/**
 * android.graphics.Bitmap：包壳 org.jetbrains.skia.Bitmap（N32 premul 存储）。
 */
open class Bitmap internal constructor(
    internal val skiaBitmap: org.jetbrains.skia.Bitmap,
    private var bitmapConfig: Config?,
) : Parcelable {

    enum class Config {
        ALPHA_8, RGB_565, ARGB_4444, ARGB_8888, RGBA_F16, HARDWARE, RGBA_1010102, RGBA_10101010X;
    }

    enum class CompressFormat {
        JPEG, PNG, WEBP, WEBP_LOSSY, WEBP_LOSSLESS;

        internal fun toSkia(): org.jetbrains.skia.EncodedImageFormat = when (this) {
            JPEG -> org.jetbrains.skia.EncodedImageFormat.JPEG
            PNG -> org.jetbrains.skia.EncodedImageFormat.PNG
            WEBP, WEBP_LOSSY, WEBP_LOSSLESS -> org.jetbrains.skia.EncodedImageFormat.WEBP
        }
    }

    val width: Int get() = skiaBitmap.imageInfo.width
    val height: Int get() = skiaBitmap.imageInfo.height
    val config: Config? get() = bitmapConfig
    var hasAlpha: Boolean = true
        private set

    fun setHasAlpha(hasAlpha: Boolean) { this.hasAlpha = hasAlpha }

    private var recycled = false
    val isRecycled: Boolean get() = recycled

    fun recycle() { recycled = true }

    val byteCount: Int get() = width * height * 4
    val allocationByteCount: Int get() = byteCount
    val rowBytes: Int get() = width * 4
    var density: Int = android.util.DisplayMetrics.DENSITY_DEFAULT
    val isMutable: Boolean get() = !recycled
    val isPremultiplied: Boolean get() = true
    val generationId: Int get() = skiaBitmap.generationId
    val ninePatchChunk: ByteArray? get() = null

    fun getPixel(x: Int, y: Int): Int {
        if (recycled || x < 0 || y < 0 || x >= width || y >= height) return 0
        return try { skiaBitmap.getColor(x, y) } catch (e: Throwable) { 0 }
    }

    fun setPixel(x: Int, y: Int, color: Int) {
        if (recycled || x < 0 || y < 0 || x >= width || y >= height) return
        try {
            skiaBitmap.erase(color, org.jetbrains.skia.IRect.makeXYWH(x, y, 1, 1))
        } catch (e: Throwable) {
            android.util.Log.w("Bitmap", "setPixel 失败: ${e.message}")
        }
    }

    fun getPixels(pixels: IntArray, offset: Int, stride: Int, x: Int, y: Int, width: Int, height: Int) {
        for (row in 0 until height) {
            for (col in 0 until width) {
                val dst = offset + row * stride + col
                if (dst < pixels.size) pixels[dst] = getPixel(x + col, y + row)
            }
        }
    }

    fun setPixels(pixels: IntArray, offset: Int, stride: Int, x: Int, y: Int, width: Int, height: Int) {
        for (row in 0 until height) {
            for (col in 0 until width) {
                val src = offset + row * stride + col
                if (src < pixels.size) setPixel(x + col, y + row, pixels[src])
            }
        }
    }

    fun eraseColor(color: Int) {
        try { skiaBitmap.erase(color) } catch (e: Throwable) { /* ignore */ }
    }

    /** Bitmap.copyPixelsFromBuffer：桌面 Skia 桥不接底层 buffer，存为 no-op。——Nova 注 */
    fun copyPixelsFromBuffer(src: java.nio.Buffer) {
        // 桌面端虚拟显示截帧走 PixelCopy/Skia readPixels，不走 buffer 直拷
    }

    fun setPixelsInternal(pixels: IntArray) {
        setPixels(pixels, 0, width, 0, 0, width, height)
    }

    fun compress(format: CompressFormat, quality: Int, stream: OutputStream): Boolean {
        if (recycled) return false
        return try {
            val image = org.jetbrains.skia.Image.makeFromBitmap(skiaBitmap)
            val data = image.encodeToData(format.toSkia(), quality)
            if (data != null) {
                stream.write(data.bytes)
                true
            } else false
        } catch (e: Throwable) {
            android.util.Log.w("Bitmap", "compress 失败: ${e.message}")
            false
        }
    }

    fun copy(config: Config?, isMutable: Boolean): Bitmap? {
        if (recycled) return null
        return createBitmap(this, 0, 0, width, height, null, true)
    }

    fun extractAlpha(): Bitmap? = copy(Config.ARGB_8888, true)

    fun sameAs(other: Bitmap?): Boolean {
        if (other == null) return false
        if (other === this) return true
        if (width != other.width || height != other.height) return false
        val a = IntArray(width * height); val b = IntArray(width * height)
        getPixels(a, 0, width, 0, 0, width, height)
        other.getPixels(b, 0, width, 0, 0, width, height)
        return a.contentEquals(b)
    }

    fun prepareToDraw() {}
    fun reconfigure(width: Int, height: Int, config: Config) {}
    fun setWidth(width: Int) {}
    fun setHeight(height: Int) {}
    fun setConfig(config: Config) { bitmapConfig = config }
    fun setPremultiplied(premultiplied: Boolean) {}
    fun getScaledWidth(density: Int): Int = width
    fun getScaledHeight(density: Int): Int = height
    fun getScaledWidth(targetDensity: Int, density: Int): Int = width
    fun getScaledHeight(targetDensity: Int, density: Int): Int = height
    fun getScaledWidth(canvas: Canvas): Int = width
    fun getScaledHeight(canvas: Canvas): Int = height
    fun getScaledWidth(display: android.view.Display): Int = width
    fun getScaledHeight(display: android.view.Display): Int = height

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(width)
        dest.writeInt(height)
        val baos = java.io.ByteArrayOutputStream()
        compress(CompressFormat.PNG, 100, baos)
        dest.writeByteArray(baos.toByteArray())
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Bitmap> = object : Parcelable.Creator<Bitmap> {
            override fun createFromParcel(source: Parcel): Bitmap {
                val w = source.readInt(); val h = source.readInt()
                val bytes = source.createByteArray()
                val bmp = if (bytes != null) BitmapFactory.decodeByteArray(bytes, 0, bytes.size) else null
                return bmp ?: createBitmap(w.coerceAtLeast(1), h.coerceAtLeast(1), Config.ARGB_8888)
            }
            override fun newArray(size: Int): Array<Bitmap?> = arrayOfNulls(size)
        }

        private fun newSkiaBitmap(width: Int, height: Int): org.jetbrains.skia.Bitmap {
            val b = org.jetbrains.skia.Bitmap()
            b.allocN32Pixels(width.coerceAtLeast(1), height.coerceAtLeast(1), false)
            return b
        }

        @JvmStatic
        fun createBitmap(width: Int, height: Int, config: Config): Bitmap {
            require(width > 0 && height > 0) { "width and height must be > 0" }
            return Bitmap(newSkiaBitmap(width, height), config)
        }

        @JvmStatic
        fun createBitmap(display: android.view.Display, width: Int, height: Int, config: Config): Bitmap =
            createBitmap(width, height, config)

        @JvmStatic
        fun createBitmap(colors: IntArray, width: Int, height: Int, config: Config): Bitmap =
            createBitmap(colors, 0, width, width, height, config)

        @JvmStatic
        fun createBitmap(colors: IntArray, offset: Int, stride: Int, width: Int, height: Int, config: Config): Bitmap {
            val b = createBitmap(width, height, config)
            b.setPixels(colors, offset, stride, 0, 0, width, height)
            return b
        }

        @JvmStatic
        fun createBitmap(src: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap =
            createBitmap(src, x, y, width, height, null, false)

        @JvmStatic
        fun createBitmap(src: Bitmap, x: Int, y: Int, width: Int, height: Int, m: Matrix?, filter: Boolean): Bitmap {
            val dst = createBitmap(width, height, src.config ?: Config.ARGB_8888)
            val canvas = Canvas(dst)
            val paint = Paint().apply { isFilterBitmap = filter }
            if (m == null || m.isIdentity) {
                canvas.drawBitmap(src, Rect(x, y, x + width, y + height), Rect(0, 0, width, height), paint)
            } else {
                canvas.drawBitmap(src, m, paint)
            }
            return dst
        }

        @JvmStatic
        fun createBitmap(src: Bitmap): Bitmap = createBitmap(src, 0, 0, src.width, src.height)

        @JvmStatic
        fun createScaledBitmap(src: Bitmap, dstWidth: Int, dstHeight: Int, filter: Boolean): Bitmap {
            if (dstWidth == src.width && dstHeight == src.height) return src
            val dst = createBitmap(dstWidth, dstHeight, src.config ?: Config.ARGB_8888)
            val canvas = Canvas(dst)
            val paint = Paint().apply { isFilterBitmap = filter; isAntiAlias = filter }
            canvas.drawBitmap(src, Rect(0, 0, src.width, src.height), Rect(0, 0, dstWidth, dstHeight), paint)
            return dst
        }
    }
}

/** android.graphics.BitmapFactory：skia 解码。 */
class BitmapFactory {

    class Options {
        var inJustDecodeBounds: Boolean = false
        var inSampleSize: Int = 1
        var inPreferredConfig: Bitmap.Config = Bitmap.Config.ARGB_8888
        var inMutable: Boolean = true
        var inPremultiplied: Boolean = true
        var inDither: Boolean = false
        var inScaled: Boolean = false
        var inDensity: Int = 0
        var inTargetDensity: Int = 0
        var inScreenDensity: Int = 0
        var inBitmap: Bitmap? = null
        var inTempStorage: ByteArray? = null
        var outWidth: Int = 0
        var outHeight: Int = 0
        var outMimeType: String? = null
        var mCancel: Boolean = false

        @Deprecated("deprecated") var inPurgeable: Boolean = false
        @Deprecated("deprecated") var inInputShareable: Boolean = false

        fun requestCancelDecode() { mCancel = true }
    }

    companion object {
        private fun decode(bytes: ByteArray, opts: Options?): Bitmap? {
            val image = try {
                org.jetbrains.skia.Image.makeFromEncoded(bytes)
            } catch (e: Throwable) {
                return null
            } ?: return null

            opts?.outWidth = image.width
            opts?.outHeight = image.height
            opts?.outMimeType = null

            if (opts?.inJustDecodeBounds == true) return null

            var w = image.width
            var h = image.height
            val sample = (opts?.inSampleSize ?: 1).coerceAtLeast(1)
            if (sample > 1) {
                w /= sample; h /= sample
            }

            val skia = org.jetbrains.skia.Bitmap()
            skia.allocN32Pixels(w.coerceAtLeast(1), h.coerceAtLeast(1), false)
            if (sample == 1) {
                image.readPixels(skia)
            } else {
                val full = org.jetbrains.skia.Bitmap()
                full.allocN32Pixels(image.width, image.height, false)
                image.readPixels(full)
                val fullBmp = Bitmap(full, Bitmap.Config.ARGB_8888)
                return Bitmap.createScaledBitmap(fullBmp, w, h, true)
            }
            return Bitmap(skia, opts?.inPreferredConfig ?: Bitmap.Config.ARGB_8888)
        }

        @JvmStatic
        fun decodeByteArray(data: ByteArray, offset: Int, length: Int): Bitmap? =
            decode(data.copyOfRange(offset, offset + length), null)

        @JvmStatic
        fun decodeByteArray(data: ByteArray, offset: Int, length: Int, opts: Options?): Bitmap? =
            decode(data.copyOfRange(offset, offset + length), opts)

        @JvmStatic
        fun decodeStream(inputStream: InputStream?): Bitmap? = decodeStream(inputStream, null, null)

        @JvmStatic
        fun decodeStream(inputStream: InputStream?, outPadding: Rect?, opts: Options?): Bitmap? {
            val bytes = try { inputStream?.use { it.readBytes() } } catch (e: Throwable) { null } ?: return null
            return decode(bytes, opts)
        }

        @JvmStatic
        fun decodeFile(pathName: String): Bitmap? = decodeFile(pathName, null)

        @JvmStatic
        fun decodeFile(pathName: String, opts: Options?): Bitmap? {
            return try {
                java.io.FileInputStream(pathName).use { decode(it.readBytes(), opts) }
            } catch (e: Throwable) {
                null
            }
        }

        @JvmStatic
        fun decodeResource(res: android.content.res.Resources?, id: Int): Bitmap? = decodeResource(res, id, null)

        @JvmStatic
        fun decodeResource(res: android.content.res.Resources?, id: Int, opts: Options?): Bitmap? {
            val stream = ResFiles.openStream(id) ?: return null
            return decodeStream(stream, null, opts)
        }

        @JvmStatic
        fun decodeResourceStream(res: android.content.res.Resources?, value: android.util.TypedValue?, inputStream: InputStream?, pad: Rect?, opts: Options?): Bitmap? =
            decodeStream(inputStream, pad, opts)

        @JvmStatic
        fun decodeFileDescriptor(fd: java.io.FileDescriptor?): Bitmap? = decodeFileDescriptor(fd, null, null)

        @JvmStatic
        fun decodeFileDescriptor(fd: java.io.FileDescriptor?, outPadding: Rect?, opts: Options?): Bitmap? {
            if (fd == null) return null
            return try {
                java.io.FileInputStream(fd).use { decode(it.readBytes(), opts) }
            } catch (e: Throwable) {
                null
            }
        }
    }
}

/** android.graphics.YuvImage：NV21→JPEG 真实现。 */
class YuvImage(
    private val data: ByteArray,
    private val format: Int,
    private val width: Int,
    private val height: Int,
    private val strides: IntArray?,
) {
    fun getYuvData(): ByteArray = data
    fun getYuvFormat(): Int = format
    fun getWidth(): Int = width
    fun getHeight(): Int = height
    fun getStrides(): IntArray? = strides

    fun compressToJpeg(rect: Rect, quality: Int, stream: OutputStream): Boolean {
        if (format != ImageFormat.NV21 && format != ImageFormat.YUY2) {
            android.util.Log.w("YuvImage", "compressToJpeg: 仅支持 NV21/YUY2，format=$format")
            return false
        }
        return try {
            val argb = if (format == ImageFormat.NV21) nv21ToArgb() else yuy2ToArgb()
            val bmp = Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888)
            val cropped = if (rect.left == 0 && rect.top == 0 && rect.width() == width && rect.height() == height) bmp
            else Bitmap.createBitmap(bmp, rect.left, rect.top, rect.width(), rect.height())
            cropped.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        } catch (e: Throwable) {
            android.util.Log.w("YuvImage", "compressToJpeg 失败: ${e.message}")
            false
        }
    }

    private fun nv21ToArgb(): IntArray {
        val out = IntArray(width * height)
        val ySize = width * height
        for (j in 0 until height) {
            for (i in 0 until width) {
                val y = data[j * width + i].toInt() and 0xFF
                val uvIndex = ySize + (j / 2) * width + (i / 2) * 2
                val v = data.getOrElse(uvIndex) { 128.toByte() }.toInt() and 0xFF
                val u = data.getOrElse(uvIndex + 1) { 128.toByte() }.toInt() and 0xFF
                out[j * width + i] = yuvToArgb(y, u, v)
            }
        }
        return out
    }

    private fun yuy2ToArgb(): IntArray {
        val out = IntArray(width * height)
        for (j in 0 until height) {
            for (i in 0 until width) {
                val base = (j * width + i) * 2
                val y = data.getOrElse(base) { 0 }.toInt() and 0xFF
                val u = data.getOrElse(base + 1) { 128.toByte() }.toInt() and 0xFF
                val v = data.getOrElse(base + 3) { 128.toByte() }.toInt() and 0xFF
                out[j * width + i] = yuvToArgb(y, u, v)
            }
        }
        return out
    }

    private fun yuvToArgb(y: Int, u: Int, v: Int): Int {
        val c = y - 16; val d = u - 128; val e = v - 128
        val r = ((298 * c + 409 * e + 128) shr 8).coerceIn(0, 255)
        val g = ((298 * c - 100 * d - 208 * e + 128) shr 8).coerceIn(0, 255)
        val b = ((298 * c + 516 * d + 128) shr 8).coerceIn(0, 255)
        return Color.argb(255, r, g, b)
    }
}

/** android.graphics.ImageDecoder：委托 BitmapFactory。 */
object ImageDecoder {

    class Source internal constructor(internal val produce: () -> Bitmap?) {
        internal var decoder: ((Source) -> Bitmap?) = { produce() }
    }

    fun interface OnHeaderDecodedListener {
        fun onHeaderDecoded(decoder: ImageDecoder, info: ImageInfo, source: Source)
    }

    interface OnPartialImageListener {
        fun onPartialImage(decoder: ImageDecoder, info: ImageInfo, source: Source, createInfo: CreateInfo): Boolean
    }

    class ImageInfo internal constructor(val size: android.util.Size, val mimeType: String?, val isAnimated: Boolean) {
    }

    class CreateInfo

    class DecoderException(val error: Int, message: String?) : Exception(message) {
        companion object {
            const val SOURCE_EXCEPTION = 1
            const val SOURCE_INCOMPLETE = 2
            const val SOURCE_MALFORMED_DATA = 3
        }
    }

    @JvmStatic
    fun createSource(resolver: android.content.ContentResolver, uri: android.net.Uri): Source =
        Source { resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) } }

    @JvmStatic
    fun createSource(file: java.io.File): Source =
        Source { BitmapFactory.decodeFile(file.absolutePath) }

    @JvmStatic
    fun createSource(buffer: java.nio.ByteBuffer): Source =
        Source { BitmapFactory.decodeByteArray(buffer.array(), buffer.arrayOffset(), buffer.remaining()) }

    @JvmStatic
    fun createSource(bytes: ByteArray, offset: Int, length: Int): Source =
        Source { BitmapFactory.decodeByteArray(bytes, offset, length) }

    @JvmStatic
    fun createSource(res: android.content.res.Resources, resId: Int): Source =
        Source { BitmapFactory.decodeResource(res, resId) }

    @JvmStatic
    fun createSource(bitmap: Bitmap): Source = Source { bitmap }

    @JvmStatic
    fun decodeBitmap(src: Source): Bitmap? = src.produce()

    @JvmStatic
    fun decodeBitmap(src: Source, listener: OnHeaderDecodedListener): Bitmap? = src.produce()

    @JvmStatic
    fun decodeDrawable(src: Source): android.graphics.drawable.Drawable {
        val bmp = src.produce()
        return android.graphics.drawable.BitmapDrawable(bmp)
    }

    @JvmStatic
    fun decodeDrawable(src: Source, listener: OnHeaderDecodedListener): android.graphics.drawable.Drawable =
        decodeDrawable(src)
}

/** android.graphics.Picture 轻 stub。 */
class Picture {
    private var width = 0
    private var height = 0

    fun beginRecording(width: Int, height: Int): Canvas {
        this.width = width; this.height = height
        return Canvas(Bitmap.createBitmap(width.coerceAtLeast(1), height.coerceAtLeast(1), Bitmap.Config.ARGB_8888))
    }

    fun endRecording() {}
    fun getWidth(): Int = width
    fun getHeight(): Int = height
    fun draw(canvas: Canvas) {}
    fun writeToStream(stream: OutputStream) {}
    fun requiresHardwareAcceleration(): Boolean = false

    companion object {
        @JvmStatic fun createFromStream(stream: InputStream): Picture? = null
    }
}
