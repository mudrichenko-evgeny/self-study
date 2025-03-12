package com.mudrichenkoevgeny.selfstudy.ui.custom_view.linear_progress_bar

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.ViewLinearProgressBarBinding
import com.mudrichenkoevgeny.selfstudy.extensions.setVisibleOrGone
import com.mudrichenkoevgeny.selfstudy.extensions.setVisibleOrInvisible

class LinearProgressBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var binding: ViewLinearProgressBarBinding

    private var accentColor: Int = getDefaultAccentColor()
    private var primaryColor: Int = getDefaultPrimaryBackgroundColor()
    private var textVisible: Boolean = true

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        binding = ViewLinearProgressBarBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        context.obtainStyledAttributes(attrs, R.styleable.LinearProgressBarView).apply {
            accentColor = getColor(
                R.styleable.LinearProgressBarView_progressColor,
                getDefaultAccentColor()
            )
            primaryColor = getColor(
                R.styleable.LinearProgressBarView_backgroundColor,
                getDefaultPrimaryBackgroundColor()
            )
            textVisible = getBoolean(
                R.styleable.LinearProgressBarView_textVisible,
                true
            )
            binding.textView.apply {
                setVisibleOrInvisible(textVisible)
                backgroundTintList = ColorStateList.valueOf(primaryColor)
                setTextColor(accentColor)
            }
            binding.progressBar.progressDrawable = getLinearProgressBarDrawable(
                context,
                primaryColor,
                accentColor
            )
            recycle()
        }
    }

    fun setProgress(progress: Int, max: Int) {
        binding.progressBar.progress = 100 * progress / (if (max <= 0) 1 else max)
        binding.textView.text = resources.getString(
            R.string.progress_data,
            progress,
            max,
        )
    }

    private fun getDefaultAccentColor(): Int {
        return ContextCompat.getColor(context, R.color.background_secondary)
    }

    private fun getDefaultPrimaryBackgroundColor(): Int {
        return ContextCompat.getColor(context, R.color.background_accent)
    }

}