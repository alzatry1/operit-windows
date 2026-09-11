package android.os

import android.util.Log
import java.io.Closeable
import java.io.File
import java.io.FileDescriptor
import java.io.RandomAccessFile
import java.util.Locale
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/** android.os.OperationCanceledException。 */
class OperationCanceledException : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}

/** android.os.CancellationSignal。 */
open class CancellationSignal {
    @Volatile private var canceled = false
    private var listener: OnCancelListener? = null

    fun interface OnCancelListener { fun onCancel() }

    fun cancel() {
        synchronized(this) {
            if (canceled) return
            canceled = true
        }
        listener?.onCancel()
    }

    fun isCanceled(): Boolean = canceled
    fun throwIfCanceled() { if (canceled) throw OperationCanceledException() }
    fun setOnCancelListener(listener: OnCancelListener?) {
        synchronized(this) {
            this.listener = listener
            if (canceled && listener != null) listener.onCancel()
        }
    }
}

/** android.os.StrictMode：全部 no-op。 */
object StrictMode {
    open class ThreadPolicy internal constructor(internal val mask: Int) {
        override fun toString(): String = "ThreadPolicy($mask)"
        class Builder {
            private var mask = 0
            constructor()
            constructor(policy: ThreadPolicy) { mask = policy.mask }
            fun detectAll(): Builder = apply { mask = -1 }
            fun detectDiskReads(): Builder = this
            fun detectDiskWrites(): Builder = this
            fun detectNetwork(): Builder = this
            fun detectCustomSlowCalls(): Builder = this
            fun detectResourceMismatches(): Builder = this
            fun detectUnbufferedIo(): Builder = this
            fun permitAll(): Builder = this
            fun permitDiskReads(): Builder = this
            fun permitDiskWrites(): Builder = this
            fun permitNetwork(): Builder = this
            fun permitCustomSlowCalls(): Builder = this
            fun permitResourceMismatches(): Builder = this
            fun permitUnbufferedIo(): Builder = this
            fun penaltyLog(): Builder = this
            fun penaltyDeath(): Builder = this
            fun penaltyDialog(): Builder = this
            fun penaltyFlashScreen(): Builder = this
            fun penaltyDeathOnNetwork(): Builder = this
            fun penaltyDropBox(): Builder = this
            fun penaltyListener(executor: java.util.concurrent.Executor?, l: OnThreadViolationListener?): Builder = this
            fun build(): ThreadPolicy = ThreadPolicy(mask)
        }
        companion object { @JvmField val LAX = ThreadPolicy(0) }
    }

    open class VmPolicy internal constructor(internal val mask: Int) {
        class Builder {
            constructor()
            constructor(policy: VmPolicy)
            fun detectAll(): Builder = this
            fun detectLeakedSqlLiteObjects(): Builder = this
            fun detectLeakedClosableObjects(): Builder = this
            fun detectLeakedRegistrationObjects(): Builder = this
            fun detectActivityLeaks(): Builder = this
            fun detectFileUriExposure(): Builder = this
            fun detectCleartextNetwork(): Builder = this
            fun detectContentUriWithoutPermission(): Builder = this
            fun detectCredentialProtectedWhileLocked(): Builder = this
            fun detectImplicitDirectBoot(): Builder = this
            fun detectIncorrectContextUse(): Builder = this
            fun detectNonSdkApiUsage(): Builder = this
            fun detectUnsafeIntentLaunch(): Builder = this
            fun permitNonSdkApiUsage(): Builder = this
            fun penaltyLog(): Builder = this
            fun penaltyDeath(): Builder = this
            fun penaltyDeathOnCleartextNetwork(): Builder = this
            fun penaltyDeathOnFileUriExposure(): Builder = this
            fun penaltyListener(executor: java.util.concurrent.Executor?, l: OnVmViolationListener?): Builder = this
            fun setClassInstanceLimit(klass: Class<*>, instanceLimit: Int): Builder = this
            fun build(): VmPolicy = VmPolicy(0)
        }
        companion object { @JvmField val LAX = VmPolicy(0) }
    }

