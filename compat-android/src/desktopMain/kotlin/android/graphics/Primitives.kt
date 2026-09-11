package android.graphics

import android.os.Parcel
import android.os.Parcelable

/**
 * android.graphics 基础几何与枚举。
 */

/** android.graphics.Color：Int ARGB 工具类 + 常量。 */
object Color {
    const val BLACK = -0x1000000
    const val DKGRAY = -0xbbbbbc
    const val GRAY = -0x777778
    const val LTGRAY = -0x333334
    const val WHITE = -0x1
    const val RED = -0x10000
    const val GREEN = -0xff0100
    const val BLUE = -0xffff01
    const val YELLOW = -0x100
    const val CYAN = -0xff0001
    const val MAGENTA = -0xff01
    const val TRANSPARENT = 0

    @JvmStatic fun alpha(color: Int): Int = color ushr 24
    @JvmStatic fun red(color: Int): Int = (color shr 16) and 0xFF
    @JvmStatic fun green(color: Int): Int = (color shr 8) and 0xFF
    @JvmStatic fun blue(color: Int): Int = color and 0xFF

    @JvmStatic fun rgb(red: Int, green: Int, blue: Int): Int =
        (0xFF shl 24) or (red shl 16) or (green shl 8) or blue

    @JvmStatic fun argb(alpha: Int, red: Int, green: Int, blue: Int): Int =
        (alpha shl 24) or (red shl 16) or (green shl 8) or blue

    @JvmStatic
    fun argb(alpha: Float, red: Float, green: Float, blue: Float): Int =
        ((alpha * 255.0f).toInt() shl 24) or ((red * 255.0f).toInt() shl 16) or
            ((green * 255.0f).toInt() shl 8) or (blue * 255.0f).toInt()

    @JvmStatic
    fun parseColor(colorString: String): Int {
        if (colorString.startsWith("#")) {
            val hex = colorString.substring(1)
            return when (hex.length) {
                3 -> { // #RGB
                    val r = hex[0].digitToInt(16); val g = hex[1].digitToInt(16); val b = hex[2].digitToInt(16)
                    argb(255, r * 17, g * 17, b * 17)
                }
                4 -> { // #ARGB
                    val a = hex[0].digitToInt(16); val r = hex[1].digitToInt(16)
                    val g = hex[2].digitToInt(16); val b = hex[3].digitToInt(16)
                    argb(a * 17, r * 17, g * 17, b * 17)
                }
                6 -> hex.toLong(16).toInt() or (0xFF shl 24)
                8 -> hex.toLong(16).toInt()
                else -> throw IllegalArgumentException("Unknown color: $colorString")
            }
        }
        return when (colorString.lowercase()) {
            "black" -> BLACK; "darkgray" -> DKGRAY; "gray" -> GRAY; "lightgray" -> LTGRAY
            "white" -> WHITE; "red" -> RED; "green" -> GREEN; "blue" -> BLUE
            "yellow" -> YELLOW; "cyan" -> CYAN; "magenta" -> MAGENTA; "transparent" -> TRANSPARENT
            "aqua" -> CYAN; "fuchsia" -> MAGENTA; "lime" -> -0xff0100; "maroon" -> -0x800000
            "navy" -> -0xffff80; "olive" -> -0x7f8000; "purple" -> -0x7fff80; "silver" -> -0x3f3f40
            "teal" -> -0xff7f80; "grey" -> GRAY; "lightgrey" -> LTGRAY; "darkgrey" -> DKGRAY
            else -> throw IllegalArgumentException("Unknown color: $colorString")
        }
    }

    @JvmStatic
    fun HSVToColor(hsv: FloatArray): Int = HSVToColor(255, hsv)

