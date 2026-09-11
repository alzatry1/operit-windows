package android.content

import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * android.content.Intent 桌面版：完整数据模型 + 常量。
 */
open class Intent() : Parcelable, Cloneable {

    var action: String? = null
    var data: Uri? = null
    var type: String? = null
    var `package`: String? = null
    var component: ComponentName? = null
    var flags: Int = 0
    var clipData: ClipData? = null
    var selector: Intent? = null
    var sourceBounds: android.graphics.Rect? = null

    private val categoriesSet = LinkedHashSet<String>()
    var extras: Bundle? = null
        private set

    constructor(o: Intent) : this() {
        action = o.action; data = o.data; type = o.type
        `package` = o.`package`; component = o.component?.clone()
        flags = o.flags; clipData = o.clipData
        categoriesSet.addAll(o.categoriesSet)
        extras = o.extras?.clone()
        selector = o.selector?.clone()
    }
    constructor(action: String?) : this() { this.action = action }
    constructor(action: String?, uri: Uri?) : this() { this.action = action; this.data = uri }
    constructor(packageContext: Context, cls: Class<*>) : this() { setClass(packageContext, cls) }
    constructor(action: String?, uri: Uri?, packageContext: Context, cls: Class<*>) : this() {
        this.action = action; this.data = uri; setClass(packageContext, cls)
    }

    // 注意：属性自动生成 getXxx() JVM 方法，不要再手写同名函数（会 JVM 签名冲突）。

    // ---- 修改器（链式，返回 Intent；与属性 setter JVM 签名不同，不冲突）----
    fun setAction(action: String?): Intent = apply { this.action = action }
    fun setData(data: Uri?): Intent = apply { this.data = data }
    fun setDataAndType(data: Uri?, type: String?): Intent = apply { this.data = data; this.type = type }
    fun setType(type: String?): Intent = apply { this.type = type }
    fun setPackage(packageName: String?): Intent = apply { this.`package` = packageName }
    fun setTypeAndNormalize(type: String?): Intent = apply { this.type = type }
    fun setComponent(component: ComponentName?): Intent = apply { this.component = component }
    fun setClipData(clipData: ClipData?): Intent = apply { this.clipData = clipData }
    fun setSelector(selector: Intent?): Intent = apply { this.selector = selector }
    fun setSourceBounds(r: android.graphics.Rect?) = apply { this.sourceBounds = r }

    fun addCategory(category: String): Intent = apply { categoriesSet.add(category) }
    fun removeCategory(category: String) { categoriesSet.remove(category) }
    fun hasCategory(category: String): Boolean = categoriesSet.contains(category)
    fun hasCategories(): Boolean = categoriesSet.isNotEmpty()

    fun addFlags(flags: Int): Intent = apply { this.flags = this.flags or flags }
    fun setFlags(flags: Int): Intent = apply { this.flags = flags }
    fun removeFlags(flags: Int) { this.flags = this.flags and flags.inv() }

    fun setClass(packageContext: Context, cls: Class<*>): Intent = apply {
        component = ComponentName(packageContext.packageName, cls.name)
    }
    fun setClassName(context: Context, className: String): Intent = apply {
        component = ComponentName(context.packageName, className)
    }
    fun setClassName(packageName: String, className: String): Intent = apply {
        component = ComponentName(packageName, className)
    }

    fun resolveActivity(pm: android.content.pm.PackageManager): ComponentName? = component

    // ---- Extras ----
    fun hasExtra(name: String?): Boolean = extras?.containsKey(name) == true
    fun removeExtra(name: String?) { extras?.remove(name) }
    fun replaceExtras(extras: Bundle?): Intent = apply { this.extras = extras?.clone() }
    fun replaceExtras(src: Intent?): Intent = apply { this.extras = src?.extras?.clone() }
    fun putExtras(src: Intent): Intent = apply {
        src.extras?.let { if (extras == null) extras = it.clone() else extras!!.putAll(it) }
    }
    fun putExtras(extras: Bundle): Intent = apply {
        if (this.extras == null) this.extras = extras.clone() else this.extras!!.putAll(extras)
    }
    fun removeUnsafeExtras() {}

