package android.view

import android.content.Context
import android.util.Log

/**
 * android.view.WindowManager 桌面 stub。
 * 真实 AOSP 为 interface；桌面无窗口系统桥接，做成 open class 便于直接实例化。
 */
open class WindowManager {

    open class LayoutParams {
        var width: Int = WRAP_CONTENT
        var height: Int = WRAP_CONTENT
        var x: Int = 0
        var y: Int = 0
        var type: Int = 0
        var flags: Int = 0
        /** setBlurBehindRadius（悬浮窗毛玻璃模糊半径）。——Nova 注 */
        open fun setBlurBehindRadius(blurBehindRadius: Int) {}
        open fun getBlurBehindRadius(): Int = 0
        var format: Int = android.graphics.PixelFormat.OPAQUE
        var gravity: Int = Gravity.NO_GRAVITY
        var softInputMode: Int = 0
        var layoutInDisplayCutoutMode: Int = 0
        var windowAnimations: Int = 0
        var alpha: Float = 1.0f
        var dimAmount: Float = 0f
        var screenBrightness: Float = BRIGHTNESS_OVERRIDE_NONE
        var buttonBrightness: Float = BRIGHTNESS_OVERRIDE_NONE
        var rotationAnimation: Int = ROTATION_ANIMATION_ROTATE
        var token: android.os.IBinder? = null
        var packageName: String? = null
        var title: CharSequence? = null
        var horizontalMargin: Float = 0f
        var verticalMargin: Float = 0f

        constructor()

        constructor(width: Int, height: Int) {
            this.width = width
            this.height = height
        }

        constructor(width: Int, height: Int, type: Int, flags: Int, format: Int) {
            this.width = width
            this.height = height
            this.type = type
            this.flags = flags
            this.format = format
        }

        constructor(width: Int, height: Int, x: Int, y: Int, type: Int, flags: Int, format: Int) {
            this.width = width
            this.height = height
            this.x = x
            this.y = y
            this.type = type
            this.flags = flags
            this.format = format
        }

        fun copyFrom(o: LayoutParams): Int {
            width = o.width; height = o.height; x = o.x; y = o.y
            type = o.type; flags = o.flags; format = o.format
            gravity = o.gravity; softInputMode = o.softInputMode
            return 0
        }

