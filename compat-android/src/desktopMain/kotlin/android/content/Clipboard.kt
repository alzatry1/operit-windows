package android.content

import android.net.Uri
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

/** android.content.ClipDescription。 */
class ClipDescription {
    private val label: CharSequence?
    private val mimeTypes: Array<String>

    constructor(label: CharSequence?, mimeTypes: Array<String>) {
        this.label = label; this.mimeTypes = mimeTypes
    }

    constructor(old: ClipDescription) {
        this.label = old.label; this.mimeTypes = old.mimeTypes.copyOf()
    }

    fun getLabel(): CharSequence? = label
    fun getMimeTypeCount(): Int = mimeTypes.size
    fun getMimeType(index: Int): String = mimeTypes[index]
    fun hasMimeType(mimeType: String?): Boolean = mimeTypes.any { compareMimeTypes(it, mimeType) }
    fun filterMimeTypes(mimeType: String): Array<String>? = mimeTypes.filter { compareMimeTypes(it, mimeType) }.toTypedArray()
    override fun toString(): String = "ClipDescription { $label $mimeTypes }"

    companion object {
        const val MIMETYPE_TEXT_PLAIN = "text/plain"
        const val MIMETYPE_TEXT_HTML = "text/html"
        const val MIMETYPE_TEXT_URILIST = "text/uri-list"
        const val MIMETYPE_TEXT_INTENT = "text/vnd.android.intent"
        const val EXTRA_TARGET_COMPONENT_NAME = "android.content.extra.TARGET_COMPONENT_NAME"
        const val EXTRA_USER_SERIAL = "android.content.extra.USER_SERIAL"

        @JvmStatic
        fun compareMimeTypes(concrete: String, desired: String?): Boolean {
            if (desired == null) return false
            if (desired == "*/*") return true
            if (concrete == desired) return true
            val slash = desired.indexOf('/')
            if (slash > 0 && desired.endsWith("/*")) {
                return concrete.startsWith(desired.substring(0, slash + 1))
            }
            return false
        }
    }
}

/** android.content.ClipData。 */
class ClipData : android.os.Parcelable {
    private val description: ClipDescription
    private val items = ArrayList<Item>()

    constructor(description: ClipDescription, item: Item) {
        this.description = description; items.add(item)
    }

    constructor(description: CharSequence?, mimeTypes: Array<String>, item: Item) {
        this.description = ClipDescription(description, mimeTypes); items.add(item)
    }

    constructor(old: ClipData) {
        this.description = ClipDescription(old.description)
        this.items.addAll(old.items.map { Item(it) })
    }

    fun getDescription(): ClipDescription = description
    fun addItem(item: Item) { items.add(item) }
    fun addItem(resolver: ContentResolver, item: Item) { items.add(item) }
    val itemCount: Int get() = items.size
    fun getItemAt(index: Int): Item = items[index]
    fun clearItems() { items.clear() }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {}
    override fun toString(): String = "ClipData { ${description} items=${items.size} }"

    /** ClipData.Item。 */
    class Item {
        val text: CharSequence?
        val htmlText: String?
        val intent: Intent?
        val uri: Uri?

        constructor(text: CharSequence?) { this.text = text; htmlText = null; intent = null; uri = null }
        constructor(text: CharSequence?, htmlText: String?) { this.text = text; this.htmlText = htmlText; intent = null; uri = null }
        constructor(intent: Intent) { text = null; htmlText = null; this.intent = intent; uri = null }
        constructor(uri: Uri) { text = null; htmlText = null; intent = null; this.uri = uri }
        constructor(text: CharSequence?, intent: Intent, uri: Uri) {
            this.text = text; htmlText = null; this.intent = intent; this.uri = uri
        }
        constructor(old: Item) { text = old.text; htmlText = old.htmlText; intent = old.intent; uri = old.uri }

        fun coerceToText(context: Context): CharSequence {
            text?.let { return it }
            uri?.let { return it.toString() }
            intent?.let { return it.toUri(Intent.URI_INTENT_SCHEME) }
            return ""
        }

        fun coerceToHtmlText(context: Context): String = htmlText ?: coerceToText(context).toString()

        override fun toString(): String = "ClipData.Item { text=$text, uri=$uri, intent=$intent }"
    }