    fun putExtra(name: String?, value: Boolean): Intent = apply { x().putBoolean(name, value) }
    fun putExtra(name: String?, value: Byte): Intent = apply { x().putByte(name, value) }
    fun putExtra(name: String?, value: Char): Intent = apply { x().putChar(name, value) }
    fun putExtra(name: String?, value: Short): Intent = apply { x().putShort(name, value) }
    fun putExtra(name: String?, value: Int): Intent = apply { x().putInt(name, value) }
    fun putExtra(name: String?, value: Long): Intent = apply { x().putLong(name, value) }
    fun putExtra(name: String?, value: Float): Intent = apply { x().putFloat(name, value) }
    fun putExtra(name: String?, value: Double): Intent = apply { x().putDouble(name, value) }
    fun putExtra(name: String?, value: String?): Intent = apply { x().putString(name, value) }
    fun putExtra(name: String?, value: CharSequence?): Intent = apply { x().putCharSequence(name, value) }
    fun putExtra(name: String?, value: Parcelable?): Intent = apply { x().putParcelable(name, value) }
    fun putExtra(name: String?, value: Serializable?): Intent = apply { x().putSerializable(name, value) }
    fun putExtra(name: String?, value: Bundle?): Intent = apply { x().putBundle(name, value) }
    fun putExtra(name: String?, value: BooleanArray?): Intent = apply { x().putBooleanArray(name, value) }
    fun putExtra(name: String?, value: ByteArray?): Intent = apply { x().putByteArray(name, value) }
    fun putExtra(name: String?, value: CharArray?): Intent = apply { x().putCharArray(name, value) }
    fun putExtra(name: String?, value: ShortArray?): Intent = apply { x().putShortArray(name, value) }
    fun putExtra(name: String?, value: IntArray?): Intent = apply { x().putIntArray(name, value) }
    fun putExtra(name: String?, value: LongArray?): Intent = apply { x().putLongArray(name, value) }
    fun putExtra(name: String?, value: FloatArray?): Intent = apply { x().putFloatArray(name, value) }
    fun putExtra(name: String?, value: DoubleArray?): Intent = apply { x().putDoubleArray(name, value) }
    fun putExtra(name: String?, value: Array<String>?): Intent = apply { x().putStringArray(name, value) }
    fun putExtra(name: String?, value: Array<CharSequence>?): Intent = apply { x().putCharSequenceArray(name, value) }
    fun putExtra(name: String?, value: Array<Parcelable>?): Intent = apply { x().putParcelableArray(name, value) }

    fun putStringArrayListExtra(name: String?, value: ArrayList<String>?): Intent =
        apply { x().putStringArrayList(name, value) }
    fun putIntegerArrayListExtra(name: String?, value: ArrayList<Int>?): Intent =
        apply { x().putIntegerArrayList(name, value) }
    fun putCharSequenceArrayListExtra(name: String?, value: ArrayList<CharSequence>?): Intent =
        apply { x().putCharSequenceArrayList(name, value) }
    fun putParcelableArrayListExtra(name: String?, value: ArrayList<out Parcelable>?): Intent =
        apply { x().putParcelableArrayList(name, value) }

    private fun x(): Bundle {
        if (extras == null) extras = Bundle()
        return extras!!
    }

    fun getStringExtra(name: String?): String? = extras?.getString(name)
    fun getBooleanExtra(name: String?, defaultValue: Boolean): Boolean = extras?.getBoolean(name, defaultValue) ?: defaultValue
    fun getByteExtra(name: String?, defaultValue: Byte): Byte = extras?.getByte(name, defaultValue) ?: defaultValue
    fun getCharExtra(name: String?, defaultValue: Char): Char = extras?.getChar(name, defaultValue) ?: defaultValue
    fun getShortExtra(name: String?, defaultValue: Short): Short = extras?.getShort(name, defaultValue) ?: defaultValue
    fun getIntExtra(name: String?, defaultValue: Int): Int = extras?.getInt(name, defaultValue) ?: defaultValue
    fun getLongExtra(name: String?, defaultValue: Long): Long = extras?.getLong(name, defaultValue) ?: defaultValue
    fun getFloatExtra(name: String?, defaultValue: Float): Float = extras?.getFloat(name, defaultValue) ?: defaultValue
    fun getDoubleExtra(name: String?, defaultValue: Double): Double = extras?.getDouble(name, defaultValue) ?: defaultValue
    fun getCharSequenceExtra(name: String?): CharSequence? = extras?.getCharSequence(name)
    fun getBundleExtra(name: String?): Bundle? = extras?.getBundle(name)
    fun getStringArrayExtra(name: String?): Array<String>? = extras?.getStringArray(name)
    fun getBooleanArrayExtra(name: String?): BooleanArray? = extras?.getBooleanArray(name)
    fun getByteArrayExtra(name: String?): ByteArray? = extras?.getByteArray(name)
    fun getCharArrayExtra(name: String?): CharArray? = extras?.getCharArray(name)
    fun getShortArrayExtra(name: String?): ShortArray? = extras?.getShortArray(name)
    fun getIntArrayExtra(name: String?): IntArray? = extras?.getIntArray(name)
    fun getLongArrayExtra(name: String?): LongArray? = extras?.getLongArray(name)
    fun getFloatArrayExtra(name: String?): FloatArray? = extras?.getFloatArray(name)
    fun getDoubleArrayExtra(name: String?): DoubleArray? = extras?.getDoubleArray(name)
    fun getCharSequenceArrayExtra(name: String?): Array<CharSequence>? = extras?.getCharSequenceArray(name)
    fun getStringArrayListExtra(name: String?): ArrayList<String>? = extras?.getStringArrayList(name)
    fun getIntegerArrayListExtra(name: String?): ArrayList<Int>? = extras?.getIntegerArrayList(name)
    fun getCharSequenceArrayListExtra(name: String?): ArrayList<CharSequence>? = extras?.getCharSequenceArrayList(name)

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    fun <T : Parcelable?> getParcelableExtra(name: String?): T? = extras?.get(name) as? T

    @Suppress("UNCHECKED_CAST")
    fun <T> getParcelableExtra(name: String?, clazz: Class<T>): T? =
        extras?.get(name)?.let { if (clazz.isInstance(it)) it as T else null }

