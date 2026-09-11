package com.ai.assistance.operit.terminal

import android.os.Binder
import android.os.IBinder
import android.os.IInterface

/**
 * ITerminalService AIDL 的桌面 Kotlin 移植（P3-B4）。
 * 桌面端 TerminalService 与调用方同进程：Stub.asInterface 经
 * queryLocalInterface 直连，无 Parcel 编组。
 */
interface ITerminalService : IInterface {

    fun createSession(): String?
    fun switchToSession(sessionId: String?)
    fun closeSession(sessionId: String?)
    fun sendCommand(command: String?): String?
    fun sendInterruptSignal()
    fun registerCallback(callback: ITerminalCallback?)
    fun unregisterCallback(callback: ITerminalCallback?)
    fun requestStateUpdate()

    abstract class Stub : Binder(), ITerminalService {

        init {
            attachInterface(this, DESCRIPTOR)
        }

        override fun asBinder(): IBinder = this

        companion object {
            private const val DESCRIPTOR = "com.ai.assistance.operit.terminal.ITerminalService"

            const val TRANSACTION_createSession = IBinder.FIRST_CALL_TRANSACTION
            const val TRANSACTION_switchToSession = IBinder.FIRST_CALL_TRANSACTION + 1
            const val TRANSACTION_closeSession = IBinder.FIRST_CALL_TRANSACTION + 2
            const val TRANSACTION_sendCommand = IBinder.FIRST_CALL_TRANSACTION + 3
            const val TRANSACTION_sendInterruptSignal = IBinder.FIRST_CALL_TRANSACTION + 4
            const val TRANSACTION_registerCallback = IBinder.FIRST_CALL_TRANSACTION + 5
            const val TRANSACTION_unregisterCallback = IBinder.FIRST_CALL_TRANSACTION + 6
            const val TRANSACTION_requestStateUpdate = IBinder.FIRST_CALL_TRANSACTION + 7

            @JvmStatic
            fun asInterface(obj: IBinder?): ITerminalService? {
                if (obj == null) return null
                return obj.queryLocalInterface(DESCRIPTOR) as? ITerminalService
            }
        }
    }
}
