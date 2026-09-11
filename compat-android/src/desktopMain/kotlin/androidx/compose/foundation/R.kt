package androidx.compose.foundation

/**
 * androidx.compose.foundation.R 的桌面垫片。
 * 库内部的 R 类（id/attr 等）在桌面不生成，app 引用了 R.id.compose_prefetch_scheduler
 * 作 view tag。提供一个稳定的 id 常量即可。——Nova 注
 */
object R {
    object id {
        const val compose_prefetch_scheduler: Int = 0x7f0f0001
        const val compose_view_tree_saved_state_registry_owner: Int = 0x7f0f0002
    }
}
