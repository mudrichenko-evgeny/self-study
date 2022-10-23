package com.mudrichenko.evgeniy.selfstudy.ui.screen.quiz_packs

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mudrichenko.evgeniy.selfstudy.data.SingleLiveEvent
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.AppError
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.DataResponse
import com.mudrichenko.evgeniy.selfstudy.data.model.`object`.QuizPack
import com.mudrichenko.evgeniy.selfstudy.domain.file_repository.FileRepository
import com.mudrichenko.evgeniy.selfstudy.domain.quiz_packs_repository.QuizPacksRepository
import com.mudrichenko.evgeniy.selfstudy.ui.adapter.quiz_pack.QuizPackAdapterItemVM
import com.mudrichenko.evgeniy.selfstudy.ui.adapter.quiz_pack.QuizPackAdapterListener
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.BaseFragmentViewModel
import com.mudrichenko.evgeniy.selfstudy.ui.base.fragment.NavigationCommand
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class QuizPacksViewModel @Inject constructor(
    private val fileRepository: FileRepository,
    private val quizPacksRepository: QuizPacksRepository
) : BaseFragmentViewModel() {

    private var _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _quizPacksList = MutableLiveData<List<QuizPackAdapterItemVM>>()
    val quizPacksList: LiveData<List<QuizPackAdapterItemVM>> = _quizPacksList

    private var _pickFileEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val pickFileEvent: LiveData<SingleLiveEvent<Unit>> = _pickFileEvent

    private var _showPackCreateConfirmationEvent = MutableLiveData<SingleLiveEvent<String>>()
    val showPackCreateConfirmationEvent: LiveData<SingleLiveEvent<String>> = _showPackCreateConfirmationEvent

    private var _setActiveQuizEvent = MutableLiveData<SingleLiveEvent<QuizPack>>()
    val setActiveQuizEvent: LiveData<SingleLiveEvent<QuizPack>> = _setActiveQuizEvent

    private var _deleteQuizEvent = MutableLiveData<SingleLiveEvent<QuizPack>>()
    val deleteQuizEvent: LiveData<SingleLiveEvent<QuizPack>> = _deleteQuizEvent

    private var _deleteQuizConfirmedEvent = MutableLiveData<SingleLiveEvent<QuizPack>>()
    val deleteQuizConfirmedEvent: LiveData<SingleLiveEvent<QuizPack>> = _deleteQuizConfirmedEvent

    private var pickedFile: File? = null

    val quizPackAdapterListener: QuizPackAdapterListener = object: QuizPackAdapterListener {

        override fun onEditClicked(quizPackAdapterItemVM: QuizPackAdapterItemVM) {
            navigateTo(NavigationCommand.NavigateTo.Direction.Screen.QuestionPackEdit)
        }

        override fun onStatisticsClicked(quizPackAdapterItemVM: QuizPackAdapterItemVM) {
            navigateTo(NavigationCommand.NavigateTo.Direction.Screen.Statistics)
        }

        override fun onDeleteClicked(quizPack: QuizPack) {
            onDeleteQuizPackClicked(quizPack)
        }

        override fun onSetActiveClicked(quizPack: QuizPack) {
            onSwitchQuizPackClicked(quizPack)
        }

        override fun onHelpExpand() {
            onExpandHelpItemClicked()
        }

    }

    init {
        loadData()
    }

    private fun loadData() {
        _isLoading.value = true
        viewModelScope.launch {
            collectQuizPacksList()
        }
    }

    private fun collectQuizPacksList() {
        viewModelScope.launch {
            quizPacksRepository.getQuizPacksListFlow().collect { quizPacksList ->
                handleQuizPacksListDataResponse(quizPacksList)
            }
        }
    }

    private fun handleQuizPacksListDataResponse(quizPacksListResponse: DataResponse<List<QuizPack>>) {
        when (quizPacksListResponse) {
            is DataResponse.Successful -> {
                questionListReceived(quizPacksListResponse.data)
            }
            is DataResponse.Error -> {
                showSnackBar(quizPacksListResponse)
            }
        }
    }

    private fun questionListReceived(quizPacks: List<QuizPack>) {
        val quizPacksAdapterItems: MutableList<QuizPackAdapterItemVM> = quizPacks
            .sortedByDescending { it.isActive }
            .map { quizPack ->
                if (quizPack.isActive)
                    QuizPackAdapterItemVM.QuizPackItemActive(quizPack)
                else
                    QuizPackAdapterItemVM.QuizPackItem(quizPack)
            }
            .toMutableList()
        quizPacksAdapterItems.add(QuizPackAdapterItemVM.Help())
        _quizPacksList.value = quizPacksAdapterItems
        _isLoading.value = false
    }

    private fun onDeleteQuizPackClicked(quizPack: QuizPack) {
        _deleteQuizEvent.value = SingleLiveEvent(quizPack)
    }

    private fun onSwitchQuizPackClicked(quizPack: QuizPack) {
        _setActiveQuizEvent.value = SingleLiveEvent(quizPack)
    }

    private fun onExpandHelpItemClicked() {
        _quizPacksList.value
            ?.filterIsInstance<QuizPackAdapterItemVM.Help>()
            ?.lastOrNull()?.let { adapterItemHelp ->
                adapterItemHelp.expanded.value.let { expanded ->
                    adapterItemHelp.expanded.value = !(expanded ?: false)
                }
            }
    }

    fun onPickFileButtonClicked() {
        _pickFileEvent.value = SingleLiveEvent(Unit)
    }

    fun onFilePicked(uri: Uri?) {
        uri?.let { fileUri ->
            viewModelScope.launch {
                onPackFileCreated(fileRepository.convertContentUriToFile(fileUri))
            }
        }
    }

    private fun onPackFileCreated(file: File?) {
        this.pickedFile = file
        Log.d("FILE_LOGS", "file.path = ${file?.path}")
        file?.let { _showPackCreateConfirmationEvent.value = SingleLiveEvent(file.path) }
    }

    fun onPackCreationCancelled(filePath: String?) {
        deletePackTempFile(filePath)
    }

    private fun deletePackTempFile(filePath: String?) {
        pickedFile?.let { file ->
            if (filePath == file.path) {
                viewModelScope.launch {
                    fileRepository.deleteFile(
                        directory = fileRepository.getTempDir().path,
                        fileName = file.name
                    )
                }
            }
        }
    }

    fun onPackCreationApplied(filePath: String?, packName: String?) {
        pickedFile.let { file ->
            if (file == null || filePath != file.path) {
                showSnackBar(
                    InfoSnackBarModel(
                        textResId = AppError.QUESTION_PACK_CREATE.getStringResource()
                    )
                )
                onPackCreationCancelled(filePath)
                return
            }
            createQuizPack(
                file = file,
                packName = packName ?: file.name
            )
        }
    }

    private fun createQuizPack(file: File, packName: String) {
        viewModelScope.launch {
            fileRepository.copyFile(
                filePath = file.path,
                outDirectory = fileRepository.getUserPacksDirectory(),
                outFileName = file.name
            )
            deletePackTempFile(file.path)
            quizPacksRepository.saveQuizPack(
                packName = packName,
                packFileName = file.name
            )
        }
    }

    fun deletePackRequest(quizPackId: Long) {
        quizPacksList.value
            ?.filterIsInstance<QuizPackAdapterItemVM.QuizPackItem>()
            ?.find { quizPackAdapterItemVM ->
                quizPackAdapterItemVM.getId() == quizPackId
            }
            ?.quizPack?.let { quizPack ->
                _deleteQuizConfirmedEvent.value = SingleLiveEvent(quizPack)
            }
    }

}
