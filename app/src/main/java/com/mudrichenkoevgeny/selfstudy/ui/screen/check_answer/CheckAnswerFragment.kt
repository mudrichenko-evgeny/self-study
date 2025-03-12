package com.mudrichenkoevgeny.selfstudy.ui.screen.check_answer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.FragmentCheckAnswerBinding
import com.mudrichenkoevgeny.selfstudy.extensions.applyInsets
import com.mudrichenkoevgeny.selfstudy.ui.custom_view.audio_recorder.AudioRecorderView
import com.mudrichenkoevgeny.selfstudy.ui.screen.base_audio_recorder.BaseAudioRecorderFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckAnswerFragment : BaseAudioRecorderFragment<FragmentCheckAnswerBinding, CheckAnswerViewModel>() {

    override val layoutResId = R.layout.fragment_check_answer

    override val viewModel: CheckAnswerViewModel by viewModels()

    override val isBottomMenuVisible = false

    private val args: CheckAnswerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setQuizQuestionId(args.quizQuestionId)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainLayout.applyInsets(top = true, bottom = true)
    }

    override fun getAudioRecorderView(): AudioRecorderView {
        return binding.audioRecorderView
    }

}