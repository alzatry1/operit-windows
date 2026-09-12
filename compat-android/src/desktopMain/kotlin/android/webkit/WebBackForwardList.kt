package android.webkit

/** android.webkit.WebHistoryItem 桌面 stub（浏览历史条目）。 */
open class WebHistoryItem {
    open val url: String = ""
    open val originalUrl: String = ""
    open val title: String = ""
}

/** android.webkit.WebBackForwardList 桌面 stub（前进/后退历史栈）。 */
open class WebBackForwardList {
    open val size: Int = 0
    open val currentIndex: Int = -1
    open val currentItem: WebHistoryItem? = null
    open fun getItemAtIndex(index: Int): WebHistoryItem? = null
    open fun clone(): WebBackForwardList = WebBackForwardList()
}
