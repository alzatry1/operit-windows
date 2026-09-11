package android

/**
 * android.R 系统资源垫片（P3-B3）。
 * ID 段从 900000 起编，与应用 R（1..7xxx）不冲突。
 * string 系的 id 已在 com.ai.assistance.operit.res.Strings.androidNames 注册，
 * 经 Resources.getString 可解析出中文文案（res-strings/default.properties 的 __android_* 条目）。
 */
object R {
    object attr {
        const val colorBackground: Int = 920001
        const val colorPrimary: Int = 920002
        const val colorPrimaryDark: Int = 920003
    }

    object color {
        const val black: Int = 930001
        const val white: Int = 930002
    }

    object drawable {
        const val ic_btn_speak_now: Int = 910001
        const val ic_dialog_info: Int = 910002
        const val ic_lock_silent_mode_off: Int = 910003
        const val ic_menu_close_clear_cancel: Int = 910004
        const val ic_menu_info_details: Int = 910005
        const val ic_menu_manage: Int = 910006
        const val ic_menu_view: Int = 910007
    }

    object id {
        const val content: Int = 940001
        const val copy: Int = 940002
        const val cut: Int = 940003
        const val paste: Int = 940004
        const val selectAll: Int = 940005
    }

    object string {
        const val ok: Int = 900001
        const val cancel: Int = 900002
        const val copy: Int = 900003
        const val selectAll: Int = 900004
    }

    object style {
        const val Animation_Toast: Int = 950001
    }
}