    @Suppress("UNCHECKED_CAST")
    fun <T : Parcelable?> getParcelableArrayExtra(name: String?): Array<T>? =
        extras?.get(name) as? Array<T>

    @Suppress("UNCHECKED_CAST")
    fun <T : Parcelable?> getParcelableArrayListExtra(name: String?): ArrayList<T>? =
        extras?.getParcelableArrayList(name)

    @Suppress("UNCHECKED_CAST")
    fun <T> getParcelableArrayListExtra(name: String?, clazz: Class<T>): ArrayList<T>? =
        (extras?.get(name) as? List<*>)?.filterIsInstance(clazz)?.let { ArrayList(it) }

    @Suppress("DEPRECATION")
    fun getSerializableExtra(name: String?): Serializable? = extras?.getSerializable(name)

    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable?> getSerializableExtra(name: String?, clazz: Class<T>): T? =
        extras?.get(name)?.let { if (clazz.isInstance(it)) it as T else null }

    // ---- 序列化/比较 ----
    fun toUri(flags: Int): String = toString()
    fun toInsecureString(): String = toString()
    fun toShortString(secure: Boolean, comp: Boolean, extras: Boolean, clip: Boolean): String = toString()

    fun fillIn(other: Intent, flags: Int): Int {
        var changes = 0
        if (other.action != null && (flags and FILL_IN_ACTION != 0 || action == null)) { action = other.action; changes = changes or FILL_IN_ACTION }
        if ((other.data != null || other.type != null) && (flags and FILL_IN_DATA != 0 || (data == null && type == null))) {
            data = other.data; type = other.type; changes = changes or FILL_IN_DATA
        }
        if (other.categoriesSet.isNotEmpty() && (flags and FILL_IN_CATEGORIES != 0 || categoriesSet.isEmpty())) {
            categoriesSet.addAll(other.categoriesSet); changes = changes or FILL_IN_CATEGORIES
        }
        if (other.`package` != null && (flags and FILL_IN_PACKAGE != 0 || `package` == null)) {
            `package` = other.`package`; changes = changes or FILL_IN_PACKAGE
        }
        if (other.component != null && (flags and FILL_IN_COMPONENT != 0 || component == null)) {
            component = other.component?.clone(); changes = changes or FILL_IN_COMPONENT
        }
        return changes
    }

    fun filterEquals(other: Intent?): Boolean =
        other != null && action == other.action && data == other.data && type == other.type &&
            `package` == other.`package` && component == other.component && categoriesSet == other.categoriesSet

    fun filterHashCode(): Int = (action?.hashCode() ?: 0) * 31 + (data?.hashCode() ?: 0)

