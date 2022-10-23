package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.audio_recorder

import com.mudrichenko.evgeniy.selfstudy.extensions.millisToSeconds
import com.mudrichenko.evgeniy.selfstudy.extensions.secondsToFormattedTime
import com.mudrichenko.evgeniy.selfstudy.extensions.secondsToMillis
import java.util.*

class AudioRecorderData(
    status: AudioRecorderStatus = AudioRecorderStatus.NO_AUDIO,
    val audioDurationInMillis: Long? = null,
    val audioSeekToInMillis: Long? = null,
    val controlsEnabled: Boolean = true
) {

    var playerStatus = getPlayerStatus(status, audioDurationInMillis)
    var audioDurationInSeconds = getTimeInSeconds(audioDurationInMillis)
    var audioSeekToInSeconds = getTimeInSeconds(audioSeekToInMillis)

    private fun getPlayerStatus(
        status: AudioRecorderStatus,
        audioDurationInMillis: Long?
    ): AudioRecorderStatus {
        return when {
            (audioDurationInMillis ?: -1) < 0 -> AudioRecorderStatus.NO_AUDIO
            else -> status
        }
    }

    private fun getTimeInSeconds(timeInMs: Long?): Int {
        return if (timeInMs != null && timeInMs >= 0)
            timeInMs.millisToSeconds()
        else
            0
    }

    fun getFormattedDuration(): String {
        return audioDurationInSeconds.secondsToFormattedTime()
    }

    fun getFormattedSeekTo(): String {
        return audioSeekToInSeconds.secondsToFormattedTime()
    }

    fun getSeekToValue(sliderValue: Int): Long {
        return sliderValue.secondsToMillis()
    }

    fun canDeleteAudio(): Boolean {
        return playerStatus == AudioRecorderStatus.STOPPED
    }

    fun canPlayAudio(): Boolean {
        return playerStatus == AudioRecorderStatus.STOPPED
    }

    fun canPauseAudio(): Boolean {
        return playerStatus == AudioRecorderStatus.PLAYBACK
    }

    fun canStartRecordingAudio(): Boolean {
        return playerStatus == AudioRecorderStatus.NO_AUDIO
    }

    fun canStopRecordingAudio(): Boolean {
        return playerStatus == AudioRecorderStatus.RECORDING
    }

    fun audioReady(): Boolean {
        return playerStatus.audioReady()
    }

    private fun audioNotEmpty(): Boolean {
        return (audioDurationInMillis ?: -1) > 0
    }

    fun seekBarEnabled(): Boolean {
        return audioReady() && audioNotEmpty()
    }

    fun seekBarMax(): Int {
        return if (audioDurationInSeconds > 0) audioDurationInSeconds else 1
    }

    fun seekBarProgress(): Int {
        return audioSeekToInSeconds
    }

    fun copy(
        status: AudioRecorderStatus? = null,
        audioDurationInMillis: Long? = null,
        audioSeekToInMillis: Long? = null,
        controlsEnabled: Boolean? = null
    ): AudioRecorderData {
        return AudioRecorderData(
            status = status ?: this.playerStatus,
            audioDurationInMillis = audioDurationInMillis ?: this.audioDurationInMillis,
            audioSeekToInMillis = audioSeekToInMillis ?: this.audioSeekToInMillis,
            controlsEnabled = controlsEnabled ?: this.controlsEnabled
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AudioRecorderData) return false
        return other.playerStatus == this.playerStatus
                && other.audioDurationInMillis == this.audioDurationInMillis
                && other.audioSeekToInMillis == this.audioSeekToInMillis
    }

    override fun hashCode(): Int {
        return Objects.hash(playerStatus, audioDurationInMillis, audioSeekToInMillis)
    }

}