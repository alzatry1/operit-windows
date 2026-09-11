package com.google.android.exoplayer2

import android.net.Uri

/** com.google.android.exoplayer2.MediaMetadata 垫片（P3-B3，编译形状）。 */
class MediaMetadata private constructor(
    val title: CharSequence?,
    val artist: CharSequence?,
    val displayTitle: CharSequence?,
    val artworkUri: Uri?
) {
    class Builder {
        private var title: CharSequence? = null
        private var artist: CharSequence? = null
        private var displayTitle: CharSequence? = null
        private var artworkUri: Uri? = null

        fun setTitle(title: CharSequence?): Builder = apply { this.title = title }
        fun setArtist(artist: CharSequence?): Builder = apply { this.artist = artist }
        fun setDisplayTitle(displayTitle: CharSequence?): Builder = apply { this.displayTitle = displayTitle }
        fun setArtworkUri(artworkUri: Uri?): Builder = apply { this.artworkUri = artworkUri }

        fun build(): MediaMetadata = MediaMetadata(title, artist, displayTitle, artworkUri)
    }
}