    public override fun clone(): Intent = Intent(this)
    fun cloneFilter(): Intent = Intent(action, data).apply { this.type = this@Intent.type; component = this@Intent.component }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(action)
        dest.writeString(data?.toString())
        dest.writeString(type)
    }

    override fun toString(): String {
        val sb = StringBuilder("Intent { ")
        action?.let { sb.append("act=").append(it).append(' ') }
        categoriesSet.takeIf { it.isNotEmpty() }?.let { sb.append("cat=").append(it).append(' ') }
        data?.let { sb.append("dat=").append(it).append(' ') }
        type?.let { sb.append("typ=").append(it).append(' ') }
        if (flags != 0) sb.append("flg=0x").append(flags.toString(16)).append(' ')
        `package`?.let { sb.append("pkg=").append(it).append(' ') }
        component?.let { sb.append("cmp=").append(it.flattenToShortString()).append(' ') }
        clipData?.let { sb.append("clip=").append(it).append(' ') }
        sb.append("(has extras) ".takeIf { extras != null } ?: "")
        return sb.toString().trimEnd() + " }"
    }

    companion object {
        // ---- 常用 ACTION ----
        const val ACTION_MAIN = "android.intent.action.MAIN"
        const val ACTION_VIEW = "android.intent.action.VIEW"
        const val ACTION_DEFAULT = ACTION_VIEW
        const val ACTION_ATTACH_DATA = "android.intent.action.ATTACH_DATA"
        const val ACTION_EDIT = "android.intent.action.EDIT"
        const val ACTION_INSERT_OR_EDIT = "android.intent.action.INSERT_OR_EDIT"
        const val ACTION_PICK = "android.intent.action.PICK"
        const val ACTION_CREATE_SHORTCUT = "android.intent.action.CREATE_SHORTCUT"
        const val ACTION_CHOOSER = "android.intent.action.CHOOSER"
        const val ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT"
        const val ACTION_DIAL = "android.intent.action.DIAL"
        const val ACTION_CALL = "android.intent.action.CALL"
        const val ACTION_SEND = "android.intent.action.SEND"
        const val ACTION_SENDTO = "android.intent.action.SENDTO"
        const val ACTION_ANSWER = "android.intent.action.ANSWER"
        const val ACTION_INSERT = "android.intent.action.INSERT"
        const val ACTION_DELETE = "android.intent.action.DELETE"
        const val ACTION_RUN = "android.intent.action.RUN"
        const val ACTION_SYNC = "android.intent.action.SYNC"
        const val ACTION_PICK_ACTIVITY = "android.intent.action.PICK_ACTIVITY"
        const val ACTION_SEARCH = "android.intent.action.SEARCH"
        const val ACTION_WEB_SEARCH = "android.intent.action.WEB_SEARCH"
        const val ACTION_FACTORY_TEST = "android.intent.action.FACTORY_TEST"
        const val ACTION_CALL_BUTTON = "android.intent.action.CALL_BUTTON"
        const val ACTION_VOICECOMMAND = "android.intent.action.VOICECOMMAND"
        const val ACTION_SEARCH_LONG_PRESS = "android.intent.action.SEARCH_LONG_PRESS"
        const val ACTION_APP_ERROR = "android.intent.action.APP_ERROR"
        const val ACTION_POWER_USAGE_SUMMARY = "android.intent.action.POWER_USAGE_SUMMARY"
        const val ACTION_MANAGE_PACKAGE_USAGE = "android.intent.action.MANAGE_PACKAGE_USAGE"
        const val ACTION_ONGOING = "android.intent.action.ONGOING"
        const val ACTION_BUG_REPORT = "android.intent.action.BUG_REPORT"
        const val ACTION_SEND_MULTIPLE = "android.intent.action.SEND_MULTIPLE"
        const val ACTION_OPEN_DOCUMENT = "android.intent.action.OPEN_DOCUMENT"
        const val ACTION_CREATE_DOCUMENT = "android.intent.action.CREATE_DOCUMENT"
        const val ACTION_OPEN_DOCUMENT_TREE = "android.intent.action.OPEN_DOCUMENT_TREE"
        const val ACTION_INSTALL_PACKAGE = "android.intent.action.INSTALL_PACKAGE"
        const val ACTION_UNINSTALL_PACKAGE = "android.intent.action.UNINSTALL_PACKAGE"
        const val ACTION_PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED"
        const val ACTION_PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED"
        const val ACTION_PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED"
        const val ACTION_PACKAGE_INSTALL = "android.intent.action.PACKAGE_INSTALL"
        const val ACTION_PACKAGE_CHANGED = "android.intent.action.PACKAGE_CHANGED"
        const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
        const val ACTION_TIME_TICK = "android.intent.action.TIME_TICK"
        const val ACTION_TIME_CHANGED = "android.intent.action.TIME_SET"
        const val ACTION_DATE_CHANGED = "android.intent.action.DATE_CHANGED"
        const val ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED"
        const val ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON"
        const val ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF"
        const val ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT"
        const val ACTION_CONFIGURATION_CHANGED = "android.intent.action.CONFIGURATION_CHANGED"
        const val ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED"
        const val ACTION_CLOSE_SYSTEM_DIALOGS = "android.intent.action.CLOSE_SYSTEM_DIALOGS"
        const val ACTION_WALLPAPER_CHANGED = "android.intent.action.WALLPAPER_CHANGED"
        const val ACTION_DEVICE_STORAGE_LOW = "android.intent.action.DEVICE_STORAGE_LOW"
        const val ACTION_DEVICE_STORAGE_OK = "android.intent.action.DEVICE_STORAGE_OK"
        const val ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION"
        const val ACTION_VIEW_PERMISSION_USAGE = "android.intent.action.VIEW_PERMISSION_USAGE"
        const val ACTION_REVIEW_PERMISSIONS = "android.intent.action.REVIEW_PERMISSIONS"
        const val ACTION_ASSIST = "android.intent.action.ASSIST"
        const val ACTION_VOICE_COMMAND = "android.intent.action.VOICE_COMMAND"
        const val ACTION_QUICK_VIEW = "android.intent.action.QUICK_VIEW"
        const val ACTION_PROCESS_TEXT = "android.intent.action.PROCESS_TEXT"
        const val ACTION_ALL_APPS = "android.intent.action.ALL_APPS"
        const val ACTION_SET_WALLPAPER = "android.intent.action.SET_WALLPAPER"
        const val ACTION_AIRPLANE_MODE_CHANGED = "android.intent.action.AIRPLANE_MODE"
        const val ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW"
        const val ACTION_BATTERY_OKAY = "android.intent.action.BATTERY_OKAY"
        const val ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG"
        const val ACTION_MEDIA_BUTTON = "android.intent.action.MEDIA_BUTTON"
        const val ACTION_CAMERA_BUTTON = "android.intent.action.CAMERA_BUTTON"
        const val ACTION_SHOW_APP_INFO = "android.intent.action.SHOW_APP_INFO"
        const val ACTION_PASTE = "android.intent.action.PASTE"
        const val ACTION_QUICK_CLOCK = "android.intent.action.QUICK_CLOCK"
        const val ACTION_APPLICATION_PREFERENCES = "android.intent.action.APPLICATION_PREFERENCES"

        // ---- CATEGORY ----
        const val CATEGORY_DEFAULT = "android.intent.category.DEFAULT"
        const val CATEGORY_BROWSABLE = "android.intent.category.BROWSABLE"
        const val CATEGORY_ALTERNATIVE = "android.intent.category.ALTERNATIVE"
        const val CATEGORY_SELECTED_ALTERNATIVE = "android.intent.category.SELECTED_ALTERNATIVE"
        const val CATEGORY_TAB = "android.intent.category.TAB"
        const val CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER"
        const val CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER"
        const val CATEGORY_INFO = "android.intent.category.INFO"
        const val CATEGORY_HOME = "android.intent.category.HOME"
        const val CATEGORY_PREFERENCE = "android.intent.category.PREFERENCE"
        const val CATEGORY_DEVELOPMENT_PREFERENCE = "android.intent.category.DEVELOPMENT_PREFERENCE"
        const val CATEGORY_EMBED = "android.intent.category.EMBED"
        const val CATEGORY_APP_MARKET = "android.intent.category.APP_MARKET"
        const val CATEGORY_MONKEY = "android.intent.category.MONKEY"
        const val CATEGORY_TEST = "android.intent.category.TEST"
        const val CATEGORY_UNIT_TEST = "android.intent.category.UNIT_TEST"
        const val CATEGORY_SAMPLE_CODE = "android.intent.category.SAMPLE_CODE"
        const val CATEGORY_OPENABLE = "android.intent.category.OPENABLE"
        const val CATEGORY_FRAMEWORK_INSTRUMENTATION_TEST = "android.intent.category.FRAMEWORK_INSTRUMENTATION_TEST"
        const val CATEGORY_CAR_DOCK = "android.intent.category.CAR_DOCK"
        const val CATEGORY_DESK_DOCK = "android.intent.category.DESK_DOCK"
        const val CATEGORY_LE_DESK_DOCK = "android.intent.category.LE_DESK_DOCK"
        const val CATEGORY_HE_DESK_DOCK = "android.intent.category.HE_DESK_DOCK"
        const val CATEGORY_CAR_MODE = "android.intent.category.CAR_MODE"
        const val CATEGORY_APP_CALCULATOR = "android.intent.category.APP_CALCULATOR"
        const val CATEGORY_APP_CALENDAR = "android.intent.category.APP_CALENDAR"
        const val CATEGORY_APP_CONTACTS = "android.intent.category.APP_CONTACTS"
        const val CATEGORY_APP_EMAIL = "android.intent.category.APP_EMAIL"
        const val CATEGORY_APP_GALLERY = "android.intent.category.APP_GALLERY"
        const val CATEGORY_APP_MAPS = "android.intent.category.APP_MAPS"
        const val CATEGORY_APP_MESSAGING = "android.intent.category.APP_MESSAGING"
        const val CATEGORY_APP_MUSIC = "android.intent.category.APP_MUSIC"
        const val CATEGORY_APP_BROWSER = "android.intent.category.APP_BROWSER"
        const val CATEGORY_APP_FILES = "android.intent.category.APP_FILES"
        const val CATEGORY_ACCESSIBILITY_SHORTCUT_TARGET = "android.intent.category.ACCESSIBILITY_SHORTCUT_TARGET"
        const val CATEGORY_VOICE = "android.intent.category.VOICE"

        // ---- EXTRA ----
        const val EXTRA_ALARM_COUNT = "android.intent.extra.ALARM_COUNT"
        const val EXTRA_BCC = "android.intent.extra.BCC"
        const val EXTRA_CC = "android.intent.extra.CC"
        const val EXTRA_CHANGED_COMPONENT_NAME = "android.intent.extra.changed_component_name"
        const val EXTRA_DATA_REMOVED = "android.intent.extra.DATA_REMOVED"
        const val EXTRA_DOCK_STATE = "android.intent.extra.DOCK_STATE"
        const val EXTRA_DOCK_STATE_HE_DESK = 1
        const val EXTRA_DOCK_STATE_LE_DESK = 2
        const val EXTRA_DOCK_STATE_CAR = 3
        const val EXTRA_DOCK_STATE_DESK = 4
        const val EXTRA_DOCK_STATE_UNDOCKED = 0
        const val EXTRA_DONT_KILL_APP = "android.intent.extra.DONT_KILL_APP"
        const val EXTRA_EMAIL = "android.intent.extra.EMAIL"
        const val EXTRA_INITIAL_INTENTS = "android.intent.extra.INITIAL_INTENTS"
        const val EXTRA_INTENT = "android.intent.extra.INTENT"
        const val EXTRA_KEY_EVENT = "android.intent.extra.KEY_EVENT"
        const val EXTRA_ORIGINATING_URI = "android.intent.extra.ORIGINATING_URI"
        const val EXTRA_REFERRER = "android.intent.extra.REFERRER"
        const val EXTRA_REMOTE_INTENT_TOKEN = "android.intent.extra.remote_intent_token"
        const val EXTRA_REPLACING = "android.intent.extra.REPLACING"
        const val EXTRA_SHORTCUT_ICON = "android.intent.extra.shortcut.ICON"
        const val EXTRA_SHORTCUT_ICON_RESOURCE = "android.intent.extra.shortcut.ICON_RESOURCE"
        const val EXTRA_SHORTCUT_INTENT = "android.intent.extra.shortcut.INTENT"
        const val EXTRA_STREAM = "android.intent.extra.STREAM"
        const val EXTRA_SHORTCUT_NAME = "android.intent.extra.shortcut.NAME"
        const val EXTRA_SUBJECT = "android.intent.extra.SUBJECT"
        const val EXTRA_TEMPLATE = "android.intent.extra.TEMPLATE"
        const val EXTRA_TEXT = "android.intent.extra.TEXT"
        const val EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT"
        const val EXTRA_TITLE = "android.intent.extra.TITLE"
        const val EXTRA_UID = "android.intent.extra.UID"
        const val EXTRA_USER_INITIATED = "android.intent.extra.USER_INITIATED"
        const val EXTRA_ALLOW_REPLACE = "android.intent.extra.ALLOW_REPLACE"
        const val EXTRA_ALLOW_MULTIPLE = "android.intent.extra.ALLOW_MULTIPLE"
        const val EXTRA_USER = "android.intent.extra.USER"
        const val EXTRA_USER_HANDLE = "android.intent.extra.user_handle"
        const val EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER"
        const val EXTRA_BUG_REPORT = "android.intent.extra.BUG_REPORT"
        const val EXTRA_PACKAGE_NAME = "android.intent.extra.PACKAGE_NAME"
        const val EXTRA_SPLIT_NAME = "android.intent.extra.SPLIT_NAME"
        const val EXTRA_MIME_TYPES = "android.intent.extra.MIME_TYPES"
        const val EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY"
        const val EXTRA_INDEX = "android.intent.extra.INDEX"
        const val EXTRA_PROCESS_TEXT = "android.intent.extra.PROCESS_TEXT"
        const val EXTRA_PROCESS_TEXT_READONLY = "android.intent.extra.PROCESS_TEXT_READONLY"
        const val EXTRA_QUICK_VIEW_ADVANCED = "android.intent.extra.QUICK_VIEW_ADVANCED"
        const val EXTRA_QUICK_VIEW_FEATURES = "android.intent.extra.QUICK_VIEW_FEATURES"
        const val EXTRA_DURATION_LIMIT = "android.intent.extra.durationLimit"
        const val EXTRA_CHOOSER_TARGETS = "android.intent.extra.CHOOSER_TARGETS"
        const val EXTRA_CHOOSER_REFINEMENT_INTENT_SENDER = "android.intent.extra.CHOOSER_REFINEMENT_INTENT_SENDER"
        const val EXTRA_CHOSEN_COMPONENT = "android.intent.extra.CHOSEN_COMPONENT"
        const val EXTRA_EXCLUDE_COMPONENTS = "android.intent.extra.EXCLUDE_COMPONENTS"
        const val EXTRA_RETURN_RESULT = "android.intent.extra.RETURN_RESULT"
        const val EXTRA_CONTENT_ANNOTATIONS = "android.intent.extra.CONTENT_ANNOTATIONS"
        const val EXTRA_RESULT_RECEIVER = "android.intent.extra.RESULT_RECEIVER"
        const val EXTRA_TASK_ID = "android.intent.extra.TASK_ID"
        const val EXTRA_SUSPENDED_PACKAGE_EXTRAS = "android.intent.extra.SUSPENDED_PACKAGE_EXTRAS"
        const val EXTRA_AUTO_LAUNCH = "android.intent.extra.AUTO_LAUNCH"
        const val EXTRA_FROM_STORAGE = "android.intent.extra.FROM_STORAGE"
        const val EXTRA_ATTRIBUTION_TAGS = "android.intent.extra.ATTRIBUTION_TAGS"
        const val EXTRA_TIME = "android.intent.extra.TIME"

        // ---- FLAG_ACTIVITY_* ----
        const val FLAG_GRANT_READ_URI_PERMISSION = 0x00000001
        const val FLAG_GRANT_WRITE_URI_PERMISSION = 0x00000002
        const val FLAG_FROM_BACKGROUND = 0x00000004
        const val FLAG_DEBUG_LOG_RESOLUTION = 0x00000008
        const val FLAG_EXCLUDE_STOPPED_PACKAGES = 0x00000010
        const val FLAG_INCLUDE_STOPPED_PACKAGES = 0x00000020
        const val FLAG_GRANT_PERSISTABLE_URI_PERMISSION = 0x00000040
        const val FLAG_GRANT_PREFIX_URI_PERMISSION = 0x00000080
        const val FLAG_DIRECT_BOOT_AUTO = 0x00000100
        const val FLAG_IGNORE_EPHEMERAL = 0x00000200
        const val FLAG_ACTIVITY_NO_HISTORY = 0x40000000
        const val FLAG_ACTIVITY_SINGLE_TOP = 0x20000000
        const val FLAG_ACTIVITY_NEW_TASK = 0x10000000
        const val FLAG_ACTIVITY_MULTIPLE_TASK = 0x08000000
        const val FLAG_ACTIVITY_CLEAR_TOP = 0x04000000
        const val FLAG_ACTIVITY_FORWARD_RESULT = 0x02000000
        const val FLAG_ACTIVITY_PREVIOUS_IS_TOP = 0x01000000
        const val FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 0x00800000
        const val FLAG_ACTIVITY_BROUGHT_TO_FRONT = 0x00400000
        const val FLAG_ACTIVITY_RESET_TASK_IF_NEEDED = 0x00200000
        const val FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY = 0x00100000
        const val FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET = 0x00080000
        const val FLAG_ACTIVITY_NEW_DOCUMENT = 0x00080000
        const val FLAG_ACTIVITY_NO_USER_ACTION = 0x00040000
        const val FLAG_ACTIVITY_REORDER_TO_FRONT = 0x00020000
        const val FLAG_ACTIVITY_NO_ANIMATION = 0x00010000
        const val FLAG_ACTIVITY_CLEAR_TASK = 0x00008000
        const val FLAG_ACTIVITY_TASK_ON_HOME = 0x00004000
        const val FLAG_ACTIVITY_RETAIN_IN_RECENTS = 0x00002000
        const val FLAG_ACTIVITY_LAUNCH_ADJACENT = 0x00001000
        const val FLAG_ACTIVITY_MATCH_EXTERNAL = 0x00000800
        const val FLAG_ACTIVITY_REQUIRE_NON_BROWSER = 0x00000400
        const val FLAG_ACTIVITY_REQUIRE_DEFAULT = 0x00000200
        const val FLAG_RECEIVER_REGISTERED_ONLY = 0x40000000
        const val FLAG_RECEIVER_REPLACE_PENDING = 0x20000000
        const val FLAG_RECEIVER_FOREGROUND = 0x10000000
        const val FLAG_RECEIVER_NO_ABORT = 0x08000000
        const val FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT = 0x04000000
        const val FLAG_RECEIVER_BOOT_UPGRADE = 0x02000000
        const val FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000
        const val FLAG_RECEIVER_EXCLUDE_BACKGROUND = 0x00800000
        const val FLAG_RECEIVER_FROM_SHELL = 0x00400000
        const val FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS = 0x00200000
        // PendingIntent flags（同值域，定义在 Intent 以便 FLAG_* 引用）
        const val FLAG_IMMUTABLE = 0x04000000
        const val FLAG_MUTABLE = 0x02000000
        const val FLAG_ONE_SHOT = 0x40000000
        const val FLAG_NO_CREATE = 0x20000000
        const val FLAG_CANCEL_CURRENT = 0x10000000
        const val FLAG_UPDATE_CURRENT = 0x08000000

        const val FILL_IN_ACTION = 1
        const val FILL_IN_DATA = 2
        const val FILL_IN_CATEGORIES = 4
        const val FILL_IN_COMPONENT = 8
        const val FILL_IN_PACKAGE = 32
        const val FILL_IN_SOURCE_BOUNDS = 256
        const val FILL_IN_SELECTOR = 64
        const val FILL_IN_CLIP_DATA = 128

        const val URI_INTENT_SCHEME = 1
        const val URI_ANDROID_APP_SCHEME = 2
        const val URI_ALLOW_UNSAFE = 4

        const val METADATA_DOCK_HOME = "android.dock_home"
        const val METADATA_SETUP_VERSION = "android.SETUP_VERSION"

        @JvmStatic
        fun createChooser(target: Intent?, title: CharSequence?): Intent =
            Intent(ACTION_CHOOSER).apply {
                putExtra(EXTRA_INTENT, target)
                putExtra(EXTRA_TITLE, title)
            }

        @JvmStatic
        fun createChooser(target: Intent?, title: CharSequence?, sender: android.content.IntentSender?): Intent =
            createChooser(target, title)

        @JvmStatic
        fun makeMainActivity(mainActivity: ComponentName): Intent =
            Intent(ACTION_MAIN).apply { component = mainActivity; addCategory(CATEGORY_LAUNCHER) }

        @JvmStatic
        fun makeMainSelectorActivity(selectorAction: String?, selectorCategory: String?): Intent =
            Intent(ACTION_MAIN).apply {
                selector = Intent(selectorAction).apply { selectorCategory?.let { addCategory(it) } }
            }

        @JvmStatic
        fun makeRestartActivityTask(mainActivity: ComponentName): Intent =
            makeMainActivity(mainActivity).apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
            }

        @JvmStatic
        @Deprecated("Use parseUri", ReplaceWith("parseUri(uri, flags)"))
        fun getIntent(uri: String): Intent = parseUri(uri, 0)

        @JvmStatic
        fun parseUri(uri: String, flags: Int): Intent {
            // 简化解析：intent:#Intent;action=xxx;...;end 或普通 URI
            if (uri.startsWith("intent:") || uri.startsWith("#Intent;")) {
                var body = uri.removePrefix("intent:").removePrefix("#Intent;")
                body = body.removeSuffix("end").removeSuffix(";")
                val intent = Intent()
                for (part in body.split(";")) {
                    val kv = part.split("=", limit = 2)
                    if (kv.size != 2) continue
                    when (kv[0]) {
                        "action" -> intent.action = kv[1]
                        "category" -> intent.addCategory(kv[1])
                        "type" -> intent.type = kv[1]
                        "launchFlags" -> intent.flags = kv[1].removePrefix("0x").toIntOrNull(16) ?: 0
                        "package" -> intent.`package` = kv[1]
                        "component" -> intent.component = ComponentName.unflattenFromString(kv[1])
                        "scheme" -> {}
                    }
                }
                return intent
            }
            return Intent(ACTION_VIEW, Uri.parse(uri))
        }

        @JvmStatic
        fun normalizeMimeType(type: String?): String? = type?.lowercase()

        @JvmField val CREATOR: Parcelable.Creator<Intent> = object : Parcelable.Creator<Intent> {
            override fun createFromParcel(source: Parcel): Intent = Intent(source.readString())
            override fun newArray(size: Int): Array<Intent?> = arrayOfNulls(size)
        }
    }
}

