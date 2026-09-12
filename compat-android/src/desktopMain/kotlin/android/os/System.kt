package android.os

import java.io.File
import java.lang.management.ManagementFactory

/** android.os.SystemClock：单调时钟。 */
object SystemClock {
    private val runtimeMx = ManagementFactory.getRuntimeMXBean()

    @JvmStatic fun uptimeMillis(): Long = runtimeMx.uptime
    @JvmStatic fun elapsedRealtime(): Long = runtimeMx.uptime
    @JvmStatic fun elapsedRealtimeNanos(): Long = runtimeMx.uptime * 1_000_000L
    @JvmStatic fun currentThreadTimeMillis(): Long = try {
        ManagementFactory.getThreadMXBean().currentThreadCpuTime / 1_000_000L
    } catch (e: Throwable) { uptimeMillis() }
    @JvmStatic fun sleep(ms: Long) { try { Thread.sleep(ms) } catch (e: InterruptedException) { Thread.currentThread().interrupt() } }
    @JvmStatic fun setCurrentTimeMillis(millis: Long): Boolean = false
}

/** android.os.Process。 */
object Process {
    const val THREAD_PRIORITY_DEFAULT = 0
    const val THREAD_PRIORITY_LOWEST = 19
    const val THREAD_PRIORITY_BACKGROUND = 10
    const val THREAD_PRIORITY_FOREGROUND = -2
    const val THREAD_PRIORITY_DISPLAY = -4
    const val THREAD_PRIORITY_URGENT_DISPLAY = -8
    const val THREAD_PRIORITY_AUDIO = -16
    const val THREAD_PRIORITY_URGENT_AUDIO = -19
    const val THREAD_PRIORITY_MORE_FAVORABLE = -1
    const val THREAD_PRIORITY_LESS_FAVORABLE = 1

    const val SIGNAL_QUIT = 3
    const val SIGNAL_KILL = 9
    const val SIGNAL_USR1 = 10
    const val SIGNAL_TERM = 15

    const val SYSTEM_UID = 1000
    const val FIRST_APPLICATION_UID = 10000

    @JvmStatic fun myPid(): Int = try { ProcessHandle.current().pid().toInt() } catch (e: Throwable) { -1 }
    @JvmStatic fun myTid(): Int = Thread.currentThread().id.toInt()
    @JvmStatic fun myUid(): Int = FIRST_APPLICATION_UID
    @JvmStatic fun myUserHandle(): UserHandle = UserHandle.SYSTEM
    @JvmStatic fun isApplicationUid(uid: Int): Boolean = uid >= FIRST_APPLICATION_UID
    @JvmStatic fun setThreadPriority(priority: Int) { /* no-op */ }
    @JvmStatic fun setThreadPriority(tid: Int, priority: Int) { /* no-op */ }
    @JvmStatic fun getThreadPriority(tid: Int): Int = THREAD_PRIORITY_DEFAULT
    @JvmStatic fun killProcess(pid: Int) { android.util.Log.w("Process", "killProcess($pid) no-op on desktop") }
    @JvmStatic fun sendSignal(pid: Int, signal: Int) { /* no-op */ }
    @JvmStatic fun getStartElapsedRealtime(): Long = SystemClock.uptimeMillis()
    @JvmStatic fun getStartRequestedElapsedRealtime(): Long = SystemClock.uptimeMillis()
    @JvmStatic fun supportsProcesses(): Boolean = true
}

/** android.os.UserHandle。 */
class UserHandle(private val identifier: Int) : Parcelable {
    fun getIdentifier(): Int = identifier
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeInt(identifier) }
    override fun equals(other: Any?): Boolean = other is UserHandle && other.identifier == identifier
    override fun hashCode(): Int = identifier
    override fun toString(): String = "UserHandle{$identifier}"

    companion object {
        @JvmField val SYSTEM = UserHandle(0)
        @JvmField val ALL = UserHandle(-1)
        @JvmField val CURRENT = UserHandle(-2)
        @JvmField val CURRENT_OR_SELF = UserHandle(-3)
        @JvmField val OWNER = UserHandle(0)
        @JvmStatic fun of(userId: Int): UserHandle = UserHandle(userId)
        @JvmStatic fun getUserHandle(uid: Int): UserHandle = UserHandle(uid / 100000)
        @JvmStatic fun getUserId(uid: Int): Int = uid / 100000
        @JvmStatic fun getAppId(uid: Int): Int = uid % 100000
        @JvmStatic fun isSameUser(uid1: Int, uid2: Int): Boolean = getUserId(uid1) == getUserId(uid2)
        @JvmStatic fun isSameApp(uid1: Int, uid2: Int): Boolean = getAppId(uid1) == getAppId(uid2)
    }
}

