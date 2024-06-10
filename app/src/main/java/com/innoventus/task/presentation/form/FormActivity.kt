package com.innoventus.task.presentation.form

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.innoventus.task.R
import com.innoventus.task.base.BaseActivity
import com.innoventus.task.data.uimodel.FormModel
import com.innoventus.task.databinding.ActivityFormBinding
import com.innoventus.task.domain.interactor.ClickEvent
import com.innoventus.task.util.observeEvent
import com.innoventus.task.util.toast
import kotlinx.coroutines.launch

class FormActivity : BaseActivity() {

    private var _binding: ActivityFormBinding? = null
    private val binding get() = _binding!!

    private val formViewModel: FormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBindingVariables()
        setupSpannable()
        listenToObservers()
    }

    private fun initBindingVariables() = with(binding) {
        lifecycleOwner = this@FormActivity
        viewModel = formViewModel
        item = FormModel()
    }

    private fun setupSpannable() = with(binding) {
        val spannableString =
            SpannableString(getString(R.string.tv_info))
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    widget.cancelPendingInputEvents()
                    toast(getString(R.string.learn_more))
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ContextCompat.getColor(this@FormActivity, R.color.primary)
                    ds.isUnderlineText = false
                    ds.isFakeBoldText = true
                }
            }, getString(R.string.tv_info).length - 10, getString(R.string.tv_info).length,
            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
        tvInfo.text = spannableString
        tvInfo.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun listenToObservers() = lifecycleScope.launch {
        formViewModel.buttonClickEvent.observeEvent(this@FormActivity) {
            when (it) {
                ClickEvent.BUTTON -> {
                    toast(getString(R.string.success_msg))
                }
                ClickEvent.TEXT -> {
                    finishAffinity()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}