    @JvmStatic
    fun HSVToColor(alpha: Int, hsv: FloatArray): Int {
        val h = (hsv[0].coerceIn(0f, 360f)) / 60f
        val s = hsv[1].coerceIn(0f, 1f)
        val v = hsv[2].coerceIn(0f, 1f)
        val c = v * s
        val x = c * (1 - kotlin.math.abs((h % 2) - 1))
        val m = v - c
        val (r, g, b) = when {
            h < 1 -> Triple(c, x, 0f)
            h < 2 -> Triple(x, c, 0f)
            h < 3 -> Triple(0f, c, x)
            h < 4 -> Triple(0f, x, c)
            h < 5 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }
        return argb(alpha, ((r + m) * 255).toInt(), ((g + m) * 255).toInt(), ((b + m) * 255).toInt())
    }

    @JvmStatic fun hsvToColor(hsv: FloatArray): Int = HSVToColor(hsv)
    @JvmStatic fun hsvToColor(alpha: Int, hsv: FloatArray): Int = HSVToColor(alpha, hsv)

    @JvmStatic
    fun colorToHSV(color: Int, hsv: FloatArray) {
        RGBToHSV(red(color), green(color), blue(color), hsv)
    }

    @JvmStatic
    fun RGBToHSV(red: Int, green: Int, blue: Int, hsv: FloatArray) {
        val r = red / 255f; val g = green / 255f; val b = blue / 255f
        val max = maxOf(r, g, b); val min = minOf(r, g, b)
        val d = max - min
        val h = when {
            d == 0f -> 0f
            max == r -> ((g - b) / d) % 6f
            max == g -> (b - r) / d + 2
            else -> (r - g) / d + 4
        } * 60f
        hsv[0] = if (h < 0) h + 360 else h
        hsv[1] = if (max == 0f) 0f else d / max
        hsv[2] = max
    }

    @JvmStatic fun luminance(color: Int): Float =
        (0.2126f * red(color) + 0.7152f * green(color) + 0.0722f * blue(color)) / 255f

    @JvmStatic fun toArgb(color: Int): Int = color

    @JvmStatic fun valueOf(color: Int): Color = Color

    @JvmStatic fun valueOf(color: Long): Color = Color
}

/** android.graphics.Rect：完整 int 矩形。 */
open class Rect : Parcelable {
    @JvmField var left: Int = 0
    @JvmField var top: Int = 0
    @JvmField var right: Int = 0
    @JvmField var bottom: Int = 0

    constructor()
    constructor(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left; this.top = top; this.right = right; this.bottom = bottom
    }
    constructor(r: Rect?) { if (r != null) set(r) }

    fun isEmpty(): Boolean = left >= right || top >= bottom
    fun width(): Int = right - left
    fun height(): Int = bottom - top
    fun centerX(): Int = (left + right) shr 1
    fun centerY(): Int = (top + bottom) shr 1
    fun exactCenterX(): Float = (left + right) * 0.5f
    fun exactCenterY(): Float = (top + bottom) * 0.5f

    fun setEmpty() { left = 0; top = 0; right = 0; bottom = 0 }
    fun set(left: Int, top: Int, right: Int, bottom: Int) {
        this.left = left; this.top = top; this.right = right; this.bottom = bottom
    }
    fun set(src: Rect) { set(src.left, src.top, src.right, src.bottom) }
    fun set(src: RectF) {
        set(src.left.toInt(), src.top.toInt(), src.right.toInt(), src.bottom.toInt())
    }
    fun offset(dx: Int, dy: Int) { left += dx; top += dy; right += dx; bottom += dy }
    fun offsetTo(newLeft: Int, newTop: Int) { offset(newLeft - left, newTop - top) }
    fun inset(dx: Int, dy: Int) { left += dx; top += dy; right -= dx; bottom -= dy }
    fun contains(x: Int, y: Int): Boolean = x in left until right && y in top until bottom
    fun contains(r: Rect): Boolean = left < right && top < bottom &&
        left <= r.left && top <= r.top && right >= r.right && bottom >= r.bottom

    fun intersect(r: Rect): Boolean = intersect(r.left, r.top, r.right, r.bottom)
    fun intersect(left: Int, top: Int, right: Int, bottom: Int): Boolean {
        if (this.left < right && left < this.right && this.top < bottom && top < this.bottom) {
            if (this.left < left) this.left = left
            if (this.top < top) this.top = top
            if (this.right > right) this.right = right
            if (this.bottom > bottom) this.bottom = bottom
            return true
        }
        return false
    }
    fun intersects(r: Rect): Boolean = intersect(r.left, r.top, r.right, r.bottom)

