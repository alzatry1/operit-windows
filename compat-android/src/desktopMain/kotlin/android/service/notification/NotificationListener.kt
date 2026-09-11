package android.service.notification

import android.app.Notification
import android.content.Intent
import android.os.IBinder

/**
 * android.service.notification（P3-B2 新增，编译级 stub）。
 * 桌面无系统通知栏：getActiveNotifications→空数组、cancelNotification no-op。
 */

/** android.service.notification.StatusBarNotification。 */
open class StatusBarNotification {
    open var packageName: String? = null
    open var id: Int = 0
    open var tag: String? = null
    open var key: String? = null
    open var postTime: Long = 0L
    open var notification: Notification? = null
    open var isOngoing: Boolean = false
    open var isClearable: Boolean = true
    open var userId: Int = 0

    constructor()

    constructor(packageName: String?, id: Int, tag: String?, postTime: Long) {
        this.packageName = packageName
        this.id = id
        this.tag = tag
        this.postTime = postTime
    }

    open fun getGroupKey(): String? = null
    open fun getOverrideGroupKey(): String? = null
    open fun getUid(): Int = 0
    open fun getInitialPid(): Int = 0

    override fun toString(): String = "StatusBarNotification(pkg=$packageName id=$id tag=$tag)"
}

/** android.service.notification.NotificationListenerService。 */
open class NotificationListenerService : android.app.Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    open fun onListenerConnected() {}
    open fun onListenerDisconnected() {}
    open fun onNotificationPosted(sbn: StatusBarNotification) {}
    open fun onNotificationPosted(sbn: StatusBarNotification, rankingMap: RankingMap) {}
    open fun onNotificationRemoved(sbn: StatusBarNotification) {}
    open fun onNotificationRemoved(sbn: StatusBarNotification, rankingMap: RankingMap) {}
    open fun onNotificationRankingUpdate(rankingMap: RankingMap) {}

    open val activeNotifications: Array<StatusBarNotification>? get() = emptyArray()
    open fun cancelNotification(key: String?) {}
    open fun requestRebind(componentName: android.content.ComponentName?) {}
    open fun requestUnbind() {}

    /** android.service.notification.NotificationListenerService.RankingMap。 */
    class RankingMap {
        fun getRanking(key: String?, ranking: Ranking?): Boolean = false
        fun getOrderedKeys(): Array<String> = emptyArray()
    }

    /** android.service.notification.NotificationListenerService.Ranking。 */
    class Ranking {
        var importance: Int = 0
        var channel: Any? = null
        var isAmbient: Boolean = false
        var matchesFilter: Boolean = true
    }

    companion object {
        const val SERVICE_INTERFACE = "android.service.notification.NotificationListenerService"
        const val STATUS_BAR_NOTIFICATION_EXTRA_GROUP_KEY = "android.app.Notification.extra.GROUP_KEY"
    }
}
