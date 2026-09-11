package android.app

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentSender
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

/** android.app.Application：全局应用单例（AppGlobals.applicationContext 的类型）。 */
open class Application : Context(), ComponentCallbacks2 {

    interface ActivityLifecycleCallbacks {
        fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        fun onActivityStarted(activity: Activity) {}
        fun onActivityResumed(activity: Activity) {}
        fun onActivityPaused(activity: Activity) {}
        fun onActivityStopped(activity: Activity) {}
        fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        fun onActivityDestroyed(activity: Activity) {}
    }

    private val componentCallbacks = CopyOnWriteArrayList<ComponentCallbacks2>()
    private val lifecycleCallbacks = CopyOnWriteArrayList<ActivityLifecycleCallbacks>()

    override val applicationContext: Context
        get() = this

    open fun onCreate() {}

    open fun onTerminate() {}

    override fun onConfigurationChanged(newConfig: Configuration) {
        componentCallbacks.forEach { it.onConfigurationChanged(newConfig) }
    }

    override fun onLowMemory() {
        componentCallbacks.forEach { it.onLowMemory() }
    }

    override fun onTrimMemory(level: Int) {
        componentCallbacks.forEach { it.onTrimMemory(level) }
    }

    open fun registerComponentCallbacks(callback: ComponentCallbacks2) {
        if (!componentCallbacks.contains(callback)) componentCallbacks.add(callback)
    }

    open fun unregisterComponentCallbacks(callback: ComponentCallbacks2) {
        componentCallbacks.remove(callback)
    }

    open fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        if (!lifecycleCallbacks.contains(callback)) lifecycleCallbacks.add(callback)
    }

    open fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        lifecycleCallbacks.remove(callback)
    }

    open fun onProvideAssistData(assistContent: Any?) {}

    companion object {
        fun getProcessName(): String = com.ai.assistance.operit.compat.AppGlobals.PACKAGE_NAME
    }
}

/** android.app.Service。 */
abstract class Service : ContextWrapper(null), ComponentCallbacks2 {
    private val handler by lazy { Handler(Looper.getMainLooper()) }


    /** 被系统调用绑定。 */
    abstract fun onBind(intent: Intent?): IBinder?

    open fun onUnbind(intent: Intent): Boolean = false
    open fun onRebind(intent: Intent) {}
    open fun onCreate() {}
    open fun onDestroy() {}
    open fun onStart(intent: Intent?, startId: Int) {}
    open fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    open fun onTaskRemoved(rootIntent: Intent?) {}
    override fun onConfigurationChanged(newConfig: Configuration) {}
    override fun onLowMemory() {}
    override fun onTrimMemory(level: Int) {}

    open fun stopSelf() {}
    open fun stopSelf(startId: Int) {}
    open fun stopSelfResult(startId: Int): Boolean = true

    open fun startForeground(id: Int, notification: Notification) {
        Log.d("Service", "startForeground($id) no-op")
    }

    open fun startForeground(id: Int, notification: Notification, foregroundServiceType: Int) {
        startForeground(id, notification)
    }

    open fun stopForeground(removeNotification: Boolean) {}
    open fun stopForeground(flags: Int) {}

    open fun getApplication(): Application = applicationContext as Application

    companion object {
        const val START_STICKY_COMPATIBILITY = 0
        const val START_STICKY = 1
        const val START_NOT_STICKY = 2
        const val START_REDELIVER_INTENT = 3
        const val START_CONTINUATION_MASK = 0x0f
        const val START_FLAG_REDELIVERY = 0x0001
        const val START_FLAG_RETRY = 0x0002
        const val START_FOREGROUND_REMOVE = 1
        const val START_FOREGROUND_DETACH = 2
        const val STOP_FOREGROUND_REMOVE = 1
        const val STOP_FOREGROUND_DETACH = 2
    }
}

/** android.app.ActivityManager。 */
open class ActivityManager {

    open class MemoryInfo {
        var availMem: Long = 0
        var totalMem: Long = 0
        var threshold: Long = 0
        var lowMemory: Boolean = false
        var foregroundServicesThreshold: Long = 0

        fun readFromParcel(source: android.os.Parcel) {}
    }

    class RunningAppProcessInfo {
        var processName: String = "main"
        var pid: Int = android.os.Process.myPid()
        var uid: Int = android.os.Process.myUid()
        var importance: Int = IMPORTANCE_FOREGROUND
        var pkgList: Array<String> = arrayOf("com.ai.assistance.operit")
    }

    class RunningServiceInfo {
        var process: String = "main"
        var pid: Int = android.os.Process.myPid()
        var service: android.content.ComponentName? = null
    }

    class AppTask {
        val taskInfo: Any? = null
        open fun setExcludeFromRecents(exclude: Boolean) {}
        open fun moveToFront() {}
        open fun finishAndRemoveTask() {}
    }

    class RecentTaskInfo {
        var id: Int = 0
        var persistentId: Int = 0
        var baseIntent: Intent? = null
        var origActivity: android.content.ComponentName? = null
        var description: CharSequence? = null
    }

    class ProcessErrorStateInfo {
        var condition: Int = 0
        var processName: String = ""
        var pid: Int = 0
        var uid: Int = 0
        var tag: String? = null
        var shortMsg: String = ""
        var longMsg: String = ""
        var crashData: ByteArray? = null
    }

