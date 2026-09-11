package android.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet

/**
 * android.view Surface 体系（P3-B2 新增，编译级 stub）。
 * 桌面无 SurfaceFlinger；lockCanvas 返回 null，isValid 恒 true。
 */

/** android.view.Surface。 */
open class Surface {
    open fun isValid(): Boolean = true
    open fun lockCanvas(inOutDirty: Rect?): Canvas? = null
    open fun lockHardwareCanvas(): Canvas? = null
    open fun unlockCanvasAndPost(canvas: Canvas?) {}
    open fun unlockCanvas(canvas: Canvas?) {}
    open fun release() {}
    open fun destroy() {}
    override fun toString(): String = "Surface(compat)"
}

/** android.view.SurfaceHolder。 */
interface SurfaceHolder {
    fun addCallback(callback: Callback)
    fun removeCallback(callback: Callback)
    fun lockCanvas(): Canvas?
    fun lockCanvas(dirty: Rect?): Canvas?
    fun unlockCanvasAndPost(canvas: Canvas?)
    fun setFormat(format: Int)
    fun setFixedSize(width: Int, height: Int)
    fun setSizeFromLayout() {}
    fun setKeepScreenOn(screenOn: Boolean) {}
    fun setType(type: Int) {}
    val surface: Surface
    val surfaceFrame: Rect
    val isCreating: Boolean get() = false

    interface Callback {
        fun surfaceCreated(holder: SurfaceHolder)
        fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int)
        fun surfaceDestroyed(holder: SurfaceHolder)
    }

    interface Callback2 : Callback {
        fun surfaceRedrawNeeded(holder: SurfaceHolder) {}
    }

    companion object {
        const val SURFACE_TYPE_NORMAL = 0
        const val SURFACE_TYPE_HARDWARE = 1
        const val SURFACE_TYPE_GPU = 2
        const val SURFACE_TYPE_PUSH_BUFFERS = 3
    }
}

/** android.view.SurfaceView。 */
open class SurfaceView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val surfaceHolder = object : SurfaceHolder {
        private val surf = Surface()
        private val frame = Rect()
        override fun addCallback(callback: SurfaceHolder.Callback) {}
        override fun removeCallback(callback: SurfaceHolder.Callback) {}
        override fun lockCanvas(): Canvas? = null
        override fun lockCanvas(dirty: Rect?): Canvas? = null
        override fun unlockCanvasAndPost(canvas: Canvas?) {}
        override fun setFormat(format: Int) {}
        override fun setFixedSize(width: Int, height: Int) {}
        override val surface: Surface get() = surf
        override val surfaceFrame: Rect get() = frame
    }

    open val holder: SurfaceHolder get() = surfaceHolder

    open fun setZOrderOnTop(onTop: Boolean) {}
    open fun setZOrderMediaOverlay(isMediaOverlay: Boolean) {}
    open fun setZOrderOnBottom(onBottom: Boolean) {}
    open fun setSecure(isSecure: Boolean) {}
    open fun setVisible(visible: Boolean) {}
}