    fun union(r: Rect) { union(r.left, r.top, r.right, r.bottom) }
    fun union(left: Int, top: Int, right: Int, bottom: Int) {
        if (left < right && top < bottom) {
            if (this.left < this.right && this.top < this.bottom) {
                if (this.left > left) this.left = left
                if (this.top > top) this.top = top
                if (this.right < right) this.right = right
                if (this.bottom < bottom) this.bottom = bottom
            } else set(left, top, right, bottom)
        }
    }
    fun union(x: Int, y: Int) { union(x, y, x + 1, y + 1) }

    fun sort() {
        if (left > right) { val t = left; left = right; right = t }
        if (top > bottom) { val t = top; top = bottom; bottom = t }
    }

    fun flattenToString(): String = "$left $top $right $bottom"
    fun toShortString(): String = "[$left,$top][$right,$bottom]"

    override fun equals(other: Any?): Boolean = other is Rect &&
        other.left == left && other.top == top && other.right == right && other.bottom == bottom
    override fun hashCode(): Int = ((left * 31 + top) * 31 + right) * 31 + bottom
    override fun toString(): String = "Rect($left, $top - $right, $bottom)"

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(left); dest.writeInt(top); dest.writeInt(right); dest.writeInt(bottom)
    }

    companion object {
        @JvmStatic fun intersects(a: Rect, b: Rect): Boolean =
            a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom

        @JvmStatic fun unflattenFromString(str: String): Rect? {
            val parts = str.trim().split(" ")
            if (parts.size != 4) return null
            return try {
                Rect(parts[0].toInt(), parts[1].toInt(), parts[2].toInt(), parts[3].toInt())
            } catch (e: NumberFormatException) { null }
        }

        @JvmField val CREATOR: Parcelable.Creator<Rect> = object : Parcelable.Creator<Rect> {
            override fun createFromParcel(source: Parcel): Rect =
                Rect(source.readInt(), source.readInt(), source.readInt(), source.readInt())
            override fun newArray(size: Int): Array<Rect?> = arrayOfNulls(size)
        }
    }
}

/** android.graphics.RectF。 */
open class RectF : Parcelable {
    @JvmField var left: Float = 0f
    @JvmField var top: Float = 0f
    @JvmField var right: Float = 0f
    @JvmField var bottom: Float = 0f

    constructor()
    constructor(left: Float, top: Float, right: Float, bottom: Float) {
        this.left = left; this.top = top; this.right = right; this.bottom = bottom
    }
    constructor(r: RectF?) { if (r != null) set(r) }
    constructor(r: Rect) { set(r) }

    fun isEmpty(): Boolean = left >= right || top >= bottom
    fun width(): Float = right - left
    fun height(): Float = bottom - top
    fun centerX(): Float = (left + right) * 0.5f
    fun centerY(): Float = (top + bottom) * 0.5f

    fun setEmpty() { left = 0f; top = 0f; right = 0f; bottom = 0f }
    fun set(left: Float, top: Float, right: Float, bottom: Float) {
        this.left = left; this.top = top; this.right = right; this.bottom = bottom
    }
    fun set(src: RectF) { set(src.left, src.top, src.right, src.bottom) }
    fun set(src: Rect) { set(src.left.toFloat(), src.top.toFloat(), src.right.toFloat(), src.bottom.toFloat()) }
    fun offset(dx: Float, dy: Float) { left += dx; top += dy; right += dx; bottom += dy }
    fun offsetTo(newLeft: Float, newTop: Float) { offset(newLeft - left, newTop - top) }
    fun inset(dx: Float, dy: Float) { left += dx; top += dy; right -= dx; bottom -= dy }
    fun contains(x: Float, y: Float): Boolean = x >= left && x < right && y >= top && y < bottom
    fun contains(r: RectF): Boolean = left < right && top < bottom &&
        left <= r.left && top <= r.top && right >= r.right && bottom >= r.bottom
    fun intersect(r: RectF): Boolean = intersect(r.left, r.top, r.right, r.bottom)
    fun intersect(left: Float, top: Float, right: Float, bottom: Float): Boolean {
        if (this.left < right && left < this.right && this.top < bottom && top < this.bottom) {
            if (this.left < left) this.left = left
            if (this.top < top) this.top = top
            if (this.right > right) this.right = right
            if (this.bottom > bottom) this.bottom = bottom
            return true
        }
        return false
    }
    fun intersects(r: RectF): Boolean = intersect(r.left, r.top, r.right, r.bottom)

