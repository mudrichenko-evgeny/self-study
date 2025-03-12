package com.mudrichenkoevgeny.selfstudy.ui.custom_view.audio_recorder

import androidx.annotation.StringRes

interface AudioRecorderListener {
    fun onStartRecordingClicked()
    fun onStopRecordingClicked(filePath: String?)
    fun onDeleteRecordingClicked()
    fun onMessageReceived(@StringRes messageResId: Int)
}