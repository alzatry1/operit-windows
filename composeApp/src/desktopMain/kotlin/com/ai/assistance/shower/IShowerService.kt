package com.ai.assistance.shower

import android.os.Binder
import android.os.IBinder
import android.os.IInterface

/**
 * IShowerService AIDL 的桌面 Kotlin 移植（P3-B4）。
 * 桌面无跨进程 Binder IPC：Stub.asInterface 经 queryLocalInterface 进程内直连；
 * 无本地实现时返回 null（调用方均已判空）。TRANSACTION_* 常量保持与 AIDL 一致。
 */
interface IShowerService : IInterface {

    fun ensureDisplay(width: Int, height: Int, dpi: Int, bitrateKbps: Int): Int
    fun destroyDisplay(displayId: Int)
    fun launchApp(packageName: String?, displayId: Int)
    fun tap(displayId: Int, x: Float, y: Float)
    fun swipe(displayId: Int, x1: Float, y1: Float, x2: Float, y2: Float, durationMs: Long)
    fun touchDown(displayId: Int, x: Float, y: Float)
    fun touchMove(displayId: Int, x: Float, y: Float)
    fun touchUp(displayId: Int, x: Float, y: Float)
    fun injectTouchEvent(
        displayId: Int,
        action: Int,
        x: Float,
        y: Float,
        downTime: Long,
        eventTime: Long,
        pressure: Float,
        size: Float,
        metaState: Int,
        xPrecision: Float,
        yPrecision: Float,
        deviceId: Int,
        edgeFlags: Int
    )
    fun injectKey(displayId: Int, keyCode: Int)
    fun injectKeyWithMeta(displayId: Int, keyCode: Int, metaState: Int)
    fun requestScreenshot(displayId: Int): ByteArray?
    fun setVideoSink(displayId: Int, sink: IBinder?)

    abstract class Stub : Binder(), IShowerService {

        init {
            attachInterface(this, DESCRIPTOR)
        }

        override fun asBinder(): IBinder = this

        companion object {
            private const val DESCRIPTOR = "com.ai.assistance.shower.IShowerService"

            const val TRANSACTION_ensureDisplay = IBinder.FIRST_CALL_TRANSACTION
            const val TRANSACTION_destroyDisplay = IBinder.FIRST_CALL_TRANSACTION + 1
            const val TRANSACTION_launchApp = IBinder.FIRST_CALL_TRANSACTION + 2
            const val TRANSACTION_tap = IBinder.FIRST_CALL_TRANSACTION + 3
            const val TRANSACTION_swipe = IBinder.FIRST_CALL_TRANSACTION + 4
            const val TRANSACTION_touchDown = IBinder.FIRST_CALL_TRANSACTION + 5
            const val TRANSACTION_touchMove = IBinder.FIRST_CALL_TRANSACTION + 6
            const val TRANSACTION_touchUp = IBinder.FIRST_CALL_TRANSACTION + 7
            const val TRANSACTION_injectKey = IBinder.FIRST_CALL_TRANSACTION + 8
            const val TRANSACTION_requestScreenshot = IBinder.FIRST_CALL_TRANSACTION + 9
            const val TRANSACTION_injectKeyWithMeta = IBinder.FIRST_CALL_TRANSACTION + 10
            const val TRANSACTION_setVideoSink = IBinder.FIRST_CALL_TRANSACTION + 11
            const val TRANSACTION_injectTouchEvent = IBinder.FIRST_CALL_TRANSACTION + 12

            @JvmStatic
            fun asInterface(obj: IBinder?): IShowerService? {
                if (obj == null) return null
                return obj.queryLocalInterface(DESCRIPTOR) as? IShowerService
            }
        }
    }
}