    fun interface OnThreadViolationListener { fun onThreadViolation(v: Any?) }
    fun interface OnVmViolationListener { fun onVmViolation(v: Any?) }

    @JvmStatic fun setThreadPolicy(policy: ThreadPolicy) {}
    @JvmStatic fun getThreadPolicy(): ThreadPolicy = ThreadPolicy.LAX
    @JvmStatic fun allowThreadDiskReads(): ThreadPolicy = ThreadPolicy.LAX
    @JvmStatic fun allowThreadDiskWrites(): ThreadPolicy = ThreadPolicy.LAX
    @JvmStatic fun setVmPolicy(policy: VmPolicy) {}
    @JvmStatic fun getVmPolicy(): VmPolicy = VmPolicy.LAX
    @JvmStatic fun enableDefaults() {}
    @JvmStatic fun noteSlowCall(name: String) {}
    @JvmStatic fun noteDiskRead() {}
    @JvmStatic fun noteDiskWrite() {}
}

/** android.os.Trace：no-op。 */
object Trace {
    const val TRACE_TAG_NEVER = 0L
    const val TRACE_TAG_ALWAYS = 1L
    const val TRACE_TAG_GRAPHICS = 2L
    const val TRACE_TAG_INPUT = 4L
    const val TRACE_TAG_VIEW = 8L
    const val TRACE_TAG_WEBVIEW = 16L
    const val TRACE_TAG_WINDOW_MANAGER = 32L
    const val TRACE_TAG_ACTIVITY_MANAGER = 64L
    const val TRACE_TAG_SYNC_MANAGER = 128L
    const val TRACE_TAG_AUDIO = 256L
    const val TRACE_TAG_VIDEO = 512L
    const val TRACE_TAG_CAMERA = 1024L
    const val TRACE_TAG_HAL = 2048L
    const val TRACE_TAG_APP = 4096L
    const val TRACE_TAG_RESOURCES = 8192L
    const val TRACE_TAG_DALVIK = 16384L
    const val TRACE_TAG_RS = 32768L
    const val TRACE_TAG_BITMAP = 65536L
    const val TRACE_TAG_PACKAGE_MANAGER = 1L shl 17
    const val TRACE_TAG_SYSTEM_SERVER = 1L shl 18
    const val TRACE_TAG_DATABASE = 1L shl 19
    const val TRACE_TAG_NETWORK = 1L shl 20
    const val TRACE_TAG_ADB = 1L shl 21
    const val TRACE_TAG_VIBRATOR = 1L shl 22
    const val TRACE_TAG_AIDL = 1L shl 24
    const val TRACE_TAG_NNAPI = 1L shl 25
    const val TRACE_TAG_RRO = 1L shl 26

    @JvmStatic fun beginSection(name: String) {}
    @JvmStatic fun endSection() {}
    @JvmStatic fun beginAsyncSection(name: String, cookie: Int) {}
    @JvmStatic fun endAsyncSection(name: String, cookie: Int) {}
    @JvmStatic fun setCounter(name: String, value: Int) {}
    @JvmStatic fun isEnabled(): Boolean = false
    @JvmStatic fun isTagEnabled(traceTag: Long): Boolean = false
}

/** android.os.AsyncTask：协程/线程池简化实现，默认串行执行器与 AOSP 一致。 */
abstract class AsyncTask<Params, Progress, Result> {
    enum class Status { PENDING, RUNNING, FINISHED }

    @Volatile private var status = Status.PENDING
    @Volatile private var cancelled = false
    private val future = CompletableFuture<Result>()

    protected abstract fun doInBackground(vararg params: Params): Result

    protected open fun onPreExecute() {}
    protected open fun onPostExecute(result: Result) {}
    protected open fun onProgressUpdate(vararg values: Progress) {}
    protected open fun onCancelled(result: Result?) {}
    protected open fun onCancelled() { onCancelled(null) }

