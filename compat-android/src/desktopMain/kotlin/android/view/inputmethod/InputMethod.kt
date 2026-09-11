package android.view.inputmethod

import android.os.IBinder
import android.view.KeyEvent
import android.view.View

/**
 * android.view.inputmethod 输入法体系（P3-B2 新增，编译级 stub）。
 * 桌面 IME 走 Compose 文本输入通道；这里仅保证 app 代码编译。
 */

/** android.view.inputmethod.InputMethodManager。 */
open class InputMethodManager {

    open fun showSoftInput(view: View?, flags: Int): Boolean = false
    open fun hideSoftInputFromWindow(windowToken: IBinder?, flags: Int): Boolean = false
    open fun hideSoftInputFromWindow(windowToken: IBinder?, flags: Int, resultReceiver: Any?): Boolean = false
    open fun toggleSoftInput(showFlags: Int, hideFlags: Int) {}
    open fun toggleSoftInputFromWindow(windowToken: IBinder?, showFlags: Int, hideFlags: Int) {}
    open fun restartInput(view: View?) {}
    open fun updateSelection(view: View?, selStart: Int, selEnd: Int, candidatesStart: Int, candidatesEnd: Int) {}
    open fun viewClicked(view: View?) {}
    open fun isWatchingCursor(view: View?): Boolean = false
    open fun isAcceptingText(): Boolean = false
    open fun isActive(view: View?): Boolean = false
    open fun isActive(): Boolean = false
    open fun showStatusIcon(imeToken: IBinder?, packageName: String?, iconId: Int) {}
    open fun hideStatusIcon(imeToken: IBinder?) {}
    open fun shouldOfferSwitchingToNextInputMethod(imeToken: IBinder?): Boolean = false

    companion object {
        const val SHOW_IMPLICIT = 0x0001
        const val SHOW_FORCED = 0x0002
        const val HIDE_IMPLICIT_ONLY = 0x0001
        const val HIDE_NOT_ALWAYS = 0x0002
        const val RESULT_UNCHANGED_SHOWN = 0
        const val RESULT_UNCHANGED_HIDDEN = 1
        const val RESULT_SHOWN = 2
        const val RESULT_HIDDEN = 3
    }
}

/** android.view.inputmethod.EditorInfo。 */
open class EditorInfo {
    var inputType: Int = 0
    var imeOptions: Int = 0
    var privateImeOptions: String? = null
    var actionId: Int = 0
    var actionLabel: CharSequence? = null
    var initialSelStart: Int = -1
    var initialSelEnd: Int = -1
    var initialCapsMode: Int = 0
    var hintText: CharSequence? = null
    var label: CharSequence? = null
    var packageName: String? = null
    var fieldId: Int = 0
    var fieldName: String? = null
    var extras: android.os.Bundle? = null
    var hintLocales: Any? = null
    var contentMimeTypes: Array<String>? = null

    companion object {
        const val IME_ACTION_NONE = 0
        const val IME_ACTION_GO = 1
        const val IME_ACTION_SEARCH = 2
        const val IME_ACTION_SEND = 3
        const val IME_ACTION_NEXT = 4
        const val IME_ACTION_DONE = 5
        const val IME_ACTION_PREVIOUS = 7
        const val IME_ACTION_UNSPECIFIED = 0
        const val IME_FLAG_NO_EXTRACT_UI = 0x10000000
        const val IME_FLAG_NO_FULLSCREEN = 0x02000000
        const val IME_FLAG_NAVIGATE_PREVIOUS = 0x00000004
        const val IME_FLAG_NAVIGATE_NEXT = 0x00000002
        const val IME_FLAG_NO_PERSONALIZED_LEARNING = 0x01000000
        const val IME_NULL = 0x00000000
        // TYPE_* 输入类型常量
        const val TYPE_CLASS_TEXT = 0x00000001
        const val TYPE_TEXT_FLAG_MULTI_LINE = 0x00020000
        const val TYPE_CLASS_NUMBER = 0x00000002
        const val TYPE_CLASS_PHONE = 0x00000003
        const val TYPE_CLASS_DATETIME = 0x00000004
        const val TYPE_NULL = 0x00000000
    }
}

/** android.view.inputmethod.ExtractedTextRequest。 */
open class ExtractedTextRequest {
    var token: Int = 0
    var flags: Int = 0
    var hintMaxLines: Int = 0
    var hintMaxChars: Int = 0
}

/** android.view.inputmethod.ExtractedText。 */
open class ExtractedText {
    var text: CharSequence? = null
    var startOffset: Int = 0
    var partialStartOffset: Int = 0
    var partialEndOffset: Int = 0
    var selectionStart: Int = 0
    var selectionEnd: Int = 0
    var flags: Int = 0
    var hint: CharSequence? = null

    companion object {
        const val FLAG_SINGLE_LINE = 0x0001
        const val FLAG_SELECTING = 0x0002
    }
}

/** android.view.inputmethod.InputConnection。 */
interface InputConnection {
    fun getTextBeforeCursor(n: Int, flags: Int): CharSequence? = null
    fun getTextAfterCursor(n: Int, flags: Int): CharSequence? = null
    fun getSelectedText(flags: Int): CharSequence? = null
    fun getCursorCapsMode(reqModes: Int): Int = 0
    fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean = false
    fun setComposingText(text: CharSequence?, newCursorPosition: Int): Boolean = false
    fun setComposingRegion(start: Int, end: Int): Boolean = false
    fun finishComposingText(): Boolean = false
    fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean = false
    fun setSelection(start: Int, end: Int): Boolean = false
    fun sendKeyEvent(event: KeyEvent?): Boolean = false
    fun performEditorAction(actionCode: Int): Boolean = false
    fun performContextMenuAction(id: Int): Boolean = false
    fun beginBatchEdit(): Boolean = false
    fun endBatchEdit(): Boolean = false
    fun clearMetaKeyStates(states: Int): Boolean = false
    fun reportFullscreenMode(enabled: Boolean): Boolean = false
    fun requestCursorUpdates(cursorUpdateMode: Int): Boolean = false

    companion object {
        const val GET_TEXT_WITH_STYLES = 0x0001
        const val GET_EXTRACTED_TEXT_MONITOR = 0x0001
        const val CURSOR_UPDATE_IMMEDIATE = 1
    }
}

/** android.view.inputmethod.BaseInputConnection。 */
open class BaseInputConnection(
    private val targetView: View?,
    private val fullEditor: Boolean,
) : InputConnection
