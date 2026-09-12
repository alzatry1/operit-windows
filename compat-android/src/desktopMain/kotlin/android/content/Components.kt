package android.content

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.KeyEvent

/** android.content.ComponentName。 */
class ComponentName : Parcelable, Cloneable, Comparable<ComponentName> {
    private val pkg: String
    private val cls: String

    constructor(pkg: String?, cls: String?) {
        this.pkg = pkg ?: ""; this.cls = cls ?: ""
    }

    constructor(context: Context, cls: Class<*>) : this(context.packageName, cls.name)
    constructor(context: Context, cls: String) : this(context.packageName, cls)

    /** ComponentName.packageName（Kotlin 类需真属性才能被 component.packageName 访问；同时保留方法式兼容）。——Nova 注 */
    val packageName: String get() = pkg
    val className: String get() = cls
    fun getShortClassName(): String {
        val prefix = "$pkg."
        return if (cls.startsWith(prefix)) cls.substring(prefix.length) else cls
    }

    fun flattenToString(): String = "$pkg/$cls"
    fun flattenToShortString(): String = "$pkg/$shortClassNameCompat"
    private val shortClassNameCompat: String get() = getShortClassName()

    fun toShortString(): String = "{$pkg/$cls}"

    public override fun clone(): ComponentName = ComponentName(pkg, cls)

    override fun equals(other: Any?): Boolean = other is ComponentName && other.pkg == pkg && other.cls == cls
    override fun hashCode(): Int = pkg.hashCode() * 31 + cls.hashCode()
    override fun toString(): String = "ComponentInfo{$pkg/$cls}"
    override fun compareTo(other: ComponentName): Int =
        pkg.compareTo(other.pkg).takeIf { it != 0 } ?: cls.compareTo(other.cls)

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(pkg)
        dest.writeString(cls)
    }

    companion object {
        @JvmStatic
        fun unflattenFromString(str: String): ComponentName? {
            val sep = str.indexOf('/')
            if (sep < 0 || sep + 1 >= str.length) return null
            var cls = str.substring(sep + 1)
            val pkg = str.substring(0, sep)
            if (cls.startsWith(".")) cls = pkg + cls
            else if (cls.indexOf('.') < 0) cls = "$pkg.$cls"
            return ComponentName(pkg, cls)
        }

        @JvmStatic
        fun createRelative(context: Context, cls: String): ComponentName = ComponentName(context, cls)

        @JvmStatic
        fun createRelative(pkg: String, cls: String): ComponentName {
            val full = if (cls.startsWith(".")) pkg + cls else if (cls.indexOf('.') < 0) "$pkg.$cls" else cls
            return ComponentName(pkg, full)
        }

        @JvmField val CREATOR: Parcelable.Creator<ComponentName> = object : Parcelable.Creator<ComponentName> {
            override fun createFromParcel(source: Parcel): ComponentName =
                ComponentName(source.readString() ?: "", source.readString() ?: "")
            override fun newArray(size: Int): Array<ComponentName?> = arrayOfNulls(size)
        }
    }
}

/** android.content.BroadcastReceiver。 */
abstract class BroadcastReceiver {
    abstract fun onReceive(context: Context, intent: Intent)

    fun goAsync(): PendingResult = PendingResult()
    fun peekService(myContext: Context, service: Intent): android.os.IBinder? = null
    fun setResultCode(code: Int) {}
    fun getResultCode(): Int = 0
    fun setResultData(data: String?) {}
    fun getResultData(): String? = null
    fun setResultExtras(extras: Bundle) {}
    fun getResultExtras(makeMap: Boolean): Bundle = Bundle()
    fun setResult(code: Int, data: String?, extras: Bundle?) {}
    fun getAbortBroadcast(): Boolean = false
    fun setAbortBroadcast(abort: Boolean) {}
    fun getDebugUnregister(): Boolean = false
    fun setDebugUnregister(debugUnregister: Boolean) {}
    fun isInitialStickyBroadcast(): Boolean = false
    fun isOrderedBroadcast(): Boolean = false
    fun setOrderedHint(isOrdered: Boolean) {}
    fun clearAbortBroadcast() {}
    fun setPriority(priority: Int) {}

