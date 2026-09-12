package moe.shizuku.server

import android.os.IBinder

/**
 * moe.shizuku.server AIDL 垫片（P3-B3，编译形状）。
 * 桌面无 Shizuku 服务；asInterface 恒 null，newProcess 恒 null。
 * app 侧对流成员的访问走反射（process::class.java），返回类型保持 Any? 即可。
 */
interface IShizukuService {

    /** IShizukuService.asBinder（AIDL 的 binder 引用，桌面 stub 返回 null）。——Nova 注 */
    fun asBinder(): IBinder? = null

    fun newProcess(cmd: Array<String>?, env: Array<String>?, dir: String?): Any? = null

    fun getUid(): Int = -1

    fun getVersion(): Int = -1

    fun checkPermission(permission: String?): Int = -1

    abstract class Stub {
        companion object {
            @JvmStatic
            fun asInterface(binder: IBinder?): IShizukuService? = null
        }
    }
}