    class AppExitInfo {
        val reason: Int = 0
        val importance: Int = 0
        val pss: Long = 0
        val rss: Long = 0
        val timestamp: Long = 0
        val description: String? = null
        val processName: String = ""
        val pid: Int = 0
        val realUid: Int = 0
        val packageUid: Int = 0
        val definingUid: Int = 0
        val status: Int = 0
        val traceInputStream: java.io.InputStream? = null
    }

    class RunningTaskInfo {
        var id: Int = 0
        var taskId: Int = 0
        var baseActivity: android.content.ComponentName? = null
        var topActivity: android.content.ComponentName? = null
        var numActivities: Int = 0
        var description: CharSequence? = null
        var isRunning: Boolean = false
        var isVisible: Boolean = false
        var isFocused: Boolean = false
    }

    class DeviceConfigurationInfo {
        var reqTouchScreen: Int = 0
        var reqKeyboardType: Int = 0
        var reqNavigation: Int = 0
        var reqInputFeatures: Int = 0
        var reqGlEsVersion: Int = 0
        fun getGlEsVersion(): String = "3.2"
    }

    open fun getMemoryInfo(outInfo: MemoryInfo) {
        val runtime = Runtime.getRuntime()
        outInfo.availMem = runtime.freeMemory()
        outInfo.totalMem = runtime.totalMemory()
        outInfo.lowMemory = false
        outInfo.threshold = 0
        outInfo.foregroundServicesThreshold = 0
    }

    open fun getDeviceMemoryInfo(outInfo: MemoryInfo) = getMemoryInfo(outInfo)
    open fun getMemoryClass(): Int = 256
    open fun getLargeMemoryClass(): Int = 512
    open fun isLowRamDevice(): Boolean = false
    open fun isHighEndGfx(): Boolean = true
    open fun isUserAMonkey(): Boolean = false
    open fun isRunningInTestHarness(): Boolean = false
    open fun isRunningInUserTestHarness(): Boolean = false
    open fun getLauncherLargeIconDensity(): Int = 0
    open fun getLauncherLargeIconSize(): Int = 0
    open fun clearApplicationUserData(): Boolean = false
    open fun clearWatchHeapAlloc(heapFd: android.os.ParcelFileDescriptor) {}
    open fun getHistoricalProcessExitReasons(packageName: String?, pid: Int, maxNum: Int): List<AppExitInfo> = emptyList()
    open fun getHistoricalProcessExitReasons(packageName: String?, pid: Int, maxNum: Int, filter: Bundle?): List<AppExitInfo> = emptyList()
    open fun getMyMemoryState(outState: RunningAppProcessInfo) {
        outState.pid = android.os.Process.myPid()
        outState.uid = android.os.Process.myUid()
    }
    open fun getProcessMemoryInfo(pids: IntArray): Array<android.os.Debug.MemoryInfo> =
        Array(pids.size) { android.os.Debug.MemoryInfo() }
    open fun getAppTasks(): List<AppTask> = emptyList()
    /** runningAppProcesses 属性（app 用 activityManager.runningAppProcesses）。——Nova 注 */
    open val runningAppProcesses: List<RunningAppProcessInfo> get() = emptyList()
    open fun getRunningServices(maxNum: Int): List<RunningServiceInfo> = emptyList()
    open fun getRunningTasks(maxNum: Int): List<RunningTaskInfo> = emptyList()
    open fun killBackgroundProcesses(packageName: String) { Log.d("ActivityManager", "killBackgroundProcesses($packageName) no-op") }
    open fun restartPackage(packageName: String) { Log.d("ActivityManager", "restartPackage($packageName) no-op") }
    open fun moveTaskToFront(taskId: Int, flags: Int) {}
    open fun moveTaskToFront(taskId: Int, flags: Int, options: Bundle?) {}
    open fun appNotResponding(reason: String) {}
    open fun appNotResponding(reason: String, packageName: String?) {}
    open fun addAppTask(activity: Any, intent: Intent, description: Any?, thumbnail: android.graphics.Bitmap?): Int = -1
    open fun setVrThread(tid: Int) {}
    open fun setSchedulingGroup(group: Int) {}
    open fun getProcessErrorStateInfo(pid: Int, pss: LongArray?): ProcessErrorStateInfo? = null
    open fun getProcessesInErrorState(): List<ProcessErrorStateInfo>? = null
    open fun getPackageImportance(packageName: String): Int = IMPORTANCE_FOREGROUND
    open fun getUidImportance(uid: Int): Int = IMPORTANCE_FOREGROUND
    open fun isBackgroundRestricted(): Boolean = false
    open fun getSystemServiceFeatureList(): List<String> = emptyList()

    companion object {
        const val IMPORTANCE_FOREGROUND = 100
        const val IMPORTANCE_FOREGROUND_SERVICE = 125
        const val IMPORTANCE_TOP_SLEEPING = 325
        const val IMPORTANCE_VISIBLE = 200
        const val IMPORTANCE_PERCEPTIBLE = 230
        const val IMPORTANCE_CANT_SAVE_STATE = 270
        const val IMPORTANCE_SERVICE = 300
        const val IMPORTANCE_CACHED = 400
        const val IMPORTANCE_GONE = 1000
        const val IMPORTANCE_EMPTY = 500
        const val MOVE_TASK_WITH_HOME = 1
        const val MOVE_TASK_NO_USER_ACTION = 2
        const val RECENT_WITH_EXCLUDED = 1
        const val RECENT_IGNORE_UNAVAILABLE = 2
        const val LOCK_TASK_MODE_NONE = 0
        const val LOCK_TASK_MODE_LOCKED = 1
        const val LOCK_TASK_MODE_PINNED = 2
        const val LOCK_TASK_MODE_ALWAYS = 3
        const val PROCESS_STATE_NONEXISTENT = -1
        const val PROCESS_STATE_IMPORTANT_FOREGROUND = 400
        const val PROCESS_STATE_TOP = 378
    }
}

