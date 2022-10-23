package com.mudrichenko.evgeniy.selfstudy.ui.screen.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenko.evgeniy.selfstudy.data.SingleLiveEvent
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizPack
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_repository.QuizRepository
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_use_case.QuizUseCase
import com.mudrichenko.evgeniy.selfstudy.ui.base.activity.BaseActivityViewModel
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
        Log.d("LIFE_LOGS", "appInitialization")
        viewModelScope.launch {
            quizRepository.syncData()
            quizPacksRepository.syncData()
            quizUseCase.checkPacks()
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