    fun union(r: RectF) { union(r.left, r.top, r.right, r.bottom) }
    fun union(left: Float, top: Float, right: Float, bottom: Float) {
        if (left < right && top < bottom) {
            if (this.left < this.right && this.top < this.bottom) {
                if (this.left > left) this.left = left
                if (this.top > top) this.top = top
                if (this.right < right) this.right = right
                if (this.bottom < bottom) this.bottom = bottom
            } else set(left, top, right, bottom)
        }
    }

    fun round(out: Rect) {
        out.set(
            Math.round(left), Math.round(top), Math.round(right), Math.round(bottom),
        )
    }
    fun roundOut(out: Rect) {
        out.set(
            kotlin.math.floor(left.toDouble()).toInt(), kotlin.math.floor(top.toDouble()).toInt(),
            kotlin.math.ceil(right.toDouble()).toInt(), kotlin.math.ceil(bottom.toDouble()).toInt(),
        )
    }
    fun sort() {
        if (left > right) { val t = left; left = right; right = t }
        if (top > bottom) { val t = top; top = bottom; bottom = t }
    }

    override fun equals(other: Any?): Boolean = other is RectF &&
        other.left == left && other.top == top && other.right == right && other.bottom == bottom
    override fun hashCode(): Int = ((left.toBits() * 31 + top.toBits()) * 31 + right.toBits()) * 31 + bottom.toBits()
    override fun toString(): String = "RectF($left, $top, $right, $bottom)"

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeFloat(left); dest.writeFloat(top); dest.writeFloat(right); dest.writeFloat(bottom)
    }

    companion object {
        @JvmStatic fun intersects(a: RectF, b: RectF): Boolean =
            a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom

        @JvmField val CREATOR: Parcelable.Creator<RectF> = object : Parcelable.Creator<RectF> {
            override fun createFromParcel(source: Parcel): RectF =
                RectF(source.readFloat(), source.readFloat(), source.readFloat(), source.readFloat())
            override fun newArray(size: Int): Array<RectF?> = arrayOfNulls(size)
        }
    }
}

/** android.graphics.Point。 */
class Point : Parcelable {
    @JvmField var x: Int = 0
    @JvmField var y: Int = 0

    constructor()
    constructor(x: Int, y: Int) { this.x = x; this.y = y }
    constructor(src: Point) { this.x = src.x; this.y = src.y }

    fun set(x: Int, y: Int) { this.x = x; this.y = y }
    fun negate() { x = -x; y = -y }
    fun offset(dx: Int, dy: Int) { x += dx; y += dy }

    override fun equals(other: Any?): Boolean = other is Point && other.x == x && other.y == y
    fun equals(x: Int, y: Int): Boolean = this.x == x && this.y == y
    override fun hashCode(): Int = x * 31 + y
    override fun toString(): String = "Point($x, $y)"

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeInt(x); dest.writeInt(y) }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Point> = object : Parcelable.Creator<Point> {
            override fun createFromParcel(source: Parcel): Point = Point(source.readInt(), source.readInt())
            override fun newArray(size: Int): Array<Point?> = arrayOfNulls(size)
        }
    }
}

/** android.graphics.PointF。 */
class PointF : Parcelable {
    @JvmField var x: Float = 0f
    @JvmField var y: Float = 0f

    constructor()
    constructor(x: Float, y: Float) { this.x = x; this.y = y }
    constructor(src: PointF) { this.x = src.x; this.y = src.y }

