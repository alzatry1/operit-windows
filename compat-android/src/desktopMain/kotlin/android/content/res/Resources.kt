package android.content.res

import android.os.LocaleList
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import com.ai.assistance.operit.res.Colors
import com.ai.assistance.operit.res.ResFiles
import com.ai.assistance.operit.res.Strings
import java.io.File
import java.io.InputStream
import java.util.Locale

/**
 * android.content.res.Resources：字符串/颜色走生成的资源表，尺寸/Drawable 给合理默认。
 */
open class Resources {

    /** Resources.updateConfiguration（桌面版：就地更新 configuration 的可变字段；val 不可重赋）——Nova 注 */
    open fun updateConfiguration(config: Configuration?, metrics: android.util.DisplayMetrics?) {
        config?.let { newConfig ->
            configuration.locales = newConfig.locales
            configuration.locale = newConfig.locale
            configuration.fontScale = newConfig.fontScale
            configuration.uiMode = newConfig.uiMode
            configuration.screenWidthDp = newConfig.screenWidthDp
            configuration.screenHeightDp = newConfig.screenHeightDp
        }
    }

    open class NotFoundException : RuntimeException {
        constructor() : super()
        constructor(name: String?) : super(name)
        constructor(name: String?, cause: Throwable?) : super(name, cause)
    }

    val displayMetrics: DisplayMetrics by lazy { DisplayMetrics() }
    val configuration: Configuration by lazy { Configuration() }
    private var warnedDimen = false

    // ---- 字符串 ----
    open fun getString(id: Int): String = Strings.get(id)

    /** android.content.res.Resources.getFont。桌面无字体资源表，回退默认字库。 */
    open fun getFont(id: Int): android.graphics.Typeface = android.graphics.Typeface.DEFAULT

    open fun getString(id: Int, vararg formatArgs: Any?): String = Strings.getString(id, *formatArgs)

    open fun getText(id: Int): CharSequence = getString(id)

    open fun getText(id: Int, def: CharSequence?): CharSequence? =
        if (Strings.get(id).startsWith("<missing:")) def else getString(id)

    open fun getQuantityString(id: Int, quantity: Int): String =
        Strings.getQuantityString(id, quantity)

    open fun getQuantityString(id: Int, quantity: Int, vararg formatArgs: Any?): String =
        Strings.getQuantityString(id, quantity, *formatArgs)

    open fun getQuantityText(id: Int, quantity: Int): CharSequence = getQuantityString(id, quantity)

    open fun getStringArray(id: Int): Array<String> = Strings.getArray(id)

    open fun getTextArray(id: Int): Array<CharSequence> = getStringArray(id).map { it }.toTypedArray()

    open fun obtainTypedArray(id: Int): TypedArray = TypedArray.EMPTY

    open fun getIntArray(id: Int): IntArray = IntArray(0)

    // ---- 尺寸 ----
    open fun getDimension(id: Int): Float {
        if (!warnedDimen) {
            warnedDimen = true
            Log.w("Resources", "getDimension: dimen 体系未实现，返回 0")
        }
        return 0f
    }

    open fun getDimensionPixelSize(id: Int): Int = getDimension(id).toInt()
    open fun getDimensionPixelOffset(id: Int): Int = getDimension(id).toInt()
    open fun getFraction(id: Int, base: Int, pbase: Int): Float = 0f

    // ---- 颜色/Drawable ----
    open fun getColor(id: Int): Int = Colors.getColor(id)
    open fun getColor(id: Int, theme: Theme?): Int = Colors.getColor(id)
    open fun getColorStateList(id: Int): ColorStateList = ColorStateList.valueOf(Colors.getColor(id))
    open fun getColorStateList(id: Int, theme: Theme?): ColorStateList = getColorStateList(id)

    open fun getDrawable(id: Int): android.graphics.drawable.Drawable? = null
    open fun getDrawable(id: Int, theme: Theme?): android.graphics.drawable.Drawable? = null
    open fun getDrawableForDensity(id: Int, density: Int): android.graphics.drawable.Drawable? = null
    open fun getDrawableForDensity(id: Int, density: Int, theme: Theme?): android.graphics.drawable.Drawable? = null