/** android.app.Notification。 */
open class Notification : android.os.Parcelable {
    var `when`: Long = 0
    var icon: Int = 0
    var iconLevel: Int = 0
    var number: Int = 0
    var contentIntent: PendingIntent? = null
    var deleteIntent: PendingIntent? = null
    var fullScreenIntent: PendingIntent? = null
    var tickerText: CharSequence? = null
    var contentView: Any? = null
    var bigContentView: Any? = null
    var headsUpContentView: Any? = null
    var audioAttributes: android.media.AudioAttributes? = null
    var sound: android.net.Uri? = null
    var vibrate: LongArray? = null
    var ledARGB: Int = 0
    var ledOnMS: Int = 0
    var ledOffMS: Int = 0
    var defaults: Int = 0
    var flags: Int = 0
    var color: Int = 0
    var category: String? = null
    var group: String? = null
    var sortKey: String? = null
    var extras: Bundle = Bundle()
    var actions: Array<Action>? = null
    var visibility: Int = VISIBILITY_PRIVATE
    var publicVersion: Notification? = null
    var priority: Int = PRIORITY_DEFAULT
    var badgeIconType: Int = BADGE_ICON_NONE
    var mChannelId: String? = null
    var channelId: String?
        get() = mChannelId
        set(value) { mChannelId = value }
    var timeoutAfter: Long = 0
    var showWhen: Boolean = true
    var usesChronometer: Boolean = false
    var ongoing: Boolean = false
    var autoCancel: Boolean = false
    var subText: CharSequence? = null
    var infoText: CharSequence? = null

    class Action(
        var icon: Int = 0,
        var title: CharSequence? = null,
        var actionIntent: PendingIntent? = null,
    ) {
        open class Builder {
            private val action = Action()
            fun build(): Action = action
        }
    }

    open class Style {
        open fun setBuilder(builder: Notification.Builder) {}
    }

    open class BigTextStyle : Style() {
        open fun bigText(cs: CharSequence?): BigTextStyle = this
        open fun setBigContentTitle(title: CharSequence?): BigTextStyle = this
        open fun setSummaryText(cs: CharSequence?): BigTextStyle = this
    }

    open class BigPictureStyle : Style() {
        open fun bigPicture(b: android.graphics.Bitmap?): BigPictureStyle = this
        open fun setBigContentTitle(title: CharSequence?): BigPictureStyle = this
    }

    open class InboxStyle : Style() {
        open fun addLine(cs: CharSequence?): InboxStyle = this
        open fun setBigContentTitle(title: CharSequence?): InboxStyle = this
    }

    open class MessagingStyle(personDisplayName: CharSequence) : Style() {
        open class Message(text: CharSequence, timestamp: Long, sender: CharSequence?) {
            val text: CharSequence = text
            val timestamp: Long = timestamp
        }
        open fun addMessage(text: CharSequence, timestamp: Long, sender: CharSequence?): MessagingStyle = this
    }

    open class DecoratedCustomViewStyle : Style()

    open class Builder {
        private val notification = Notification()
        private var context: Context? = null

        constructor(context: Context) { this.context = context }
        constructor(context: Context, channelId: String) { this.context = context; notification.mChannelId = channelId }
        constructor(context: Context, notification: Notification) { this.context = context; this.notification.flags = notification.flags }

