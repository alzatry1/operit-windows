package com.google.android.exoplayer2.ui

import android.content.Context
import android.util.AttributeSet
import com.google.android.exoplayer2.Player

/**
 * com.google.android.exoplayer2.ui.StyledPlayerView 垫片（P3-B3，编译形状）。
 * 桌面真实视频渲染走 P8 的 VLCJ 组件；本 View 仅持有 player 引用。
 */
open class StyledPlayerView : AspectRatioFrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var player: Player? = null
    var useController: Boolean = true
    var controllerAutoShow: Boolean = true
    var controllerShowTimeoutMs: Int = 5000

    fun showController() {}

    fun hideController() {}

    fun isControllerFullyVisible(): Boolean = false
}
