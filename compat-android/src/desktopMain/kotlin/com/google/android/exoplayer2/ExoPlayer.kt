package com.google.android.exoplayer2

import android.content.Context
import com.google.android.exoplayer2.audio.AudioAttributes

/**
 * com.google.android.exoplayer2.ExoPlayer 垫片（P3-B3，编译形状）。
 * 桌面真实播放走 P8 的 VLCJ；本实现仅维护内存状态字段，listener 不主动回调。
 */
interface ExoPlayer : Player {

    class Builder(private val context: Context) {
        private var audioAttributes: AudioAttributes = AudioAttributes.DEFAULT
        private var handleAudioFocus: Boolean = false
        private var loadControl: LoadControl? = null
        private var seekBackIncrementMs: Long = 10000
        private var seekForwardIncrementMs: Long = 30000
        private var handleAudioBecomingNoisy: Boolean = false

        fun setAudioAttributes(audioAttributes: AudioAttributes, handleAudioFocus: Boolean): Builder =
            apply {
                this.audioAttributes = audioAttributes
                this.handleAudioFocus = handleAudioFocus
            }

        fun setLoadControl(loadControl: LoadControl): Builder = apply { this.loadControl = loadControl }
        fun setSeekBackIncrementMs(seekBackIncrementMs: Long): Builder =
            apply { this.seekBackIncrementMs = seekBackIncrementMs }

        fun setSeekForwardIncrementMs(seekForwardIncrementMs: Long): Builder =
            apply { this.seekForwardIncrementMs = seekForwardIncrementMs }

        fun setHandleAudioBecomingNoisy(handleAudioBecomingNoisy: Boolean): Builder =
            apply { this.handleAudioBecomingNoisy = handleAudioBecomingNoisy }

        fun build(): ExoPlayer = StubExoPlayer(context)
    }
}

/** ExoPlayer 内存态实现：字段记录 + 状态机最小迁移，无任何真实解码/渲染。 */
internal class StubExoPlayer(@Suppress("unused") private val context: Context) : ExoPlayer {
    private val listeners = mutableListOf<Player.Listener>()
    private val mediaItems = mutableListOf<MediaItem>()

    override var playWhenReady: Boolean = false
    override var repeatMode: Int = Player.REPEAT_MODE_OFF
    override var volume: Float = 1.0f
    override var isPlaying: Boolean = false
        private set
    override var playbackState: Int = Player.STATE_IDLE
        private set
    override var currentPosition: Long = 0L
        private set
    override val duration: Long
        get() = C.TIME_UNSET
    override var currentMediaItemIndex: Int = 0
        private set
    override val currentMediaItem: MediaItem?
        get() = mediaItems.getOrNull(currentMediaItemIndex)

    override fun play() {
        isPlaying = true
    }

    override fun pause() {
        isPlaying = false
    }

    override fun stop() {
        isPlaying = false
        playbackState = Player.STATE_IDLE
    }

    override fun release() {
        stop()
        listeners.clear()
        mediaItems.clear()
    }

    override fun seekTo(positionMs: Long) {
        currentPosition = positionMs
    }

    override fun seekTo(mediaItemIndex: Int, positionMs: Long) {
        currentMediaItemIndex = mediaItemIndex
        currentPosition = positionMs
    }

    override fun setMediaItem(mediaItem: MediaItem) {
        mediaItems.clear()
        mediaItems.add(mediaItem)
        currentMediaItemIndex = 0
    }

    override fun setMediaItems(mediaItems: List<MediaItem>, startIndex: Int, startPositionMs: Long) {
        this.mediaItems.clear()
        this.mediaItems.addAll(mediaItems)
        currentMediaItemIndex = startIndex.coerceIn(0, (mediaItems.size - 1).coerceAtLeast(0))
        currentPosition = startPositionMs
    }

    override fun addMediaItem(mediaItem: MediaItem) {
        mediaItems.add(mediaItem)
    }

    override fun clearMediaItems() {
        mediaItems.clear()
        currentMediaItemIndex = 0
    }

    override fun prepare() {
        playbackState = Player.STATE_READY
    }

    override fun addListener(listener: Player.Listener) {
        if (listener !in listeners) listeners.add(listener)
    }

    override fun removeListener(listener: Player.Listener) {
        listeners.remove(listener)
    }

    override fun setAudioAttributes(audioAttributes: AudioAttributes, handleAudioFocus: Boolean) {
        // 桌面 stub：忽略音频焦点管理
    }
}