        fun setWhen(`when`: Long): Builder = apply { notification.`when` = `when` }
        fun setShowWhen(show: Boolean): Builder = apply { notification.showWhen = show }
        fun setUsesChronometer(b: Boolean): Builder = apply { notification.usesChronometer = b }
        fun setSmallIcon(icon: Int): Builder = apply { notification.icon = icon }
        fun setSmallIcon(icon: Int, level: Int): Builder = apply { notification.icon = icon; notification.iconLevel = level }
        fun setContentTitle(title: CharSequence?): Builder = apply { notification.extras.putCharSequence(EXTRA_TITLE, title) }
        fun setContentText(text: CharSequence?): Builder = apply { notification.extras.putCharSequence(EXTRA_TEXT, text) }
        fun setSubText(text: CharSequence?): Builder = apply { notification.subText = text }
        fun setNumber(number: Int): Builder = apply { notification.number = number }
        fun setContentInfo(info: CharSequence?): Builder = apply { notification.infoText = info }
        fun setProgress(max: Int, progress: Int, indeterminate: Boolean): Builder = apply {
            notification.extras.putInt(EXTRA_PROGRESS, progress)
            notification.extras.putInt(EXTRA_PROGRESS_MAX, max)
            notification.extras.putBoolean(EXTRA_PROGRESS_INDETERMINATE, indeterminate)
        }
        fun setContent(contentView: Any?): Builder = apply { notification.contentView = contentView }
        fun setContentIntent(intent: PendingIntent?): Builder = apply { notification.contentIntent = intent }
        fun setDeleteIntent(intent: PendingIntent?): Builder = apply { notification.deleteIntent = intent }
        fun setFullScreenIntent(intent: PendingIntent?, highPriority: Boolean): Builder = apply { notification.fullScreenIntent = intent }
        fun setTicker(tickerText: CharSequence?): Builder = apply { notification.tickerText = tickerText }
        fun setTicker(tickerText: CharSequence?, views: Any?): Builder = setTicker(tickerText)
        fun setLargeIcon(b: android.graphics.Bitmap?): Builder = this
        fun setSound(sound: android.net.Uri?): Builder = apply { notification.sound = sound }
        fun setSound(sound: android.net.Uri?, streamType: Int): Builder = apply { notification.sound = sound }
        fun setVibrate(pattern: LongArray?): Builder = apply { notification.vibrate = pattern }
        fun setLights(argb: Int, onMs: Int, offMs: Int): Builder = apply { notification.ledARGB = argb; notification.ledOnMS = onMs; notification.ledOffMS = offMs }
        fun setOngoing(ongoing: Boolean): Builder = apply { notification.ongoing = ongoing }
        fun setOnlyAlertOnce(onlyAlertOnce: Boolean): Builder = this
        fun setAutoCancel(autoCancel: Boolean): Builder = apply { notification.autoCancel = autoCancel }
        fun setLocalOnly(b: Boolean): Builder = this
        fun setCategory(category: String?): Builder = apply { notification.category = category }
        fun setDefaults(defaults: Int): Builder = apply { notification.defaults = defaults }
        fun setPriority(pri: Int): Builder = apply { notification.priority = pri }
        fun setColor(argb: Int): Builder = apply { notification.color = argb }
        fun setVisibility(visibility: Int): Builder = apply { notification.visibility = visibility }
        fun setPublicVersion(n: Notification?): Builder = apply { notification.publicVersion = n }
        fun setStyle(style: Style?): Builder = apply { style?.setBuilder(this) }
        fun setChannelId(channelId: String): Builder = apply { notification.mChannelId = channelId }
        fun setTimeoutAfter(durationMs: Long): Builder = apply { notification.timeoutAfter = durationMs }
        fun setGroup(groupKey: String?): Builder = apply { notification.group = groupKey }
        fun setGroupSummary(isGroupSummary: Boolean): Builder = this
        fun setSortKey(sortKey: String?): Builder = apply { notification.sortKey = sortKey }
        fun addAction(icon: Int, title: CharSequence, intent: PendingIntent): Builder = apply {
            notification.actions = (notification.actions ?: emptyArray()) + Action(icon, title, intent)
        }
        fun addAction(action: Action): Builder = apply {
            notification.actions = (notification.actions ?: emptyArray()) + action
        }
        fun setBadgeIconType(icon: Int): Builder = apply { notification.badgeIconType = icon }
        fun setSettingsText(text: CharSequence?): Builder = this
        fun setShortcutId(shortcutId: String?): Builder = this
        fun setAllowSystemGeneratedContextualActions(allow: Boolean): Builder = this
        fun setForegroundServiceBehavior(behavior: Int): Builder = this
        fun setSilent(silent: Boolean): Builder = this

        fun build(): Notification = notification
        fun getNotification(): Notification = build()
    }

    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {}
    override fun describeContents(): Int = 0
    override fun toString(): String = "Notification(channel=$mChannelId, flags=$flags)"

