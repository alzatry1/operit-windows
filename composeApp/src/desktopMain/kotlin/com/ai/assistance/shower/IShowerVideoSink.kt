package com.ai.assistance.shower

import android.os.Binder
import android.os.IBinder
import android.os.IInterface

/**
 * IShowerVideoSink AIDL 的桌面 Kotlin 移植（P3-B4）。
 * 桌面端视频帧回调走进程内 binder（queryLocalInterface 直连）。
 */
interface IShowerVideoSink : IInterface {

    fun onVideoFrame(data: ByteArray)

    abstract class Stub : Binder(), IShowerVideoSink {

        init {
            attachInterface(this, DESCRIPTOR)
        }

        override fun asBinder(): IBinder = this

        companion object {
            private const val DESCRIPTOR = "com.ai.assistance.shower.IShowerVideoSink"

            const val TRANSACTION_onVideoFrame = IBinder.FIRST_CALL_TRANSACTION

            @JvmStatic
            fun asInterface(obj: IBinder?): IShowerVideoSink? {
                if (obj == null) return null
                return obj.queryLocalInterface(DESCRIPTOR) as? IShowerVideoSink
            }
        }
    }
}