    protected fun isCancelled(): Boolean = cancelled

    fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        if (status == Status.FINISHED) return false
        cancelled = true
        future.cancel(mayInterruptIfRunning)
        onCancelled()
        return true
    }

    fun get(): Result = future.get()
    fun get(timeout: Long, unit: TimeUnit): Result = future.get(timeout, unit)
    fun getStatus(): Status = status

    fun execute(vararg params: Params): AsyncTask<Params, Progress, Result> =
        executeOnExecutor(SERIAL_EXECUTOR, *params)

    fun executeOnExecutor(exec: Executor, vararg params: Params): AsyncTask<Params, Progress, Result> {
        if (status != Status.PENDING) throw IllegalStateException("Cannot execute task: $status")
        status = Status.RUNNING
        mainHandler.post { onPreExecute() }
        exec.execute {
            try {
                val result = doInBackground(*params)
                if (cancelled) {
                    mainHandler.post { onCancelled(result) }
                } else {
                    future.complete(result)
                    status = Status.FINISHED
                    mainHandler.post { onPostExecute(result) }
                }
            } catch (t: Throwable) {
                future.completeExceptionally(t)
                status = Status.FINISHED
                if (!cancelled) Log.e("AsyncTask", "doInBackground failed", t)
            }
        }
        return this
    }

    protected fun publishProgress(vararg values: Progress) {
        if (!cancelled) mainHandler.post { onProgressUpdate(*values) }
    }

    companion object {
        @JvmField val THREAD_POOL_EXECUTOR: Executor = Executors.newCachedThreadPool { r ->
            Thread(r, "AsyncTask-pool").apply { isDaemon = true }
        }
        @JvmField val SERIAL_EXECUTOR: Executor = Executors.newSingleThreadExecutor { r ->
            Thread(r, "AsyncTask-serial").apply { isDaemon = true }
        }
        private val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    }
}

/** android.os.ResultReceiver。 */
open class ResultReceiver(private val handler: Handler?) : Parcelable {
    open fun onReceiveResult(resultCode: Int, resultData: Bundle?) {}

    fun send(resultCode: Int, resultData: Bundle?) {
        val h = handler
        if (h != null) h.post { onReceiveResult(resultCode, resultData) }
        else onReceiveResult(resultCode, resultData)
    }

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ResultReceiver> = object : Parcelable.Creator<ResultReceiver> {
            override fun createFromParcel(source: Parcel): ResultReceiver = ResultReceiver(null)
            override fun newArray(size: Int): Array<ResultReceiver?> = arrayOfNulls(size)
        }
    }
}

/** android.os.RemoteException。 */
open class RemoteException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    fun rethrowAsRuntimeException(): RuntimeException = RuntimeException(this)
    fun rethrowFromSystemServer(): RuntimeException = RuntimeException(this)
}

/** android.os.DeadObjectException。 */
open class DeadObjectException : RemoteException {
    constructor() : super()
    constructor(message: String?) : super(message)
}

/** android.os.IInterface。 */
interface IInterface {
    fun asBinder(): IBinder
}

/** android.os.IBinder。 */
interface IBinder {
    fun transact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean = false
    fun queryLocalInterface(descriptor: String): IInterface? = null
    fun getInterfaceDescriptor(): String? = null
    fun pingBinder(): Boolean = true
    fun isBinderAlive(): Boolean = true
    fun dump(fd: FileDescriptor, args: Array<String>?) {}
    fun dumpAsync(fd: FileDescriptor, args: Array<String>?) {}
    fun shellCommand(`in`: java.io.FileDescriptor?, out: java.io.FileDescriptor?, err: java.io.FileDescriptor?, args: Array<String>?, receiver: ResultReceiver?) {}
    fun linkToDeath(recipient: DeathRecipient, flags: Int) {}
    fun unlinkToDeath(recipient: DeathRecipient, flags: Int): Boolean = true
    fun isProxy(): Boolean = false
    fun getExtension(): IBinder? = null

