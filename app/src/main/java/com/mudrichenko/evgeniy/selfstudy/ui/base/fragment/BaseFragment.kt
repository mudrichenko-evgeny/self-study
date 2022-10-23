package com.mudrichenko.evgeniy.selfstudy.ui.base.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.mudrichenko.evgeniy.selfstudy.BR
import com.mudrichenko.evgeniy.selfstudy.data.SingleLiveEvent
import com.mudrichenko.evgeniy.selfstudy.system.model.RequestPermissionData
import com.mudrichenko.evgeniy.selfstudy.extensions.isPermissionGranted
import com.mudrichenko.evgeniy.selfstudy.extensions.toRequestPermissionDataList
import com.mudrichenko.evgeniy.selfstudy.ui.bottom_sheet_dialog.question_statistics.QuestionStatisticsBottomSheetDialogDirections
import com.mudrichenko.evgeniy.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import com.mudrichenko.evgeniy.selfstudy.ui.screen.home.HomeFragmentDirections
import com.mudrichenko.evgeniy.selfstudy.ui.screen.main.MainActivity
import com.mudrichenko.evgeniy.selfstudy.ui.screen.main.model.MainActivityNavigationData
import com.mudrichenko.evgeniy.selfstudy.ui.screen.quiz.QuizFragmentDirections
import com.mudrichenko.evgeniy.selfstudy.ui.screen.quiz_completed.QuizCompletedFragmentDirections
import com.mudrichenko.evgeniy.selfstudy.ui.screen.quiz_packs.QuizPacksFragmentDirections

abstract class BaseFragment<B: ViewDataBinding, VM: BaseFragmentViewModel>: Fragment(), FragmentEventInterface {

    @get:LayoutRes
    abstract val layoutResId: Int

    protected lateinit var binding: B

    protected abstract val viewModel: VM

    open val isBottomMenuVisible: Boolean = true

    private var requestPermissionResult: RequestPermissionsResult? = null
    private val permissionsRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        requestPermissionResult?.onPermissionsResultReceived(
            permissions.toRequestPermissionDataList()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getViewBinding(inflater, container).let { viewBinding ->
            binding = viewBinding
            binding.setVariable(BR.viewModel, viewModel)
            binding.lifecycleOwner = viewLifecycleOwner
            return viewBinding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeBottomMenuVisibility(isBottomMenuVisible)
        viewModel.navigationEvent.observeSingleLiveEvent { navigationCommand ->
            handleNavigationCommand(navigationCommand)
        }
        viewModel.mainActivityNavigationEvent.observeSingleLiveEvent { mainActivityNavigationData ->
            handleMainActivityNavigationData(mainActivityNavigationData)
        }
        viewModel.showSnackBarEvent.observeSingleLiveEvent { infoSnackBarModel ->
            this.showInfoSnackBar(infoSnackBarModel)
        }
    }

    private fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): B {
        return DataBindingUtil.inflate(inflater, layoutResId, container, false)
    }

    override fun <T> LiveData<T>.observeData(f: (T) -> Unit) {
        this.observe(viewLifecycleOwner) { data ->
            f.invoke(data)
        }
    }

    override fun <T> LiveData<SingleLiveEvent<T>>.observeSingleLiveEvent(f: (T) -> Unit) {
        this.observe(viewLifecycleOwner) { data ->
            data.getContentIfNotHandled()?.let { f.invoke(it) }
        }
    }

    private fun handleNavigationCommand(navigationCommand: NavigationCommand) {
        when (navigationCommand) {
            is NavigationCommand.Back -> {
                findNavController().navigateUp()
            }
            is NavigationCommand.NavigateTo -> {
                handleNavigationDirectionCommand(navigationCommand.direction)
            }
        }
    }

    private fun handleNavigationDirectionCommand(
        directionCommand: NavigationCommand.NavigateTo.Direction
    ) {
        when (directionCommand) {
            is NavigationCommand.NavigateTo.Direction.Screen -> {
                openScreen(directionCommand)
            }
            is NavigationCommand.NavigateTo.Direction.Dialog -> {
                openDialog(directionCommand)
            }
        }
    }

