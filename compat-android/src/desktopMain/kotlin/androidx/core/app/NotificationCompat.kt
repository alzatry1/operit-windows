package androidx.core.app

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

/**
 * androidx.core.app.NotificationCompat 桌面版。
 * Builder 全链式 set*；build() 产出 android.app.Notification stub。
 */
object NotificationCompat {

    const val PRIORITY_MIN = -2
    const val PRIORITY_LOW = -1
    const val PRIORITY_DEFAULT = 0
    const val PRIORITY_HIGH = 1
    const val PRIORITY_MAX = 2

    const val DEFAULT_ALL = -1
    const val DEFAULT_SOUND = 1
    const val DEFAULT_VIBRATE = 2
    const val DEFAULT_LIGHTS = 4

    const val FLAG_AUTO_CANCEL = 16
    const val FLAG_ONGOING_EVENT = 2
    const val FLAG_NO_CLEAR = 32
    const val FLAG_FOREGROUND_SERVICE = 64
    const val FLAG_ONLY_ALERT_ONCE = 8

    const val CATEGORY_MESSAGE = Notification.CATEGORY_MESSAGE
    const val CATEGORY_PROGRESS = Notification.CATEGORY_PROGRESS
    const val CATEGORY_SERVICE = Notification.CATEGORY_SERVICE
    const val CATEGORY_CALL = Notification.CATEGORY_CALL
    const val CATEGORY_REMINDER = Notification.CATEGORY_REMINDER
    const val CATEGORY_EVENT = Notification.CATEGORY_EVENT
    const val CATEGORY_ALARM = Notification.CATEGORY_ALARM
    const val CATEGORY_STATUS = Notification.CATEGORY_STATUS
    const val CATEGORY_SYSTEM = Notification.CATEGORY_SYSTEM

    const val VISIBILITY_PRIVATE = Notification.VISIBILITY_PRIVATE
    const val VISIBILITY_PUBLIC = Notification.VISIBILITY_PUBLIC
    const val VISIBILITY_SECRET = Notification.VISIBILITY_SECRET

    const val EXTRA_TITLE = Notification.EXTRA_TITLE
    const val EXTRA_TEXT = Notification.EXTRA_TEXT
    const val EXTRA_MESSAGES = Notification.EXTRA_MESSAGES

    // ---- Style 体系 ----
    abstract class Style {
        internal var builder: Builder? = null
        open fun setBuilder(builder: Builder?) {
            this.builder = builder
        }
        open fun build(): Notification? = builder?.build()
    }

    class BigTextStyle : Style() {
        private var bigText: CharSequence? = null
        private var bigContentTitle: CharSequence? = null
        private var summaryText: CharSequence? = null
        fun bigText(cs: CharSequence?): BigTextStyle = apply { bigText = cs }
        fun setBigContentTitle(title: CharSequence?): BigTextStyle = apply { bigContentTitle = title }
        fun setSummaryText(cs: CharSequence?): BigTextStyle = apply { summaryText = cs }
    }

    class BigPictureStyle : Style() {
        private var bigPicture: Bitmap? = null
        private var bigContentTitle: CharSequence? = null
        fun bigPicture(b: Bitmap?): BigPictureStyle = apply { bigPicture = b }
        fun setBigContentTitle(title: CharSequence?): BigPictureStyle = apply { bigContentTitle = title }
    }

    class InboxStyle : Style() {
        private val lines = ArrayList<CharSequence>()
        private var bigContentTitle: CharSequence? = null
        fun addLine(cs: CharSequence?): InboxStyle = apply { cs?.let { lines.add(it) } }
        fun setBigContentTitle(title: CharSequence?): InboxStyle = apply { bigContentTitle = title }
    }

    class MessagingStyle(personDisplayName: CharSequence) : Style() {
        class Message(val text: CharSequence, val timestamp: Long, val sender: CharSequence?)
        private val messages = ArrayList<Message>()
        fun addMessage(text: CharSequence, timestamp: Long, sender: CharSequence?): MessagingStyle =
            apply { messages.add(Message(text, timestamp, sender)) }
    }

    /** Action 嵌套类。 */
    class Action(
        var icon: Int,
        var title: CharSequence?,
        var actionIntent: PendingIntent?,
    ) {
        class Builder(private val icon: Int, private val title: CharSequence?, private val intent: PendingIntent?) {
            fun build(): Action = Action(icon, title, intent)
        }
    }

    /** Builder：全链式。 */
    class Builder {
        private val notification = Notification()

        constructor(context: Context)
        constructor(context: Context, channelId: String) {
            notification.channelId = channelId
        }

