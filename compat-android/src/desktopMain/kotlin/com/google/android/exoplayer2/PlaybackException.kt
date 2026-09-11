package com.google.android.exoplayer2

/** com.google.android.exoplayer2.PlaybackException 垫片（P3-B3，编译形状）。 */
open class PlaybackException(
    message: String? = null,
    cause: Throwable? = null,
    val errorCode: Int = ERROR_CODE_UNSPECIFIED
) : Exception(message, cause) {

    companion object {
        const val ERROR_CODE_UNSPECIFIED: Int = 1000
        const val ERROR_CODE_REMOTE_ERROR: Int = 1001
        const val ERROR_CODE_BEHIND_LIVE_WINDOW: Int = 1002
        const val ERROR_CODE_TIMEOUT: Int = 1003
        const val ERROR_CODE_IO_NETWORK_CONNECTION_FAILED: Int = 2001
        const val ERROR_CODE_IO_FILE_NOT_FOUND: Int = 2005
        const val ERROR_CODE_DECODER_INIT_FAILED: Int = 4001
    }

    open val errorCodeName: String
        get() = "ERROR_CODE_$errorCode"
}
