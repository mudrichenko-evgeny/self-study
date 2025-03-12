package com.mudrichenkoevgeny.selfstudy.ui.custom_view.circle_button_stroke

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorOrThrow
import com.mudrichenkoevgeny.selfstudy.R
import com.mudrichenkoevgeny.selfstudy.databinding.ViewCircleButtonStrokeBinding

class CircleButtonStroke @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private lateinit var binding: ViewCircleButtonStrokeBinding

    private var strokeColor: Int = getDefaultStrokeColor()

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        binding = ViewCircleButtonStrokeBinding.inflate(
            LayoutInflater.from(context), this, true
        )
        context.obtainStyledAttributes(attrs, R.styleable.CircleButtonStroke).apply {
            strokeColor = getColor(
                R.styleable.CircleButtonStroke_strokeBackgroundColorRes,
                getDefaultStrokeColor()
            )
            getDrawable(R.styleable.CircleButtonStroke_imageDrawableRes)?.let { imageDrawable ->
                binding.circleButton.setImageDrawable(imageDrawable)
            }
            try {
                binding.circleButton.setColors(
                    enabledBackgroundColor = getColorOrThrow(
                        R.styleable.CircleButtonStroke_enabledBackgroundColorRes
                    ),
                    disabledBackgroundColor = getColorOrThrow(
                        R.styleable.CircleButtonStroke_disabledBackgroundColorRes
                    ),
                    enabledImageColor = getColorOrThrow(
                        R.styleable.CircleButtonStroke_enabledImageColorRes
                    ),
                    disabledImageColor = getColorOrThrow(
                        R.styleable.CircleButtonStroke_disabledImageColorRes
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            binding.mainLayout.background.setTint(strokeColor)
            recycle()
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.circleButton.setOnClickListener {
            l?.onClick(this)
        }
    }

    private fun getDefaultStrokeColor(): Int {
        return ContextCompat.getColor(context, R.color.background_primary)
    }

}