    fun set(x: Float, y: Float) { this.x = x; this.y = y }
    fun set(p: PointF) { x = p.x; y = p.y }
    fun negate() { x = -x; y = -y }
    fun offset(dx: Float, dy: Float) { x += dx; y += dy }
    fun length(): Float = kotlin.math.hypot(x, y)

    override fun equals(other: Any?): Boolean = other is PointF && other.x == x && other.y == y
    fun equals(x: Float, y: Float): Boolean = this.x == x && this.y == y
    override fun hashCode(): Int = x.toBits() * 31 + y.toBits()
    override fun toString(): String = "PointF($x, $y)"

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeFloat(x); dest.writeFloat(y) }

    companion object {
        @JvmStatic fun length(x: Float, y: Float): Float = kotlin.math.hypot(x, y)

        @JvmField val CREATOR: Parcelable.Creator<PointF> = object : Parcelable.Creator<PointF> {
            override fun createFromParcel(source: Parcel): PointF = PointF(source.readFloat(), source.readFloat())
            override fun newArray(size: Int): Array<PointF?> = arrayOfNulls(size)
        }
    }
}

/** android.graphics.PixelFormat。 */
object PixelFormat {
    const val UNKNOWN = 0
    const val TRANSLUCENT = -3
    const val TRANSPARENT = -2
    const val OPAQUE = -1
    const val RGBA_8888 = 1
    const val RGBX_8888 = 2
    const val RGB_888 = 3
    const val RGB_565 = 4
    const val RGBA_F16 = 22
    const val RGBA_1010102 = 43
    const val A_8 = 8
    const val LA_88 = 10
    const val L_8 = 9
    const val JPEG = 256
    const val YCbCr_420_SP = 17
    const val YCbCr_422_SP = 16
    const val YCbCr_422_I = 20
}

/** android.graphics.ImageFormat。 */
object ImageFormat {
    const val UNKNOWN = 0
    const val RGB_565 = 4
    const val NV16 = 16
    const val NV21 = 17
    const val YUY2 = 20
    const val YV12 = 842094169
    const val JPEG = 256
    const val Y8 = 0x20203859
    const val YUV_420_888 = 35
    const val YUV_422_888 = 39
    const val YUV_444_888 = 40
    const val FLEX_RGB_888 = 41
    const val FLEX_RGBA_8888 = 42
    const val RAW_SENSOR = 32
    const val RAW_PRIVATE = 36
    const val RAW10 = 37
    const val RAW12 = 38
    const val DEPTH16 = 0x44363159
    const val DEPTH_POINT_CLOUD = 257
    const val PRIVATE = 34
    const val HEIC = 1212500294

    @JvmStatic
    fun getBitsPerPixel(format: Int): Int = when (format) {
        RGB_565 -> 16
        NV16, YUY2 -> 16
        NV21, YV12, YUV_420_888 -> 12
        YUV_422_888 -> 16
        YUV_444_888 -> 24
        FLEX_RGB_888 -> 24
        FLEX_RGBA_8888 -> 32
        else -> -1
    }
}

/** android.graphics.BlendMode（API 29+）。 */
enum class BlendMode {
    CLEAR, SRC, DST, SRC_OVER, DST_OVER, SRC_IN, DST_IN, SRC_OUT, DST_OUT,
    SRC_ATOP, DST_ATOP, XOR, PLUS, MODULATE, SCREEN, OVERLAY, DARKEN, LIGHTEN,
    COLOR_DODGE, COLOR_BURN, HARD_LIGHT, SOFT_LIGHT, DIFFERENCE, EXCLUSION,
    MULTIPLY, HUE, SATURATION, COLOR, LUMINOSITY;
}

/** android.graphics.Region 轻 stub。 */
class Region : Parcelable {
    private var bounds = Rect()

    constructor()
    constructor(r: Region) { bounds.set(r.bounds) }
    constructor(bounds: Rect) { this.bounds.set(bounds) }
    constructor(left: Int, top: Int, right: Int, bottom: Int) { bounds.set(left, top, right, bottom) }