    // ---- 原始资源 ----
    @Throws(NotFoundException::class)
    open fun openRawResource(id: Int): InputStream =
        ResFiles.openStream(id) ?: throw NotFoundException("raw resource #$id")

    open fun openRawResource(id: Int, value: TypedValue): InputStream = openRawResource(id)

    open fun openRawResourceFd(id: Int): AssetFileDescriptor? = null

    open fun getAnimation(id: Int): Any? = null
    open fun getXml(id: Int): Any? = null
    open fun getLayout(id: Int): Any? = null

    // ---- 杂项 ----
    open fun getInteger(id: Int): Int = 0
    open fun getBoolean(id: Int): Boolean = false

    open fun getAssets(): AssetManager = com.ai.assistance.operit.compat.AppGlobals.assets

    open fun getIdentifier(name: String?, defType: String?, defPackage: String?): Int = 0

    open fun getResourceName(resid: Int): String = "res/$resid"
    open fun getResourcePackageName(resid: Int): String = com.ai.assistance.operit.compat.AppGlobals.PACKAGE_NAME
    open fun getResourceTypeName(resid: Int): String = "string"
    open fun getResourceEntryName(resid: Int): String = resid.toString()

    open fun getValue(id: Int, outValue: TypedValue, resolveRefs: Boolean) {
        outValue.resourceId = id
    }

    open fun getValue(name: String, outValue: TypedValue, resolveRefs: Boolean) {}

    open fun obtainAttributes(set: AttributeSet?, attrs: IntArray): TypedArray = TypedArray.EMPTY

    open fun newTheme(): Theme = Theme()

    open fun getCompatibilityInfo(): Any = Any()

    /** android.content.res.Resources.Theme。 */
    open inner class Theme {
        open fun applyStyle(resId: Int, force: Boolean) {}
        open fun applyStyle(style: android.util.AttributeSet?, force: Boolean) {}
        open fun setTo(other: Theme?) {}
        open fun obtainStyledAttributes(attrs: IntArray): TypedArray = TypedArray.EMPTY
        open fun obtainStyledAttributes(attrs: AttributeSet?, set: IntArray): TypedArray = TypedArray.EMPTY
        open fun obtainStyledAttributes(set: IntArray, defStyleAttr: Int, defStyleRes: Int): TypedArray = TypedArray.EMPTY
        open fun obtainStyledAttributes(
            attrs: AttributeSet?, set: IntArray, defStyleAttr: Int, defStyleRes: Int,
        ): TypedArray = TypedArray.EMPTY
        open fun resolveAttribute(resid: Int, outValue: TypedValue, resolveRefs: Boolean): Boolean = false
        open fun resolveAttributes(attrs: IntArray, values: IntArray) {}
        open fun dump(priority: Int, tag: String, prefix: String) {}
        open fun getChangingConfigurations(): Int = 0
        open fun getDrawable(id: Int): android.graphics.drawable.Drawable? = null
        open fun newTheme(): Theme = Theme()
        open fun getExplicitStyle(attrs: AttributeSet?): Int = 0
        open fun rebase() {}
        open fun rebase(resId: Int) {}
    }

    companion object {
        private val systemInstance by lazy { Resources() }

        @JvmStatic
        fun getSystem(): Resources = systemInstance

        @JvmStatic
        fun getAttributeSetSourceResId(set: AttributeSet?): Int = 0

        @JvmStatic
        fun selectDefaultTheme(curTheme: Int, targetSdkVersion: Int): Int = curTheme

        @JvmStatic
        fun selectSystemTheme(curTheme: Int, targetSdkVersion: Int, orig: Int, holo: Int, dark: Int, deviceDefault: Int): Int =
            deviceDefault

        @JvmStatic
        fun resourceHasPackage(resid: Int): Boolean = resid ushr 24 != 0
    }
}

/** android.content.res.Configuration。 */
open class Configuration : android.os.Parcelable, Cloneable, Comparable<Configuration> {

