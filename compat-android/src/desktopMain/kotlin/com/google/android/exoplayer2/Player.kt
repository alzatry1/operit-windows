package com.google.android.exoplayer2

import com.google.android.exoplayer2.audio.AudioAttributes

/**
 * com.google.android.exoplayer2.Player 接口垫片（P3-B3，编译形状）。
 * 桌面真实播放走 P8 的 VLCJ；此处仅提供 API 形状与状态字段。
 */
interface Player {

    companion object {
        const val STATE_IDLE: Int = 1
        const val STATE_BUFFERING: Int = 2
        const val STATE_READY: Int = 3
        const val STATE_ENDED: Int = 4

        const val REPEAT_MODE_OFF: Int = 0
        const val REPEAT_MODE_ONE: Int = 1
        const val REPEAT_MODE_ALL: Int = 2

        const val PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST: Int = 1
        const val DISCONTINUITY_REASON_SEEK: Int = 1
        const val MEDIA_ITEM_TRANSITION_REASON_SEEK: Int = 1
        const val MEDIA_ITEM_TRANSITION_REASON_AUTO: Int = 2
        const val MEDIA_ITEM_TRANSITION_REASON_REPEAT: Int = 3
    }

    /** 播放状态/事件监听。真实库有 20+ 默认回调，这里覆盖 app 实际覆写面并留常用项。 */
    interface Listener {
        fun onPlaybackStateChanged(playbackState: Int) {}
        fun onPlayerError(error: PlaybackException) {}
        fun onIsPlayingChanged(isPlaying: Boolean) {}
        fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {}
        fun onRepeatModeChanged(repeatMode: Int) {}
        fun onVolumeChanged(volume: Float) {}
        fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {}
        fun onPositionDiscontinuity(oldPosition: PositionInfo, newPosition: PositionInfo, reason: Int) {}
        fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
        fun onEvents(player: Player, events: Events) {}
    }

    class PositionInfo(
        val mediaItemIndex: Int = 0,
        val positionMs: Long = 0L,
        val mediaItem: MediaItem? = null
    )

    class Events(private val flags: Set<Int> = emptySet()) {
        fun contains(event: Int): Boolean = flags.contains(event)
        fun size(): Int = flags.size
    }

    var playWhenReady: Boolean
    var repeatMode: Int
    var volume: Float

    val isPlaying: Boolean
    val playbackState: Int
    val currentPosition: Long
    val duration: Long
    /** Player.bufferedPosition（缓冲到的位置 ms，桌面恒 0）。——Nova 注 */
    val bufferedPosition: Long
    val currentMediaItemIndex: Int
    val currentMediaItem: MediaItem?

    fun play()
    fun pause()
    fun stop()
    fun release()
    fun seekTo(positionMs: Long)
    fun seekTo(mediaItemIndex: Int, positionMs: Long)
    fun setMediaItem(mediaItem: MediaItem)
    fun setMediaItems(mediaItems: List<MediaItem>, startIndex: Int, startPositionMs: Long)
    fun addMediaItem(mediaItem: MediaItem)
    fun clearMediaItems()
    fun prepare()
    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)
    fun setAudioAttributes(audioAttributes: AudioAttributes, handleAudioFocus: Boolean)
}