    fun setEmpty() { bounds.setEmpty() }
    fun set(r: Region): Boolean { bounds.set(r.bounds); return true }
    fun set(bounds: Rect): Boolean { this.bounds.set(bounds); return true }
    fun setPath(path: Path, clip: Region): Boolean = true
    fun getBounds(): Rect = Rect(bounds)
    fun getBounds(r: Rect): Boolean { r.set(bounds); return true }
    fun isEmpty(): Boolean = bounds.isEmpty()
    fun isRect(): Boolean = true
    fun isComplex(): Boolean = false
    fun contains(x: Int, y: Int): Boolean = bounds.contains(x, y)
    fun quickContains(r: Rect): Boolean = bounds.contains(r)
    fun quickReject(r: Rect): Boolean = !Rect.intersects(bounds, r)
    fun quickReject(x: Int, y: Int): Boolean = !bounds.contains(x, y)
    fun union(r: Rect): Boolean { bounds.union(r); return true }
    fun op(r: Region, op: Op): Boolean = true
    fun translate(x: Int, y: Int) { bounds.offset(x, y) }

    enum class Op { DIFFERENCE, INTERSECT, UNION, XOR, REVERSE_DIFFERENCE, REPLACE }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { bounds.writeToParcel(dest, flags) }
    override fun toString(): String = "Region($bounds)"
}

/** android.graphics.Matrix：包壳 3x3 矩阵。 */
class Matrix {
    private var mat = floatArrayOf(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f)

    constructor()
    constructor(src: Matrix) { mat = src.mat.copyOf() }

    enum class ScaleToFit { FILL, START, CENTER, END }

    val isIdentity: Boolean get() = mat.contentEquals(IDENTITY_MAT)
    fun isIdentityLegacy(): Boolean = isIdentity

    fun set(src: Matrix?) { mat = src?.mat?.copyOf() ?: IDENTITY_MAT.copyOf() }
    fun reset() { mat = IDENTITY_MAT.copyOf() }

    fun setValues(values: FloatArray) { mat = values.copyOf(9) }
    fun getValues(values: FloatArray) { mat.copyInto(values, 0, 0, 9) }

    fun setTranslate(dx: Float, dy: Float) {
        mat = floatArrayOf(1f, 0f, dx, 0f, 1f, dy, 0f, 0f, 1f)
    }
    fun setTranslate(v: PointF) = setTranslate(v.x, v.y)

    fun setScale(sx: Float, sy: Float) { mat = floatArrayOf(sx, 0f, 0f, 0f, sy, 0f, 0f, 0f, 1f) }
    fun setScale(sx: Float, sy: Float, px: Float, py: Float) {
        mat = floatArrayOf(sx, 0f, px * (1 - sx), 0f, sy, py * (1 - sy), 0f, 0f, 1f)
    }

    fun setRotate(degrees: Float) {
        val rad = Math.toRadians(degrees.toDouble())
        val c = kotlin.math.cos(rad).toFloat(); val s = kotlin.math.sin(rad).toFloat()
        mat = floatArrayOf(c, -s, 0f, s, c, 0f, 0f, 0f, 1f)
    }
    fun setRotate(degrees: Float, px: Float, py: Float) {
        setTranslate(px, py)
        val r = Matrix(); r.setRotate(degrees)
        postConcat(r)
        preTranslate(-px, -py)
    }

    fun setSkew(kx: Float, ky: Float) { mat = floatArrayOf(1f, kx, 0f, ky, 1f, 0f, 0f, 0f, 1f) }
    fun setSkew(kx: Float, ky: Float, px: Float, py: Float) {
        mat = floatArrayOf(1f, kx, -ky * py, ky, 1f, -kx * px, 0f, 0f, 1f)
    }

    private fun concat(a: FloatArray, b: FloatArray): FloatArray {
        val r = FloatArray(9)
        for (i in 0 until 3) for (j in 0 until 3) {
            r[i * 3 + j] = a[i * 3] * b[j] + a[i * 3 + 1] * b[3 + j] + a[i * 3 + 2] * b[6 + j]
        }
        return r
    }