    var locale: Locale? = Locale.getDefault()
        get() = if (field == null && locales.size() > 0) locales.get(0) else field
        internal set(value) {
            field = value
            locales = if (value != null) LocaleList.of(value) else LocaleList.getDefault()
        }

    var locales: LocaleList = LocaleList.getDefault()

    /** Configuration.setLocales（app 用 LocaleListCompat 调）。——Nova 注 */
    fun setLocales(l: androidx.core.os.LocaleListCompat?) {
        locales = l?.unwrap() ?: LocaleList.getDefault()
        locale = if (locales.size() > 0) locales.get(0) else null
    }
    /** Configuration.setLocale（单个 Locale，已废弃但 app 兼容分支用）。——Nova 注 */
    fun setLocale(l: java.util.Locale?) {
        locale = l
        locales = if (l != null) LocaleList.of(l) else LocaleList.getDefault()
    }

    var fontScale: Float = 1.0f
    var mcc: Int = 0
    var mnc: Int = 0
    var uiMode: Int = UI_MODE_TYPE_NORMAL or UI_MODE_NIGHT_NO
    var screenWidthDp: Int = 1280
    var screenHeightDp: Int = 800
    var smallestScreenWidthDp: Int = 800
    var densityDpi: Int = 96
    var orientation: Int = ORIENTATION_LANDSCAPE
    var touchscreen: Int = TOUCHSCREEN_NOTOUCH
    var keyboard: Int = KEYBOARD_QWERTY
    var keyboardHidden: Int = KEYBOARDHIDDEN_NO
    var hardKeyboardHidden: Int = HARDKEYBOARDHIDDEN_NO
    var navigation: Int = NAVIGATION_NONAV
    var navigationHidden: Int = NAVIGATIONHIDDEN_YES
    var screenLayout: Int = SCREENLAYOUT_SIZE_LARGE or SCREENLAYOUT_LONG_NO
    var colorMode: Int = COLOR_MODE_STANDARD
    var windowConfiguration: Any? = null
    var seq: Int = 0
    var assetsSeq: Int = 0

    constructor()
    constructor(other: Configuration) {
        setTo(other)
    }

    fun setTo(o: Configuration) {
        locale = o.locale
        locales = o.locales
        fontScale = o.fontScale
        uiMode = o.uiMode
        screenWidthDp = o.screenWidthDp
        screenHeightDp = o.screenHeightDp
        smallestScreenWidthDp = o.smallestScreenWidthDp
        densityDpi = o.densityDpi
        orientation = o.orientation
        touchscreen = o.touchscreen
        keyboard = o.keyboard
        keyboardHidden = o.keyboardHidden
        hardKeyboardHidden = o.hardKeyboardHidden
        navigation = o.navigation
        navigationHidden = o.navigationHidden
        screenLayout = o.screenLayout
        colorMode = o.colorMode
    }

    fun setLayoutDirection(l: Locale?) {}
    fun getLayoutDirection(): Int = LAYOUT_DIRECTION_LTR

