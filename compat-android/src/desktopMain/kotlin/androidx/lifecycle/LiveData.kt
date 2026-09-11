package androidx.lifecycle

import android.os.Handler
import android.os.Looper
import java.util.concurrent.CopyOnWriteArrayList

/**
 * androidx.lifecycle.LiveData 简化桌面版（lifecycle-livedata 无 KMP 工件）。
 * 语义：粘性（新观察者立即收到当前值）、observe 的 owner 在 DESTROYED 后不再派发、
 * postValue 投递到主线程。无活跃态（STARTED）判定——桌面窗口始终视作活跃。
 */

/** androidx.lifecycle.Observer。 */
fun interface Observer<T> {
    fun onChanged(value: T)
}

open class LiveData<T> {

    private object NotSet

    @Volatile
    private var data: Any? = NotSet

    /** observeForever 注册的观察者。 */
    private val foreverObservers = CopyOnWriteArrayList<Observer<in T>>()

    /** observe(owner) 注册的观察者。 */
    private val ownedObservers = CopyOnWriteArrayList<Pair<LifecycleOwner, Observer<in T>>>()

    constructor()

    constructor(initialValue: T) {
        data = initialValue
    }

    open val value: T?
        @Suppress("UNCHECKED_CAST")
        get() = if (data === NotSet) null else data as T

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        ownedObservers.add(owner to observer)
        emitTo(observer)
    }

    fun observeForever(observer: Observer<in T>) {
        foreverObservers.add(observer)
        emitTo(observer)
    }

    fun removeObserver(observer: Observer<in T>) {
        foreverObservers.remove(observer)
        ownedObservers.removeAll { it.second === observer }
    }

    fun hasObservers(): Boolean = foreverObservers.isNotEmpty() || ownedObservers.isNotEmpty()

    fun hasActiveObservers(): Boolean = hasObservers()

    val isInitialized: Boolean
        get() = data !== NotSet

    @Suppress("UNCHECKED_CAST")
    private fun emitTo(observer: Observer<in T>) {
        if (data !== NotSet) observer.onChanged(data as T)
    }

    @Suppress("UNCHECKED_CAST")
    private fun dispatchAll() {
        if (data === NotSet) return
        val v = data as T
        for (o in foreverObservers) o.onChanged(v)
        for ((owner, o) in ownedObservers) {
            if (owner.lifecycle.currentState != Lifecycle.State.DESTROYED) {
                o.onChanged(v)
            }
        }
    }

    protected open fun setValue(value: T) {
        data = value
        dispatchAll()
    }

    /** 异步设值：投递到主 Looper（桌面 Swing EDT）。 */
    fun postValue(value: T) {
        Handler(Looper.getMainLooper()).post { setValue(value) }
    }
}

/** androidx.lifecycle.MutableLiveData。 */
open class MutableLiveData<T> : LiveData<T> {

    constructor() : super()

    constructor(initialValue: T) : super(initialValue)

    public override fun setValue(value: T) {
        super.setValue(value)
    }
}
