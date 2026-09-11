package com.google.android.exoplayer2

/** 播放速度/音调参数垫片。 */
class PlaybackParameters(
    val speed: Float = 1.0f,
    val pitch: Float = 1.0f
) {
    companion object {
        @JvmField
        val DEFAULT: PlaybackParameters = PlaybackParameters()
    }
}