    fun isNightModeActive(): Boolean = (uiMode and UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES

    fun updateFrom(other: Configuration): Int {
        setTo(other)
        return 0
    }

    fun diff(other: Configuration): Int = if (this == other) 0 else -1

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {}

    public override fun clone(): Configuration = Configuration(this)

    override fun compareTo(other: Configuration): Int = 0

    override fun equals(other: Any?): Boolean = other is Configuration &&
        other.uiMode == uiMode && other.screenWidthDp == screenWidthDp && other.screenHeightDp == screenHeightDp

    override fun hashCode(): Int = uiMode * 31 + screenWidthDp * 17 + screenHeightDp

    override fun toString(): String =
        "Configuration{mcc=$mcc,mnc=$mnc,locales=$locales,screen=${screenWidthDp}x$screenHeightDp,uiMode=$uiMode}"

    companion object {
        const val ORIENTATION_UNDEFINED = 0
        const val ORIENTATION_PORTRAIT = 1
        const val ORIENTATION_LANDSCAPE = 2
        const val ORIENTATION_SQUARE = 3

        const val TOUCHSCREEN_UNDEFINED = 0
        const val TOUCHSCREEN_NOTOUCH = 1
        const val TOUCHSCREEN_STYLUS = 2
        const val TOUCHSCREEN_FINGER = 3

        const val KEYBOARD_UNDEFINED = 0
        const val KEYBOARD_NOKEYS = 1
        const val KEYBOARD_QWERTY = 2
        const val KEYBOARD_12KEY = 3

        const val KEYBOARDHIDDEN_UNDEFINED = 0
        const val KEYBOARDHIDDEN_NO = 1
        const val KEYBOARDHIDDEN_YES = 2
        const val KEYBOARDHIDDEN_SOFT = 3

        const val HARDKEYBOARDHIDDEN_UNDEFINED = 0
        const val HARDKEYBOARDHIDDEN_NO = 1
        const val HARDKEYBOARDHIDDEN_YES = 2

        const val NAVIGATION_UNDEFINED = 0
        const val NAVIGATION_NONAV = 1
        const val NAVIGATION_DPAD = 2
        const val NAVIGATION_TRACKBALL = 3
        const val NAVIGATION_WHEEL = 4

        const val NAVIGATIONHIDDEN_UNDEFINED = 0
        const val NAVIGATIONHIDDEN_NO = 1
        const val NAVIGATIONHIDDEN_YES = 2

        const val SCREENLAYOUT_SIZE_UNDEFINED = 0
        const val SCREENLAYOUT_SIZE_SMALL = 1
        const val SCREENLAYOUT_SIZE_NORMAL = 2
        const val SCREENLAYOUT_SIZE_LARGE = 3
        const val SCREENLAYOUT_SIZE_XLARGE = 4
        const val SCREENLAYOUT_SIZE_MASK = 15

        const val SCREENLAYOUT_LONG_UNDEFINED = 0
        const val SCREENLAYOUT_LONG_NO = 16
        const val SCREENLAYOUT_LONG_YES = 32
        const val SCREENLAYOUT_LONG_MASK = 48

        const val SCREENLAYOUT_LAYOUTDIR_UNDEFINED = 0
        const val SCREENLAYOUT_LAYOUTDIR_LTR = 64
        const val SCREENLAYOUT_LAYOUTDIR_RTL = 128
        const val SCREENLAYOUT_LAYOUTDIR_MASK = 192

        const val SCREENLAYOUT_ROUND_UNDEFINED = 0
        const val SCREENLAYOUT_ROUND_NO = 256
        const val SCREENLAYOUT_ROUND_YES = 512
        const val SCREENLAYOUT_ROUND_MASK = 768

        const val UI_MODE_TYPE_UNDEFINED = 0
        const val UI_MODE_TYPE_NORMAL = 1
        const val UI_MODE_TYPE_DESK = 2
        const val UI_MODE_TYPE_CAR = 3
        const val UI_MODE_TYPE_TELEVISION = 4
        const val UI_MODE_TYPE_APPLIANCE = 5
        const val UI_MODE_TYPE_WATCH = 6
        const val UI_MODE_TYPE_VR_HEADSET = 7
        const val UI_MODE_TYPE_MASK = 15

        const val UI_MODE_NIGHT_UNDEFINED = 0
        const val UI_MODE_NIGHT_NO = 16
        const val UI_MODE_NIGHT_YES = 32
        const val UI_MODE_NIGHT_MASK = 48

        const val LAYOUT_DIRECTION_LTR = 0
        const val LAYOUT_DIRECTION_RTL = 1

        const val COLOR_MODE_UNDEFINED = 0
        const val COLOR_MODE_STANDARD = 1
        const val COLOR_MODE_WIDE_COLOR_GAMUT = 2
        const val COLOR_MODE_HDR = 4
        const val COLOR_MODE_HDR_MASK = 12

        const val SCREEN_HEIGHT_DP_UNDEFINED = 0
        const val SCREEN_WIDTH_DP_UNDEFINED = 0
        const val SMALLEST_SCREEN_WIDTH_DP_UNDEFINED = 0
        const val DENSITY_DPI_UNDEFINED = 0
        const val DENSITY_DPI_NONE = 65535
        const val DENSITY_DPI_ANY = 65534
    }
}

/** android.content.res.AssetManager：classpath assets/ 目录。 */
open class AssetManager : AutoCloseable {