    private fun openScreen(direction: NavigationCommand.NavigateTo.Direction.Screen) {
        when (direction) {
            is NavigationCommand.NavigateTo.Direction.Screen.Home -> {
                findNavController().navigate(QuizCompletedFragmentDirections.navigateToHome())
            }
            is NavigationCommand.NavigateTo.Direction.Screen.Quiz -> {
                findNavController().navigate(HomeFragmentDirections.navigateToQuiz())
            }
            is NavigationCommand.NavigateTo.Direction.Screen.QuizCompleted -> {
                findNavController().navigate(QuizFragmentDirections.navigateToQuizCompleted())
            }
            is NavigationCommand.NavigateTo.Direction.Screen.CheckAnswer -> {
                findNavController().navigate(QuizFragmentDirections.navigateToCheckAnswer(
                    direction.questionId
                ))
            }
            is NavigationCommand.NavigateTo.Direction.Screen.QuestionPackEdit -> {
                findNavController().navigate(QuizPacksFragmentDirections.navigateToQuestionPackEdit())
            }
            is NavigationCommand.NavigateTo.Direction.Screen.Statistics -> {
                findNavController().navigate(QuizPacksFragmentDirections.navigateToStatistics())
            }
        }
    }

    private fun openDialog(direction: NavigationCommand.NavigateTo.Direction.Dialog) {
        when (direction) {
            is NavigationCommand.NavigateTo.Direction.Dialog.QuestionStatistics -> {
                findNavController().navigate(
                    QuestionStatisticsBottomSheetDialogDirections
                        .showQuestionStatisticsDialog(direction.questionId)
                )
            }
            is NavigationCommand.NavigateTo.Direction.Dialog.EditQuestion -> {
                findNavController().navigate(
                    QuestionStatisticsBottomSheetDialogDirections
                        .showEditQuestionDialog(direction.quizPackId, direction.questionId)
                )
            }
        }
    }

    private fun handleMainActivityNavigationData(
        handleMainActivityNavigationData: MainActivityNavigationData
    ) {
        activity.let { fragmentActivity ->
            if (fragmentActivity is MainActivity) {
                fragmentActivity.handleMainActivityNavigationData(handleMainActivityNavigationData)
            }
        }
    }

    override fun showInfoSnackBar(infoSnackBarModel: InfoSnackBarModel) {
        activity?.let { fragmentActivity ->
            if (fragmentActivity is MainActivity)
                fragmentActivity.showInfoSnackBar(infoSnackBarModel)
        }
    }

    private fun changeBottomMenuVisibility(visible: Boolean) {
        activity?.let { fragmentActivity ->
            if (fragmentActivity is MainActivity)
                fragmentActivity.changeBottomMenuVisibility(visible)
        }
    }

    fun hasRecordAudioPermission(): Boolean {
        return hasPermissions(arrayOf(Manifest.permission.RECORD_AUDIO))
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ActivityCompat.checkSelfPermission(requireContext(), it) ==
                    PackageManager.PERMISSION_GRANTED
        }
    }

    fun requestRecordAudioPermission(requestPermissionResult: RequestPermissionResult) {
        requestPermissions(
            permissions = arrayOf(Manifest.permission.RECORD_AUDIO),
            requestPermissionResult = object : RequestPermissionsResult {
                override fun onPermissionsResultReceived(
                    requestPermissionDataList: List<RequestPermissionData>
                ) {
                    if (requestPermissionDataList.isPermissionGranted(
                            Manifest.permission.RECORD_AUDIO)
                    )
                        requestPermissionResult.onPermissionGranted()
                    else
                        requestPermissionResult.onPermissionNotGranted()
                }
            }
        )
    }

    private fun requestPermissions(
        permissions: Array<String>,
        requestPermissionResult: RequestPermissionsResult
    ) {
        this.requestPermissionResult = requestPermissionResult
        permissionsRequest.launch(permissions)
    }

}