package com.mudrichenko.evgeniy.selfstudy.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenko.evgeniy.selfstudy.R
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.*
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_use_case.QuizUseCase
import com.mudrichenko.evgeniy.selfstudy.extensions.getNextQuestion
import com.mudrichenko.evgeniy.selfstudy.extensions.getQuizStatus
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragmentViewModel
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.NavigationCommand
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.number_picker.NumberPickerView
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.switch_view.SwitchView
import com.mudrichenko.evgeniy.selfstudy.ui.screen.main.model.MainBottomNavigationGraph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val quizPacksRepository: QuizPacksRepository,
    private val quizUseCase: QuizUseCase
) : BaseFragmentViewModel() {

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private var questionListLoading: Boolean = true
        set(value) {
            field = value
            _isLoading.value = field || quizQuestionListLoading
        }

    private var quizQuestionListLoading: Boolean = true
        set(value) {
            field = value
            _isLoading.value = field || questionListLoading
        }

    private var _quizPack = MutableLiveData<QuizPack>()
    val quizPack: LiveData<QuizPack> = _quizPack

    private var _questionList = MutableLiveData<List<Question>>(emptyList())
    val questionList: LiveData<List<Question>> = _questionList

    private var _quizQuestionList = MutableLiveData<List<QuizQuestion>>(emptyList())
    val quizQuestionList: LiveData<List<QuizQuestion>> = _quizQuestionList

    private var _preferDifficultQuestions = MutableLiveData<Boolean>()
    val preferDifficultQuestions: LiveData<Boolean> = _preferDifficultQuestions

    val switchListener = object : SwitchView.Listener {
        override fun onCheckChanged(checked: Boolean) {
            onDifficultQuestionsClicked(checked)
        }
    }

    private var _numberOfQuestion = MutableLiveData<Int>()
    val numberOfQuestion: LiveData<Int> = _numberOfQuestion

    var numberOfQuestionsPickerListener = object : NumberPickerView.Listener {
        override fun onNumberChanged(number: Int) {
            _numberOfQuestion.value = number
        }
    }

    init {
        loadData()
    }

    private fun loadData() {
        questionListLoading = true
        quizQuestionListLoading = true
        collectActiveQuizPack()
    }

    private fun collectActiveQuizPack() {
        viewModelScope.launch {
            quizPacksRepository.getActiveQuizPackFlow().collect { quizPack ->
                quizPackReceived(quizPack)
            }
        }
    }

    private fun quizPackReceived(quizPack: QuizPack?) {
        _quizPack.value = quizPack
        collectData()
    }

    private fun collectData() {
        viewModelScope.launch {
            launch {
                quizRepository.getQuestionListFlow().collect { questionList ->
                    handleQuestionListDataResponse(questionList)
                }
            }
            launch {
                quizRepository.getQuizQuestionListFlow().collect { quizQuestionList ->
                    handleQuizQuestionListDataResponse(quizQuestionList)
                }
            }
        }
    }


    private fun handleQuestionListDataResponse(questionListResponse: DataResponse<List<Question>>) {
        when (questionListResponse) {
            is DataResponse.Successful -> {
                questionListReceived(questionListResponse.data)
            }
            is DataResponse.Error -> {
                showSnackBar(questionListResponse)
            }
        }
        questionListLoading = false
    }

    private fun questionListReceived(questionList: List<Question>) {
        _questionList.value = questionList
        _numberOfQuestion.value = questionList.size
    }

    private fun handleQuizQuestionListDataResponse(
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
        quizQuestionListLoading = false
    }

    private fun quizQuestionPackReceived(quizQuestionList: List<QuizQuestion>) {
        _quizQuestionList.value = quizQuestionList
    }

    fun onStartButtonClicked() {
        when (_quizQuestionList.value?.getQuizStatus()) {
            QuizStatus.NOT_STARTED -> onStartQuizClicked()
            QuizStatus.STARTED -> onContinueQuizClicked()
            else -> { }
        }
    }

    private fun onStartQuizClicked() {
        _isLoading.value = true
        viewModelScope.launch {
            onNewQuizListDataResponseReceived(
                quizUseCase.createQuiz(
                    numberOfQuestion = numberOfQuestion.value ?: 0,
                    prioritizeDifficultQuestions = _preferDifficultQuestions.value == true
                )
            )
        }
    }

    private fun onContinueQuizClicked() {
        _quizQuestionList.value?.getNextQuestion().let { nextQuestion ->
            when {
                nextQuestion == null -> {
                    navigateTo(NavigationCommand.NavigateTo.Direction.Screen.QuizCompleted)
                }
                nextQuestion.isAnswered() && !nextQuestion.isAnswerChecked() -> {
                    navigateTo(NavigationCommand.NavigateTo.Direction.Screen.CheckAnswer(
                        nextQuestion.questionId
                    ))
                }
                else -> {
                    navigateTo(NavigationCommand.NavigateTo.Direction.Screen.Quiz)
                }
            }
        }
    }

    // ======================================= QUIZ STARTED =======================================

    fun finishQuizConfirmed() {
        viewModelScope.launch {
            _isLoading.value = true
            handleQuizQuestionListDataResponse(quizRepository.finishQuiz())
        }
    }

    // ===================================== QUIZ NOT STARTED =====================================

    fun onDifficultQuestionsClicked(checked: Boolean) {
        _preferDifficultQuestions.value = checked
    }

    private fun onNewQuizListDataResponseReceived(
        quizQuestionListResponse: DataResponse<List<QuizQuestion>>
    ) {
        when (quizQuestionListResponse) {
            is DataResponse.Successful -> {
                quizQuestionPackReceived(quizQuestionListResponse.data)
                navigateTo(NavigationCommand.NavigateTo.Direction.Screen.Quiz)
            }
            is DataResponse.Error -> {
                if (quizQuestionListResponse.appError == AppError.NO_DATA) {
                    showSnackBar(
                        InfoSnackBarModel(
                            textResId = R.string.quiz_no_questions_description
                        )
                    )
                    navigate(
                        bottomNavigationGraph = MainBottomNavigationGraph.QUIZ_PACKS,
                        destinationId = R.id.questionPackEdit
                    )
                } else {
                    showSnackBar(quizQuestionListResponse)
                }
            }
        }
        _isLoading.value = false
    }

}