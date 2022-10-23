package com.mudrichenko.evgeniy.selfstudy.ui.screen.check_answer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.AnswerStatus
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenko.evgeniy.selfstudy.domain.file_repository.FileRepository
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenko.evgeniy.selfstudy.extensions.hasNextQuestion
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.NavigationCommand
import com.mudrichenko.evgeniy.selfstudy.ui.screen.base_audio_recorder.BaseAudioRecorderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckAnswerViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    fileRepository: FileRepository
) : BaseAudioRecorderViewModel(fileRepository) {

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    var hasNextQuestion: Boolean = false

    fun setQuizQuestionId(quizQuestionId: Long) {
        viewModelScope.launch {
            handleQuizQuestionResponse(quizRepository.getQuizQuestion(quizQuestionId))
        }
    }

    private fun handleQuizQuestionResponse(quizQuestionResponse: DataResponse<QuizQuestion>) {
        when (quizQuestionResponse) {
            is DataResponse.Successful -> {
                onQuizQuestionReceived(quizQuestionResponse.data)
            }
            is DataResponse.Error -> {
                showSnackBar(quizQuestionResponse)
            }
        }
        getQuizQuestionsList()
    }

    private fun onQuizQuestionReceived(quizQuestion: QuizQuestion) {
        setQuizQuestion(quizQuestion)
        if (quizQuestion.hasAudioRecord) {
            loadAudio()
        } else {
            setAudio(null)
        }
    }

    private fun getQuizQuestionsList() {
        viewModelScope.launch {
            handleQuizQuestionsListResponse(quizRepository.getQuizQuestionList())
        }
    }

    private fun handleQuizQuestionsListResponse(
        quizQuestionsListResponse: DataResponse<List<QuizQuestion>>
    ) {
        when (quizQuestionsListResponse) {
            is DataResponse.Successful -> {
                onQuizQuestionsListReceived(quizQuestionsListResponse.data)
            }
            is DataResponse.Error -> {
                showSnackBar(quizQuestionsListResponse)
            }
        }
        _isLoading.value = false
    }

    private fun onQuizQuestionsListReceived(quizQuestionsList: List<QuizQuestion>) {
        hasNextQuestion = quizQuestionsList.hasNextQuestion()
    }

    fun onWrongClicked() {
        quizQuestion.value?.answerStatus = AnswerStatus.WRONG
        onChecked()
    }

    fun onCorrectClicked() {
        quizQuestion.value?.answerStatus = AnswerStatus.CORRECT
        onChecked()
    }

    private fun onChecked() {
        quizQuestion.value.let { quizQuestion ->
            if (quizQuestion != null) {
                viewModelScope.launch {
                    quizRepository.quizQuestionChecked(quizQuestion)
                    openNextScreen()
                }
            } else {
                openNextScreen()
            }
        }
    }

    private fun openNextScreen() {
        if (hasNextQuestion) openQuizScreen() else openQuizCompletedScreen()
    }

    private fun openQuizScreen() {
        navigateTo(NavigationCommand.NavigateTo.Direction.Screen.Quiz)
    }

    private fun openQuizCompletedScreen() {
        navigateTo(NavigationCommand.NavigateTo.Direction.Screen.QuizCompleted)
    }

}