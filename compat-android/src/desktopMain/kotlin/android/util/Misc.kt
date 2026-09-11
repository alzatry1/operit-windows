package android.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern

/** android.util.AttributeSet：纯接口 stub（桌面无 XML 布局 inflation）。 */
interface AttributeSet {
    fun getAttributeCount(): Int
    fun getAttributeName(index: Int): String
    fun getAttributeValue(index: Int): String?
    fun getAttributeValue(namespace: String?, name: String): String?
    fun getPositionDescription(): String
    fun getAttributeNameResource(index: Int): Int
    fun getAttributeListValue(namespace: String?, attribute: String, options: Array<String>, defaultValue: Int): Int
    fun getAttributeBooleanValue(namespace: String?, attribute: String, defaultValue: Boolean): Boolean
    fun getAttributeBooleanValue(index: Int, defaultValue: Boolean): Boolean
    fun getAttributeResourceValue(namespace: String?, attribute: String, defaultValue: Int): Int
    fun getAttributeResourceValue(index: Int, defaultValue: Int): Int
    fun getAttributeIntValue(namespace: String?, attribute: String, defaultValue: Int): Int
    fun getAttributeIntValue(index: Int, defaultValue: Int): Int
    fun getAttributeUnsignedIntValue(namespace: String?, attribute: String, defaultValue: Int): Int
    fun getAttributeUnsignedIntValue(index: Int, defaultValue: Int): Int
    fun getAttributeFloatValue(namespace: String?, attribute: String, defaultValue: Float): Float
    fun getAttributeFloatValue(index: Int, defaultValue: Float): Float
    fun getIdAttributeResourceValue(defaultValue: Int): Int
    fun getIdAttribute(): String?
    fun getClassAttribute(): String?
    fun getStyleAttribute(): Int
}

/** android.util.DisplayMetrics：桌面屏幕指标（AWT 探测，失败回退 1920x1080）。 */
class DisplayMetrics {
    var widthPixels: Int = 0
    var heightPixels: Int = 0
    var density: Float = 1.0f
    var densityDpi: Int = DENSITY_DEFAULT
    var scaledDensity: Float = 1.0f
    var xdpi: Float = DENSITY_DEFAULT.toFloat()
    var ydpi: Float = DENSITY_DEFAULT.toFloat()
    var noncompatWidthPixels: Int = 0
    var noncompatHeightPixels: Int = 0
    var noncompatDensity: Float = 1.0f
    var noncompatDensityDpi: Int = DENSITY_DEFAULT
    var noncompatScaledDensity: Float = 1.0f
    var noncompatXdpi: Float = DENSITY_DEFAULT.toFloat()
    var noncompatYdpi: Float = DENSITY_DEFAULT.toFloat()

    init {
        setToDefaults()
    }

    fun setTo(o: DisplayMetrics) {
        widthPixels = o.widthPixels; heightPixels = o.heightPixels
        density = o.density; densityDpi = o.densityDpi; scaledDensity = o.scaledDensity
        xdpi = o.xdpi; ydpi = o.ydpi
    }

    fun setToDefaults() {
        val size = try {
            java.awt.Toolkit.getDefaultToolkit().screenSize
        } catch (t: Throwable) { null }
        widthPixels = size?.width ?: 1920
        heightPixels = size?.height ?: 1080
        densityDpi = 96
        density = densityDpi / DENSITY_DEFAULT.toFloat()
        scaledDensity = density
        xdpi = densityDpi.toFloat(); ydpi = xdpi
        noncompatWidthPixels = widthPixels; noncompatHeightPixels = heightPixels
        noncompatDensity = density; noncompatDensityDpi = densityDpi; noncompatScaledDensity = scaledDensity
        noncompatXdpi = xdpi; noncompatYdpi = ydpi
    }

    override fun equals(other: Any?): Boolean = other is DisplayMetrics &&
        other.widthPixels == widthPixels && other.heightPixels == heightPixels &&
        other.densityDpi == densityDpi && other.density == density && other.scaledDensity == scaledDensity
    fun equals(o: DisplayMetrics?): Boolean = equals(o as Any?)
    override fun hashCode(): Int = widthPixels * 31 + heightPixels * 17 + densityDpi
    override fun toString(): String = "DisplayMetrics{density=$density, width=$widthPixels, height=$heightPixels}"

