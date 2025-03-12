package com.mudrichenkoevgeny.selfstudy.ui.bottom_sheet_dialog.question_statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.Question
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenkoevgeny.selfstudy.ui.base.bottom_sheet_dialog.BaseBottomSheetDialogViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionStatisticsBottomSheetDialogViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : BaseBottomSheetDialogViewModel() {

    private var _question = MutableLiveData<Question>()
    val question: LiveData<Question> = _question

    fun setQuestionId(questionId: Long) {
        viewModelScope.launch {
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
    }

}