/** android.os.Environment：映射到 ~/.operit/ 目录树。 */
object Environment {
    const val DIRECTORY_MUSIC = "Music"
    const val DIRECTORY_PODCASTS = "Podcasts"
    const val DIRECTORY_RINGTONES = "Ringtones"
    const val DIRECTORY_ALARMS = "Alarms"
    const val DIRECTORY_NOTIFICATIONS = "Notifications"
    const val DIRECTORY_PICTURES = "Pictures"
    const val DIRECTORY_MOVIES = "Movies"
    const val DIRECTORY_DOWNLOADS = "Download"
    const val DIRECTORY_DCIM = "DCIM"
    const val DIRECTORY_DOCUMENTS = "Documents"
    const val DIRECTORY_SCREENSHOTS = "Screenshots"
    const val DIRECTORY_AUDIOBOOKS = "Audiobooks"
    const val DIRECTORY_RECORDINGS = "Recordings"

    const val MEDIA_UNKNOWN = "unknown"
    const val MEDIA_REMOVED = "removed"
    const val MEDIA_UNMOUNTED = "unmounted"
    const val MEDIA_CHECKING = "checking"
    const val MEDIA_NOFS = "nofs"
    const val MEDIA_MOUNTED = "mounted"
    const val MEDIA_MOUNTED_READ_ONLY = "mounted_ro"
    const val MEDIA_SHARED = "shared"
    const val MEDIA_BAD_REMOVAL = "bad_removal"
    const val MEDIA_UNMOUNTABLE = "unmountable"
    const val MEDIA_EJECTING = "ejecting"

    private val base: File by lazy {
        File(System.getProperty("user.home"), ".operit").apply { mkdirs() }
    }

    @JvmStatic
    fun getExternalStorageDirectory(): File = File(base, "external").apply { mkdirs() }

    @JvmStatic
    fun getExternalStoragePublicDirectory(type: String): File =
        File(getExternalStorageDirectory(), type).apply { mkdirs() }

    @JvmStatic fun getDataDirectory(): File = File(base, "data").apply { mkdirs() }
    @JvmStatic fun getDownloadCacheDirectory(): File = File(base, "cache").apply { mkdirs() }
    @JvmStatic fun getRootDirectory(): File = File(base, "root").apply { mkdirs() }
    @JvmStatic fun getStorageDirectory(): File = getExternalStorageDirectory()

    @JvmStatic fun getExternalStorageState(): String = MEDIA_MOUNTED
    @JvmStatic fun getExternalStorageState(path: File?): String = MEDIA_MOUNTED
    @JvmStatic fun getStorageState(path: File?): String = MEDIA_MOUNTED

    @JvmStatic fun isExternalStorageManager(): Boolean = true
    @JvmStatic fun isExternalStorageManager(path: File?): Boolean = true
    @JvmStatic fun isExternalStorageEmulated(): Boolean = false
    @JvmStatic fun isExternalStorageEmulated(path: File?): Boolean = false
    @JvmStatic fun isExternalStorageRemovable(): Boolean = false
    @JvmStatic fun isExternalStorageRemovable(path: File?): Boolean = false
    @JvmStatic fun isExternalStorageLegacy(): Boolean = false

    @JvmStatic fun getExternalCacheDirs(): Array<File> = arrayOf(File(base, "external-cache").apply { mkdirs() })
    @JvmStatic fun getExternalMediaDirs(): Array<File> = arrayOf(File(base, "external/media").apply { mkdirs() })
}

/** android.os.StatFs：真实查询文件系统容量。 */
class StatFs {
    private var file: File

    constructor(path: String) { file = File(path) }

    private fun ensure() { file.mkdirs() }

    fun reStat(path: String) { file = File(path) }

    fun getBlockSize(): Int = 4096
    /** StatFs.blockSizeLong（块大小，桌面恒 4096；Kotlin 类需真属性才能被 statFs.blockSizeLong 访问）。——Nova 注 */
    val blockSizeLong: Long get() = 4096L
    fun getBlockCount(): Int = (getBlockCountLong() / 1).coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
    fun getBlockCountLong(): Long = totalSpace() / 4096
    fun getFreeBlocks(): Int = (getFreeBlocksLong()).coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
    fun getFreeBlocksLong(): Long = freeSpace() / 4096
    fun getFreeBytes(): Long = freeSpace()
    fun getAvailableBlocks(): Int = (getAvailableBlocksLong()).coerceIn(0, Int.MAX_VALUE.toLong()).toInt()
    fun getAvailableBlocksLong(): Long = usableSpace() / 4096
    fun getAvailableBytes(): Long = usableSpace()
    fun getTotalBytes(): Long = totalSpace()
    fun getUsedBytes(): Long = totalSpace() - freeSpace()

