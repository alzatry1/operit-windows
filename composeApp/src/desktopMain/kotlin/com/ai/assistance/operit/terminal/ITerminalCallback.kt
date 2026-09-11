package com.ai.assistance.operit.terminal

import android.os.Binder
import android.os.IBinder
import android.os.IInterface

/**
 * ITerminalCallback AIDL 的桌面 Kotlin 移植（P3-B4）。
 * oneway 回调接口；桌面端进程内直连（queryLocalInterface）。
 */
interface ITerminalCallback : IInterface {

    fun onCommandExecutionUpdate(event: CommandExecutionEvent?)
    fun onSessionDirectoryChanged(event: SessionDirectoryEvent?)

    abstract class Stub : Binder(), ITerminalCallback {

        init {
            attachInterface(this, DESCRIPTOR)
        }

        override fun asBinder(): IBinder = this

        companion object {
            private const val DESCRIPTOR = "com.ai.assistance.operit.terminal.ITerminalCallback"

            const val TRANSACTION_onCommandExecutionUpdate = IBinder.FIRST_CALL_TRANSACTION
            const val TRANSACTION_onSessionDirectoryChanged = IBinder.FIRST_CALL_TRANSACTION + 1

            @JvmStatic
            fun asInterface(obj: IBinder?): ITerminalCallback? {
                if (obj == null) return null
                return obj.queryLocalInterface(DESCRIPTOR) as? ITerminalCallback
            }
        }
    }
}
