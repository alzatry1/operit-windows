package com.google.android.exoplayer2

import android.net.Uri

/** com.google.android.exoplayer2.MediaItem 垫片（P3-B3，编译形状）。 */
class MediaItem private constructor(
    val mediaId: String,
    val mediaMetadata: MediaMetadata,
    val localConfiguration: LocalConfiguration?
) {
    class LocalConfiguration(val uri: Uri?)

    companion object {
        @JvmStatic
        fun fromUri(uri: String): MediaItem = Builder().setUri(uri).build()

        @JvmStatic
        fun fromUri(uri: Uri): MediaItem = Builder().setUri(uri).build()
    }

    class Builder {
        private var uri: Uri? = null
        private var mediaId: String = ""
        private var mediaMetadata: MediaMetadata = MediaMetadata.Builder().build()

        fun setUri(uri: String?): Builder = apply { this.uri = uri?.let(Uri::parse) }
        fun setUri(uri: Uri?): Builder = apply { this.uri = uri }
        fun setMediaId(mediaId: String): Builder = apply { this.mediaId = mediaId }
        fun setMediaMetadata(mediaMetadata: MediaMetadata): Builder = apply { this.mediaMetadata = mediaMetadata }

        fun build(): MediaItem =
            MediaItem(mediaId, mediaMetadata, uri?.let { LocalConfiguration(it) })
    }
}
