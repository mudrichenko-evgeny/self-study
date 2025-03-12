package com.mudrichenkoevgeny.selfstudy.ui.screen.quiz_completed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.FragmentQuizCompletedBinding
import com.mudrichenkoevgeny.selfstudy.extensions.applyInsets
import com.mudrichenkoevgeny.selfstudy.ui.base.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizCompletedFragment : BaseFragment<FragmentQuizCompletedBinding, QuizCompletedViewModel>() {

    override val layoutResId = R.layout.fragment_quiz_completed

    override val viewModel: QuizCompletedViewModel by viewModels()

    override val isBottomMenuVisible = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainLayout.applyInsets(top = true, bottom = true)
    }

}