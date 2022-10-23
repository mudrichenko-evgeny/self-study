package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.audio_recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.mudrichenko.evgeniy.selfstudy.BR
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.databinding.ViewAudioRecorderBinding
import com.mudrichenko.evgeniy.selfstudy.extensions.setProgressChangedListener
import com.mudrichenko.evgeniy.selfstudy.extensions.setVisibleOrGone

class AudioRecorderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var binding: ViewAudioRecorderBinding = ViewAudioRecorderBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val viewModel by lazy {
        findViewTreeViewModelStoreOwner().let { viewModelStoreOwner ->
            if (viewModelStoreOwner != null) {
                ViewModelProvider(viewModelStoreOwner)[AudioRecorderViewModel::class.java]
            } else {
                throw Exception("ViewModelStoreOwner null")
            }
        }
    }

    private var lifecycleOwner: LifecycleOwner? = null

    private val lifecycleObserver = object : DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) {
            initPlayer()
            super.onCreate(owner)
        }

        override fun onPause(owner: LifecycleOwner) {
            stopRecording()
            pauseAudio()
            super.onPause(owner)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            stopPlayer()
            super.onDestroy(owner)
        }

    }

    private var mediaRecorder: MediaRecorder? = null
    private var player: ExoPlayer? = null
    private var deleteAudioAvailable: Boolean = false
    private var loadingAnimationAvailable: Boolean = true

    init {
        initView(context, attrs)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ViewTreeLifecycleOwner.get(this)?.let { lifecycleOwner ->
            onLifecycleOwnerAttached(lifecycleOwner)
        }
    }

    private fun onLifecycleOwnerAttached(lifecycleOwner: LifecycleOwner) {
        binding.lifecycleOwner = lifecycleOwner
        binding.setVariable(BR.viewModel, viewModel)
        viewModel.audioRecorderData.observe(lifecycleOwner, Observer { audioRecorderData ->
            setData(audioRecorderData)
        })
    }

    fun setLifecycleObserver(lifecycleOwner: LifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
    }

    fun setListener(listener: AudioRecorderListener?) {
        viewModel.listener = listener
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.AudioRecorderView).apply {
            try {
                deleteAudioAvailable = getBoolean(
                    R.styleable.AudioRecorderView_deleteAudioAvailable,
                    true
                )
                loadingAnimationAvailable = getBoolean(
                    R.styleable.AudioRecorderView_loadingAnimationAvailable,
                    true
                )
            } finally {
                recycle()
            }
        }
        binding.deleteRecordingButton.setVisibleOrGone(deleteAudioAvailable)
        setListeners()
    }

    private fun setListeners() {
        binding.startRecordingButton.setOnClickListener {
            requestAudioRecording()
        }
        binding.stopRecordingButton.setOnClickListener {
            stopRecording()
        }
        binding.deleteRecordingButton.setOnClickListener {
            viewModel.onDeleteRecording()
        }
        binding.playButton.setOnClickListener {
            playRecord()
        }
        binding.pauseButton.setOnClickListener {
            pauseAudio()
        }
        binding.seekBar.setProgressChangedListener { progress ->
            viewModel.audioRecorderData.value?.getSeekToValue(progress)?.let { positionInMillis ->
                viewModel.seekTo(player?.duration, positionInMillis)
                seekTo(positionInMillis)
            }
        }
    }

    private fun initPlayer() {
        setIsLoading(viewModel.isLoading)
        player = ExoPlayer.Builder(context).build().apply {
            this.repeatMode = ExoPlayer.REPEAT_MODE_OFF
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when(playbackState) {
                        Player.STATE_READY -> {
                            viewModel.setAudioRecordData(player?.duration, player?.currentPosition)
                            setIsLoading(false)
                        }
                        Player.STATE_ENDED -> {
                            player?.pause()
                            if (player?.currentMediaItem != null) {
                                player?.seekTo(0)
                            }
                            viewModel.onPlayAudioEnded(player?.duration)
                        }
                        else -> { }
                    }
                }
            })
        }
        setAudio(viewModel.audioFilePath)
    }

    private fun setData(data: AudioRecorderData) {
        binding.apply {
            deleteRecordingButton.apply {
                setVisibleOrGone(deleteAudioAvailable && data.audioReady())
                isEnabled = deleteAudioAvailable && data.canDeleteAudio() && data.controlsEnabled
            }
            playButton.apply {
                setVisibleOrGone(data.canPlayAudio())
                isEnabled = data.controlsEnabled
            }
            pauseButton.apply {
                setVisibleOrGone(data.canPauseAudio())
                isEnabled = data.controlsEnabled
            }
            startRecordingButton.apply {
                setVisibleOrGone(data.canStartRecordingAudio())
                isEnabled = data.controlsEnabled
            }
            stopRecordingButton.apply {
                setVisibleOrGone(data.canStopRecordingAudio())
                isEnabled = data.controlsEnabled
            }
            audioRecorderTextView.text = when(data.playerStatus) {
                AudioRecorderStatus.NO_AUDIO -> {
                    resources.getString(R.string.player_state_no_audio)
                }
                AudioRecorderStatus.RECORDING -> {
                    resources.getString(
                        R.string.player_state_recording,
                        data.getFormattedDuration()
                    )
                }
                AudioRecorderStatus.STOPPED, AudioRecorderStatus.PLAYBACK -> {
                    resources.getString(
                        R.string.player_state_audio_available,
                        data.getFormattedSeekTo(),
                        data.getFormattedDuration(),
                    )
                }
            }
            seekBar.apply {
                max = data.seekBarMax()
                progress = data.seekBarProgress()
                isEnabled = data.seekBarEnabled() && data.controlsEnabled
            }
        }
    }

    fun setIsLoading(isLoading: Boolean) {
        viewModel.isLoading = isLoading
        if (loadingAnimationAvailable) binding.progressBar.setVisibleOrGone(isLoading)
    }

    fun setAudio(filePath: String?) {
        viewModel.audioFilePath = filePath
        if (filePath.isNullOrEmpty()) {
            player?.clearMediaItems()
            viewModel.setAudioRecordData(null, null)
            setIsLoading(false)
        } else {
            setIsLoading(true)
            val media = MediaItem.Builder()
                .setUri(filePath)
                .build()
            player?.setMediaItem(media)
            player?.prepare()
        }
    }

    private fun requestAudioRecording() {
        viewModel.requestAudioRecording()
    }

    fun startRecording(filePath: String) {
        viewModel.recordingFilePath = filePath
        mediaRecorder = getMediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
                start()
                viewModel.onAudioRecordingStarted()
            } catch (e: Exception) {
                e.printStackTrace()
                viewModel.listener?.onMessageReceived(R.string.error_unknown)
            }
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            try {
                stop()
                reset()
                release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mediaRecorder = null
        viewModel.onStopRecording()
    }

    private fun stopPlayer() {
        player?.release()
        player = null
    }

    private fun playRecord() {
        viewModel.onPlayRecordClicked(player?.duration, player?.currentPosition)
        player?.play()
    }

    private fun pauseAudio() {
        viewModel.onPauseClicked(player?.duration, player?.currentPosition)
        player?.pause()
    }

    private fun seekTo(positionMillis: Long) {
        player?.seekTo(positionMillis)
    }

    @Suppress("DEPRECATION")
    private fun getMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            MediaRecorder(context)
        else
            MediaRecorder()
    }

}