    companion object {
        const val DEFAULT_SOUND = 1
        const val DEFAULT_VIBRATE = 2
        const val DEFAULT_LIGHTS = 4
        const val DEFAULT_ALL = -1
        const val FLAG_SHOW_LIGHTS = 1
        const val FLAG_ONGOING_EVENT = 2
        const val FLAG_INSISTENT = 4
        const val FLAG_ONLY_ALERT_ONCE = 8
        const val FLAG_AUTO_CANCEL = 16
        const val FLAG_NO_CLEAR = 32
        const val FLAG_FOREGROUND_SERVICE = 64
        const val FLAG_HIGH_PRIORITY = 128
        const val FLAG_LOCAL_ONLY = 256
        const val FLAG_GROUP_SUMMARY = 512
        const val FLAG_NOISY = 1024
        const val FLAG_CAN_COLORIZE = 2048
        const val FLAG_BUBBLE = 4096
        const val PRIORITY_MIN = -2
        const val PRIORITY_LOW = -1
        const val PRIORITY_DEFAULT = 0
        const val PRIORITY_HIGH = 1
        const val PRIORITY_MAX = 2
        const val VISIBILITY_PRIVATE = 0
        const val VISIBILITY_PUBLIC = 1
        const val VISIBILITY_SECRET = -1
        const val BADGE_ICON_NONE = 0
        const val BADGE_ICON_SMALL = 1
        const val BADGE_ICON_LARGE = 2
        const val CATEGORY_ALARM = "alarm"
        const val CATEGORY_CALL = "call"
        const val CATEGORY_EMAIL = "email"
        const val CATEGORY_ERROR = "err"
        const val CATEGORY_EVENT = "event"
        const val CATEGORY_LOCATION_SHARING = "location_sharing"
        const val CATEGORY_MESSAGE = "msg"
        const val CATEGORY_MISSED_CALL = "missed_call"
        const val CATEGORY_NAVIGATION = "navigation"
        const val CATEGORY_PROGRESS = "progress"
        const val CATEGORY_PROMO = "promo"
        const val CATEGORY_RECOMMENDATION = "recommendation"
        const val CATEGORY_REMINDER = "reminder"
        const val CATEGORY_SERVICE = "service"
        const val CATEGORY_SOCIAL = "social"
        const val CATEGORY_STATUS = "status"
        const val CATEGORY_SYSTEM = "sys"
        const val CATEGORY_TRANSPORT = "transport"
        const val EXTRA_TITLE = "android.title"
        const val EXTRA_TITLE_BIG = "android.title.big"
        const val EXTRA_TEXT = "android.text"
        const val EXTRA_TEXT_LINES = "android.textLines"
        const val EXTRA_SUB_TEXT = "android.subText"
        const val EXTRA_INFO_TEXT = "android.infoText"
        const val EXTRA_SUMMARY_TEXT = "android.summaryText"
        const val EXTRA_BIG_TEXT = "android.bigText"
        const val EXTRA_ICON = "android.icon"
        const val EXTRA_ICON_BIG = "android.largeIcon.big"
        const val EXTRA_LARGE_ICON = "android.largeIcon"
        const val EXTRA_LARGE_ICON_BIG = "android.largeIcon.big"
        const val EXTRA_PROGRESS = "android.progress"
        const val EXTRA_PROGRESS_MAX = "android.progressMax"
        const val EXTRA_PROGRESS_INDETERMINATE = "android.progressIndeterminate"
        const val EXTRA_SHOW_CHRONOMETER = "android.showChronometer"
        const val EXTRA_SHOW_WHEN = "android.showWhen"
        const val EXTRA_CHANNEL_ID = "android.channelId"
        const val EXTRA_COMPACT_ACTIONS = "android.compactActions"
        const val EXTRA_MESSAGES = "android.messages"
        const val EXTRA_NOTIFICATION_ID = "android.id"
        const val EXTRA_NOTIFICATION_TAG = "android.tag"
        const val EXTRA_PICTURE = "android.picture"
        const val EXTRA_PICTURE_CONTENT_DESCRIPTION = "android.pictureContentDescription"
        const val EXTRA_PEOPLE = "android.people"
        const val EXTRA_SELF_DISPLAY_NAME = "android.selfDisplayName"
        const val EXTRA_TEMPLATE = "android.template"
        const val EXTRA_WEARABLE_EXTENSIONS = "android.wearable.EXTENSIONS"
        const val EXTRA_MEDIA_SESSION = "android.mediaSession"
        const val EXTRA_CONVERSATION_TITLE = "android.conversationTitle"
        const val EXTRA_COLORIZED = "android.colorized"
        const val EXTRA_ALLOW_DURING_SETUP = "android.allowDuringSetup"
        const val INTENT_CATEGORY_NOTIFICATION_PREFERENCES = "android.intent.category.NOTIFICATION_PREFERENCES"
    }
}

/** android.app.NotificationChannel。 */
class NotificationChannel(
    private val id: String,
    private var name: CharSequence?,
    private var importance: Int,
) {
    var description: String? = null
    private var group: String? = null
    private var showBadge = true
    private var lightsEnabled = false
    private var lightColor = 0
    private var vibrationEnabled = false
    private var vibrationPattern: LongArray? = null
    private var sound: android.net.Uri? = null
    private var lockscreenVisibility = 0
    private var canBypassDnd = false
    private var canBubble = false
    private var allowBubbles = true

    fun getId(): String = id
    fun getName(): CharSequence? = name
    fun setName(name: CharSequence?) { this.name = name }
    fun getImportance(): Int = importance
    fun setImportance(importance: Int) { this.importance = importance }
    fun getGroup(): String? = group
    fun setGroup(groupId: String?) { this.group = groupId }
    fun canShowBadge(): Boolean = showBadge
    fun setShowBadge(showBadge: Boolean) { this.showBadge = showBadge }
    fun shouldShowLights(): Boolean = lightsEnabled
    fun enableLights(lights: Boolean) { this.lightsEnabled = lights }
    fun getLightColor(): Int = lightColor
    fun setLightColor(argb: Int) { this.lightColor = argb }
    fun shouldVibrate(): Boolean = vibrationEnabled
    fun enableVibration(vibration: Boolean) { this.vibrationEnabled = vibration }
    fun getVibrationPattern(): LongArray? = vibrationPattern
    fun setVibrationPattern(vibrationPattern: LongArray?) { this.vibrationPattern = vibrationPattern }
    fun getSound(): android.net.Uri? = sound
    fun setSound(sound: android.net.Uri?, audioAttributes: android.media.AudioAttributes?) { this.sound = sound }
    fun getLockscreenVisibility(): Int = lockscreenVisibility
    fun setLockscreenVisibility(lockscreenVisibility: Int) { this.lockscreenVisibility = lockscreenVisibility }
    fun canBypassDnd(): Boolean = canBypassDnd
    fun setBypassDnd(bypassDnd: Boolean) { this.canBypassDnd = bypassDnd }
    fun canBubble(): Boolean = canBubble
    fun setAllowBubbles(allowBubbles: Boolean) { this.allowBubbles = allowBubbles }
    fun isImportantConversation(): Boolean = false
    fun setConversationId(parentChannelId: String, conversationId: String) {}
    fun isDeleted(): Boolean = false
    fun setDeleted(deleted: Boolean) {}

    companion object {
        const val DEFAULT_CHANNEL_ID = "default"
        const val IMPORTANCE_NONE = 0
        const val IMPORTANCE_MIN = 1
        const val IMPORTANCE_LOW = 2
        const val IMPORTANCE_DEFAULT = 3
        const val IMPORTANCE_HIGH = 4
        const val IMPORTANCE_MAX = 5
        const val IMPORTANCE_UNSPECIFIED = -1000
    }
}