        companion object {
            const val MATCH_PARENT = -1
            const val WRAP_CONTENT = -2
            const val BRIGHTNESS_OVERRIDE_NONE = -1.0f
            const val BRIGHTNESS_OVERRIDE_OFF = 0.0f
            const val BRIGHTNESS_OVERRIDE_FULL = 1.0f
            const val ROTATION_ANIMATION_ROTATE = 0
            const val ROTATION_ANIMATION_CROSSFADE = 1
            const val ROTATION_ANIMATION_JUMPCUT = 2
            const val ROTATION_ANIMATION_SEAMLESS = 3

            const val FLAG_ALLOW_LOCK_WHILE_SCREEN_ON = 0x00000001
            const val FLAG_DIM_BEHIND = 0x00000002
            const val FLAG_BLUR_BEHIND = 0x00000004
            const val FLAG_NOT_FOCUSABLE = 0x00000008
            const val FLAG_NOT_TOUCHABLE = 0x00000010
            const val FLAG_NOT_TOUCH_MODAL = 0x00000020
            const val FLAG_TOUCHABLE_WHEN_WAKING = 0x00000040
            const val FLAG_KEEP_SCREEN_ON = 0x00000080
            const val FLAG_LAYOUT_IN_SCREEN = 0x00000100
            const val FLAG_LAYOUT_NO_LIMITS = 0x00000200
            const val FLAG_FULLSCREEN = 0x00000400
            const val FLAG_FORCE_NOT_FULLSCREEN = 0x00000800
            const val FLAG_DITHER = 0x00001000
            const val FLAG_SECURE = 0x00002000
            const val FLAG_SCALED = 0x00004000
            const val FLAG_IGNORE_CHEEK_PRESSES = 0x00008000
            const val FLAG_LAYOUT_INSET_DECOR = 0x00010000
            const val FLAG_ALT_FOCUSABLE_IM = 0x00020000
            const val FLAG_WATCH_OUTSIDE_TOUCH = 0x00040000
            const val FLAG_SHOW_WHEN_LOCKED = 0x00080000
            const val FLAG_SHOW_WALLPAPER = 0x00100000
            const val FLAG_TURN_SCREEN_ON = 0x00200000
            const val FLAG_DISMISS_KEYGUARD = 0x00400000
            const val FLAG_SPLIT_TOUCH = 0x00800000
            const val FLAG_HARDWARE_ACCELERATED = 0x01000000
            const val FLAG_LAYOUT_IN_OVERSCAN = 0x02000000
            const val FLAG_TRANSLUCENT_STATUS = 0x04000000
            const val FLAG_TRANSLUCENT_NAVIGATION = 0x08000000
            const val FLAG_LOCAL_FOCUS_MODE = 0x10000000
            const val FLAG_SLIPPERY = 0x20000000
            const val FLAG_LAYOUT_ATTACHED_IN_DECOR = 0x40000000
            const val FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = 0x80000000.toInt()

            const val SOFT_INPUT_STATE_UNSPECIFIED = 0
            const val SOFT_INPUT_STATE_UNCHANGED = 1
            const val SOFT_INPUT_STATE_HIDDEN = 2
            const val SOFT_INPUT_STATE_ALWAYS_HIDDEN = 3
            const val SOFT_INPUT_STATE_VISIBLE = 4
            const val SOFT_INPUT_STATE_ALWAYS_VISIBLE = 5
            const val SOFT_INPUT_ADJUST_UNSPECIFIED = 0x00
            const val SOFT_INPUT_ADJUST_RESIZE = 0x10
            const val SOFT_INPUT_ADJUST_PAN = 0x20
            const val SOFT_INPUT_ADJUST_NOTHING = 0x30

            const val TYPE_APPLICATION = 2
            const val TYPE_APPLICATION_STARTING = 3
            const val TYPE_APPLICATION_PANEL = 1000
            const val TYPE_APPLICATION_MEDIA = 1001
            const val TYPE_APPLICATION_SUB_PANEL = 1002
            const val TYPE_APPLICATION_ATTACHED_DIALOG = 1003
            const val TYPE_TOAST = 2005
            const val TYPE_SYSTEM_ALERT = 2003
            const val TYPE_SYSTEM_DIALOG = 2008
            const val TYPE_STATUS_BAR = 2000
            const val TYPE_INPUT_METHOD = 2011
            const val TYPE_WALLPAPER = 2013
            const val TYPE_APPLICATION_OVERLAY = 2038
            const val TYPE_SYSTEM_ERROR = 2010
            const val TYPE_PHONE = 2002
            const val TYPE_PRIORITY_PHONE = 2007
            const val TYPE_SYSTEM_OVERLAY = 2006

            const val LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT = 0
            const val LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES = 1
            const val LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER = 2
            const val LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS = 3
        }
    }

    open class BadTokenException : RuntimeException {
        constructor() : super()
        constructor(name: String?) : super(name)
    }

    open val defaultDisplay: Display get() = com.ai.assistance.operit.compat.AppGlobals.display
    open val currentWindowMetrics: Any? get() = null

    open fun addView(view: View?, params: LayoutParams?) {
        Log.d("WindowManager", "addView no-op")
    }

    open fun updateViewLayout(view: View?, params: LayoutParams?) {}

    open fun removeView(view: View?) {}

    open fun removeViewImmediate(view: View?) {}
}

/**
 * android.view.Window 桌面 stub（B1b-1 新建）。
 * 记录属性状态；无真实窗口系统桥接。
 */
open class Window(open val context: Context) {

    open var statusBarColor: Int = 0
    open var navigationBarColor: Int = 0
    open var navigationBarDividerColor: Int = 0
    open var isStatusBarContrastEnforced: Boolean = false
    open var isNavigationBarContrastEnforced: Boolean = true

    open var attributes: WindowManager.LayoutParams = WindowManager.LayoutParams()

    open val windowManager: WindowManager = WindowManager()

    open val decorView: View by lazy { View(context) }