    interface DeathRecipient {
        fun binderDied()
        fun binderDied(who: IBinder) { binderDied() }
    }

    companion object {
        const val FLAG_ONEWAY = 1
        const val FIRST_CALL_TRANSACTION = 1
        const val LAST_CALL_TRANSACTION = 16777215
        const val PING_TRANSACTION = 1598968902
        const val DUMP_TRANSACTION = 1598311760
        const val SHELL_COMMAND_TRANSACTION = 1598246212
        const val INTERFACE_TRANSACTION = 1598968902
        const val TWEET_TRANSACTION = 1599362907
        const val SUGGESTED_MAX_IPC_SIZE = 65536
        fun getSuggestedMaxIpcSizeBytes(): Int = SUGGESTED_MAX_IPC_SIZE
    }
}

/** android.os.Binder。 */
open class Binder : IBinder {
    private var descriptor: String? = null
    private var owner: IInterface? = null

    constructor()
    constructor(descriptor: String?) { this.descriptor = descriptor }

    fun attachInterface(owner: IInterface?, descriptor: String?) {
        this.owner = owner
        this.descriptor = descriptor
    }

    override fun getInterfaceDescriptor(): String? = descriptor
    override fun queryLocalInterface(descriptor: String): IInterface? =
        if (this.descriptor == null || this.descriptor == descriptor) owner else null
    override fun pingBinder(): Boolean = true
    override fun isBinderAlive(): Boolean = true
    override fun dump(fd: FileDescriptor, args: Array<String>?) {}
    override fun linkToDeath(recipient: IBinder.DeathRecipient, flags: Int) {}
    override fun unlinkToDeath(recipient: IBinder.DeathRecipient, flags: Int): Boolean = true

    override fun transact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        data.setDataPosition(0)
        val result = try {
            onTransact(code, data, reply, flags)
        } catch (e: RemoteException) {
            throw e
        } catch (e: Exception) {
            Log.e("Binder", "onTransact failed", e)
            false
        }
        reply?.setDataPosition(0)
        return result
    }

    protected open fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        if (code == IBinder.INTERFACE_TRANSACTION) {
            reply?.writeString(getInterfaceDescriptor())
            return true
        }
        return false
    }

    override fun isProxy(): Boolean = false
    override fun getExtension(): IBinder? = null

    companion object {
        @JvmStatic fun getCallingPid(): Int = Process.myPid()
        @JvmStatic fun getCallingUid(): Int = Process.myUid()
        @JvmStatic fun getCallingUidOrThrow(): Int = Process.myUid()
        @JvmStatic fun getCallingUserHandle(): UserHandle = UserHandle.SYSTEM
        @JvmStatic fun clearCallingIdentity(): Long = 0L
        @JvmStatic fun restoreCallingIdentity(token: Long) {}
        @JvmStatic fun flushPendingCommands() {}
        @JvmStatic fun joinThreadPool() {}
        @JvmStatic fun isProxy(iface: IInterface?): Boolean = false
        @JvmStatic fun allowBlocking(iface: IBinder?): IBinder? = iface
        @JvmStatic fun defaultBlocking(iface: IBinder?): IBinder? = iface
        @JvmStatic fun setThreadStrictModePolicy(policyMask: Int) {}
        @JvmStatic fun getThreadStrictModePolicy(): Int = 0
        @JvmStatic fun withCleanCallingIdentity(action: java.util.concurrent.Callable<Unit>) { action.call() }
    }
}

