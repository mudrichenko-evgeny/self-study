package com.mudrichenko.evgeniy.selfstudy.ui.screen.quiz_completed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizQuestion
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragmentViewModel
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizCompletedViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : BaseFragmentViewModel() {

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _quizQuestionList = MutableLiveData<List<QuizQuestion>>(emptyList())
    val quizQuestionList: LiveData<List<QuizQuestion>> = _quizQuestionList

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            quizRepository.getQuizQuestionList().let { quizQuestionListResponse ->
                when (quizQuestionListResponse) {
                    is DataResponse.Successful -> {
                        questionListReceived(quizQuestionListResponse.data)
                    }
                    is DataResponse.Error -> {
                        showSnackBar(quizQuestionListResponse)
                    }
                }
                _isLoading.value = false
            }
        }
    }

    private fun questionListReceived(quizQuestionList: List<QuizQuestion>) {
        _quizQuestionList.value = quizQuestionList
    }

    fun onQuitClicked() {
        viewModelScope.launch {
            handleFinishQuizResponse(quizRepository.finishQuiz())
        }
    }

    private fun handleFinishQuizResponse(finishQuizResponse: DataResponse<List<QuizQuestion>>) {
        when (finishQuizResponse) {
            is DataResponse.Successful -> {
                quizSuccessfullyFinished()
            }
            is DataResponse.Error -> {
                showSnackBar(finishQuizResponse)
            }
        }
    }

    private fun quizSuccessfullyFinished() {
        navigateTo(NavigationCommand.NavigateTo.Direction.Screen.Home)
    }

}