    open var callback: Callback? = null

    /** decorFitsSystemWindows 状态由 WindowCompat 写入。 */
    internal open var decorFitsSystemWindows: Boolean = true

    open fun setFlags(flags: Int, mask: Int) {
        attributes.flags = (attributes.flags and mask.inv()) or (flags and mask)
    }

    open fun addFlags(flags: Int) = setFlags(flags, flags)
    open fun clearFlags(flags: Int) = setFlags(0, flags)
    open fun hasFlags(flags: Int): Boolean = attributes.flags and flags == flags

    open fun setSoftInputMode(mode: Int) {
        attributes.softInputMode = mode
    }

    open fun setType(type: Int) {
        attributes.type = type
    }

    open fun setGravity(gravity: Int) {
        attributes.gravity = gravity
    }

    open fun setLayout(width: Int, height: Int) {
        attributes.width = width
        attributes.height = height
    }

    open fun setDimAmount(amount: Float) {
        attributes.dimAmount = amount
    }

    open fun setTitle(title: CharSequence?) {}
    open fun setContentView(layoutResID: Int) {}
    open fun setContentView(view: View?) {}
    open fun setFeatureInt(featureId: Int, value: Int) {}
    open fun setWindowManager(wm: WindowManager?) {}
    open fun requestFeature(featureId: Int): Boolean = true
    open fun hasFeature(feature: Int): Boolean = true
    open fun setUiOptions(uiOptions: Int) {}
    open fun setUiOptions(uiOptions: Int, mask: Int) {}
    open fun setIcon(resId: Int) {}
    open fun setDefaultIcon(resId: Int) {}
    open fun setLogo(resId: Int) {}
    open fun setBackgroundDrawable(drawable: android.graphics.drawable.Drawable?) {}
    open fun setBackgroundDrawableResource(resid: Int) {}
    open fun setWindowAnimations(resId: Int) {}
    open fun setAllowSystemUiVisibility(flags: Int) {}
    // attributes 属性已提供 setter，不再单独定义 setAttributes 方法

    open fun peekDecorView(): View? = decorView
    open fun isFloating(): Boolean = false
    open fun isShortcutKey(keyCode: Int, event: KeyEvent?): Boolean = false
    open fun superDispatchKeyEvent(event: KeyEvent?): Boolean = false

    /** Window.Callback 最小接口。 */
    interface Callback {
        fun dispatchKeyEvent(event: KeyEvent): Boolean = false
        fun onWindowFocusChanged(hasFocus: Boolean) {}
        fun onAttachedToWindow() {}
        fun onDetachedFromWindow() {}
        fun onContentChanged() {}
    }

    companion object {
        const val FEATURE_OPTIONS_PANEL = 0
        const val FEATURE_NO_TITLE = 7
        const val FEATURE_PROGRESS = 2
        const val FEATURE_LEFT_ICON = 3
        const val FEATURE_RIGHT_ICON = 4
        const val FEATURE_INDETERMINATE_PROGRESS = 5
        const val FEATURE_CONTEXT_MENU = 6
        const val FEATURE_CUSTOM_TITLE = 7
        const val FEATURE_ACTION_BAR = 8
        const val FEATURE_ACTION_BAR_OVERLAY = 9
        const val FEATURE_ACTION_MODE_OVERLAY = 10
        const val FEATURE_SWIPE_TO_DISMISS = 11
        const val FEATURE_CONTENT_TRANSITIONS = 12
        const val FEATURE_ACTIVITY_TRANSITIONS = 13

        const val ID_ANDROID_CONTENT = 16908290

        const val PROGRESS_VISIBILITY_ON = -1
        const val PROGRESS_VISIBILITY_OFF = -2
        const val PROGRESS_INDETERMINATE_ON = -3
        const val PROGRESS_INDETERMINATE_OFF = -4
        const val PROGRESS_START = 0
        const val PROGRESS_END = 10000
        const val PROGRESS_SECONDARY_START = 20000
        const val PROGRESS_SECONDARY_END = 30000

        @Deprecated("deprecated") const val DEFAULT_FEATURES = 65
    }
}
