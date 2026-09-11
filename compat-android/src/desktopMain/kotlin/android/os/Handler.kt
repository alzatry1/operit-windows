package android.os

import android.util.Log
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Handler/Looper/Message/Messenger 桌面实现。
 * 主 Looper 后端是名为 "android-main" 的单线程 ScheduledExecutorService。
 */

class Looper private constructor(val thread: Thread) {
    internal val executor = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "android-main").apply { isDaemon = true }
    }
    @Volatile private var quit = false

    val isCurrentThread: Boolean
        get() = Thread.currentThread() == thread

    fun quit() { quit = true }
    fun quitSafely() = quit()
    fun isIdle(): Boolean = true
    fun getQueue(): MessageQueue = queue

    internal val queue = MessageQueue()

    override fun toString(): String = "Looper (main, tid ${thread.id}) {${System.identityHashCode(this).toString(16)}}"

    companion object {
        private val main: Looper by lazy { Looper(Thread.currentThread()) }
        private val threadLocal = ThreadLocal<Looper?>()

        @JvmStatic fun getMainLooper(): Looper = main
        @JvmStatic fun myLooper(): Looper? = threadLocal.get() ?: main
        @JvmStatic fun prepare() { threadLocal.set(main) }
        @JvmStatic fun prepareMainLooper() { threadLocal.set(main) }
        @JvmStatic fun loop() { /* 桌面事件循环由 Swing EDT 承担，此处 no-op */ }
    }
}

/** 轻量 MessageQueue 占位。 */
class MessageQueue {
    fun isIdle(): Boolean = true
    fun addIdleHandler(handler: IdleHandler): Boolean = true
    fun removeIdleHandler(handler: IdleHandler) {}
    interface IdleHandler { fun queueIdle(): Boolean }
}

class Message {
    var what: Int = 0
    var arg1: Int = 0
    var arg2: Int = 0
    var obj: Any? = null
    var target: Handler? = null
    var replyTo: Messenger? = null
    var sendingUid: Int = -1
    var flags: Int = 0
    var whenMillis: Long = 0
    var callback: Runnable? = null
    private var data: Bundle? = null

    fun getData(): Bundle {
        if (data == null) data = Bundle()
        return data!!
    }

    fun peekData(): Bundle? = data
    fun setData(b: Bundle?) { data = b }

    fun sendToTarget() {
        target?.sendMessage(this) ?: Log.w("Message", "sendToTarget without target")
    }

    fun copyFrom(o: Message) {
        what = o.what; arg1 = o.arg1; arg2 = o.arg2; obj = o.obj
        data = o.data?.clone()
    }

    fun recycle() {
        what = 0; arg1 = 0; arg2 = 0; obj = null; target = null; replyTo = null
        data = null; callback = null; flags = 0
    }

    fun isAsynchronous(): Boolean = false
    fun setAsynchronous(async: Boolean) {}

    fun getWhen(): Long = whenMillis

    override fun toString(): String = "{ what=$what when=$whenMillis obj=$obj target=$target }"

    companion object {
        @JvmStatic fun obtain(): Message = Message()
        @JvmStatic fun obtain(h: Handler?): Message = Message().apply { target = h }
        @JvmStatic fun obtain(h: Handler?, what: Int): Message = Message().apply { target = h; this.what = what }
        @JvmStatic fun obtain(h: Handler?, what: Int, obj: Any?): Message =
            Message().apply { target = h; this.what = what; this.obj = obj }
        @JvmStatic fun obtain(h: Handler?, what: Int, arg1: Int, arg2: Int): Message =
            Message().apply { target = h; this.what = what; this.arg1 = arg1; this.arg2 = arg2 }
        @JvmStatic fun obtain(h: Handler?, what: Int, arg1: Int, arg2: Int, obj: Any?): Message =
            Message().apply { target = h; this.what = what; this.arg1 = arg1; this.arg2 = arg2; this.obj = obj }
        @JvmStatic fun obtain(orig: Message): Message = Message().apply { copyFrom(orig); target = orig.target }
    }
}

open class Handler {
    private val looper: Looper
    private val callback: Callback?
    private val posted = ConcurrentHashMap<Runnable, ScheduledFuture<*>>()
    private val messages = ConcurrentHashMap<Message, ScheduledFuture<*>>()

    interface Callback {
        fun handleMessage(msg: Message): Boolean
    }

    constructor() : this(Looper.myLooper() ?: Looper.getMainLooper(), null)
    constructor(callback: Callback?) : this(Looper.myLooper() ?: Looper.getMainLooper(), callback)
    constructor(looper: Looper) : this(looper, null)
    constructor(looper: Looper, callback: Callback?) {
        this.looper = looper
        this.callback = callback
    }

    open fun handleMessage(msg: Message) {}

    fun dispatchMessage(msg: Message) {
        val cb = msg.callback
        if (cb != null) { cb.run(); return }
        if (callback?.handleMessage(msg) != true) handleMessage(msg)
    }

    fun getLooper(): Looper = looper

    private fun schedule(r: Runnable, delayMs: Long): Boolean {
        return try {
            val f = looper.executor.schedule({
                posted.remove(r)
                try { r.run() } catch (t: Throwable) { Log.e("Handler", "posted runnable failed", t) }
            }, delayMs.coerceAtLeast(0), TimeUnit.MILLISECONDS)
            posted[r] = f
            true
        } catch (e: Exception) {
            Log.e("Handler", "schedule failed", e); false
        }
    }

