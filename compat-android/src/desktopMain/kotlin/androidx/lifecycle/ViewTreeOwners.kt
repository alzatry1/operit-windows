package androidx.lifecycle

import android.view.View

/**
 * androidx.lifecycle.ViewTreeLifecycleOwner / ViewTreeViewModelStoreOwner 桌面版。
 * 这两个类只存在于 Android 专属工件（KMP 版 lifecycle jar 没有），这里补齐。
 * 语义：把 owner 存进 View 的 tag 表（与真实实现同构，真实实现用 R.id 资源做 key）。
 * 桌面没有 View 树遍历体系，get/find 只查当前 View 自身。
 */

private const val VIEW_TREE_LIFECYCLE_OWNER_KEY = 0x7f0f0001
private const val VIEW_TREE_VIEWMODEL_STORE_OWNER_KEY = 0x7f0f0002

object ViewTreeLifecycleOwner {

    @JvmStatic
    fun set(view: View, lifecycleOwner: LifecycleOwner?) {
        view.setTag(VIEW_TREE_LIFECYCLE_OWNER_KEY, lifecycleOwner)
    }

    @JvmStatic
    fun get(view: View): LifecycleOwner? =
        view.getTag(VIEW_TREE_LIFECYCLE_OWNER_KEY) as? LifecycleOwner
}

/** `view.setViewTreeLifecycleOwner(owner)` 扩展（lifecycle-runtime Android 工件里的顶层函数）。 */
fun View.setViewTreeLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
    ViewTreeLifecycleOwner.set(this, lifecycleOwner)
}

fun View.findViewTreeLifecycleOwner(): LifecycleOwner? = ViewTreeLifecycleOwner.get(this)

object ViewTreeViewModelStoreOwner {

    @JvmStatic
    fun set(view: View, viewModelStoreOwner: ViewModelStoreOwner?) {
        view.setTag(VIEW_TREE_VIEWMODEL_STORE_OWNER_KEY, viewModelStoreOwner)
    }

    @JvmStatic
    fun get(view: View): ViewModelStoreOwner? =
        view.getTag(VIEW_TREE_VIEWMODEL_STORE_OWNER_KEY) as? ViewModelStoreOwner
}

/** `view.setViewTreeViewModelStoreOwner(owner)` 扩展（lifecycle-viewmodel Android 工件里的顶层函数）。 */
fun View.setViewTreeViewModelStoreOwner(viewModelStoreOwner: ViewModelStoreOwner?) {
    ViewTreeViewModelStoreOwner.set(this, viewModelStoreOwner)
}

fun View.findViewTreeViewModelStoreOwner(): ViewModelStoreOwner? = ViewTreeViewModelStoreOwner.get(this)
