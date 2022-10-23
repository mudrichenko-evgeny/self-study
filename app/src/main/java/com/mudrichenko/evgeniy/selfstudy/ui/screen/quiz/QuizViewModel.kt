package com.mudrichenko.evgeniy.selfstudy.ui.screen.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.AnswerStatus
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenko.evgeniy.selfstudy.domain.file_repository.FileRepository
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenko.evgeniy.selfstudy.extensions.getNextQuestion
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.NavigationCommand
import com.mudrichenko.evgeniy.selfstudy.ui.screen.base_audio_recorder.BaseAudioRecorderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    fileRepository: FileRepository
) : BaseAudioRecorderViewModel(fileRepository) {

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private fun openCheckAnswerScreen(quizQuestion: QuizQuestion) {
        navigateTo(NavigationCommand.NavigateTo.Direction.Screen.CheckAnswer(quizQuestion.questionId))
    }

    private fun openQuizCompletedScreen() {
        navigateTo(NavigationCommand.NavigateTo.Direction.Screen.QuizCompleted)
    }

    init {
        loadQuizQuestionList()
    }

    private fun loadQuizQuestionList() {
        viewModelScope.launch {
            handleQuizQuestionListDatabaseResponse(quizRepository.getQuizQuestionList())
        }
    }

    private fun handleQuizQuestionListDatabaseResponse(
        quizQuestionListResponse: DataResponse<List<QuizQuestion>>
    ) {
        when (quizQuestionListResponse) {
            is DataResponse.Successful -> {
                quizQuestionPackReceived(quizQuestionListResponse.data)
            }
            is DataResponse.Error -> {
                showSnackBar(quizQuestionListResponse)
            }
        }
    }

    private fun quizQuestionPackReceived(quizQuestionList: List<QuizQuestion>) {
        quizQuestionList.getNextQuestion().let { nextQuestion ->
            when {
                nextQuestion == null -> {
                    openQuizCompletedScreen()
                }
                nextQuestion.isAnswered() && !nextQuestion.isAnswerChecked() -> {
                    openCheckAnswerScreen(nextQuestion)
                }
                else -> {
                    onQuizQuestionReceived(nextQuestion)
                }
            }
        }
    }

    private fun onQuizQuestionReceived(quizQuestion: QuizQuestion) {
        viewModelScope.launch {
            deleteAudio()
            setQuizQuestion(quizQuestion)
            _isLoading.value = false
        }
    }

    fun onAnswerChanged(answer: String) {
        getQuizQuestionData()?.let { quizQuestion ->
            quizQuestion.userAnswerText = answer
            setQuizQuestion(quizQuestion)
        }
    }

    fun onCheckAnswerClicked() {
        quizQuestion.value?.let { quizQuestion ->
            viewModelScope.launch {
                quizQuestion.answerStatus = AnswerStatus.ANSWERED
                quizRepository.answeredOnQuizQuestion(quizQuestion)
                openCheckAnswerScreen(quizQuestion)
            }
        }
    }

}