    class PendingResult {
        fun finish() {}
        fun setResultCode(code: Int) {}
        fun setResultData(data: String?) {}
        fun setResultExtras(extras: Bundle?) {}
        fun setResult(code: Int, data: String?, extras: Bundle?) {}
    }
}

/** android.content.ServiceConnection 已迁移至 Java 版（java/android/content/ServiceConnection.java）。——Nova 注 */

/** android.content.ActivityNotFoundException。 */
class ActivityNotFoundException : RuntimeException {
    constructor() : super()
    constructor(name: String?) : super(name)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

/** android.content.ReceiverCallNotAllowedException。 */
class ReceiverCallNotAllowedException : RuntimeException {
    constructor(msg: String?) : super(msg)
    constructor(msg: String?, cause: Throwable?) : super(msg, cause)
}

/** android.content.DialogInterface + 监听器。 */
interface DialogInterface {
    fun cancel()
    fun dismiss()

    fun interface OnClickListener {
        fun onClick(dialog: DialogInterface, which: Int)
    }

    fun interface OnCancelListener {
        fun onCancel(dialog: DialogInterface)
    }

    fun interface OnDismissListener {
        fun onDismiss(dialog: DialogInterface)
    }

    fun interface OnShowListener {
        fun onShow(dialog: DialogInterface)
    }

    fun interface OnMultiChoiceClickListener {
        fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean)
    }

    fun interface OnKeyListener {
        fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean
    }

    companion object {
        const val BUTTON_POSITIVE = -1
        const val BUTTON_NEGATIVE = -2
        const val BUTTON_NEUTRAL = -3
        const val BUTTON1 = -1
        const val BUTTON2 = -2
        const val BUTTON3 = -3
    }
}

/** android.content.ContentValues。 */
class ContentValues : Parcelable {
    private val values = LinkedHashMap<String, Any?>()

    constructor()
    constructor(size: Int)
    constructor(from: ContentValues) { values.putAll(from.values) }

    fun valueSet(): Set<Map.Entry<String, Any?>> = values.entries
    fun keySet(): Set<String> = values.keys
    fun size(): Int = values.size
    fun isEmpty(): Boolean = values.isEmpty()
    fun containsKey(key: String?): Boolean = values.containsKey(key)
    fun get(key: String?): Any? = values[key]
    fun remove(key: String?) { values.remove(key) }
    fun clear() = values.clear()
    fun putAll(other: ContentValues) { values.putAll(other.values) }

    fun put(key: String, value: String?) { values[key] = value }
    fun put(key: String, value: Byte?) { values[key] = value }
    fun put(key: String, value: Short?) { values[key] = value }
    fun put(key: String, value: Int?) { values[key] = value }
    fun put(key: String, value: Long?) { values[key] = value }
    fun put(key: String, value: Float?) { values[key] = value }
    fun put(key: String, value: Double?) { values[key] = value }
    fun put(key: String, value: Boolean?) { values[key] = value }
    fun put(key: String, value: ByteArray?) { values[key] = value }
    fun putNull(key: String) { values[key] = null }