    companion object {
        const val DENSITY_UNKNOWN = 0
        const val DENSITY_DEFAULT = 160
        const val DENSITY_LOW = 120
        const val DENSITY_MEDIUM = 160
        const val DENSITY_TV = 213
        const val DENSITY_HIGH = 240
        const val DENSITY_XHIGH = 320
        const val DENSITY_XXHIGH = 480
        const val DENSITY_XXXHIGH = 640
        const val DENSITY_400 = 400
        const val DENSITY_260 = 260
        const val DENSITY_280 = 280
        const val DENSITY_300 = 300
        const val DENSITY_340 = 340
        const val DENSITY_360 = 360
        const val DENSITY_420 = 420
        const val DENSITY_440 = 440
        const val DENSITY_450 = 450
        const val DENSITY_560 = 560
        const val DENSITY_600 = 600
        const val DENSITY_DEVICE_STABLE = 160
    }
}

/** android.util.TypedValue：值容器 + applyDimension。 */
class TypedValue {
    var type: Int = TYPE_NULL
    var string: CharSequence? = null
    var data: Int = 0
    var resourceId: Int = 0
    var changingConfigurations: Int = 0
    var density: Int = 0
    var assetCookie: Int = 0

    fun getFloat(): Float = java.lang.Float.intBitsToFloat(data)
    fun setTo(other: TypedValue) {
        type = other.type; string = other.string; data = other.data
        resourceId = other.resourceId; density = other.density
    }
    fun coerceToString(): CharSequence? {
        return when (type) {
            TYPE_STRING -> string
            TYPE_FLOAT -> getFloat().toString()
            TYPE_DIMENSION -> data.toString()
            else -> if (type in TYPE_FIRST_INT..TYPE_LAST_INT) data.toString() else null
        }
    }
    override fun toString(): String = "TypedValue{t=$type d=$data}"

    companion object {
        const val TYPE_NULL = 0
        const val TYPE_REFERENCE = 1
        const val TYPE_ATTRIBUTE = 2
        const val TYPE_STRING = 3
        const val TYPE_FLOAT = 4
        const val TYPE_DIMENSION = 5
        const val TYPE_FRACTION = 6
        const val TYPE_DYNAMIC_REFERENCE = 7
        const val TYPE_FIRST_INT = 16
        const val TYPE_INT_DEC = 16
        const val TYPE_INT_HEX = 17
        const val TYPE_INT_BOOLEAN = 18
        const val TYPE_FIRST_COLOR_INT = 28
        const val TYPE_INT_COLOR_ARGB8 = 28
        const val TYPE_INT_COLOR_RGB8 = 29
        const val TYPE_INT_COLOR_ARGB4 = 30
        const val TYPE_INT_COLOR_RGB4 = 31
        const val TYPE_LAST_COLOR_INT = 31
        const val TYPE_LAST_INT = 31

        const val COMPLEX_UNIT_PX = 0
        const val COMPLEX_UNIT_DIP = 1
        const val COMPLEX_UNIT_SP = 2
        const val COMPLEX_UNIT_PT = 3
        const val COMPLEX_UNIT_IN = 4
        const val COMPLEX_UNIT_MM = 5
        const val COMPLEX_UNIT_SHIFT = 0
        const val COMPLEX_UNIT_MASK = 15
        const val COMPLEX_UNIT_FRACTION = 0
        const val COMPLEX_UNIT_FRACTION_PARENT = 1
        const val COMPLEX_RADIX_23p0 = 0
        const val COMPLEX_RADIX_16p7 = 1
        const val COMPLEX_RADIX_8p15 = 2
        const val COMPLEX_RADIX_0p23 = 3
        const val COMPLEX_RADIX_SHIFT = 4
        const val COMPLEX_RADIX_MASK = 3
        const val COMPLEX_MANTISSA_SHIFT = 8
        const val COMPLEX_MANTISSA_MASK = 0xFFFFFF

        const val DENSITY_NONE = 0

        @JvmStatic
        fun applyDimension(unit: Int, value: Float, metrics: DisplayMetrics): Float = when (unit) {
            COMPLEX_UNIT_PX -> value
            COMPLEX_UNIT_DIP -> value * metrics.density
            COMPLEX_UNIT_SP -> value * metrics.scaledDensity
            COMPLEX_UNIT_PT -> value * metrics.xdpi * (1.0f / 72)
            COMPLEX_UNIT_IN -> value * metrics.xdpi
            COMPLEX_UNIT_MM -> value * metrics.xdpi * (1.0f / 25.4f)
            else -> 0f
        }

        @JvmStatic
        fun complexToFloat(complex: Int): Float =
            (complex and COMPLEX_MANTISSA_MASK) * RADIX_MULTS[(complex shr COMPLEX_RADIX_SHIFT) and COMPLEX_RADIX_MASK]

        @JvmStatic
        fun complexToDimension(complex: Int, metrics: DisplayMetrics): Float =
            applyDimension((complex shr COMPLEX_UNIT_SHIFT) and COMPLEX_UNIT_MASK, complexToFloat(complex), metrics)

        @JvmStatic
        fun complexToDimensionPixelSize(complex: Int, metrics: DisplayMetrics): Int {
            val value = complexToDimension(complex, metrics)
            return (if (value >= 0) value + 0.5f else value - 0.5f).toInt()
        }

        @JvmStatic
        fun complexToDimensionPixelOffset(complex: Int, metrics: DisplayMetrics): Int =
            complexToDimension(complex, metrics).toInt()

        private val RADIX_MULTS = floatArrayOf(1.0f, 1.0f / (1 shl 23), 1.0f / (1 shl 15), 1.0f / (1 shl 7))
    }
}