    fun post(r: Runnable): Boolean = schedule(r, 0)
    fun postDelayed(r: Runnable, delayMillis: Long): Boolean = schedule(r, delayMillis)
    fun postDelayed(r: Runnable, token: Any?, delayMillis: Long): Boolean = schedule(r, delayMillis)
    fun postAtTime(r: Runnable, uptimeMillis: Long): Boolean =
        schedule(r, uptimeMillis - SystemClock.uptimeMillis())
    fun postAtTime(r: Runnable, token: Any?, uptimeMillis: Long): Boolean = postAtTime(r, uptimeMillis)
    fun postAtFrontOfQueue(r: Runnable): Boolean = schedule(r, 0)

    fun removeCallbacks(r: Runnable) { posted.remove(r)?.cancel(false) }
    fun removeCallbacks(r: Runnable, token: Any?) = removeCallbacks(r)
    fun removeCallbacksAndMessages(token: Any?) {
        posted.values.forEach { it.cancel(false) }; posted.clear()
        messages.values.forEach { it.cancel(false) }; messages.clear()
    }

    fun removeMessages(what: Int) {
        val it = messages.entries.iterator()
        while (it.hasNext()) {
            val e = it.next()
            if (e.key.what == what) { e.value.cancel(false); it.remove() }
        }
    }

    fun removeMessages(what: Int, obj: Any?) {
        val it = messages.entries.iterator()
        while (it.hasNext()) {
            val e = it.next()
            if (e.key.what == what && (obj == null || e.key.obj == obj)) { e.value.cancel(false); it.remove() }
        }
    }

    fun hasMessages(what: Int): Boolean = messages.keys.any { it.what == what }
    fun hasMessages(what: Int, obj: Any?): Boolean = messages.keys.any { it.what == what && it.obj == obj }
    fun hasCallbacks(r: Runnable): Boolean = posted.containsKey(r)

    fun sendEmptyMessage(what: Int): Boolean = sendMessage(Message.obtain(this, what))
    fun sendEmptyMessageDelayed(what: Int, delayMillis: Long): Boolean =
        sendMessageDelayed(Message.obtain(this, what), delayMillis)
    fun sendEmptyMessageAtTime(what: Int, uptimeMillis: Long): Boolean =
        sendMessageAtTime(Message.obtain(this, what), uptimeMillis)

    fun sendMessage(msg: Message): Boolean = sendMessageDelayed(msg, 0)
    fun sendMessageDelayed(msg: Message, delayMillis: Long): Boolean {
        msg.target = this
        return try {
            val f = looper.executor.schedule({
                messages.remove(msg)
                try { dispatchMessage(msg) } catch (t: Throwable) { Log.e("Handler", "dispatch failed", t) }
            }, delayMillis.coerceAtLeast(0), TimeUnit.MILLISECONDS)
            messages[msg] = f
            true
        } catch (e: Exception) {
            Log.e("Handler", "schedule failed", e); false
        }
    }

    fun sendMessageAtTime(msg: Message, uptimeMillis: Long): Boolean =
        sendMessageDelayed(msg, uptimeMillis - SystemClock.uptimeMillis())

    fun sendMessageAtFrontOfQueue(msg: Message): Boolean = sendMessageDelayed(msg, 0)

    fun obtainMessage(): Message = Message.obtain(this)
    fun obtainMessage(what: Int): Message = Message.obtain(this, what)
    fun obtainMessage(what: Int, obj: Any?): Message = Message.obtain(this, what, obj)
    fun obtainMessage(what: Int, arg1: Int, arg2: Int): Message = Message.obtain(this, what, arg1, arg2)
    fun obtainMessage(what: Int, arg1: Int, arg2: Int, obj: Any?): Message = Message.obtain(this, what, arg1, arg2, obj)

    fun dump(pw: java.io.PrintWriter, prefix: String) { pw.println("$prefix$looper") }
    fun getMessageName(message: Message): String = "0x${message.what.toString(16)}"

    override fun toString(): String = "Handler (${looper.thread.name}) {${System.identityHashCode(this).toString(16)}}"

    companion object {
        @JvmStatic fun createAsync(looper: Looper): Handler = Handler(looper)
        @JvmStatic fun createAsync(looper: Looper, callback: Callback): Handler = Handler(looper, callback)
        @JvmStatic fun getMain(): Handler = Handler(Looper.getMainLooper())
    }
}

class Messenger {
    private val handler: Handler?
    private val binder: IBinder?

    constructor(target: Handler) { handler = target; binder = null }
    constructor(binder: IBinder?) { handler = null; this.binder = binder }

    fun send(message: Message) {
        if (handler != null) handler.sendMessage(message)
        else Log.w("Messenger", "send on binder-backed messenger (no-op)")
    }

    fun getBinder(): IBinder? = binder

    override fun equals(other: Any?): Boolean = other is Messenger && other.handler == handler && other.binder == binder
    override fun hashCode(): Int = (handler?.hashCode() ?: 0) * 31 + (binder?.hashCode() ?: 0)

    companion object {
        @JvmStatic fun readMessengerOrNullFromParcel(parcel: Parcel): Messenger? = null
        @JvmStatic fun writeMessengerOrNullToParcel(messenger: Messenger?, parcel: Parcel) {}
    }
}
