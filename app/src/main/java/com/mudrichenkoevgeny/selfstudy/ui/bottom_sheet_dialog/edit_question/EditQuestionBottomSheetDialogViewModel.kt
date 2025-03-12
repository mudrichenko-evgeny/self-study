package com.mudrichenkoevgeny.selfstudy.ui.bottom_sheet_dialog.edit_question

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.data.SingleLiveEvent
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.Question
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import com.mudrichenkoevgeny.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenkoevgeny.selfstudy.ui.base.bottom_sheet_dialog.BaseBottomSheetDialogViewModel
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditQuestionBottomSheetDialogViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val quizPacksRepository: QuizPacksRepository
) : BaseBottomSheetDialogViewModel() {

    private var quizPack: QuizPack? = null

    private var _question = MutableLiveData<Question>()
    val question: LiveData<Question> = _question

    val questionContent: MutableLiveData<String> = MutableLiveData<String>()

    val questionAnswer: MutableLiveData<String> = MutableLiveData<String>()

    private var _editSuccessfulEvent = MutableLiveData<SingleLiveEvent<QuizPack?>>()
    val editSuccessfulEvent: LiveData<SingleLiveEvent<QuizPack?>> = _editSuccessfulEvent

    fun setData(quizPackId: Long, questionId: Long) {
        viewModelScope.launch {
            getQuizPack(quizPackId)
            getQuestion(questionId)
        }
    }

    private suspend fun getQuizPack(quizPackId: Long) {
        quizPacksRepository.getQuizPack(quizPackId).let { quizPackResponse ->
            when (quizPackResponse) {
                is DataResponse.Successful -> {
                    quizPack = quizPackResponse.data()
                }
                is DataResponse.Error -> {
                    showSnackBar(quizPackResponse)
                }
            }
        }
    }

    private suspend fun getQuestion(questionId: Long) {
        if (questionId < 0) {
            onQuestionReceived(
                Question(
                    id = questionId,
                    content = "",
                    answer = "",
                    attemptCount = 0,
                    correctCount = 0
                )
            )
        } else {
            onQuestionDataReceived(quizRepository.getQuestion(questionId))
        }
    }

    private fun onQuestionDataReceived(questionResponse: DataResponse<Question>) {
        when (questionResponse) {
            is DataResponse.Successful -> {
                onQuestionReceived(questionResponse.data)
            }
            is DataResponse.Error -> {
                showSnackBar(questionResponse)
            }
        }
    }

    private fun onQuestionReceived(question: Question) {
        _question.value = question
        questionContent.value = question.content
        questionAnswer.value = question.answer
    }

    fun onSaveClicked() {
        val content = questionContent.value
        val answer = questionAnswer.value
        if (content?.isNotBlank() == true && answer?.isNotBlank() == true) {
            saveQuestion(content, answer)
        } else {
            showSnackBar(
                InfoSnackBarModel(
                    textResId = R.string.error_cannot_be_empty
                )
            )
        }
    }

    private fun saveQuestion(content: String, answer: String) {
        question.value?.let { question ->
            viewModelScope.launch {
                onSaveResultReceived(
                    quizRepository.editQuestion(
                        question = question,
                        content = content,
                        answer = answer
                    )
                )
            }
        }
    }

    private fun onSaveResultReceived(dataResponse: DataResponse<Question>) {
        when (dataResponse) {
            is DataResponse.Successful -> {
                _editSuccessfulEvent.value = SingleLiveEvent(quizPack)
            }
            is DataResponse.Error -> {
                showSnackBar(dataResponse)
            }
        }
    }

    fun onDeleteClicked() {
        question.value?.id?.let { questionId ->
            viewModelScope.launch {
                onDeleteResultReceived(
                    quizRepository.deleteQuestion(questionId)
                )
            }
        }
    }

    private fun onDeleteResultReceived(dataResponse: DataResponse<Long>) {
        when (dataResponse) {
            is DataResponse.Successful -> {
                _editSuccessfulEvent.value = SingleLiveEvent(quizPack)
            }
            is DataResponse.Error -> {
                showSnackBar(dataResponse)
            }
        }
    }

}