/** android.os.LocaleList：包装 Locale 列表。 */
class LocaleList private constructor(private val locales: Array<Locale>) : Iterable<Locale>, Parcelable {
    fun get(index: Int): Locale = locales[index]
    fun size(): Int = locales.size
    fun isEmpty(): Boolean = locales.isEmpty()
    fun indexOf(locale: Locale?): Int = locales.indexOf(locale)
    fun toLanguageTags(): String = locales.joinToString(",") { it.toLanguageTag() }
    override fun iterator(): Iterator<Locale> = locales.iterator()
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeString(toLanguageTags()) }
    override fun equals(other: Any?): Boolean = other is LocaleList && other.locales.contentEquals(locales)
    override fun hashCode(): Int = locales.contentHashCode()
    override fun toString(): String = locales.joinToString(",", "[", "]")

    companion object {
        @JvmField val EMPTY = LocaleList(emptyArray())
        @Volatile private var default = LocaleList(arrayOf(Locale.getDefault()))

        @JvmStatic fun getDefault(): LocaleList = default
        @JvmStatic fun getAdjustedDefault(): LocaleList = default
        @JvmStatic fun setDefault(locales: LocaleList) { default = locales }
        @JvmStatic fun of(vararg locales: Locale): LocaleList = LocaleList(arrayOf(*locales))
        @JvmStatic fun forLanguageTags(list: String?): LocaleList {
            if (list.isNullOrBlank()) return EMPTY
            return LocaleList(list.split(",").map { Locale.forLanguageTag(it.trim()) }.toTypedArray())
        }
        @JvmStatic fun matchesLanguageAndScript(supported: Locale, desired: Locale): Boolean = true
    }
}

/** android.os.ParcelFileDescriptor：包壳 RandomAccessFile。 */
class ParcelFileDescriptor : Parcelable, Closeable {
    private val raf: RandomAccessFile?
    private val fd: FileDescriptor?
    private val fakeFd: Int

    private constructor(raf: RandomAccessFile?) {
        this.raf = raf
        this.fd = raf?.fd
        this.fakeFd = fdCounter.incrementAndGet()
    }

    fun getFd(): Int = fakeFd
    val fileDescriptor: FileDescriptor? get() = fd
    fun getStatSize(): Long = try { raf?.length() ?: -1 } catch (e: Exception) { -1 }
    fun canDetectErrors(): Boolean = false
    fun detachFd(): Int = fakeFd
    fun dup(): ParcelFileDescriptor = ParcelFileDescriptor(raf)
    fun dup(newMode: Int): ParcelFileDescriptor = ParcelFileDescriptor(raf)

    override fun close() {
        try { raf?.close() } catch (e: Exception) { /* ignore */ }
    }

    override fun describeContents(): Int = Parcelable.CONTENTS_FILE_DESCRIPTOR
    override fun writeToParcel(dest: Parcel, flags: Int) {}
    override fun toString(): String = "ParcelFileDescriptor{fd=$fakeFd}"

    /** AOSP ParcelFileDescriptor.AutoCloseInputStream。 */
    class AutoCloseInputStream(private val pfd: ParcelFileDescriptor) :
        java.io.FileInputStream(pfd.fileDescriptor ?: throw IllegalArgumentException("null fd")) {
        override fun close() {
            try { super.close() } finally { pfd.close() }
        }
    }

    /** AOSP ParcelFileDescriptor.AutoCloseOutputStream。 */
    class AutoCloseOutputStream(private val pfd: ParcelFileDescriptor) :
        java.io.FileOutputStream(pfd.fileDescriptor ?: throw IllegalArgumentException("null fd")) {
        override fun close() {
            try { super.close() } finally { pfd.close() }
        }
    }

    /** AOSP ParcelFileDescriptor.InputStream（不自动关闭）。 */
    class InputStream(pfd: ParcelFileDescriptor) :
        java.io.FileInputStream(pfd.fileDescriptor ?: throw IllegalArgumentException("null fd"))

    /** AOSP ParcelFileDescriptor.OutputStream。 */
    class OutputStream(pfd: ParcelFileDescriptor) :
        java.io.FileOutputStream(pfd.fileDescriptor ?: throw IllegalArgumentException("null fd"))

