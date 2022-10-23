package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.audio_recorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mudrichenko.evgeniy.selfstudy.extensions.secondsToMillis
import com.mudrichenko.evgeniy.selfstudy.utils.tickerFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class AudioRecorderViewModel: ViewModel() {

    private var recordingJob: Job? = null
    private var playingJob: Job? = null

    private var _audioRecorderData = MutableLiveData(AudioRecorderData())
    val audioRecorderData: LiveData<AudioRecorderData> = _audioRecorderData

    private fun setAudioRecorderData(audioRecorderData: AudioRecorderData) {
        _audioRecorderData.postValue(audioRecorderData)
    }

    var recordingFilePath: String? = null
    var audioFilePath: String? = null
    var isLoading: Boolean = true

    var listener: AudioRecorderListener? = null

    fun setAudioRecordData(durationMillis: Long?, positionMillis: Long?) {
        setAudioRecorderData(
            AudioRecorderData(
                status = AudioRecorderStatus.STOPPED,
                audioDurationInMillis = durationMillis,
                audioSeekToInMillis = positionMillis
            )
        )
    }

    fun onPlayRecordClicked(durationMillis: Long?, positionMillis: Long?) {
        playingJob?.cancel()
        setAudioRecorderData(
            AudioRecorderData(
                status = AudioRecorderStatus.PLAYBACK,
                audioDurationInMillis = durationMillis,
                audioSeekToInMillis = positionMillis
            )
        )
        playingJob = viewModelScope.launch {
            tickerFlow(1.seconds)
                .collect { seconds ->
                    setAudioRecorderData(
                        AudioRecorderData(
                            status = AudioRecorderStatus.PLAYBACK,
                            audioDurationInMillis = durationMillis,
                            audioSeekToInMillis = (positionMillis ?: 0) + seconds.secondsToMillis()
                        )
                    )
                }
        }
    }

    fun onPauseClicked(durationMillis: Long?, positionMillis: Long?) {
        playingJob?.cancel()
        setAudioRecorderData(
            AudioRecorderData(
                status = AudioRecorderStatus.STOPPED,
                audioDurationInMillis = durationMillis,
                audioSeekToInMillis = positionMillis
            )
        )
    }

    fun onPlayAudioEnded(durationMillis: Long?) {
        playingJob?.cancel()
        setAudioRecorderData(
            AudioRecorderData(
                status = AudioRecorderStatus.STOPPED,
                audioDurationInMillis = durationMillis
            )
        )
    }

    fun requestAudioRecording() {
        _audioRecorderData.value?.copy(
            controlsEnabled = false
        )?.let { audioRecorderData ->
            setAudioRecorderData(audioRecorderData)
        }
        listener?.onStartRecordingClicked()
    }

    fun onAudioRecordingStarted() {
        setAudioRecorderData(
            AudioRecorderData(
                status = AudioRecorderStatus.RECORDING
            )
        )
        recordingJob = viewModelScope.launch {
            tickerFlow(1.seconds)
                .collect { seconds ->
                    setAudioRecorderData(
                        AudioRecorderData(
                            status = AudioRecorderStatus.RECORDING,
                            audioDurationInMillis = seconds.secondsToMillis()
                        )
                    )
                }
        }
    }

    fun onStopRecording() {
        if(recordingJob?.isActive == true) {
            _audioRecorderData.value?.copy(
                controlsEnabled = false
            )?.let { audioRecorderData ->
                setAudioRecorderData(audioRecorderData)
            }
            listener?.onStopRecordingClicked(recordingFilePath)
            recordingJob?.cancel()
        }
    }

    fun onDeleteRecording() {
        setAudioRecorderData(
            AudioRecorderData(
                status = AudioRecorderStatus.NO_AUDIO
            )
        )
        recordingFilePath = null
        listener?.onDeleteRecordingClicked()
    }

    fun seekTo(durationMillis: Long?, positionInMillis: Long) {
        onPlayRecordClicked(durationMillis, positionInMillis)
    }

}