    companion object {
        @JvmStatic
        fun newPlainText(label: CharSequence?, text: CharSequence?): ClipData =
            ClipData(label, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), Item(text))

        @JvmStatic
        fun newHtmlText(label: CharSequence?, text: CharSequence?, htmlText: String?): ClipData =
            ClipData(label, arrayOf(ClipDescription.MIMETYPE_TEXT_HTML), Item(text, htmlText))

        @JvmStatic
        fun newIntent(label: CharSequence?, intent: Intent): ClipData =
            ClipData(label, arrayOf(ClipDescription.MIMETYPE_TEXT_INTENT), Item(intent))

        @JvmStatic
        fun newUri(resolver: ContentResolver?, label: CharSequence?, uri: Uri): ClipData =
            ClipData(label, arrayOf(ClipDescription.MIMETYPE_TEXT_URILIST), Item(uri))

        @JvmStatic
        fun newRawUri(label: CharSequence?, uri: Uri): ClipData =
            ClipData(label, arrayOf(ClipDescription.MIMETYPE_TEXT_URILIST), Item(uri))
    }
}

/** android.content.ClipboardManager：文本接 AWT 系统剪贴板。 */
open class ClipboardManager(private val context: Context) {

    fun interface OnPrimaryClipChangedListener {
        fun onPrimaryClipChanged()
    }

    private val listeners = CopyOnWriteArrayList<OnPrimaryClipChangedListener>()
    @Volatile private var memoryClip: ClipData? = null

    private val awtClipboard: java.awt.datatransfer.Clipboard? by lazy {
        try { java.awt.Toolkit.getDefaultToolkit().systemClipboard }
        catch (t: Throwable) {
            Log.w("ClipboardManager", "AWT 剪贴板不可用（headless?），退回内存剪贴板")
            null
        }
    }

    open fun setPrimaryClip(clip: ClipData) {
        memoryClip = clip
        val text = clip.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(context)?.toString()
        if (text != null) {
            val cb = awtClipboard
            if (cb != null) {
                try {
                    cb.setContents(java.awt.datatransfer.StringSelection(text), null)
                } catch (t: Throwable) {
                    Log.w("ClipboardManager", "写入系统剪贴板失败: ${t.message}")
                }
            }
        }
        // 非文本（图片等）记日志
        if (clip.itemCount > 0 && clip.getItemAt(0).text == null && clip.getItemAt(0).uri != null) {
            Log.d("ClipboardManager", "setPrimaryClip: 非文本内容（uri），已记录内存副本")
        }
        notifyChanged()
    }

    open fun getPrimaryClip(): ClipData? {
        memoryClip?.let { return it }
        val cb = awtClipboard ?: return null
        return try {
            val text = cb.getData(java.awt.datatransfer.DataFlavor.stringFlavor) as? String
            text?.let { ClipData.newPlainText("text", it) }
        } catch (t: Throwable) {
            null
        }
    }

    open fun getPrimaryClipDescription(): ClipDescription? = getPrimaryClip()?.getDescription()

    open fun hasPrimaryClip(): Boolean = getPrimaryClip() != null

    open fun getText(): CharSequence? =
        getPrimaryClip()?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(context)

    @Deprecated("deprecated", ReplaceWith("setPrimaryClip(ClipData.newPlainText(null, text))"))
    open fun setText(text: CharSequence?) {
        setPrimaryClip(ClipData.newPlainText("text", text ?: ""))
    }

    open fun clearPrimaryClip() {
        memoryClip = null
        try {
            awtClipboard?.setContents(java.awt.datatransfer.StringSelection(""), null)
        } catch (t: Throwable) { /* ignore */ }
        notifyChanged()
    }

    open fun addPrimaryClipChangedListener(listener: OnPrimaryClipChangedListener) {
        if (!listeners.contains(listener)) listeners.add(listener)
    }

    open fun removePrimaryClipChangedListener(listener: OnPrimaryClipChangedListener) {
        listeners.remove(listener)
    }

    private fun notifyChanged() {
        for (l in listeners) {
            try { l.onPrimaryClipChanged() } catch (t: Throwable) { Log.e("ClipboardManager", "listener failed", t) }
        }
    }
}
