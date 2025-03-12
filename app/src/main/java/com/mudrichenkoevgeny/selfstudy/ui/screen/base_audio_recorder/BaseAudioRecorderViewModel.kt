package com.mudrichenkoevgeny.selfstudy.ui.screen.base_audio_recorder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.selfstudy.data.SingleLiveEvent
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenkoevgeny.selfstudy.domain.file_repository.FileRepository
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.BaseFragmentViewModel
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.audio_recorder.AudioRecorderListener
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import kotlinx.coroutines.launch

abstract class BaseAudioRecorderViewModel constructor(
    private val fileRepository: FileRepository
) : BaseFragmentViewModel() {

    private var _showAudioLoadingEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val showAudioLoadingEvent: LiveData<SingleLiveEvent<Unit>> =_showAudioLoadingEvent

    private var _quizQuestion = MutableLiveData<QuizQuestion?>()
    val quizQuestion: LiveData<QuizQuestion?> = _quizQuestion

    protected fun setQuizQuestion(quizQuestion: QuizQuestion?) {
        _quizQuestion.value = quizQuestion
    }

    protected fun getQuizQuestionData(): QuizQuestion? {
        return _quizQuestion.value
    }

    private var _setAudioEvent = MutableLiveData<SingleLiveEvent<String>>()
    val setAudioEvent: LiveData<SingleLiveEvent<String>> = _setAudioEvent
    protected fun setAudio(filePath: String?) {
        _setAudioEvent.value = SingleLiveEvent(filePath ?: "")
    }

    private var _startRecordingEvent = MutableLiveData<SingleLiveEvent<String>>()
    val startRecordingEvent: LiveData<SingleLiveEvent<String>> = _startRecordingEvent
    private fun startRecording(filePath: String) {
        _startRecordingEvent.value = SingleLiveEvent(filePath)
    }

    val audioRecorderListener = object : AudioRecorderListener {
        override fun onStartRecordingClicked() {
            startRecordingEventReceived()
        }

        override fun onStopRecordingClicked(filePath: String?) {
            stopRecordingEventReceived(filePath)
        }

        override fun onDeleteRecordingClicked() {
            deleteRecordingEventReceived()
        }

        override fun onMessageReceived(messageResId: Int) {
            showSnackBar(
                InfoSnackBarModel(
                    textResId = messageResId
                )
            )
        }
    }

    private fun startRecordingEventReceived() {
        fileRepository.makeDirectory(getAudioFileDirectory())
        startRecording(getAudioRecordPath())
    }

    private fun stopRecordingEventReceived(filePath: String?) {
        setQuizQuestion(
            getQuizQuestionData().also {
                it?.hasAudioRecord = !filePath.isNullOrEmpty()
            }
        )
        setAudio(filePath)
    }

    private fun deleteRecordingEventReceived() {
        viewModelScope.launch {
            deleteAudio()
        }
    }

    protected suspend fun deleteAudio() {
        _showAudioLoadingEvent.value = SingleLiveEvent(Unit)
        getQuizQuestionData()?.getAudioRecordName()?.let { audioRecordName ->
            fileRepository.deleteFile(
                directory = getAudioFileDirectory(),
                fileName = audioRecordName
            )
        }
        setQuizQuestion(
            getQuizQuestionData().also {
                it?.hasAudioRecord = false
            }
        )
        setAudio(null)
    }

    protected fun loadAudio() {
        _showAudioLoadingEvent.value = SingleLiveEvent(Unit)
        viewModelScope.launch {
            fileRepository.getFile(
                directory = getAudioFileDirectory(),
                fileName = getAudioRecordFileName()
            ).let { file ->
                if (file != null) {
                    setQuizQuestion(
                        getQuizQuestionData().also {
                            it?.hasAudioRecord = true
                        }
                    )
                    setAudio(file.path)
                } else {
                    setQuizQuestion(
                        getQuizQuestionData().also {
                            it?.hasAudioRecord = false
                        }
                    )
                    setAudio(null)
                }
            }
        }
    }

    private fun getAudioRecordFileName(): String {
        return "${getQuizQuestionData()?.getAudioRecordName() ?: 0}"
    }

    private fun getAudioFileDirectory(): String {
        return fileRepository.getAudioRecordsDirectory()
    }

    private fun getAudioRecordPath(): String {
        return "${getAudioFileDirectory()}/${getAudioRecordFileName()}"
    }

}