/** android.app.NotificationChannelGroup。 */
class NotificationChannelGroup(
    private val id: String,
    private var name: CharSequence?,
) {
    fun getId(): String = id
    fun getName(): CharSequence? = name
    fun setName(name: CharSequence?) { this.name = name }
    fun isBlocked(): Boolean = false
    fun setBlocked(blocked: Boolean) {}
}

/** android.app.PendingIntent。 */
class PendingIntent private constructor(
    internal val requestCode: Int,
    internal val intent: Intent?,
    internal val flags: Int,
    internal val type: Int,
) : android.os.Parcelable {

    fun send() { Log.d("PendingIntent", "send() no-op") }
    fun send(code: Int) { Log.d("PendingIntent", "send($code) no-op") }
    fun send(context: Context?, code: Int, intent: Intent?) { send(code) }
    fun send(context: Context?, code: Int, intent: Intent?, onFinished: OnFinished?, handler: Handler?) { send(code) }

    fun cancel() {}
    fun getCreatorPackage(): String = "com.ai.assistance.operit"
    fun getCreatorUid(): Int = 10000
    fun isActivity(): Boolean = type == TYPE_ACTIVITY
    fun isBroadcast(): Boolean = type == TYPE_BROADCAST
    fun isService(): Boolean = type == TYPE_SERVICE
    fun isForegroundService(): Boolean = type == TYPE_FOREGROUND_SERVICE
    fun isImmutable(): Boolean = flags and FLAG_IMMUTABLE != 0
    fun getIntentSender(): IntentSender = IntentSender()
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {}

    fun interface OnFinished {
        fun onSendFinished(pendingIntent: PendingIntent, intent: Intent, resultCode: Int, resultData: String?, resultExtras: Bundle?)
    }

    companion object {
        const val TYPE_ACTIVITY = 0
        const val TYPE_BROADCAST = 1
        const val TYPE_SERVICE = 2
        const val TYPE_FOREGROUND_SERVICE = 3

        const val FLAG_ONE_SHOT = 0x40000000
        const val FLAG_NO_CREATE = 0x20000000
        const val FLAG_CANCEL_CURRENT = 0x10000000
        const val FLAG_UPDATE_CURRENT = 0x08000000
        const val FLAG_IMMUTABLE = 0x04000000
        const val FLAG_MUTABLE = 0x02000000
        const val FLAG_ALLOW_UNSAFE_IMPLICIT_INTENT = 0x01000000

        @JvmStatic
        fun getActivity(context: Context?, requestCode: Int, intent: Intent): PendingIntent =
            PendingIntent(requestCode, intent, 0, TYPE_ACTIVITY)

        @JvmStatic
        fun getActivity(context: Context?, requestCode: Int, intent: Intent, flags: Int): PendingIntent =
            PendingIntent(requestCode, intent, flags, TYPE_ACTIVITY)

        @JvmStatic
        fun getActivity(context: Context?, requestCode: Int, intent: Intent, flags: Int, options: Bundle?): PendingIntent =
            PendingIntent(requestCode, intent, flags, TYPE_ACTIVITY)

        @JvmStatic
        fun getActivities(context: Context?, requestCode: Int, intents: Array<Intent>): PendingIntent =
            PendingIntent(requestCode, intents.firstOrNull(), 0, TYPE_ACTIVITY)

        @JvmStatic
        fun getActivities(context: Context?, requestCode: Int, intents: Array<Intent>, flags: Int): PendingIntent =
            PendingIntent(requestCode, intents.firstOrNull(), flags, TYPE_ACTIVITY)

        @JvmStatic
        fun getBroadcast(context: Context?, requestCode: Int, intent: Intent): PendingIntent =
            PendingIntent(requestCode, intent, 0, TYPE_BROADCAST)

        @JvmStatic
        fun getBroadcast(context: Context?, requestCode: Int, intent: Intent, flags: Int): PendingIntent =
            PendingIntent(requestCode, intent, flags, TYPE_BROADCAST)

        @JvmStatic
        fun getService(context: Context?, requestCode: Int, intent: Intent): PendingIntent =
            PendingIntent(requestCode, intent, 0, TYPE_SERVICE)

        @JvmStatic
        fun getService(context: Context?, requestCode: Int, intent: Intent, flags: Int): PendingIntent =
            PendingIntent(requestCode, intent, flags, TYPE_SERVICE)

        @JvmStatic
        fun getForegroundService(context: Context?, requestCode: Int, intent: Intent, flags: Int): PendingIntent =
            PendingIntent(requestCode, intent, flags, TYPE_FOREGROUND_SERVICE)

        @JvmStatic
        fun readPendingIntentOrNullFromParcel(parcel: android.os.Parcel): PendingIntent? = null

        @JvmStatic
        fun writePendingIntentOrNullToParcel(sender: PendingIntent?, out: android.os.Parcel) {}

        @JvmField val CREATOR: android.os.Parcelable.Creator<PendingIntent> = object : android.os.Parcelable.Creator<PendingIntent> {
            override fun createFromParcel(source: android.os.Parcel): PendingIntent = PendingIntent(0, null, 0, TYPE_ACTIVITY)
            override fun newArray(size: Int): Array<PendingIntent?> = arrayOfNulls(size)
        }
    }
}

