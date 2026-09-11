package rikka.shizuku

import android.os.IBinder

/**
 * rikka.shizuku.Shizuku 垫片（P3-B3，编译形状）。
 * Shizuku 是 Android binder 特权服务，桌面不存在：全部成员为安全空实现
 * （pingBinder=false、checkSelfPermission=PERMISSION_DENIED、监听器注册即弃）。
 */
object Shizuku {

    fun interface OnBinderReceivedListener {
        fun onBinderReceived()
    }

    fun interface OnBinderDeadListener {
        fun onBinderDead()
    }

    fun interface OnRequestPermissionResultListener {
        fun onRequestPermissionResult(requestCode: Int, grantResult: Int)
    }

    @JvmStatic
    fun pingBinder(): Boolean = false

    @JvmStatic
    fun getBinder(): IBinder? = null

    @JvmStatic
    fun getUid(): Int = -1

    @JvmStatic
    fun getVersion(): Int = -1

    @JvmStatic
    fun checkSelfPermission(): Int = -1 // PackageManager.PERMISSION_DENIED

    @JvmStatic
    fun requestPermission(requestCode: Int) {}

    @JvmStatic
    fun shouldShowRequestPermissionRationale(): Boolean = false

    @JvmStatic
    fun addRequestPermissionResultListener(listener: OnRequestPermissionResultListener) {}

    @JvmStatic
    fun removeRequestPermissionResultListener(listener: OnRequestPermissionResultListener) {}

    @JvmStatic
    fun addBinderReceivedListener(listener: OnBinderReceivedListener) {}

    @JvmStatic
    fun addBinderReceivedListenerSticky(listener: OnBinderReceivedListener) {}

    @JvmStatic
    fun removeBinderReceivedListener(listener: OnBinderReceivedListener) {}

    @JvmStatic
    fun addBinderDeadListener(listener: OnBinderDeadListener) {}

    @JvmStatic
    fun removeBinderDeadListener(listener: OnBinderDeadListener) {}
}
