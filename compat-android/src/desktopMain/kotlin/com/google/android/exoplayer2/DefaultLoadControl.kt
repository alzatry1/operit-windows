package com.google.android.exoplayer2

/** LoadControl 接口垫片（P3-B3）。真实接口含缓冲判定方法，桌面 stub 为空。 */
interface LoadControl

/** com.google.android.exoplayer2.DefaultLoadControl 垫片（编译形状）。 */
class DefaultLoadControl private constructor(
    val minBufferMs: Int,
    val maxBufferMs: Int,
    val bufferForPlaybackMs: Int,
    val bufferForPlaybackAfterRebufferMs: Int,
    val targetBufferBytes: Int,
    val prioritizeTimeOverSizeThresholds: Boolean
) : LoadControl {

    class Builder {
        private var minBufferMs: Int = DEFAULT_MIN_BUFFER_MS
        private var maxBufferMs: Int = DEFAULT_MAX_BUFFER_MS
        private var bufferForPlaybackMs: Int = DEFAULT_BUFFER_FOR_PLAYBACK_MS
        private var bufferForPlaybackAfterRebufferMs: Int = DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
        private var targetBufferBytes: Int = C.LENGTH_UNSET
        private var prioritizeTimeOverSizeThresholds: Boolean = DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS

        fun setBufferDurationsMs(
            minBufferMs: Int,
            maxBufferMs: Int,
            bufferForPlaybackMs: Int,
            bufferForPlaybackAfterRebufferMs: Int
        ): Builder = apply {
            this.minBufferMs = minBufferMs
            this.maxBufferMs = maxBufferMs
            this.bufferForPlaybackMs = bufferForPlaybackMs
            this.bufferForPlaybackAfterRebufferMs = bufferForPlaybackAfterRebufferMs
        }

        fun setTargetBufferBytes(targetBufferBytes: Int): Builder =
            apply { this.targetBufferBytes = targetBufferBytes }

        fun setPrioritizeTimeOverSizeThresholds(prioritizeTimeOverSizeThresholds: Boolean): Builder =
            apply { this.prioritizeTimeOverSizeThresholds = prioritizeTimeOverSizeThresholds }

        fun build(): DefaultLoadControl = DefaultLoadControl(
            minBufferMs, maxBufferMs, bufferForPlaybackMs,
            bufferForPlaybackAfterRebufferMs, targetBufferBytes, prioritizeTimeOverSizeThresholds
        )
    }

    companion object {
        const val DEFAULT_MIN_BUFFER_MS: Int = 50000
        const val DEFAULT_MAX_BUFFER_MS: Int = 50000
        const val DEFAULT_BUFFER_FOR_PLAYBACK_MS: Int = 2500
        const val DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS: Int = 5000
        const val DEFAULT_PRIORITIZE_TIME_OVER_SIZE_THRESHOLDS: Boolean = true
    }
}