/** android.util.AtomicFile：先写 .bak 再原子改名。 */
class AtomicFile(private val baseFile: File) {
    fun getBaseFile(): File = baseFile

    fun delete() {
        baseFile.delete()
        File(baseFile.path + ".bak").delete()
        File(baseFile.path + ".new").delete()
    }

    fun exists(): Boolean = baseFile.exists()

    fun lastModified(): Long = baseFile.lastModified()

    fun getLastModifiedTime(): Long = baseFile.lastModified()

    @Throws(IOException::class)
    fun openRead(): FileInputStream = FileInputStream(baseFile)

    @Throws(IOException::class)
    fun readFully(): ByteArray = FileInputStream(baseFile).use { it.readBytes() }

    @Throws(IOException::class)
    fun startWrite(): FileOutputStream {
        baseFile.parentFile?.mkdirs()
        val tmp = File(baseFile.path + ".new")
        return FileOutputStream(tmp)
    }

    fun finishWrite(str: FileOutputStream?) {
        if (str == null) return
        sync(str)
        str.close()
        val tmp = File(baseFile.path + ".new")
        if (baseFile.exists()) baseFile.delete()
        tmp.renameTo(baseFile)
    }

    fun failWrite(str: FileOutputStream?) {
        if (str == null) return
        try { sync(str); str.close() } catch (e: IOException) { /* ignore */ }
        File(baseFile.path + ".new").delete()
    }

    private fun sync(stream: FileOutputStream) {
        try { stream.fd.sync() } catch (e: IOException) { /* ignore */ }
    }

    @Deprecated("use openRead()", ReplaceWith("openRead()"))
    @Throws(IOException::class)
    fun openAppend(): FileOutputStream = FileOutputStream(baseFile, true)

    fun truncate() {
        startWrite().use { }
    }
}

/** android.util.Patterns：常用正则。 */
object Patterns {
    @JvmField
    val EMAIL_ADDRESS: Pattern = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    )

    @JvmField
    val DOMAIN_NAME: Pattern = Pattern.compile(
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    )

    @JvmField
    val IP_ADDRESS: Pattern = Pattern.compile(
        "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"
    )

    @JvmField
    val WEB_URL: Pattern = Pattern.compile(
        "((https?|ftp)://)?(www\\.)?[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+(:\\d{1,5})?(/\\S*)?"
    )

    @JvmField
    val PHONE: Pattern = Pattern.compile(
        "(\\+?[0-9][0-9 \\-()]{4,17}[0-9])"
    )

    @JvmField
    val TOP_LEVEL_DOMAIN: Pattern = Pattern.compile(
        "[a-zA-Z]{2,63}"
    )

    @JvmField
    val TOP_LEVEL_DOMAIN_FOR_WEB_URL: Pattern = TOP_LEVEL_DOMAIN
}
