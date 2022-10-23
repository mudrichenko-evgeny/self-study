package com.mudrichenko.evgeniy.selfstudy.ui.custom_view.audio_recorder

enum class AudioRecorderStatus {
    NO_AUDIO, STOPPED, RECORDING, PLAYBACK;

    fun audioReady(): Boolean {
        return this == STOPPED || this == PLAYBACK
    }

}