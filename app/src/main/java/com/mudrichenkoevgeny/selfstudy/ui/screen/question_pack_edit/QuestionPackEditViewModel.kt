package com.mudrichenkoevgeny.selfstudy.ui.screen.question_pack_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.Question
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import com.mudrichenkoevgeny.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenkoevgeny.selfstudy.ui.adapter.question.QuestionAdapterItemVM
import com.mudrichenkoevgeny.selfstudy.ui.adapter.question.QuestionAdapterListener
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.BaseFragmentViewModel
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.NavigationCommand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionPackEditViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val quizPacksRepository: QuizPacksRepository
) : BaseFragmentViewModel() {

    private var _quizPack = MutableLiveData<QuizPack?>()
    val quizPack: LiveData<QuizPack?> = _quizPack

    private var _questionList = MutableLiveData<List<QuestionAdapterItemVM>>()
    val questionList: LiveData<List<QuestionAdapterItemVM>> = _questionList

    val questionAdapterListener: QuestionAdapterListener = object : QuestionAdapterListener {

        override fun onEditClicked(questionAdapterItemVM: QuestionAdapterItemVM) {
            questionAdapterItemVM.questionLiveData.value?.let { question ->
                showEditQuestionDialog(question)
            }
        }

        override fun onExpand(questionAdapterItemVM: QuestionAdapterItemVM) {
            onExpandQuestionItemClicked(questionAdapterItemVM)
        }

    }

    init {
        collectActiveQuizPack()
        collectQuizQuestionList()
    }

    private fun collectActiveQuizPack() {
        viewModelScope.launch {
            quizPacksRepository.getActiveQuizPackFlow().collect { quizPack ->
                _quizPack.value = quizPack
            }
        }
    }

    private fun collectQuizQuestionList() {
        viewModelScope.launch {
            quizRepository.getQuestionListFlow().collect { questionList ->
                handleQuestionListDataResponse(questionList)
            }
        }
    }

    private fun handleQuestionListDataResponse(questionListResponse: DataResponse<List<Question>>) {
        when (questionListResponse) {
            is DataResponse.Successful -> {
                questionPackReceived(questionListResponse.data)
            }
            is DataResponse.Error -> {
                showSnackBar(questionListResponse)
            }
        }
    }

    private fun questionPackReceived(questionList: List<Question>) {
        _questionList.value = questionList.map { question ->
            QuestionAdapterItemVM(
                questionLiveData = MutableLiveData(question)
            )
        }
    }

    fun onAddQuestionButtonClicked() {
        showAddQuestionDialog()
    }

    private fun showEditQuestionDialog(question: Question) {
        navigateTo(
            NavigationCommand.NavigateTo.Direction.Dialog.EditQuestion(
                quizPackId = _quizPack.value?.id ?: -1,
                questionId = question.id
            )
        )
    }

    private fun showAddQuestionDialog() {
        navigateTo(
            NavigationCommand.NavigateTo.Direction.Dialog.EditQuestion(
                quizPackId = _quizPack.value?.id ?: -1,
                questionId = -1
            )
        )
    }

    fun onPackNameChanged(packName: String?) {
        packName?.let { name ->
            viewModelScope.launch {
                quizPacksRepository.renameActiveQuizPack(name).let { renamePackResponse ->
                    if (renamePackResponse is DataResponse.Error) {
                        showSnackBar(renamePackResponse)
                    }
                }
            }
        }
    }

    private fun onExpandQuestionItemClicked(questionAdapterItemVM: QuestionAdapterItemVM) {
        _questionList.value?.find { questionItem ->
            questionItem.getId() == questionAdapterItemVM.getId()
        }?.let { questionItem ->
            questionItem.expanded.value.let { expanded ->
                questionItem.expanded.value = !(expanded ?: false)
            }
        }
    }

}