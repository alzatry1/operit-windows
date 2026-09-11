package android.app

import android.content.Context
import android.content.DialogInterface

/**
 * android.widget.TimePicker 垫片（桌面无原生控件，由 Compose 层接管）。——Nova 注
 */
open class TimePicker(context: Context) : android.view.View(context) {
    open var hour: Int = 0
    open var minute: Int = 0
    open var is24HourView: Boolean = true
    open fun setOnTimeChangedListener(l: OnTimeChangedListener?) {}
    fun interface OnTimeChangedListener {
        fun onTimeChanged(view: TimePicker, hourOfDay: Int, minute: Int)
    }
}

/**
 * android.app.TimePickerDialog 垫片。桌面由 Compose DateTimePicker 接管，这里提供编译形状。
 * 不继承 android.app.Dialog（compat 暂无该类），独立提供 show/dismiss 等。——Nova 注
 */
open class TimePickerDialog(open val context: Context) {

    fun interface OnTimeSetListener {
        fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int)
    }

    private var listener: OnTimeSetListener? = null
    private var initialHour: Int = 0
    private var initialMinute: Int = 0
    private var is24: Boolean = true

    constructor(
        context: Context,
        listener: OnTimeSetListener?,
        hourOfDay: Int,
        minute: Int,
        is24HourView: Boolean,
    ) : this(context) {
        this.listener = listener
        this.initialHour = hourOfDay
        this.initialMinute = minute
        this.is24 = is24HourView
    }

    constructor(
        context: Context,
        themeResId: Int,
        listener: OnTimeSetListener?,
        hourOfDay: Int,
        minute: Int,
        is24HourView: Boolean,
    ) : this(context, listener, hourOfDay, minute, is24HourView)

    open fun show() {}
    open fun dismiss() {}
    open fun cancel() {}
    open fun isShowing(): Boolean = false
    open fun updateTime(hourOfDay: Int, minute: Int) {}
    open fun setOnDismissListener(l: DialogInterface.OnDismissListener?) {}
    open fun setCancelable(flag: Boolean) {}
    open fun setCanceledOnTouchOutside(cancel: Boolean) {}
    open fun setTitle(title: CharSequence?) {}
    open fun setTitle(titleResId: Int) {}
    open fun setButton(whichButton: Int, text: CharSequence?, listener: DialogInterface.OnClickListener?) {}
}