    companion object {
        private val fdCounter = AtomicInteger(1000)

        const val MODE_READ_ONLY = 0x10000000
        const val MODE_WRITE_ONLY = 0x20000000
        const val MODE_READ_WRITE = 0x30000000
        const val MODE_CREATE = 0x08000000
        const val MODE_TRUNCATE = 0x04000000
        const val MODE_APPEND = 0x02000000
        const val MODE_WORLD_READABLE = 0x00000001
        const val MODE_WORLD_WRITEABLE = 0x00000002
        const val MODE_MASK = 0x30000000

        @JvmField val CREATOR: Parcelable.Creator<ParcelFileDescriptor> =
            object : Parcelable.Creator<ParcelFileDescriptor> {
                override fun createFromParcel(source: Parcel): ParcelFileDescriptor = ParcelFileDescriptor(null)
                override fun newArray(size: Int): Array<ParcelFileDescriptor?> = arrayOfNulls(size)
            }

        @JvmStatic
        fun open(file: File, mode: Int): ParcelFileDescriptor {
            file.parentFile?.mkdirs()
            val rw = (mode and MODE_MASK) != MODE_READ_ONLY
            if (!file.exists()) {
                if (mode and MODE_CREATE != 0) file.createNewFile()
                else throw java.io.FileNotFoundException(file.path)
            }
            if (rw && mode and MODE_TRUNCATE != 0) java.io.FileOutputStream(file).use { }
            val raf = RandomAccessFile(file, if (rw) "rw" else "r")
            if (rw && mode and MODE_APPEND != 0) raf.seek(raf.length())
            return ParcelFileDescriptor(raf)
        }

        @JvmStatic
        fun adoptFd(fd: Int): ParcelFileDescriptor = ParcelFileDescriptor(null)

        @JvmStatic
        fun fromFd(fd: Int): ParcelFileDescriptor = ParcelFileDescriptor(null)

        @JvmStatic
        fun fromData(data: ByteArray?, name: String?): ParcelFileDescriptor = ParcelFileDescriptor(null)

        @JvmStatic
        fun createPipe(): Array<ParcelFileDescriptor> {
            throw UnsupportedOperationException("createPipe 桌面版未实现")
        }

        @JvmStatic
        fun parseMode(mode: String): Int = when (mode) {
            "r" -> MODE_READ_ONLY
            "w", "wt" -> MODE_WRITE_ONLY or MODE_CREATE or MODE_TRUNCATE
            "wa" -> MODE_WRITE_ONLY or MODE_CREATE or MODE_APPEND
            "rw" -> MODE_READ_WRITE or MODE_CREATE
            "rwt" -> MODE_READ_WRITE or MODE_CREATE or MODE_TRUNCATE
            else -> MODE_READ_ONLY
        }
    }
}

/** android.os.FileObserver：桌面 stub（不监听文件系统）。 */
open class FileObserver {
    private val path: String

    constructor(path: File) { this.path = path.absolutePath }
    constructor(path: File, mask: Int) { this.path = path.absolutePath }
    constructor(path: String) { this.path = path }
    constructor(path: String, mask: Int) { this.path = path }
    constructor(paths: List<File>, mask: Int) { this.path = paths.firstOrNull()?.absolutePath ?: "" }

    open fun onEvent(event: Int, path: String?) {}
    fun startWatching() {}
    fun stopWatching() {}

    companion object {
        const val ACCESS = 1
        const val MODIFY = 2
        const val ATTRIB = 4
        const val CLOSE_WRITE = 8
        const val CLOSE_NOWRITE = 16
        const val OPEN = 32
        const val MOVED_FROM = 64
        const val MOVED_TO = 128
        const val CREATE = 256
        const val DELETE = 512
        const val DELETE_SELF = 1024
        const val MOVE_SELF = 2048
        const val ALL_EVENTS = 4095
    }
}