/** android.content.IntentFilter。 */
open class IntentFilter : Parcelable {
    private val actionsList = ArrayList<String>()
    private val categoriesList = ArrayList<String>()
    private val dataSchemes = ArrayList<String>()
    private val dataTypes = ArrayList<String>()
    private var priorityValue = 0

    constructor()
    constructor(action: String) { addAction(action) }
    constructor(action: String, dataType: String) { addAction(action); addDataType(dataType) }
    constructor(o: IntentFilter) {
        actionsList.addAll(o.actionsList)
        categoriesList.addAll(o.categoriesList)
        dataSchemes.addAll(o.dataSchemes)
        dataTypes.addAll(o.dataTypes)
        priorityValue = o.priorityValue
    }

    fun addAction(action: String) { if (!actionsList.contains(action)) actionsList.add(action) }
    fun countActions(): Int = actionsList.size
    fun getAction(i: Int): String = actionsList[i]
    fun hasAction(action: String?): Boolean = action != null && actionsList.contains(action)
    fun actionsIterator(): Iterator<String>? = actionsList.iterator()
    fun matchAction(action: String?): Boolean = hasAction(action)

    fun addCategory(category: String) { if (!categoriesList.contains(category)) categoriesList.add(category) }
    fun countCategories(): Int = categoriesList.size
    fun getCategory(i: Int): String = categoriesList[i]
    fun hasCategory(category: String?): Boolean = category != null && categoriesList.contains(category)
    fun matchCategories(categories: Set<String>?): String? {
        categories?.forEach { if (!categoriesList.contains(it)) return it }
        return null
    }

