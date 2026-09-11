package com.ai.assistance.shower

import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable

/**
 * ShowerBinderContainer 的桌面 Kotlin 移植（P3-B4）。
 * 桌面端 Intent extra 走内存 map（不经过 Parcel 序列化），
 * CREATOR/writeToParcel 仅为保持 API 形状。
 */
class ShowerBinderContainer(val binder: IBinder?) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ShowerBinderContainer> =
            object : Parcelable.Creator<ShowerBinderContainer> {
                override fun createFromParcel(source: Parcel): ShowerBinderContainer =
                    ShowerBinderContainer(null)

                override fun newArray(size: Int): Array<ShowerBinderContainer?> = arrayOfNulls(size)
            }
    }
}