    private val classLoader: ClassLoader get() = javaClass.classLoader

    open fun open(fileName: String): InputStream = open(fileName, ACCESS_STREAMING)

    open fun open(fileName: String, accessMode: Int): InputStream {
        val path = "assets/$fileName"
        return classLoader.getResourceAsStream(path)
            ?: throw java.io.FileNotFoundException("assets/$fileName")
    }

    open fun list(path: String): Array<String>? {
        val full = "assets/$path"
        try {
            val url = classLoader.getResource(full) ?: return emptyArray()
            return when (url.protocol) {
                "file" -> File(url.toURI()).list() ?: emptyArray()
                "jar" -> {
                    val conn = url.openConnection() as? java.net.JarURLConnection ?: return emptyArray()
                    val prefix = conn.entryName + "/"
                    conn.jarFile.entries().asSequence()
                        .filter { it.name.startsWith(prefix) && !it.isDirectory }
                        .map { it.name.substring(prefix.length).substringBefore('/') }
                        .distinct()
                        .toList()
                        .toTypedArray()
                }
                else -> emptyArray()
            }
        } catch (e: Exception) {
            return emptyArray()
        }
    }

    open fun openFd(fileName: String): AssetFileDescriptor? = null
    open fun openNonAssetFd(fileName: String): AssetFileDescriptor? = null
    open fun openXmlResourceParser(fileName: String): Any? = null

    open fun getLocales(): Array<String> = arrayOf(Locale.getDefault().toLanguageTag())

    open fun addAssetPath(path: String): Int = 0
    override fun close() {}

    companion object {
        const val ACCESS_UNKNOWN = 0
        const val ACCESS_STREAMING = 1
        const val ACCESS_RANDOM = 2
        const val ACCESS_BUFFER = 3
    }
}

/** android.content.res.ColorStateList。 */
open class ColorStateList : android.os.Parcelable {
    private val states: Array<IntArray>
    private val colors: IntArray

    constructor(states: Array<IntArray>, colors: IntArray) {
        this.states = states; this.colors = colors
    }

    fun isStateful(): Boolean = states.size > 1
    fun isOpaque(): Boolean = colors.all { (it ushr 24) == 0xFF }
    fun getDefaultColor(): Int = colors[0]

    fun withAlpha(alpha: Int): ColorStateList =
        ColorStateList(states, IntArray(colors.size) { i ->
            (colors[i] and 0x00FFFFFF) or (alpha shl 24)
        })

    fun withLStar(lStar: Float): ColorStateList = this

    fun getColorForState(stateSet: IntArray?, defaultColor: Int): Int {
        if (stateSet != null) {
            for (i in states.indices) {
                if (states[i].isEmpty() || states[i].all { it in stateSet }) return colors[i]
            }
        }
        return colors.getOrElse(0) { defaultColor }
    }

    fun getStates(): Array<IntArray> = states
    fun getColors(): IntArray = colors

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {}
    override fun toString(): String = "ColorStateList{default=${Integer.toHexString(getDefaultColor())}}"

