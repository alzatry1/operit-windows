package androidx.work

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

// ---------------- 数据与约束 ----------------

/** androidx.work.Data：键值容器。 */
class Data private constructor(
    private val map: Map<String, Any?>,
) {
    fun getString(key: String): String? = map[key] as? String
    fun getInt(key: String, defaultValue: Int): Int = (map[key] as? Number)?.toInt() ?: defaultValue
    fun getLong(key: String, defaultValue: Long): Long = (map[key] as? Number)?.toLong() ?: defaultValue
    fun getFloat(key: String, defaultValue: Float): Float = (map[key] as? Number)?.toFloat() ?: defaultValue
    fun getDouble(key: String, defaultValue: Double): Double = (map[key] as? Number)?.toDouble() ?: defaultValue
    fun getBoolean(key: String, defaultValue: Boolean): Boolean = (map[key] as? Boolean) ?: defaultValue
    fun getStringArray(key: String): Array<String>? = (map[key] as? List<*>)?.filterIsInstance<String>()?.toTypedArray()
    fun hasKey(key: String): Boolean = map.containsKey(key)
    val size: Int get() = map.size

    class Builder {
        private val map = LinkedHashMap<String, Any?>()
        fun putString(key: String, value: String?): Builder = apply { map[key] = value }
        fun putInt(key: String, value: Int): Builder = apply { map[key] = value }
        fun putLong(key: String, value: Long): Builder = apply { map[key] = value }
        fun putFloat(key: String, value: Float): Builder = apply { map[key] = value }
        fun putDouble(key: String, value: Double): Builder = apply { map[key] = value }
        fun putBoolean(key: String, value: Boolean): Builder = apply { map[key] = value }
        fun putStringArray(key: String, value: Array<String>): Builder = apply { map[key] = value.toList() }
        fun putAll(data: Data): Builder = apply { map.putAll(data.map) }
        fun build(): Data = Data(map)
    }

    companion object {
        @JvmField val EMPTY = Data(emptyMap())

        @JvmStatic
        fun fromByteArray(bytes: ByteArray): Data = EMPTY
    }
}

/** androidx.work.workDataOf 顶层函数。 */
fun workDataOf(vararg pairs: Pair<String, Any?>): Data {
    val b = Data.Builder()
    for ((k, v) in pairs) {
        when (v) {
            is String -> b.putString(k, v)
            is Int -> b.putInt(k, v)
            is Long -> b.putLong(k, v)
            is Float -> b.putFloat(k, v)
            is Double -> b.putDouble(k, v)
            is Boolean -> b.putBoolean(k, v)
            is Array<*> -> @Suppress("UNCHECKED_CAST") b.putStringArray(k, v.filterIsInstance<String>().toTypedArray())
            null -> b.putString(k, null)
            else -> b.putString(k, v.toString())
        }
    }
    return b.build()
}

/** androidx.work.Constraints。 */
class Constraints private constructor(
    val requiredNetworkType: NetworkType,
    val requiresCharging: Boolean,
    val requiresDeviceIdle: Boolean,
    val requiresBatteryNotLow: Boolean,
    val requiresStorageNotLow: Boolean,
    val contentTriggerMaxDelayMillis: Long,
    val contentTriggerUpdateDelayMillis: Long,
) {
    enum class NetworkType { NOT_REQUIRED, CONNECTED, UNMETERED, NOT_ROAMING, METERED }

    class Builder {
        private var networkType = NetworkType.NOT_REQUIRED
        private var charging = false
        private var deviceIdle = false
        private var batteryNotLow = false
        private var storageNotLow = false
        private var triggerMaxDelay = -1L
        private var triggerUpdateDelay = -1L

        fun setRequiredNetworkType(networkType: NetworkType): Builder = apply { this.networkType = networkType }
        fun setRequiresCharging(requiresCharging: Boolean): Builder = apply { charging = requiresCharging }
        fun setRequiresDeviceIdle(requiresDeviceIdle: Boolean): Builder = apply { deviceIdle = requiresDeviceIdle }
        fun setRequiresBatteryNotLow(requiresBatteryNotLow: Boolean): Builder = apply { batteryNotLow = requiresBatteryNotLow }
        fun setRequiresStorageNotLow(requiresStorageNotLow: Boolean): Builder = apply { storageNotLow = requiresStorageNotLow }
        fun setTriggerContentMaxDelay(duration: Long, unit: TimeUnit): Builder = apply { triggerMaxDelay = unit.toMillis(duration) }
        fun setTriggerContentUpdateDelay(duration: Long, unit: TimeUnit): Builder = apply { triggerUpdateDelay = unit.toMillis(duration) }
        fun build(): Constraints = Constraints(networkType, charging, deviceIdle, batteryNotLow, storageNotLow, triggerMaxDelay, triggerUpdateDelay)
    }

    companion object {
        @JvmField val NONE = Builder().build()
    }
}

