package android.content

/**
 * android.content.DialogInterface 垫片（对话框通用监听器容器）。——Nova 注
 */
interface DialogInterface {
    fun cancel() {}
    fun dismiss() {}

    fun interface OnClickListener {
        fun onClick(dialog: DialogInterface, which: Int)
    }

    fun interface OnDismissListener {
        fun onDismiss(dialog: DialogInterface)
    }

    fun interface OnCancelListener {
        fun onCancel(dialog: DialogInterface)
    }

    fun interface OnShowListener {
        fun onShow(dialog: DialogInterface)
    }

    fun interface OnMultiChoiceClickListener {
        fun onClick(dialog: DialogInterface, which: Int, isChecked: Boolean)
    }

    fun interface OnKeyListener {
        fun onKey(dialog: DialogInterface, keyCode: Int, event: android.view.KeyEvent): Boolean
    }

    companion object {
        const val BUTTON_POSITIVE = -1
        const val BUTTON_NEGATIVE = -2
        const val BUTTON_NEUTRAL = -3
    }
}
