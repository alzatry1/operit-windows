package androidx.activity.result.contract

import android.content.Context
import android.content.Intent

/** androidx.activity.result.contract.ActivityResultContract 基类。 */
abstract class ActivityResultContract<I, O> {

    abstract fun createIntent(context: Context, input: I): Intent

    open fun getSynchronousResult(context: Context, input: I): SynchronousResult<O>? = null

    abstract fun parseResult(resultCode: Int, intent: Intent?): O

    class SynchronousResult<O>(val value: O)
}