// ---------------- Worker 体系 ----------------

/** androidx.work.ListenableWorker.Result。 */
abstract class ListenableWorker {
    abstract fun startWork(): Result

    /** inputData 属性（真身 getInputData()）。——Nova 注 */
    open val inputData: Data get() = Data.EMPTY

    /** Result：success/failure/retry。 */
    class Result private constructor(
        internal val kind: Kind,
        internal val outputData: Data,
    ) {
        internal enum class Kind { SUCCESS, FAILURE, RETRY }

        companion object {
            @JvmStatic fun success(): Result = Result(Kind.SUCCESS, Data.EMPTY)
            @JvmStatic fun success(outputData: Data): Result = Result(Kind.SUCCESS, outputData)
            @JvmStatic fun failure(): Result = Result(Kind.FAILURE, Data.EMPTY)
            @JvmStatic fun failure(outputData: Data): Result = Result(Kind.FAILURE, outputData)
            @JvmStatic fun retry(): Result = Result(Kind.RETRY, Data.EMPTY)
        }
    }
}

/** androidx.work.WorkerParameters。 */
class WorkerParameters(
    val id: UUID,
    val inputData: Data,
    val tags: Set<String>,
    val runAttemptCount: Int,
    val backgroundExecutor: Executor,
    val taskExecutor: Executor,
    val workerFactory: WorkerFactory,
    val progressUpdater: Any?,
    val foregroundUpdater: Any?,
) {
    @JvmOverloads
    constructor(
        id: UUID,
        inputData: Data,
        tags: Set<String>,
        runAttemptCount: Int,
    ) : this(
        id, inputData, tags, runAttemptCount,
        backgroundExecutor = Executor { it.run() },
        taskExecutor = Executor { it.run() },
        workerFactory = WorkerFactory.getDefaultWorkerFactory(),
        progressUpdater = null,
        foregroundUpdater = null,
    )
}

/** androidx.work.WorkerFactory。 */
open class WorkerFactory {
    open fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? = null

    companion object {
        @JvmStatic
        fun getDefaultWorkerFactory(): WorkerFactory = WorkerFactory()
    }
}

/** androidx.work.CoroutineWorker。 */
abstract class CoroutineWorker(
    val appContext: Context,
    val params: WorkerParameters,
) : ListenableWorker() {

    abstract suspend fun doWork(): Result

    override val inputData: Data get() = params.inputData

    override fun startWork(): Result {
        return kotlinx.coroutines.runBlocking {
            try {
                doWork()
            } catch (t: Throwable) {
                Log.e("CoroutineWorker", "doWork 异常", t)
                Result.failure()
            }
        }
    }

    open suspend fun getForegroundInfo(): Any? = null
    open suspend fun setProgress(data: Data) {}
    open suspend fun setForeground(foregroundInfo: Any) {}
}

// ---------------- WorkRequest ----------------

/** androidx.work.WorkRequest。 */
abstract class WorkRequest internal constructor(
    val id: UUID,
    internal val workerClass: Class<out ListenableWorker>,
    internal val inputData: Data,
    internal val tags: Set<String>,
    internal val constraints: Constraints,
    internal val initialDelayMillis: Long,
    internal val backoffPolicy: BackoffPolicy,
    internal val backoffDelayMillis: Long,
) {
    abstract class Builder<B : Builder<B, W>, W : WorkRequest> internal constructor(
        internal val workerClass: Class<out ListenableWorker>,
    ) {
        internal var inputData: Data = Data.EMPTY
        internal var tags = LinkedHashSet<String>()
        internal var constraints: Constraints = Constraints.NONE
        internal var initialDelayMillis: Long = 0
        internal var backoffPolicy: BackoffPolicy = BackoffPolicy.EXPONENTIAL
        internal var backoffDelayMillis: Long = DEFAULT_BACKOFF_DELAY_MILLIS
        internal var id: UUID = UUID.randomUUID()

        @Suppress("UNCHECKED_CAST")
        protected fun getThis(): B = this as B

        fun setInputData(inputData: Data): B = run { this.inputData = inputData; getThis() }
        fun addTag(tag: String): B = run { tags.add(tag); getThis() }
        fun setConstraints(constraints: Constraints): B = run { this.constraints = constraints; getThis() }
        fun setInitialDelay(duration: Long, unit: TimeUnit): B = run { initialDelayMillis = unit.toMillis(duration); getThis() }
        fun setBackoffCriteria(backoffPolicy: BackoffPolicy, backoffDelay: Long, unit: TimeUnit): B = run {
            this.backoffPolicy = backoffPolicy
            this.backoffDelayMillis = unit.toMillis(backoffDelay)
            getThis()
        }
        fun setId(id: UUID): B = run { this.id = id; getThis() }
        abstract fun build(): W
    }

    companion object {
        const val DEFAULT_BACKOFF_DELAY_MILLIS = 30000L
        const val MIN_BACKOFF_MILLIS = 10000L
        const val MAX_BACKOFF_MILLIS = 5 * 60 * 60 * 1000L
    }
}

