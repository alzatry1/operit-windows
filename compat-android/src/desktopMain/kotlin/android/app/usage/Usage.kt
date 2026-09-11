package android.app.usage

/**
 * android.app.usage（P3-B2 新增，轻 stub）。
 * 桌面无使用统计服务：query 全部返回空。
 */

/** android.app.usage.UsageStats。 */
open class UsageStats {
    open var packageName: String? = null
    open var totalTimeInForeground: Long = 0L
    open var totalTimeVisible: Long = 0L
    open var lastTimeUsed: Long = 0L
    open var firstTimeStamp: Long = 0L
    open var lastTimeStamp: Long = 0L
    open var lastTimeForegroundServiceUsed: Long = 0L
    open var totalTimeForegroundServiceUsed: Long = 0L
    open var lastTimeAnyComponentUsed: Long = 0L

    open fun add(other: UsageStats?) {}
}

/** android.app.usage.UsageEvents。 */
open class UsageEvents {

    /** android.app.usage.UsageEvents.Event。 */
    open class Event {
        open var packageName: String? = null
        open var className: String? = null
        open var timeStamp: Long = 0L
        open var eventType: Int = 0
        open var instanceId: Int = 0
        open var taskRootPackageName: String? = null
        open var taskRootClassName: String? = null

        companion object {
            const val MOVE_TO_FOREGROUND = 1
            const val MOVE_TO_BACKGROUND = 2
            const val END_OF_DAY = 3
            const val CONTINUE_PREVIOUS_DAY = 4
            const val USER_INTERACTION = 5
            const val SHORTCUT_INVOCATION = 8
            const val STANDBY_BUCKET_CHANGED = 11
            const val SCREEN_INTERACTIVE = 15
            const val SCREEN_NON_INTERACTIVE = 16
            const val KEYGUARD_SHOWN = 17
            const val KEYGUARD_HIDDEN = 18
            const val FOREGROUND_SERVICE_START = 19
            const val FOREGROUND_SERVICE_STOP = 20
        }
    }

    open fun hasNextEvent(): Boolean = false
    open fun getNextEvent(eventOut: Event?): Boolean = false
    open fun resetToStart() {}
}

/** android.app.usage.UsageStatsManager。 */
class UsageStatsManager private constructor() {

    fun queryUsageStats(intervalType: Int, beginTime: Long, endTime: Long): List<UsageStats> = emptyList()

    fun queryEvents(beginTime: Long, endTime: Long): UsageEvents = UsageEvents()

    fun queryEventsForSelf(beginTime: Long, endTime: Long): UsageEvents = UsageEvents()

    fun isAppInactive(packageName: String?): Boolean = false

    fun getAppStandbyBucket(): Int = STANDBY_BUCKET_ACTIVE

    companion object {
        const val INTERVAL_DAILY = 0
        const val INTERVAL_WEEKLY = 1
        const val INTERVAL_MONTHLY = 2
        const val INTERVAL_YEARLY = 3
        const val INTERVAL_BEST = 4

        const val STANDBY_BUCKET_ACTIVE = 10
        const val STANDBY_BUCKET_WORKING_SET = 20
        const val STANDBY_BUCKET_FREQUENT = 30
        const val STANDBY_BUCKET_RARE = 40
        const val STANDBY_BUCKET_RESTRICTED = 45
        const val STANDBY_BUCKET_EXEMPTED = 5
    }
}
