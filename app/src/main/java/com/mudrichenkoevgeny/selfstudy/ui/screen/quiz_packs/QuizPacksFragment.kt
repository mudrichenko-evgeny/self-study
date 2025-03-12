package com.mudrichenkoevgeny.selfstudy.ui.screen.quiz_packs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.DataResponse
import com.mudrichenkoevgeny.selfstudy.data.model.`object`.QuizPack
import com.mudrichenkoevgeny.selfstudy.databinding.FragmentQuizPacksBinding
import com.mudrichenkoevgeny.selfstudy.extensions.applyInsets
import com.mudrichenkoevgeny.selfstudy.extensions.getHeightWithMargin
import com.mudrichenkoevgeny.selfstudy.system.Intents
import com.mudrichenkoevgeny.selfstudy.system.model.MimeFileType
import com.mudrichenkoevgeny.selfstudy.ui.adapter.quiz_pack.QuizPackAdapter
import com.mudrichenkoevgeny.selfstudy.ui.alert_dialog.alert.AlertDialog
import com.mudrichenkoevgeny.selfstudy.ui.alert_dialog.alert.AlertDialogDirections
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.BaseFragment
import com.mudrichenkoevgeny.selfstudy.ui.bottom_sheet_dialog.text_input.TextInputBottomSheetDialog
import com.mudrichenkoevgeny.selfstudy.ui.bottom_sheet_dialog.text_input.TextInputBottomSheetDialogDirections
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.info_snackbar.InfoSnackBarModel
import com.mudrichenkoevgeny.selfstudy.ui.screen.main.MainActivityViewModel
import com.mudrichenkoevgeny.selfstudy.ui.screen.main.QuizModificationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizPacksFragment : BaseFragment<FragmentQuizPacksBinding, QuizPacksViewModel>() {

    override val layoutResId = R.layout.fragment_quiz_packs

    override val viewModel: QuizPacksViewModel by viewModels()

    private val mainViewModel: MainActivityViewModel by activityViewModels()

    private val quizPackAdapter by lazy { QuizPackAdapter() }

    private val pickFileActivityResultLauncher = registerForActivityResult(
        PickFileContract(
            Intents.pick(MimeFileType.ALL)
        )
    ) { intent ->
        viewModel.onFilePicked(intent?.data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainLayout.applyInsets(top = true)
        binding.quizPacksRecyclerView.apply {
            quizPackAdapter.marginBottom = binding.createPackButton.getHeightWithMargin()
            quizPackAdapter.listener = viewModel.quizPackAdapterListener
            this.adapter = quizPackAdapter
        }
        viewModel.pickFileEvent.observeSingleLiveEvent {
            onPickFile()
        }
        viewModel.showPackCreateConfirmationEvent.observeSingleLiveEvent { filePath ->
            showPackCreationNameInputDialog(filePath)
        }
        viewModel.setActiveQuizEvent.observeSingleLiveEvent { quizPack ->
            mainViewModel.setActiveQuiz(
                quizPack = quizPack,
                resultListener = object : QuizModificationListener {
                    override fun onComplete(response: DataResponse<Unit>) {
                        when(response) {
                            is DataResponse.Successful -> {
                                // nothing
                            }
                            is DataResponse.Error -> {
                                showInfoSnackBar(InfoSnackBarModel(textResId = response.appError.getStringResource()))
                            }
                        }
                    }
                }
            )
        }
        viewModel.deleteQuizEvent.observeSingleLiveEvent { quizPack ->
            showPackDeleteConfirmationDialog(quizPack)
        }
        viewModel.deleteQuizConfirmedEvent.observeSingleLiveEvent { quizPack ->
            deletePackConfirmed(quizPack)
        }
    }

    private fun onPickFile() {
        Intents.pick(MimeFileType.ALL).let { pickIntent ->
            if (pickIntent.resolveActivity(requireActivity().packageManager) != null) {
                pickFileActivityResultLauncher.launch(pickIntent)
            } else {
                showInfoSnackBar(
                    InfoSnackBarModel(
                        textResId = R.string.answer
                    )
                )
            }
        }
    }

    private fun showPackCreationNameInputDialog(filePath: String) {
        findNavController().navigate(
            TextInputBottomSheetDialogDirections.showTextInputDialog(
                titleText = getString(R.string.enter_pack_name),
                textContent = getString(R.string.pack_name),
                contentCanBeEmpty = false,
                hintText = getString(R.string.pack_name),
                confirmButtonText = getString(R.string.set_name),
                extraData = filePath
            )
        )
    }

    private fun showPackDeleteConfirmationDialog(quizPack: QuizPack) {
        findNavController().navigate(
            AlertDialogDirections.showConfirmationDialog(
                titleText = getString(R.string.delete_quiz_title),
                descriptionText = getString(R.string.delete_quiz_description, quizPack.name),
                cancelButtonText = getString(R.string.cancel),
                confirmButtonText = getString(R.string.delete),
                extraData = quizPack.id.toString()
            )
        )
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(TextInputBottomSheetDialog.REQUEST_KEY) { _, bundle ->
            bundle.getBoolean(TextInputBottomSheetDialog.RESULT_KEY_CONFIRMED).let { textInputConfirmed ->
                if (textInputConfirmed) {
                    viewModel.onPackCreationApplied(
                        filePath = bundle.getString(TextInputBottomSheetDialog.RESULT_KEY_EXTRA_DATA),
                        packName = bundle.getString(TextInputBottomSheetDialog.RESULT_KEY_TEXT)
                    )
                } else {
                    viewModel.onPackCreationCancelled(
                        filePath = bundle.getString(TextInputBottomSheetDialog.RESULT_KEY_EXTRA_DATA)
                    )
                }
            }
        }
        setFragmentResultListener(AlertDialog.REQUEST_KEY) { _, bundle ->
            bundle.getBoolean(AlertDialog.RESULT_KEY).let { deletePackConfirmed ->
                if (deletePackConfirmed)  {
                    bundle.getString(TextInputBottomSheetDialog.RESULT_KEY_EXTRA_DATA)
                        ?.toLongOrNull()
                        ?.let { quizPackId ->
                            viewModel.deletePackRequest(quizPackId)
                        }
                }
            }
        }
    }

    private fun deletePackConfirmed(quizPack: QuizPack) {
        mainViewModel.deleteQuiz(
            quizPack = quizPack,
            resultListener = object : QuizModificationListener {
                override fun onComplete(response: DataResponse<Unit>) {
                    when(response) {
                        is DataResponse.Successful -> {
                            // nothing
                        }
                        is DataResponse.Error -> {
                            showInfoSnackBar(InfoSnackBarModel(textResId = response.appError.getStringResource()))
                        }
                    }
                }
            }
        )
    }

    class PickFileContract(private val intent: Intent): ActivityResultContract<Intent, Intent?>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
            return intent
        }
    }

}
