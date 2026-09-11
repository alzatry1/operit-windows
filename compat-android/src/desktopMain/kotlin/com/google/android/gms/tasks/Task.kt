package com.google.android.gms.tasks

/**
 * com.google.android.gms.tasks.Task 最小垫片（P3-B4）。
 * 桌面无 Play Services；本实现仅承载 mlkit stub 的监听器链式调用。
 */
open class Task<TResult> {
    fun interface OnSuccessListener<in TResult> { fun onSuccess(result: TResult) }
    fun interface OnFailureListener { fun onFailure(e: Exception) }
    fun interface OnCompleteListener<TRes> { fun onComplete(task: Task<TRes>) }
    fun interface OnCanceledListener { fun onCanceled() }

    open val isComplete: Boolean get() = true
    open val isSuccessful: Boolean get() = false
    open val isCanceled: Boolean get() = false
    open val result: TResult? get() = null
    open val exception: Exception? get() = null

    open fun addOnSuccessListener(listener: OnSuccessListener<in TResult>): Task<TResult> = this
    open fun addOnFailureListener(listener: OnFailureListener): Task<TResult> = this
    open fun addOnCompleteListener(listener: OnCompleteListener<TResult>): Task<TResult> = this
    open fun addOnCanceledListener(listener: OnCanceledListener): Task<TResult> = this
}
