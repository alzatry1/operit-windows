package com.ai.assistance.operit.provider

import android.os.IBinder

/**
 * IAccessibilityProvider 桌面垫片（对应 android-src 的 IAccessibilityProvider.aidl）。
 * Android 上由 AIDL 生成；桌面无障碍走 UIA/桌面自动化通道，这里提供编译形状 + 安全默认实现。
 * ——Nova 注
 */
interface IAccessibilityProvider : android.os.IInterface {
    fun getUiHierarchy(): String?
    fun performClick(x: Int, y: Int): Boolean
    fun performLongPress(x: Int, y: Int): Boolean
    fun performGlobalAction(actionId: Int): Boolean
    fun performSwipe(startX: Int, startY: Int, endX: Int, endY: Int, duration: Long): Boolean
    fun findFocusedNodeId(): String?
    fun setTextOnNode(nodeId: String?, text: String?): Boolean
    fun takeScreenshot(path: String?, format: String?): Boolean
    fun isAccessibilityServiceEnabled(): Boolean
    fun getCurrentActivityName(): String?

    /** AIDL Stub：asInterface 把 IBinder 转成接口代理。桌面返回空实现。 */
    abstract class Stub : android.os.Binder(), IAccessibilityProvider {
        override fun getUiHierarchy(): String? = null
        override fun performClick(x: Int, y: Int): Boolean = false
        override fun performLongPress(x: Int, y: Int): Boolean = false
        override fun performGlobalAction(actionId: Int): Boolean = false
        override fun performSwipe(startX: Int, startY: Int, endX: Int, endY: Int, duration: Long): Boolean = false
        override fun findFocusedNodeId(): String? = null
        override fun setTextOnNode(nodeId: String?, text: String?): Boolean = false
        override fun takeScreenshot(path: String?, format: String?): Boolean = false
        override fun isAccessibilityServiceEnabled(): Boolean = false
        override fun getCurrentActivityName(): String? = null
        override fun asBinder(): IBinder = this

        companion object {
            @JvmStatic
            fun asInterface(binder: IBinder?): IAccessibilityProvider? {
                if (binder == null) return null
                if (binder is IAccessibilityProvider) return binder
                return object : Stub() {}
            }
        }
    }
}