    companion object {
        @JvmStatic
        fun valueOf(color: Int): ColorStateList = ColorStateList(arrayOf(IntArray(0)), intArrayOf(color))

        // createFromXml 需要 XmlResourceParser（B 批次未实现），见报告已知缺口。

        @JvmField val CREATOR: android.os.Parcelable.Creator<ColorStateList> =
            object : android.os.Parcelable.Creator<ColorStateList> {
                override fun createFromParcel(source: android.os.Parcel): ColorStateList = valueOf(0)
                override fun newArray(size: Int): Array<ColorStateList?> = arrayOfNulls(size)
            }
    }
}

/** android.content.res.TypedArray：空 stub（桌面无属性系统）。 */
class TypedArray private constructor() {
    fun length(): Int = 0
    fun getIndexCount(): Int = 0
    fun getIndex(at: Int): Int = 0
    fun getResources(): Resources = com.ai.assistance.operit.compat.AppGlobals.resources
    fun getString(index: Int): String? = null
    fun getText(index: Int): CharSequence? = null
    fun getNonResourceString(index: Int): String? = null
    fun getBoolean(index: Int, defValue: Boolean): Boolean = defValue
    fun getInt(index: Int, defValue: Int): Int = defValue
    fun getFloat(index: Int, defValue: Float): Float = defValue
    fun getColor(index: Int, defValue: Int): Int = defValue
    fun getColorStateList(index: Int): ColorStateList? = null
    fun getInteger(index: Int, defValue: Int): Int = defValue
    fun getDimension(index: Int, defValue: Float): Float = defValue
    fun getDimensionPixelOffset(index: Int, defValue: Int): Int = defValue
    fun getDimensionPixelSize(index: Int, defValue: Int): Int = defValue
    fun getLayoutDimension(index: Int, defValue: Int): Int = defValue
    fun getLayoutDimension(index: Int, name: String?): Int = 0
    fun getDrawable(index: Int): android.graphics.drawable.Drawable? = null
    fun getFont(index: Int): android.graphics.Typeface? = null
    fun getTextArray(index: Int): Array<CharSequence>? = null
    fun getResourceId(index: Int, defValue: Int): Int = defValue
    fun getType(index: Int): Int = TypedValue.TYPE_NULL
    fun getValue(index: Int, outValue: TypedValue): Boolean = false
    fun peekValue(index: Int): TypedValue? = null
    fun hasValue(index: Int): Boolean = false
    fun hasValueOrEmpty(index: Int): Boolean = false
    fun getSourceResourceId(index: Int, defValue: Int): Int = defValue
    fun getPositionDescription(): String = ""
    fun getChangingConfigurations(): Int = 0
    fun recycle() {}
    override fun toString(): String = "TypedArray{EMPTY}"

    companion object {
        @JvmField val EMPTY = TypedArray()
    }
}

/** android.content.res.AssetFileDescriptor。 */
open class AssetFileDescriptor : android.os.Parcelable, AutoCloseable {
    private val pfd: android.os.ParcelFileDescriptor?
    private val startOffset: Long
    private val length: Long
    private val extras: android.os.Bundle?

    constructor(fd: android.os.ParcelFileDescriptor?, startOffset: Long, length: Long) {
        this.pfd = fd; this.startOffset = startOffset; this.length = length; this.extras = null
    }

    constructor(fd: android.os.ParcelFileDescriptor?, startOffset: Long, length: Long, extras: android.os.Bundle?) {
        this.pfd = fd; this.startOffset = startOffset; this.length = length; this.extras = extras
    }

    fun getParcelFileDescriptor(): android.os.ParcelFileDescriptor? = pfd
    fun getFileDescriptor(): java.io.FileDescriptor? = pfd?.fileDescriptor
    fun getStartOffset(): Long = startOffset
    fun getLength(): Long = length
    fun getDeclaredLength(): Long = length
    fun getExtras(): android.os.Bundle? = extras

    fun createInputStream(): java.io.FileInputStream? = pfd?.fileDescriptor?.let { java.io.FileInputStream(it) }
    fun createOutputStream(): java.io.FileOutputStream? = pfd?.fileDescriptor?.let { java.io.FileOutputStream(it) }

    override fun close() { pfd?.close() }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {}
    override fun toString(): String = "AssetFileDescriptor{offset=$startOffset, length=$length}"

    class AutoCloseInputStream(afd: AssetFileDescriptor) :
        java.io.FileInputStream(afd.getFileDescriptor() ?: throw IllegalArgumentException("null fd")) {
        override fun close() {
            try { super.close() } finally { /* afd 由调用方管理 */ }
        }
    }

    class AutoCloseOutputStream(afd: AssetFileDescriptor) :
        java.io.FileOutputStream(afd.getFileDescriptor() ?: throw IllegalArgumentException("null fd"))

    companion object {
        const val UNKNOWN_LENGTH: Long = -1
    }
}
