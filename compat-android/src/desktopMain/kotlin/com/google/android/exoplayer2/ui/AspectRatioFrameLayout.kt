package com.google.android.exoplayer2.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * com.google.android.exoplayer2.ui.AspectRatioFrameLayout 垫片（P3-B3，编译形状）。
 * 桌面不渲染视频帧，仅保留 resizeMode 状态。
 */
open class AspectRatioFrameLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        const val RESIZE_MODE_FIT: Int = 0
        const val RESIZE_MODE_FIXED_WIDTH: Int = 1
        const val RESIZE_MODE_FIXED_HEIGHT: Int = 2
        const val RESIZE_MODE_FILL: Int = 3
        const val RESIZE_MODE_ZOOM: Int = 4
    }

    var resizeMode: Int = RESIZE_MODE_FIT

    var aspectRatio: Float = 0f
}
