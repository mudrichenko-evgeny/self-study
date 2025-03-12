package com.mudrichenkoevgeny.selfstudy.ui.custom_view.progress

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.ViewProgressBinding

class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var binding: ViewProgressBinding

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        binding = ViewProgressBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        binding.mainLayout.setOnClickListener { }
        context.obtainStyledAttributes(attrs, R.styleable.ProgressView).apply {
            binding.mainLayout.setBackgroundColor(
                getColor(
                    R.styleable.ProgressView_backgroundColor,
                    R.color.background_primary
                )
            )
            binding.progressBar.indeterminateTintList = ColorStateList.valueOf(
                getColor(
                    R.styleable.ProgressView_progressColor,
                    R.color.on_background_primary
                )
            )
            recycle()
        }
    }

}