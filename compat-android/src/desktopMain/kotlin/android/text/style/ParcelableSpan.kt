package android.text.style

import android.os.Parcelable

/** android.text.style.ParcelableSpan。 */
interface ParcelableSpan : Parcelable {
    fun getSpanTypeId(): Int = 0
}