/** androidx.work.OneTimeWorkRequest。 */
class OneTimeWorkRequest internal constructor(
    builder: Builder,
) : WorkRequest(
    builder.id, builder.workerClass, builder.inputData, builder.tags,
    builder.constraints, builder.initialDelayMillis, builder.backoffPolicy, builder.backoffDelayMillis,
) {
    class Builder(workerClass: Class<out ListenableWorker>) : WorkRequest.Builder<Builder, OneTimeWorkRequest>(workerClass) {
        override fun build(): OneTimeWorkRequest = OneTimeWorkRequest(this)
    }

    companion object {
        @JvmStatic
        fun from(workerClass: Class<out ListenableWorker>): OneTimeWorkRequest =
            Builder(workerClass).build()
    }
}

/** androidx.work.OneTimeWorkRequestBuilder 顶层函数。 */
@Suppress("UNCHECKED_CAST")
inline fun <reified W : ListenableWorker> OneTimeWorkRequestBuilder(): OneTimeWorkRequest.Builder =
    OneTimeWorkRequest.Builder(W::class.java)

/** androidx.work.PeriodicWorkRequest。 */
class PeriodicWorkRequest internal constructor(
    builder: Builder,
    val repeatIntervalMillis: Long,
) : WorkRequest(
    builder.id, builder.workerClass, builder.inputData, builder.tags,
    builder.constraints, builder.initialDelayMillis, builder.backoffPolicy, builder.backoffDelayMillis,
) {
    class Builder(
        workerClass: Class<out ListenableWorker>,
        repeatInterval: Long,
        repeatIntervalTimeUnit: TimeUnit,
    ) : WorkRequest.Builder<Builder, PeriodicWorkRequest>(workerClass) {
        internal val repeatIntervalMillis: Long = repeatIntervalTimeUnit.toMillis(repeatInterval)
        override fun build(): PeriodicWorkRequest = PeriodicWorkRequest(this, repeatIntervalMillis)
    }

    companion object {
        const val MIN_PERIODIC_INTERVAL_MILLIS = 900000L

        @JvmStatic
        fun from(workerClass: Class<out ListenableWorker>, repeatInterval: Long, repeatIntervalTimeUnit: TimeUnit): PeriodicWorkRequest =
            Builder(workerClass, repeatInterval, repeatIntervalTimeUnit).build()
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified W : ListenableWorker> PeriodicWorkRequestBuilder(
    repeatInterval: Long,
    repeatIntervalTimeUnit: TimeUnit,
): PeriodicWorkRequest.Builder = PeriodicWorkRequest.Builder(W::class.java, repeatInterval, repeatIntervalTimeUnit)

// ---------------- 策略枚举 ----------------

enum class ExistingWorkPolicy { REPLACE, KEEP, APPEND, APPEND_OR_REPLACE }
enum class ExistingPeriodicWorkPolicy { REPLACE, KEEP, CANCEL_AND_REENQUEUE, UPDATE }
enum class OutOfQuotaPolicy { RUN_AS_NON_EXPEDITED_WORK_REQUEST, DROP_WORK_REQUEST }

// ---------------- WorkInfo / Operation ----------------

/** androidx.work.WorkInfo。 */
class WorkInfo(
    val id: UUID,
    val state: State,
    val outputData: Data,
    val tags: Set<String>,
    val runAttemptCount: Int,
) {
    enum class State { ENQUEUED, RUNNING, SUCCEEDED, FAILED, BLOCKED, CANCELLED }
}

/** androidx.work.Operation。 */
interface Operation {
    val state: Any
    val result: Any?

    companion object {
        @JvmField val SUCCESS: Operation = object : Operation {
            override val state: Any = Any()
            override val result: Any? = null
        }
    }
}

// ---------------- Configuration ----------------

/** androidx.work.Configuration。 */
class Configuration private constructor(
    val executor: Executor,
    val taskExecutor: Executor,
    val minimumLoggingLevel: Int,
    val workerFactory: WorkerFactory,
) {
    class Builder {
        private var executor: Executor = Executors.newCachedThreadPool()
        private var taskExecutor: Executor = executor
        private var minimumLoggingLevel = Log.INFO
        private var workerFactory: WorkerFactory = WorkerFactory.getDefaultWorkerFactory()

        fun setExecutor(executor: Executor): Builder = apply { this.executor = executor }
        fun setTaskExecutor(taskExecutor: Executor): Builder = apply { this.taskExecutor = taskExecutor }
        fun setMinimumLoggingLevel(level: Int): Builder = apply { minimumLoggingLevel = level }
        fun setWorkerFactory(workerFactory: WorkerFactory): Builder = apply { this.workerFactory = workerFactory }
        fun build(): Configuration = Configuration(executor, taskExecutor, minimumLoggingLevel, workerFactory)
    }

    /** Provider 接口（app 侧可实现）。 */
    interface Provider {
        val workManagerConfiguration: Configuration
    }
}

// ---------------- WorkManager ----------------

/**
 * androidx.work.WorkManager 桌面版。
 * enqueue 后在后台线程池真实执行 worker（runBlocking 调 doWork）。
 */
abstract class WorkManager {

    abstract fun enqueue(requests: List<WorkRequest>): Operation
    abstract fun enqueueUniqueWork(uniqueWorkName: String, existingWorkPolicy: ExistingWorkPolicy, requests: List<WorkRequest>): Operation
    abstract fun enqueueUniquePeriodicWork(uniqueWorkName: String, existingPeriodicWorkPolicy: ExistingPeriodicWorkPolicy, request: PeriodicWorkRequest): Operation
    abstract fun cancelAllWork(): Operation
    abstract fun cancelAllWorkByTag(tag: String): Operation
    abstract fun cancelUniqueWork(uniqueWorkName: String): Operation
    abstract fun getWorkInfoByIdFlow(id: UUID): Flow<WorkInfo>
    abstract fun pruneWork(): Operation

    companion object {
        @Volatile
        private var instance: WorkManager? = null

        @JvmStatic
        fun getInstance(context: Context): WorkManager =
            instance ?: synchronized(this) {
                instance ?: DesktopWorkManager(context.applicationContext).also { instance = it }
            }

        @JvmStatic
        fun isInitialized(): Boolean = instance != null

        @JvmStatic
        fun enableSingleton(configuration: Configuration) {}
    }
}

/**
 * 桌面 WorkManager 实现：线程池 + runBlocking。
 */
internal class DesktopWorkManager(private val context: Context) : WorkManager() {

    private val executor = Executors.newCachedThreadPool { r ->
        Thread(r, "workmanager-worker").apply { isDaemon = true }
    }

    override fun enqueue(requests: List<WorkRequest>): Operation {
        for (req in requests) executeRequest(req)
        return Operation.SUCCESS
    }

    override fun enqueueUniqueWork(uniqueWorkName: String, existingWorkPolicy: ExistingWorkPolicy, requests: List<WorkRequest>): Operation {
        Log.d("WorkManager", "enqueueUniqueWork($uniqueWorkName, $existingWorkPolicy)")
        return enqueue(requests)
    }

    override fun enqueueUniquePeriodicWork(uniqueWorkName: String, existingPeriodicWorkPolicy: ExistingPeriodicWorkPolicy, request: PeriodicWorkRequest): Operation {
        Log.d("WorkManager", "enqueueUniquePeriodicWork($uniqueWorkName) — 桌面简化为单次执行")
        executeRequest(request)
        return Operation.SUCCESS
    }

    override fun cancelAllWork(): Operation {
        Log.d("WorkManager", "cancelAllWork no-op")
        return Operation.SUCCESS
    }

    override fun cancelAllWorkByTag(tag: String): Operation = Operation.SUCCESS

    override fun cancelUniqueWork(uniqueWorkName: String): Operation = Operation.SUCCESS

    override fun getWorkInfoByIdFlow(id: UUID): Flow<WorkInfo> = flowOf()

    override fun pruneWork(): Operation = Operation.SUCCESS

    private fun executeRequest(request: WorkRequest) {
        executor.execute {
            try {
                Log.i("WorkManager", "开始执行 ${request.workerClass.simpleName} (id=${request.id})")
                val worker = instantiateWorker(request)
                val result = worker.startWork()
                Log.i("WorkManager", "${request.workerClass.simpleName} 完成: $result")
            } catch (t: Throwable) {
                Log.e("WorkManager", "${request.workerClass.simpleName} 执行失败", t)
            }
        }
    }

    private fun instantiateWorker(request: WorkRequest): ListenableWorker {
        val params = WorkerParameters(
            id = request.id,
            inputData = request.inputData,
            tags = request.tags,
            runAttemptCount = 0,
        )
        return try {
            request.workerClass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
                .newInstance(context, params)
        } catch (e: NoSuchMethodException) {
            // 兼容 (Context, WorkerParameters) 之外的构造器签名
            request.workerClass.getDeclaredConstructor().newInstance()
        }
    }
}