    private fun totalSpace(): Long = try { ensure(); file.totalSpace } catch (e: Throwable) { 512L * 1024 * 1024 * 1024 }
    private fun freeSpace(): Long = try { file.freeSpace } catch (e: Throwable) { 256L * 1024 * 1024 * 1024 }
    private fun usableSpace(): Long = try { file.usableSpace } catch (e: Throwable) { 256L * 1024 * 1024 * 1024 }

    override fun toString(): String = "StatFs{path=${file.path}, total=${totalSpace()}, free=${freeSpace()}}"
}

/** android.os.Debug。 */
object Debug {
    class MemoryInfo {
        var dalvikPss: Int = 0
        var dalvikPrivateDirty: Int = 0
        var dalvikSharedDirty: Int = 0
        var nativePss: Int = 0
        var nativePrivateDirty: Int = 0
        var nativeSharedDirty: Int = 0
        var otherPss: Int = 0
        var otherPrivateDirty: Int = 0
        var otherSharedDirty: Int = 0
        fun getTotalPss(): Int = dalvikPss + nativePss + otherPss
        fun getTotalPrivateDirty(): Int = dalvikPrivateDirty + nativePrivateDirty + otherPrivateDirty
        fun getTotalSharedDirty(): Int = dalvikSharedDirty + nativeSharedDirty + otherSharedDirty
        fun readFromParcel(source: Parcel) {}
    }

    @JvmStatic fun waitForDebugger() {}
    @JvmStatic fun waitingForDebugger(): Boolean = false
    @JvmStatic fun isDebuggerConnected(): Boolean =
        ManagementFactory.getRuntimeMXBean().inputArguments.any { it.contains("jdwp") }
    @JvmStatic fun startMethodTracing() {}
    @JvmStatic fun startMethodTracing(tracePath: String?) {}
    @JvmStatic fun startMethodTracing(tracePath: String?, bufferSize: Int) {}
    @JvmStatic fun stopMethodTracing() {}
    @JvmStatic fun startAllocCounting() {}
    @JvmStatic fun stopAllocCounting() {}
    @JvmStatic fun getGlobalAllocCount(): Int = 0
    @JvmStatic fun getGlobalAllocSize(): Int = 0
    @JvmStatic fun getGlobalFreedCount(): Int = 0
    @JvmStatic fun getGlobalFreedSize(): Int = 0
    @JvmStatic fun getThreadAllocCount(): Int = 0
    @JvmStatic fun getThreadAllocSize(): Int = 0
    @JvmStatic fun resetAllCounts() {}
    @JvmStatic fun threadCpuTimeNanos(): Long = try {
        ManagementFactory.getThreadMXBean().currentThreadCpuTime
    } catch (e: Throwable) { 0L }
    @JvmStatic fun getNativeHeapSize(): Long = Runtime.getRuntime().totalMemory()
    @JvmStatic fun getNativeHeapAllocatedSize(): Long = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
    @JvmStatic fun getNativeHeapFreeSize(): Long = Runtime.getRuntime().freeMemory()
    @JvmStatic fun getPss(): Long = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024
    @JvmStatic fun getMemoryInfo(memoryInfo: MemoryInfo) {
        val usedKb = ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024).toInt()
        memoryInfo.dalvikPss = usedKb
        memoryInfo.dalvikPrivateDirty = usedKb
    }
    @JvmStatic fun getRuntimeStat(statName: String?): String? = null
    @JvmStatic fun getRuntimeStats(): Map<String, String> = emptyMap()
    @JvmStatic fun dumpHprofData(fileName: String?) {}
    @JvmStatic fun dumpService(fileName: String?, fd: java.io.FileDescriptor?, args: Array<String>?): Boolean = false
    @JvmStatic fun getBinderDeathObjectCount(): Int = 0
    @JvmStatic fun getBinderReceivedTransactions(): Int = 0
    @JvmStatic fun getBinderSentTransactions(): Int = 0
    @JvmStatic fun getLoadedClassCount(): Int = ManagementFactory.getClassLoadingMXBean().loadedClassCount
    @JvmStatic fun printLoadedClasses(flags: Int) {}
    @JvmStatic fun enableEmulatorTraceOutput() {}
    @JvmStatic fun changeDebugPort(port: Int) {}
    @Deprecated("deprecated") @JvmStatic fun getGlobalExternalAllocCount(): Int = 0
    @Deprecated("deprecated") @JvmStatic fun getGlobalExternalAllocSize(): Int = 0
    @Deprecated("deprecated") @JvmStatic fun getGlobalExternalFreedCount(): Int = 0
    @Deprecated("deprecated") @JvmStatic fun getGlobalExternalFreedSize(): Int = 0
    @Deprecated("deprecated") @JvmStatic fun getThreadExternalAllocCount(): Int = 0
    @Deprecated("deprecated") @JvmStatic fun getThreadExternalAllocSize(): Int = 0
}