    fun preTranslate(dx: Float, dy: Float): Boolean {
        mat = concat(floatArrayOf(1f, 0f, dx, 0f, 1f, dy, 0f, 0f, 1f), mat)
        return true
    }
    fun preScale(sx: Float, sy: Float): Boolean {
        mat = concat(floatArrayOf(sx, 0f, 0f, 0f, sy, 0f, 0f, 0f, 1f), mat)
        return true
    }
    fun preScale(sx: Float, sy: Float, px: Float, py: Float): Boolean {
        preTranslate(px, py); preScale(sx, sy); preTranslate(-px, -py)
        return true
    }
    fun preRotate(degrees: Float): Boolean {
        val r = Matrix(); r.setRotate(degrees)
        mat = concat(r.mat, mat)
        return true
    }
    fun preRotate(degrees: Float, px: Float, py: Float): Boolean {
        preTranslate(px, py); preRotate(degrees); preTranslate(-px, -py)
        return true
    }
    fun preSkew(kx: Float, ky: Float): Boolean {
        val m = Matrix(); m.setSkew(kx, ky)
        mat = concat(m.mat, mat)
        return true
    }

    fun postTranslate(dx: Float, dy: Float): Boolean {
        mat = concat(mat, floatArrayOf(1f, 0f, dx, 0f, 1f, dy, 0f, 0f, 1f))
        return true
    }
    fun postScale(sx: Float, sy: Float): Boolean {
        mat = concat(mat, floatArrayOf(sx, 0f, 0f, 0f, sy, 0f, 0f, 0f, 1f))
        return true
    }
    fun postScale(sx: Float, sy: Float, px: Float, py: Float): Boolean {
        postTranslate(-px, -py); postScale(sx, sy); postTranslate(px, py)
        return true
    }
    fun postRotate(degrees: Float): Boolean {
        val r = Matrix(); r.setRotate(degrees)
        mat = concat(mat, r.mat)
        return true
    }
    fun postRotate(degrees: Float, px: Float, py: Float): Boolean {
        postTranslate(-px, -py); postRotate(degrees); postTranslate(px, py)
        return true
    }
    fun postSkew(kx: Float, ky: Float): Boolean {
        val m = Matrix(); m.setSkew(kx, ky)
        mat = concat(mat, m.mat)
        return true
    }
    fun postConcat(other: Matrix): Boolean { mat = concat(mat, other.mat); return true }
    fun preConcat(other: Matrix): Boolean { mat = concat(other.mat, mat); return true }

    fun setConcat(a: Matrix, b: Matrix): Boolean { mat = concat(a.mat, b.mat); return true }

    fun mapPoints(pts: FloatArray) { mapPoints(pts, 0, pts, 0, pts.size / 2) }

    fun mapPoints(dst: FloatArray, src: FloatArray) { mapPoints(dst, 0, src, 0, src.size / 2) }

    fun mapPoints(dst: FloatArray, dstIndex: Int, src: FloatArray, srcIndex: Int, pointCount: Int) {
        for (i in 0 until pointCount) {
            val sx = src[srcIndex + i * 2]
            val sy = src[srcIndex + i * 2 + 1]
            dst[dstIndex + i * 2] = mat[0] * sx + mat[1] * sy + mat[2]
            dst[dstIndex + i * 2 + 1] = mat[3] * sx + mat[4] * sy + mat[5]
        }
    }

    fun mapVectors(vecs: FloatArray) {
        for (i in 0 until vecs.size / 2) {
            val vx = vecs[i * 2]; val vy = vecs[i * 2 + 1]
            vecs[i * 2] = mat[0] * vx + mat[1] * vy
            vecs[i * 2 + 1] = mat[3] * vx + mat[4] * vy
        }
    }

    fun mapRect(rect: RectF): Boolean {
        mapRect(rect, rect)
        return true
    }

    fun mapRect(dst: RectF, src: RectF): Boolean {
        val pts = floatArrayOf(src.left, src.top, src.right, src.top, src.right, src.bottom, src.left, src.bottom)
        mapPoints(pts)
        dst.set(
            minOf(pts[0], pts[2], pts[4], pts[6]), minOf(pts[1], pts[3], pts[5], pts[7]),
            maxOf(pts[0], pts[2], pts[4], pts[6]), maxOf(pts[1], pts[3], pts[5], pts[7]),
        )
        return true
    }