/** android.app.NotificationManager。 */
open class NotificationManager {
    private val channels = java.util.concurrent.ConcurrentHashMap<String, NotificationChannel>()
    private val channelGroups = java.util.concurrent.ConcurrentHashMap<String, NotificationChannelGroup>()

    open fun notify(id: Int, notification: Notification) {
        Log.i("NotificationManager", "notify($id): ${notification.extras.getCharSequence(Notification.EXTRA_TITLE)}")
    }

    open fun notify(tag: String?, id: Int, notification: Notification) {
        Log.i("NotificationManager", "notify($tag, $id): ${notification.extras.getCharSequence(Notification.EXTRA_TITLE)}")
    }

    open fun cancel(id: Int) {}
    open fun cancel(tag: String?, id: Int) {}
    open fun cancelAll() {}

    open fun createNotificationChannel(channel: NotificationChannel) {
        channels[channel.getId()] = channel
    }

    open fun createNotificationChannelGroup(group: NotificationChannelGroup) {
        channelGroups[group.getId()] = group
    }

    open fun createNotificationChannels(channels: List<NotificationChannel>) {
        channels.forEach { this.channels[it.getId()] = it }
    }

    open fun createNotificationChannelGroups(groups: List<NotificationChannelGroup>) {
        groups.forEach { this.channelGroups[it.getId()] = it }
    }

    open fun getNotificationChannel(channelId: String): NotificationChannel? = channels[channelId]
    open fun getNotificationChannels(): List<NotificationChannel> = channels.values.toList()
    open fun getNotificationChannelGroup(channelGroupId: String): NotificationChannelGroup? = channelGroups[channelGroupId]
    open fun getNotificationChannelGroups(): List<NotificationChannelGroup> = channelGroups.values.toList()
    open fun deleteNotificationChannel(channelId: String) { channels.remove(channelId) }
    open fun deleteNotificationChannelGroup(groupId: String) { channelGroups.remove(groupId) }
    open fun areNotificationsEnabled(): Boolean = true
    open fun isNotificationPolicyAccessGranted(): Boolean = true
    open fun getImportance(): Int = NotificationChannel.IMPORTANCE_DEFAULT
    open fun isNotificationListenerAccessGranted(listener: android.content.ComponentName): Boolean = true
    open fun canNotify(): Boolean = true
    open fun areBubblesEnabled(): Boolean = true
    open fun getNotificationDelegate(): String? = null

    companion object {
        const val IMPORTANCE_UNSPECIFIED = NotificationChannel.IMPORTANCE_UNSPECIFIED
        const val IMPORTANCE_NONE = NotificationChannel.IMPORTANCE_NONE
        const val IMPORTANCE_MIN = NotificationChannel.IMPORTANCE_MIN
        const val IMPORTANCE_LOW = NotificationChannel.IMPORTANCE_LOW
        const val IMPORTANCE_DEFAULT = NotificationChannel.IMPORTANCE_DEFAULT
        const val IMPORTANCE_HIGH = NotificationChannel.IMPORTANCE_HIGH
        const val IMPORTANCE_MAX = NotificationChannel.IMPORTANCE_MAX

        const val INTERRUPTION_FILTER_UNKNOWN = 0
        const val INTERRUPTION_FILTER_ALL = 1
        const val INTERRUPTION_FILTER_PRIORITY = 2
        const val INTERRUPTION_FILTER_NONE = 3
        const val INTERRUPTION_FILTER_ALARMS = 4

        const val ACTION_NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED = "android.app.action.NOTIFICATION_CHANNEL_BLOCK_STATE_CHANGED"
        const val ACTION_APP_NOTIFICATION_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS"
        const val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        const val EXTRA_NOTIFICATION_CHANNEL_ID = "android.provider.extra.NOTIFICATION_CHANNEL_ID"
        const val EXTRA_APP_PACKAGE = "android.provider.extra.APP_PACKAGE"
        const val EXTRA_NOTIFICATION_ID = "android.notification.extra.NOTIFICATION_ID"
        const val EXTRA_NOTIFICATION_TAG = "android.notification.extra.NOTIFICATION_TAG"

        @JvmField val SERVICE_NOTIFICATION: Any = Any()
    }
}

/** android.app.AlarmManager 轻 stub。 */
open class AlarmManager {
    open fun set(type: Int, triggerAtMillis: Long, operation: PendingIntent) {}
    open fun set(type: Int, triggerAtMillis: Long, tag: String, listener: OnAlarmListener, targetHandler: Handler?) {}
    open fun setExact(type: Int, triggerAtMillis: Long, operation: PendingIntent) {}
    open fun setExactAndAllowWhileIdle(type: Int, triggerAtMillis: Long, operation: PendingIntent) {}
    open fun setAndAllowWhileIdle(type: Int, triggerAtMillis: Long, operation: PendingIntent) {}
    open fun setRepeating(type: Int, triggerAtMillis: Long, intervalMillis: Long, operation: PendingIntent) {}
    open fun setInexactRepeating(type: Int, triggerAtMillis: Long, intervalMillis: Long, operation: PendingIntent) {}
    open fun setWindow(type: Int, windowStartMillis: Long, windowLengthMillis: Long, operation: PendingIntent) {}
    open fun cancel(operation: PendingIntent) {}
    open fun cancel(listener: OnAlarmListener) {}
    open fun cancelAll() {}
    open fun canScheduleExactAlarms(): Boolean = true
    open fun getNextAlarmClock(): AlarmClockInfo? = null
    open fun setAlarmClock(info: AlarmClockInfo, operation: PendingIntent) {}
    open fun setTime(millis: Long) {}
    open fun setTimeZone(timeZone: String) {}

