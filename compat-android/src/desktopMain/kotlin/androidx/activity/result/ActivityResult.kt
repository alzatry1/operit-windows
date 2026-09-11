package androidx.activity.result

import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable

/**
 * androidx.activity.result.contract.ActivityResultContract 基类。
 * 注：真实 AOSP 此类位于 androidx.activity.result.contract 包；桌面版与原版保持一致，
 * 本文件仅放核心类型，contract 基类在 contract 子包内。
 */

/** androidx.activity.result.ActivityResult：结果码 + 数据。 */
class ActivityResult(
    val resultCode: Int,
    val data: Intent?,
) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(resultCode)
        dest.writeString(data?.toString())
    }

    override fun toString(): String =
        "ActivityResult{resultCode=${resultCodeToString(resultCode)}, data=$data}"

    companion object {
        @JvmStatic
        fun resultCodeToString(resultCode: Int): String = when (resultCode) {
            android.app.Activity.RESULT_OK -> "RESULT_OK"
            android.app.Activity.RESULT_CANCELED -> "RESULT_CANCELED"
            else -> resultCode.toString()
        }

        @JvmField
        val CREATOR: Parcelable.Creator<ActivityResult> = object : Parcelable.Creator<ActivityResult> {
            override fun createFromParcel(source: Parcel): ActivityResult =
                ActivityResult(source.readInt(), source.readString()?.let { Intent.parseUri(it, 0) })

            override fun newArray(size: Int): Array<ActivityResult?> = arrayOfNulls(size)
        }
    }
}

/** androidx.activity.result.ActivityResultCallback。 */
fun interface ActivityResultCallback<O> {
    fun onActivityResult(result: O)
}

/**
 * androidx.activity.result.ActivityResultLauncher。
 * 真实 Android 的 launch(input) 单参便捷版由 activity-ktx 扩展提供，这里同样用扩展函数补齐。
 */
abstract class ActivityResultLauncher<I> {

    abstract fun launch(input: I, options: androidx.core.app.ActivityOptionsCompat?)

    abstract fun unregister()

    abstract val contract: androidx.activity.result.contract.ActivityResultContract<I, *>
}

/** activity-ktx 风格：launch(input) 便捷扩展。 */
fun <I> ActivityResultLauncher<I>.launch(input: I, options: androidx.core.app.ActivityOptionsCompat? = null) =
    launch(input, options)