    fun addDataScheme(scheme: String) { if (!dataSchemes.contains(scheme)) dataSchemes.add(scheme) }
    fun countDataSchemes(): Int = dataSchemes.size
    fun getDataScheme(i: Int): String = dataSchemes[i]
    fun hasDataScheme(scheme: String?): Boolean = scheme != null && dataSchemes.contains(scheme)
    fun addDataAuthority(authority: String, port: String) {}
    fun addDataPath(path: String, type: Int) {}
    fun addDataType(type: String) { if (!dataTypes.contains(type)) dataTypes.add(type) }
    fun countDataTypes(): Int = dataTypes.size
    fun getDataType(i: Int): String = dataTypes[i]
    fun hasDataType(type: String?): Boolean = type != null && dataTypes.contains(type)

    fun setPriority(priority: Int) { priorityValue = priority }
    fun getPriority(): Int = priorityValue

    fun match(intent: Intent?): Int = if (intent == null) NO_MATCH_TYPE else 0

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    /** 便于日志。 */
    fun actionsSummary(): String = actionsList.joinToString(",")

    companion object {
        const val MATCH_CATEGORY_MASK = 0x0FFF0000
        const val MATCH_ADJUSTMENT_MASK = 0x0000FFFF
        const val MATCH_ADJUSTMENT_NORMAL = 0x0000
        const val MATCH_CATEGORY_EMPTY = 0x00100000
        const val MATCH_CATEGORY_SCHEME = 0x00200000
        const val MATCH_CATEGORY_HOST = 0x00300000
        const val MATCH_CATEGORY_PORT = 0x00400000
        const val MATCH_CATEGORY_PATH = 0x00500000
        const val MATCH_CATEGORY_TYPE = 0x00600000
        const val MATCH_CATEGORY_SCHEME_SPECIFIC_PART = 0x00700000
        const val NO_MATCH_TYPE = -1
        const val NO_MATCH_DATA = -2
        const val NO_MATCH_ACTION = -3
        const val NO_MATCH_CATEGORY = -4
        const val SYSTEM_HIGH_PRIORITY = 1000
        const val SYSTEM_LOW_PRIORITY = -1000

        @JvmStatic
        fun create(action: String, dataType: String): IntentFilter = IntentFilter(action, dataType)

        @JvmField val CREATOR: Parcelable.Creator<IntentFilter> = object : Parcelable.Creator<IntentFilter> {
            override fun createFromParcel(source: Parcel): IntentFilter = IntentFilter()
            override fun newArray(size: Int): Array<IntentFilter?> = arrayOfNulls(size)
        }
    }
}

/** android.content.IntentSender：桌面 stub。 */
class IntentSender : Parcelable {
    fun interface OnFinished {
        fun onSendFinished(
            sender: IntentSender, intent: Intent?,
            resultCode: Int, resultData: String?, resultExtras: android.os.Bundle?,
        )
    }

    fun sendIntent(
        context: Context?, code: Int, intent: Intent?,
        onFinished: OnFinished?, handler: android.os.Handler?,
    ) {
        android.util.Log.d("IntentSender", "sendIntent no-op")
        onFinished?.onSendFinished(this, intent, 0, null, null)
    }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        @JvmStatic
        fun writeIntentSenderOrNullToParcel(sender: IntentSender?, parcel: Parcel) {}

        @JvmStatic
        fun readIntentSenderOrNullFromParcel(parcel: Parcel): IntentSender? = null
    }
}