    class AlarmClockInfo(val triggerTime: Long, val showIntent: PendingIntent?)

    fun interface OnAlarmListener {
        fun onAlarm()
    }

    companion object {
        const val ELAPSED_REALTIME = 3
        const val ELAPSED_REALTIME_WAKEUP = 2
        const val RTC = 1
        const val RTC_WAKEUP = 0
        const val INTERVAL_FIFTEEN_MINUTES = 900000L
        const val INTERVAL_HALF_FIFTEEN_MINUTES = 450000L
        const val INTERVAL_HOUR = 3600000L
        const val INTERVAL_HALF_HOUR = 1800000L
        const val INTERVAL_DAY = 86400000L
        const val INTERVAL_HALF_DAY = 43200000L
        const val ACTION_NEXT_ALARM_CLOCK_CHANGED = "android.app.action.NEXT_ALARM_CLOCK_CHANGED"
        const val ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED = "android.app.action.SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED"
    }
}

/** android.app.IntentService（已废弃）：桌面 stub。 */
@Deprecated("Deprecated in Java")
abstract class IntentService(name: String) : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            Thread { onHandleIntent(intent) }.start()
        }
        return START_NOT_STICKY
    }

    @Deprecated("Deprecated in Java")
    abstract fun onHandleIntent(intent: Intent?)
}

/** android.app.TimePickerDialog 轻 stub。 */
open class TimePickerDialog(
    context: Context,
    listener: OnTimeSetListener?,
    hourOfDay: Int,
    minute: Int,
    is24HourView: Boolean,
) : android.content.DialogInterface {
    fun interface OnTimeSetListener {
        fun onTimeSet(view: Any?, hourOfDay: Int, minute: Int)
    }

    override fun cancel() {}
    override fun dismiss() {}
    open fun show() {}
    open fun updateTime(hourOfDay: Int, minute: Int) {}
}

/** android.app.DatePickerDialog 轻 stub。 */
open class DatePickerDialog(
    context: Context,
    listener: OnDateSetListener?,
    year: Int,
    month: Int,
    dayOfMonth: Int,
) : android.content.DialogInterface {
    fun interface OnDateSetListener {
        fun onDateSet(view: Any?, year: Int, month: Int, dayOfMonth: Int)
    }

    override fun cancel() {}
    override fun dismiss() {}
    open fun show() {}
    open fun updateDate(year: Int, month: Int, dayOfMonth: Int) {}
}

/** android.app.AlertDialog 轻 stub（完整 UI 在 B1b 重写）。 */
open class AlertDialog(context: Context) : android.content.DialogInterface {
    private var title: CharSequence? = null
    private var message: CharSequence? = null

    override fun cancel() {}
    override fun dismiss() {}
    open fun show() {}

    open fun setTitle(title: CharSequence?) { this.title = title }
    open fun setTitle(resId: Int) {}
    open fun setMessage(message: CharSequence?) { this.message = message }
    open fun setIcon(resId: Int) {}
    open fun setButton(whichButton: Int, text: CharSequence, listener: android.content.DialogInterface.OnClickListener?) {}
    open fun setButton(text: CharSequence, listener: android.content.DialogInterface.OnClickListener) {}

    open class Builder(private val context: Context) {
        private var title: CharSequence? = null
        private var message: CharSequence? = null
        fun setTitle(title: CharSequence?): Builder = apply { this.title = title }
        fun setTitle(titleId: Int): Builder = apply { this.title = null }
        fun setMessage(message: CharSequence?): Builder = apply { this.message = message }
        fun setMessage(messageId: Int): Builder = this
        fun setIcon(iconId: Int): Builder = this
        fun setPositiveButton(textId: Int, listener: android.content.DialogInterface.OnClickListener?): Builder = this
        fun setPositiveButton(text: CharSequence?, listener: android.content.DialogInterface.OnClickListener?): Builder = this
        fun setNegativeButton(textId: Int, listener: android.content.DialogInterface.OnClickListener?): Builder = this
        fun setNegativeButton(text: CharSequence?, listener: android.content.DialogInterface.OnClickListener?): Builder = this
        fun setNeutralButton(textId: Int, listener: android.content.DialogInterface.OnClickListener?): Builder = this
        fun setNeutralButton(text: CharSequence?, listener: android.content.DialogInterface.OnClickListener?): Builder = this
        fun setItems(items: Array<out CharSequence>, listener: android.content.DialogInterface.OnClickListener): Builder = this
        fun setItems(itemsId: Int, listener: android.content.DialogInterface.OnClickListener): Builder = this
        fun setView(view: android.view.View): Builder = this
        fun setCancelable(cancelable: Boolean): Builder = this
        fun setOnCancelListener(listener: android.content.DialogInterface.OnCancelListener): Builder = this
        fun setOnDismissListener(listener: android.content.DialogInterface.OnDismissListener): Builder = this
        fun create(): AlertDialog = AlertDialog(context)
        fun show(): AlertDialog = create().also { it.show() }
    }
}