    fun mapPointsToInts(dst: IntArray, src: FloatArray) {
        val tmp = src.copyOf()
        mapPoints(tmp)
        for (i in dst.indices) dst[i] = tmp[i].toInt()
    }

    fun mapRadius(radius: Float): Float {
        val pts = floatArrayOf(radius, 0f)
        mapVectors(pts)
        return kotlin.math.hypot(pts[0], pts[1])
    }

    fun invert(inverse: Matrix): Boolean {
        val m = mat
        val det = m[0] * (m[4] * m[8] - m[5] * m[7]) - m[1] * (m[3] * m[8] - m[5] * m[6]) +
            m[2] * (m[3] * m[7] - m[4] * m[6])
        if (det == 0f) return false
        val invDet = 1f / det
        inverse.mat = floatArrayOf(
            (m[4] * m[8] - m[5] * m[7]) * invDet, (m[2] * m[7] - m[1] * m[8]) * invDet, (m[1] * m[5] - m[2] * m[4]) * invDet,
            (m[5] * m[6] - m[3] * m[8]) * invDet, (m[0] * m[8] - m[2] * m[6]) * invDet, (m[2] * m[3] - m[0] * m[5]) * invDet,
            (m[3] * m[7] - m[4] * m[6]) * invDet, (m[1] * m[6] - m[0] * m[7]) * invDet, (m[0] * m[4] - m[1] * m[3]) * invDet,
        )
        return true
    }

    fun setRectToRect(src: RectF, dst: RectF, stf: ScaleToFit): Boolean {
        if (src.isEmpty()) { reset(); return false }
        val sx = dst.width() / src.width()
        val sy = dst.height() / src.height()
        when (stf) {
            ScaleToFit.FILL -> {
                setTranslate(dst.left - src.left * sx, dst.top - src.top * sy)
                preScale(sx, sy)
                mat[0] = sx; mat[4] = sy
                mat[2] = dst.left - src.left * sx; mat[5] = dst.top - src.top * sy
            }
            ScaleToFit.START -> {
                val s = minOf(sx, sy)
                mat = floatArrayOf(s, 0f, dst.left - src.left * s, 0f, s, dst.top - src.top * s, 0f, 0f, 1f)
            }
            ScaleToFit.CENTER -> {
                val s = minOf(sx, sy)
                val dx = dst.left + (dst.width() - src.width() * s) / 2 - src.left * s
                val dy = dst.top + (dst.height() - src.height() * s) / 2 - src.top * s
                mat = floatArrayOf(s, 0f, dx, 0f, s, dy, 0f, 0f, 1f)
            }
            ScaleToFit.END -> {
                val s = minOf(sx, sy)
                val dx = dst.right - src.width() * s - src.left * s
                val dy = dst.bottom - src.height() * s - src.top * s
                mat = floatArrayOf(s, 0f, dx, 0f, s, dy, 0f, 0f, 1f)
            }
        }
        return true
    }

    fun setPolyToPoly(src: FloatArray, srcIndex: Int, dst: FloatArray, dstIndex: Int, pointCount: Int): Boolean {
        // 仅支持 0/1 点；更复杂的用 setRectToRect
        if (pointCount == 1) {
            setTranslate(dst[dstIndex] - src[srcIndex], dst[dstIndex + 1] - src[srcIndex + 1])
            return true
        }
        return false
    }

    fun rectStaysRect(): Boolean = mat[1] == 0f && mat[3] == 0f

    override fun equals(other: Any?): Boolean = other is Matrix && other.mat.contentEquals(mat)
    override fun hashCode(): Int = mat.contentHashCode()
    override fun toString(): String = "Matrix{${mat.joinToString()}}"

    internal fun toSkia(): org.jetbrains.skia.Matrix33 = org.jetbrains.skia.Matrix33(*mat)

    companion object {
        private val IDENTITY_MAT = floatArrayOf(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f)
    }
}
