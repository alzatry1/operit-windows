package android.text.method

import android.text.Spannable
import android.text.Spanned
import android.view.View

/** android.text.method.TransformationMethod。 */
interface TransformationMethod {
    fun getTransformation(source: CharSequence?, view: View?): CharSequence?
    fun onFocusChanged(
        view: View?, sourceText: CharSequence?,
        focused: Boolean, direction: Int, previouslyFocusedRect: android.graphics.Rect?,
    )
}

/** android.text.method.LinkMovementMethod：轻 stub。 */
open class LinkMovementMethod : ScrollingMovementMethod() {
    companion object {
        private val instance = LinkMovementMethod()

        @JvmStatic
        fun getInstance(): LinkMovementMethod = instance
    }
}

/** android.text.method.ScrollingMovementMethod。 */
open class ScrollingMovementMethod : MovementMethod {
    override fun initialize(widget: Any?, text: Spannable?) {}
    override fun onKeyDown(widget: Any?, text: Spannable?, keyCode: Int, event: Any?): Boolean = false
    override fun onKeyUp(widget: Any?, text: Spannable?, keyCode: Int, event: Any?): Boolean = false
    override fun onTrackballEvent(widget: Any?, text: Spannable?, event: Any?): Boolean = false
    override fun onTouchEvent(widget: Any?, text: Spannable?, event: Any?): Boolean = false
    override fun onGenericMotionEvent(widget: Any?, text: Spannable?, event: Any?): Boolean = false
    override fun canSelectArbitrarily(): Boolean = false

    companion object {
        private val instance = ScrollingMovementMethod()

        @JvmStatic
        fun getInstance(): MovementMethod = instance
    }
}

/** android.text.method.MovementMethod。 */
interface MovementMethod {
    fun initialize(widget: Any?, text: Spannable?)
    fun onKeyDown(widget: Any?, text: Spannable?, keyCode: Int, event: Any?): Boolean
    fun onKeyUp(widget: Any?, text: Spannable?, keyCode: Int, event: Any?): Boolean
    fun onTrackballEvent(widget: Any?, text: Spannable?, event: Any?): Boolean
    fun onTouchEvent(widget: Any?, text: Spannable?, event: Any?): Boolean
    fun onGenericMotionEvent(widget: Any?, text: Spannable?, event: Any?): Boolean
    fun canSelectArbitrarily(): Boolean
}

/** android.text.method.PasswordTransformationMethod。 */
open class PasswordTransformationMethod : TransformationMethod {
    private val dotChar = '•'

    override fun getTransformation(source: CharSequence?, view: View?): CharSequence? =
        source?.let { dotChar.toString().repeat(it.length) }

    override fun onFocusChanged(
        view: View?, sourceText: CharSequence?,
        focused: Boolean, direction: Int, previouslyFocusedRect: android.graphics.Rect?,
    ) {}

    companion object {
        private val instance = PasswordTransformationMethod()

        @JvmStatic
        fun getInstance(): PasswordTransformationMethod = instance
    }
}

/** android.text.method.SingleLineTransformationMethod。 */
open class SingleLineTransformationMethod : TransformationMethod {
    override fun getTransformation(source: CharSequence?, view: View?): CharSequence? =
        source?.toString()?.replace("\n", " ")

    override fun onFocusChanged(
        view: View?, sourceText: CharSequence?,
        focused: Boolean, direction: Int, previouslyFocusedRect: android.graphics.Rect?,
    ) {}

    companion object {
        private val instance = SingleLineTransformationMethod()

        @JvmStatic
        fun getInstance(): SingleLineTransformationMethod = instance
    }
}

/** android.text.method.KeyListener。 */
interface KeyListener {
    fun getInputType(): Int
    fun onKeyDown(view: View?, content: android.text.Editable?, keyCode: Int, event: android.view.KeyEvent?): Boolean
    fun onKeyUp(view: View?, content: android.text.Editable?, keyCode: Int, event: android.view.KeyEvent?): Boolean
    fun onKeyOther(view: View?, content: android.text.Editable?, event: android.view.KeyEvent?): Boolean
    fun clearMetaKeyState(view: View?, content: android.text.Editable?, states: Int)
}
