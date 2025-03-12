package com.mudrichenkoevgeny.selfstudy.ui.screen.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.Question
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.SortType
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenkoevgeny.selfstudy.extensions.getSortedList
import com.mudrichenkoevgeny.selfstudy.ui.adapter.statistics.StatisticsAdapterItemVM
import com.mudrichenkoevgeny.selfstudy.ui.adapter.statistics.StatisticsAdapterListener
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.BaseFragmentViewModel
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : BaseFragmentViewModel() {

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    val sortType: MutableLiveData<SortType> = MutableLiveData(SortType.ASCENDING)

    private var questionList: List<Question> = emptyList()
    private var _statisticsList = MutableLiveData<List<StatisticsAdapterItemVM>>()
    val statisticsList: LiveData<List<StatisticsAdapterItemVM>> = _statisticsList

    val statisticAdapterListener: StatisticsAdapterListener = object : StatisticsAdapterListener {
        override fun onItemClicked(statisticAdapterItemVM: StatisticsAdapterItemVM) {
            showStatisticDialog(statisticAdapterItemVM.question)
        }
    }

    fun onViewCreated() {
        collectQuestionList()
    }

    private fun collectQuestionList() {
        viewModelScope.launch {
            quizRepository.getQuestionListFlow().collect { questionList ->
                handleQuestionListDataResponse(questionList)
            }
        }
    }

    private fun handleQuestionListDataResponse(
        questionListResponse: DataResponse<List<Question>>
    ) {
        when (questionListResponse) {
            is DataResponse.Successful -> {
                questionPackReceived(questionListResponse.data)
            }
            is DataResponse.Error -> {
                showSnackBar(questionListResponse)
                _isLoading.value = false
            }
        }
    }

    private fun questionPackReceived(
        questionList: List<Question>
    ) {
        this.questionList = questionList.getSortedList(sortType.value)
        _statisticsList.value = this.questionList.map { question ->
            StatisticsAdapterItemVM(
                question = question
            )
        }
        _isLoading.value = false
    }

    private fun showStatisticDialog(question: Question) {
        navigateTo(NavigationCommand.NavigateTo.Direction.Dialog.QuestionStatistics(question.id))
    }

    fun onSortClicked() {
        sortType.value?.let { oldSortType ->
            sortType.value = oldSortType.reverse()
        }
        questionPackReceived(this.questionList)
    }

    fun clearStatisticsConfirmed() {
        viewModelScope.launch {
            quizRepository.clearStatistics()
        }
    }

}