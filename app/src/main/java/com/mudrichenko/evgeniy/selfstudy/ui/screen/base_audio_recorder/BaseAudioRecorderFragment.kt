package com.mudrichenko.evgeniy.selfstudy.ui.screen.base_audio_recorder

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragment
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.RequestPermissionResult
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.audio_recorder.AudioRecorderView
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel

abstract class BaseAudioRecorderFragment<B: ViewDataBinding, VM: BaseAudioRecorderViewModel>:
    BaseFragment<B, VM>() {

    private lateinit var audioRecorderView: AudioRecorderView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAudioRecorder()
    }

    private fun onStartRecordingEvent(filePath: String) {
        if (hasRecordAudioPermission()) {
            audioRecorderView.startRecording(filePath)
        } else {
            requestRecordAudioPermission(
                object : RequestPermissionResult {
                    override fun onPermissionGranted() {
                        audioRecorderView.startRecording(filePath)
                    }

                    override fun onPermissionNotGranted() {
                        showInfoSnackBar(
                            InfoSnackBarModel(textResId = R.string.error_permissions_not_granted)
                        )
                    }
                }
            )
        }
    }

    private fun initAudioRecorder() {
        audioRecorderView = getAudioRecorderView()
        audioRecorderView.also {
            it.setLifecycleObserver(this)
            it.setListener(viewModel.audioRecorderListener)
        }
        viewModel.setAudioEvent.observeSingleLiveEvent { filePath ->
            audioRecorderView.setAudio(filePath)
        }
        viewModel.startRecordingEvent.observeSingleLiveEvent { filePath ->
            onStartRecordingEvent(filePath)
        }
        viewModel.showAudioLoadingEvent.observeSingleLiveEvent {
            audioRecorderView.setIsLoading(true)
        }
    }

    abstract fun getAudioRecorderView(): AudioRecorderView

}