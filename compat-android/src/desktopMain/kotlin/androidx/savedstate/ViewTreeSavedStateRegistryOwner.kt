package androidx.savedstate

import android.view.View

/**
 * androidx.savedstate.ViewTreeSavedStateRegistryOwner 桌面版。
 * KMP 版 savedstate jar 不含 ViewTree 系列（Android 专属），这里补齐。
 * 语义与真实实现同构：owner 存进 View 的 tag 表。
 */

private const val VIEW_TREE_SAVED_STATE_REGISTRY_OWNER_KEY = 0x7f0f0003

object ViewTreeSavedStateRegistryOwner {

    @JvmStatic
    fun set(view: View, owner: SavedStateRegistryOwner?) {
        view.setTag(VIEW_TREE_SAVED_STATE_REGISTRY_OWNER_KEY, owner)
    }

    @JvmStatic
    fun get(view: View): SavedStateRegistryOwner? =
        view.getTag(VIEW_TREE_SAVED_STATE_REGISTRY_OWNER_KEY) as? SavedStateRegistryOwner
}

/** `view.setViewTreeSavedStateRegistryOwner(owner)` 扩展（savedstate Android 工件里的顶层函数）。 */
fun View.setViewTreeSavedStateRegistryOwner(owner: SavedStateRegistryOwner?) {
    ViewTreeSavedStateRegistryOwner.set(this, owner)
}

fun View.findViewTreeSavedStateRegistryOwner(): SavedStateRegistryOwner? =
    ViewTreeSavedStateRegistryOwner.get(this)