        fun setWhen(`when`: Long): Builder = apply { notification.`when` = `when` }
        fun setShowWhen(show: Boolean): Builder = apply { notification.showWhen = show }
        fun setUsesChronometer(b: Boolean): Builder = apply { notification.usesChronometer = b }
        fun setSmallIcon(icon: Int): Builder = apply { notification.icon = icon }
        fun setSmallIcon(icon: Int, level: Int): Builder = apply { notification.icon = icon; notification.iconLevel = level }
        fun setContentTitle(title: CharSequence?): Builder = apply { notification.extras.putCharSequence(Notification.EXTRA_TITLE, title) }
        fun setContentText(text: CharSequence?): Builder = apply { notification.extras.putCharSequence(Notification.EXTRA_TEXT, text) }
        fun setSubText(text: CharSequence?): Builder = apply { notification.subText = text }
        fun setNumber(number: Int): Builder = apply { notification.number = number }
        fun setContentInfo(info: CharSequence?): Builder = apply { notification.infoText = info }
        fun setProgress(max: Int, progress: Int, indeterminate: Boolean): Builder = apply {
            notification.extras.putInt(Notification.EXTRA_PROGRESS, progress)
            notification.extras.putInt(Notification.EXTRA_PROGRESS_MAX, max)
            notification.extras.putBoolean(Notification.EXTRA_PROGRESS_INDETERMINATE, indeterminate)
        }
        fun setContentIntent(intent: PendingIntent?): Builder = apply { notification.contentIntent = intent }
        fun setDeleteIntent(intent: PendingIntent?): Builder = apply { notification.deleteIntent = intent }
        fun setFullScreenIntent(intent: PendingIntent?, highPriority: Boolean): Builder = apply { notification.fullScreenIntent = intent }
        fun setTicker(tickerText: CharSequence?): Builder = apply { notification.tickerText = tickerText }
        fun setLargeIcon(b: Bitmap?): Builder = this
        fun setSound(sound: Uri?): Builder = apply { notification.sound = sound }
        fun setVibrate(pattern: LongArray?): Builder = apply { notification.vibrate = pattern }
        fun setLights(argb: Int, onMs: Int, offMs: Int): Builder = apply {
            notification.ledARGB = argb; notification.ledOnMS = onMs; notification.ledOffMS = offMs
        }
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
        fun setChannelId(channelId: String): Builder = apply { notification.channelId = channelId }
        fun setTimeoutAfter(durationMs: Long): Builder = apply { notification.timeoutAfter = durationMs }
        fun setGroup(groupKey: String?): Builder = apply { notification.group = groupKey }
        fun setGroupSummary(isGroupSummary: Boolean): Builder = this
        fun setSortKey(sortKey: String?): Builder = apply { notification.sortKey = sortKey }
        fun addAction(icon: Int, title: CharSequence, intent: PendingIntent): Builder = apply {
            notification.actions = (notification.actions ?: emptyArray()) + Notification.Action(icon, title, intent)
        }
        fun addAction(action: Action): Builder = apply {
            notification.actions = (notification.actions ?: emptyArray()) +
                Notification.Action(action.icon, action.title, action.actionIntent)
        }
        fun setBadgeIconType(icon: Int): Builder = apply { notification.badgeIconType = icon }
        fun setSettingsText(text: CharSequence?): Builder = this
        fun setShortcutId(shortcutId: String?): Builder = this
        fun setAllowSystemGeneratedContextualActions(allow: Boolean): Builder = this
        fun setForegroundServiceBehavior(behavior: Int): Builder = this
        fun setSilent(silent: Boolean): Builder = this
        fun setContentTitle(title: Int): Builder = this
        fun setContentText(text: Int): Builder = this

        fun build(): Notification = notification
        fun getNotification(): Notification = build()
    }
}

/**
 * androidx.core.app.NotificationManagerCompat 桌面版。
 * 通知走 slf4j 日志。
 */
class NotificationManagerCompat private constructor(private val context: Context) {

    fun notify(id: Int, notification: Notification) {
        android.util.Log.i("NotificationManagerCompat", "notify($id)")
    }

    fun notify(tag: String?, id: Int, notification: Notification) {
        android.util.Log.i("NotificationManagerCompat", "notify($tag, $id)")
    }

    fun cancel(id: Int) {}
    fun cancel(tag: String?, id: Int) {}
    fun cancelAll() {}

    fun areNotificationsEnabled(): Boolean = true

    fun createNotificationChannel(channel: NotificationChannelCompat) {
        android.util.Log.d("NotificationManagerCompat", "createNotificationChannel(${channel.id})")
    }

    fun createNotificationChannels(channels: List<NotificationChannelCompat>) {
        channels.forEach { createNotificationChannel(it) }
    }

    fun getNotificationChannel(channelId: String): NotificationChannelCompat? = null
    fun deleteNotificationChannel(channelId: String) {}
    fun getImportance(): Int = android.app.NotificationManager.IMPORTANCE_DEFAULT
    fun canNotify(): Boolean = true
    fun areBubblesEnabled(): Boolean = true

    companion object {
        @JvmStatic
        fun from(context: Context): NotificationManagerCompat = NotificationManagerCompat(context)
    }
}

/** androidx.core.app.NotificationChannelCompat。 */
class NotificationChannelCompat private constructor(
    val id: String,
    val name: CharSequence?,
    val importance: Int,
) {
    class Builder(private val id: String, private val importance: Int) {
        private var name: CharSequence? = null
        fun setName(name: CharSequence?): Builder = apply { this.name = name }
        fun setDescription(description: String?): Builder = this
        fun setGroup(groupId: String?): Builder = this
        fun setShowBadge(showBadge: Boolean): Builder = this
        fun setLightsEnabled(lights: Boolean): Builder = this
        fun setLightColor(argb: Int): Builder = this
        fun setVibrationEnabled(vibration: Boolean): Builder = this
        fun setVibrationPattern(vibrationPattern: LongArray?): Builder = this
        fun setSound(sound: Uri?, audioAttributes: Any?): Builder = this
        fun build(): NotificationChannelCompat = NotificationChannelCompat(id, name, importance)
    }

    companion object {
        const val DEFAULT_CHANNEL_ID = android.app.NotificationChannel.DEFAULT_CHANNEL_ID
        const val IMPORTANCE_NONE = android.app.NotificationChannel.IMPORTANCE_NONE
        const val IMPORTANCE_MIN = android.app.NotificationChannel.IMPORTANCE_MIN
        const val IMPORTANCE_LOW = android.app.NotificationChannel.IMPORTANCE_LOW
        const val IMPORTANCE_DEFAULT = android.app.NotificationChannel.IMPORTANCE_DEFAULT
        const val IMPORTANCE_HIGH = android.app.NotificationChannel.IMPORTANCE_HIGH
        const val IMPORTANCE_MAX = android.app.NotificationChannel.IMPORTANCE_MAX
    }
}
