package com.mudrichenkoevgeny.selfstudy.ui.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenkoevgeny.selfstudy.data.SingleLiveEvent
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import com.mudrichenkoevgeny.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenkoevgeny.selfstudy.domain.quiz_use_case.QuizUseCase
import com.mudrichenkoevgeny.selfstudy.ui.base.activity.BaseActivityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val quizPacksRepository: QuizPacksRepository,
    private val quizUseCase: QuizUseCase
) : BaseActivityViewModel() {

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _appInitializationFinishedEvent = MutableLiveData<Unit>()
    val appInitializationFinishedEvent: LiveData<Unit> = _appInitializationFinishedEvent

    private var _resetQuizEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val resetQuizEvent: LiveData<SingleLiveEvent<Unit>> = _resetQuizEvent

    var resetHomeGraph: Boolean = false

    init {
        appInitialization()
    }

    private fun appInitialization() {
        viewModelScope.launch {
            quizUseCase.checkPacks()
            quizRepository.syncData()
            quizPacksRepository.syncData()
            onAppInitializationComplete()
        }
    }

    suspend fun observeToResetQuizFlow() {
        quizUseCase.resetQuizEventFlow().collect { resetQuizEvent ->
            resetQuizEvent?.let {
                _resetQuizEvent.postValue(SingleLiveEvent(Unit))
            }
        }
    }

    fun setActiveQuiz(
        quizPack: QuizPack,
        resultListener: QuizModificationListener? = null
    ) {
        viewModelScope.launch {
            quizUseCase.setActiveQuiz(quizPack).let { response ->
                resultListener?.onComplete(
                    when (response) {
                        is DataResponse.Successful -> {
                            DataResponse.Successful(Unit)
                        }
                        is DataResponse.Error -> {
                            DataResponse.Error(response.appError)
                        }
                    }
                )
            }
        }
    }

    fun deleteQuiz(
        quizPack: QuizPack,
        resultListener: QuizModificationListener? = null
    ) {
        viewModelScope.launch {
            quizUseCase.deleteQuiz(quizPack).let { response ->
                resultListener?.onComplete(
                    when (response) {
                        is DataResponse.Successful -> {
                            DataResponse.Successful(Unit)
                        }
                        is DataResponse.Error -> {
                            DataResponse.Error(response.appError)
                        }
                    }
                )
            }
        }
    }

    fun editQuiz(
        quizPack: QuizPack,
        resultListener: QuizModificationListener? = null
    ) {
        viewModelScope.launch {
            quizUseCase.editQuiz(quizPack).let { response ->
                resultListener?.onComplete(
                    when (response) {
                        is DataResponse.Successful -> {
                            DataResponse.Successful(Unit)
                        }
                        is DataResponse.Error -> {
                            DataResponse.Error(response.appError)
                        }
                    }
                )
            }
        }
    }

    private fun onAppInitializationComplete() {
        _appInitializationFinishedEvent.value = Unit
        _isLoading.value = false
    }

}