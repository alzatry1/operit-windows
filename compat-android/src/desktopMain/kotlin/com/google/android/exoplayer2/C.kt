package com.google.android.exoplayer2

/**
 * com.google.android.exoplayer2.C 常量垫片（P3-B3，编译形状）。
 * 值与真实库一致或接近；桌面仅作编译/分支占位。
 */
object C {
    const val TIME_UNSET: Long = Long.MIN_VALUE + 1
    const val INDEX_UNSET: Int = -1
    const val POSITION_UNSET: Long = Long.MIN_VALUE + 1
    const val RATE_UNSET: Float = -Float.MAX_VALUE
    const val LENGTH_UNSET: Int = -1
    const val PERCENTAGE_UNSET: Int = -1

    const val CONTENT_TYPE_MOVIE: Int = 3
    const val CONTENT_TYPE_MUSIC: Int = 2
    const val CONTENT_TYPE_SONIFICATION: Int = 4
    const val CONTENT_TYPE_SPEECH: Int = 1
    const val CONTENT_TYPE_UNKNOWN: Int = 0

    const val AUDIO_CONTENT_TYPE_MOVIE: Int = CONTENT_TYPE_MOVIE
    const val AUDIO_CONTENT_TYPE_MUSIC: Int = CONTENT_TYPE_MUSIC
    const val AUDIO_CONTENT_TYPE_SONIFICATION: Int = CONTENT_TYPE_SONIFICATION
    const val AUDIO_CONTENT_TYPE_SPEECH: Int = CONTENT_TYPE_SPEECH

    const val USAGE_UNKNOWN: Int = 0
    const val USAGE_MEDIA: Int = 5
    const val USAGE_VOICE_COMMUNICATION: Int = 2
    const val USAGE_NOTIFICATION: Int = 6
    const val USAGE_ALARM: Int = 4
    const val USAGE_GAME: Int = 14
    const val USAGE_ASSISTANCE_ACCESSIBILITY: Int = 11

    const val STREAM_TYPE_MUSIC: Int = 3
    const val STREAM_TYPE_NOTIFICATION: Int = 5

    const val BUFFER_FLAG_LAST_SAMPLE: Int = 4
    const val SELECTION_FLAG_DEFAULT: Int = 1
}
