package com.averin.android.developer.videoplayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.view.isVisible
import com.averin.android.developer.base.util.toTimerFormat
import com.averin.android.developer.dashboard.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import timber.log.Timber
import kotlin.math.max

class VideoPlayerActivity : AppCompatActivity(R.layout.ac_video_player), Player.Listener {

    private val backBtn: ImageView by lazy { findViewById(R.id.ivBack) }
    private val playerView: StyledPlayerView by lazy { findViewById(R.id.styledPlayerView) }
    private val playerControlView: StyledPlayerControlView by lazy { findViewById(R.id.playerControlView) }
    private val centralPlayPauseButton: ImageView by lazy { findViewById(R.id.ivCentralPlayPauseButton) }
    private val playPauseButton: ImageView by lazy { findViewById(R.id.ivPlayPauseButton) }
    private val videoProgressBar: AppCompatSeekBar by lazy { findViewById(R.id.videoSeekBar) }
    private val chartSeekBar: AppCompatSeekBar by lazy { findViewById(R.id.chartSeekBar) }
    private val loadingProgressBar: ProgressBar by lazy { findViewById(R.id.loadingProgressBar) }
    private val timeStart: TextView by lazy { findViewById(R.id.tvTimeStart) }
    private val timeFinish: TextView by lazy { findViewById(R.id.tvTimeFinish) }
    private val videoUrl: String? by lazy { intent.getStringExtra(VIDEO_URL_EXTRA) }
    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(this).build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    private var isUpdateSeekProgress: Boolean = true
    private var isPlaying: Boolean = false
        set(value) {
            field = value
            val bottomIcon = if (value) {
                R.drawable.ic_pause
            } else {
                R.drawable.ic_play
            }
            playPauseButton.setImageResource(bottomIcon)

            val centralIcon = if (value) {
                R.drawable.img_pause_button
            } else {
                R.drawable.img_play_button
            }
            centralPlayPauseButton.setImageResource(centralIcon)
            centralPlayPauseButton.isVisible = !value

            if (value) {
                player.playWhenReady = true
            } else {
                player.pause()
            }
        }

    override fun onBackPressed() {
        player.stop()
        player.release()
        super.onBackPressed()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backBtn.setOnClickListener { onBackPressed() }
        playPauseButton.setOnClickListener { isPlaying = !isPlaying }
        centralPlayPauseButton.setOnClickListener { isPlaying = !isPlaying }
        playerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                isPlaying = !isPlaying
            }
            true
        }
        initScreen(savedInstanceState)
    }

    private fun initScreen(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            videoUrl?.let { startPlaying(it) }
        }
    }

    override fun onResume() {
        super.onResume()
        initializePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoredPosition = savedInstanceState.getLong(KEY_START_POSITION, 0L)
        player.seekTo(restoredPosition)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val savedPosition = max(0, player.contentPosition)
        outState.putLong(KEY_START_POSITION, savedPosition)
    }

    override fun onPlayerError(error: PlaybackException) {
        error.cause?.let { throwable ->
            Timber.e(throwable)
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)) {
            loadingProgressBar.isVisible = false
            playerControlView.setProgressUpdateListener { position, bufferedPosition ->
                if (player.duration > 0L && videoProgressBar.max != player.duration.toInt()) {
                    videoProgressBar.max = player.duration.toInt()
                    chartSeekBar.max = player.duration.toInt() * 10
                }
                if (player.duration > 0L && isUpdateSeekProgress) {
                    videoProgressBar.progress = position.toInt()
                    videoProgressBar.secondaryProgress = bufferedPosition.toInt()
                    chartSeekBar.progress = position.toInt()
                }
            }
        }

        if (player.playbackState == Player.STATE_ENDED) {
            isPlaying = false
            player.seekTo(0)
        }
    }

    private fun startPlaying(url: String) {
        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(url))
            .build()
        player.addMediaItem(mediaItem)
        player.prepare()
        isPlaying = true
    }

    private fun initializePlayer() {
        player.addListener(this)
        playerView.player = player
        playerControlView.player = player
        playerControlView.showTimeoutMs = 0
        videoProgressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                val timeLeft = (player.currentPosition / 1000).toTimerFormat()
                val timeDuration = ((player.duration - player.currentPosition) / 1000).toTimerFormat()
                timeStart.text = timeLeft
                timeFinish.text = "-$timeDuration"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isUpdateSeekProgress = false
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isUpdateSeekProgress = true
                player.seekTo(seekBar.progress.toLong())
            }
        })
    }

    private fun releasePlayer() {
        isPlaying = false
        player.removeListener(this)
        playerView.player = null
        playerControlView.player = null
        videoProgressBar.setOnSeekBarChangeListener(null)
    }

    companion object {
        const val KEY_START_POSITION = "KEY_START_POSITION"
        const val VIDEO_URL_EXTRA = "VIDEO_URL_EXTRA"
        fun makeIntent(context: Context, videoUrl: String) = Intent(context, VideoPlayerActivity::class.java).apply {
            putExtra(VIDEO_URL_EXTRA, videoUrl)
        }
    }
}