    fun getAsString(key: String?): String? = values[key]?.toString()
    fun getAsLong(key: String?): Long? = (values[key] as? Number)?.toLong() ?: values[key]?.toString()?.toLongOrNull()
    fun getAsInteger(key: String?): Int? = (values[key] as? Number)?.toInt() ?: values[key]?.toString()?.toIntOrNull()
    fun getAsShort(key: String?): Short? = (values[key] as? Number)?.toShort()
    fun getAsByte(key: String?): Byte? = (values[key] as? Number)?.toByte()
    fun getAsDouble(key: String?): Double? = (values[key] as? Number)?.toDouble()
    fun getAsFloat(key: String?): Float? = (values[key] as? Number)?.toFloat()
    fun getAsBoolean(key: String?): Boolean? = when (val v = values[key]) {
        is Boolean -> v
        is Number -> v.toInt() != 0
        is String -> v.toBooleanStrictOrNull() ?: v.equals("1")
        else -> null
    }
    fun getAsByteArray(key: String?): ByteArray? = values[key] as? ByteArray

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeValue(LinkedHashMap(values)) }

    override fun equals(other: Any?): Boolean = other is ContentValues && other.values == values
    override fun hashCode(): Int = values.hashCode()
    override fun toString(): String = values.toString()

    companion object {
        const val TAG = "ContentValues"
        @JvmField val CREATOR: Parcelable.Creator<ContentValues> = object : Parcelable.Creator<ContentValues> {
            override fun createFromParcel(source: Parcel): ContentValues = ContentValues()
            override fun newArray(size: Int): Array<ContentValues?> = arrayOfNulls(size)
        }
    }
}

/** android.content.ComponentCallbacks。 */
interface ComponentCallbacks {
    fun onConfigurationChanged(newConfig: Configuration)
    fun onLowMemory()
}

/** android.content.ComponentCallbacks2。 */
interface ComponentCallbacks2 : ComponentCallbacks {
    fun onTrimMemory(level: Int)

    companion object {
        const val TRIM_MEMORY_COMPLETE = 80
        const val TRIM_MEMORY_MODERATE = 60
        const val TRIM_MEMORY_BACKGROUND = 40
        const val TRIM_MEMORY_UI_HIDDEN = 20
        const val TRIM_MEMORY_RUNNING_CRITICAL = 15
        const val TRIM_MEMORY_RUNNING_LOW = 10
        const val TRIM_MEMORY_RUNNING_MODERATE = 5
    }
}

/** android.content.UriPermission。 */
class UriPermission(
    private val uri: Uri,
    private val modeFlags: Int,
    private val persistedTime: Long,
) {
    fun getUri(): Uri = uri
    fun isReadPermission(): Boolean = modeFlags and Intent.FLAG_GRANT_READ_URI_PERMISSION != 0
    fun isWritePermission(): Boolean = modeFlags and Intent.FLAG_GRANT_WRITE_URI_PERMISSION != 0
    fun getPersistedTime(): Long = persistedTime
    override fun toString(): String = "UriPermission {uri=$uri, modeFlags=$modeFlags}"
}

/** android.content.ContentProviderOperation 轻实现。 */
class ContentProviderOperation private constructor(
    val uri: Uri?,
    private val type: Int,
) {
    fun isInsert(): Boolean = type == 1
    fun isDelete(): Boolean = type == 2
    fun isUpdate(): Boolean = type == 3
    fun isAssertQuery(): Boolean = type == 4

    class Builder internal constructor(val uri: Uri, val type: Int) {
        fun build(): ContentProviderOperation = ContentProviderOperation(uri, type)
        fun withValue(key: String, value: Any?): Builder = this
        fun withValues(values: ContentValues?): Builder = this
        fun withValueBackReferences(values: ContentValues?): Builder = this
        fun withSelection(selection: String?, selectionArgs: Array<String>?): Builder = this
        fun withExpectedCount(count: Int): Builder = this
        fun withYieldAllowed(yieldAllowed: Boolean): Builder = this
    }

    companion object {
        @JvmStatic fun newInsert(uri: Uri): Builder = Builder(uri, 1)
        @JvmStatic fun newUpdate(uri: Uri): Builder = Builder(uri, 3)
        @JvmStatic fun newDelete(uri: Uri): Builder = Builder(uri, 2)
        @JvmStatic fun newAssertQuery(uri: Uri): Builder = Builder(uri, 4)
    }
}
