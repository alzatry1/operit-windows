package com.google.android.exoplayer2.audio

import com.google.android.exoplayer2.C

/** com.google.android.exoplayer2.audio.AudioAttributes 垫片（P3-B3，编译形状）。 */
class AudioAttributes private constructor(
    val usage: Int,
    val contentType: Int,
    val allowedCapturePolicy: Int,
    val flags: Int
) {
    class Builder {
        private var usage: Int = C.USAGE_UNKNOWN
        private var contentType: Int = C.CONTENT_TYPE_UNKNOWN
        private var allowedCapturePolicy: Int = ALLOW_CAPTURE_BY_ALL
        private var flags: Int = 0

        fun setUsage(usage: Int): Builder = apply { this.usage = usage }
        fun setContentType(contentType: Int): Builder = apply { this.contentType = contentType }
        fun setAllowedCapturePolicy(allowedCapturePolicy: Int): Builder =
            apply { this.allowedCapturePolicy = allowedCapturePolicy }
        fun setFlags(flags: Int): Builder = apply { this.flags = flags }

        fun build(): AudioAttributes = AudioAttributes(usage, contentType, allowedCapturePolicy, flags)
    }

    companion object {
        const val ALLOW_CAPTURE_BY_ALL: Int = 1
        const val ALLOW_CAPTURE_BY_SYSTEM: Int = 2
        const val ALLOW_CAPTURE_BY_NONE: Int = 3

        @JvmField
        val DEFAULT: AudioAttributes = Builder().build()
    }
}
