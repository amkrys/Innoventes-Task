package com.innoventus.task.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.innoventus.task.base.BaseActivity
import com.innoventus.task.databinding.ActivityMainBinding
import com.innoventus.task.presentation.form.FormActivity
import com.innoventus.task.util.observeEvent
import com.innoventus.task.util.openActivity
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBindingVariables()
        listenToObservers()
    }

    private fun initBindingVariables() = with(binding) {
        lifecycleOwner = this@MainActivity
        viewModel = mainViewModel
    }

    private fun listenToObservers() = lifecycleScope.launch {
        mainViewModel.buttonClickEvent.observeEvent(this@MainActivity) {
            openActivity<FormActivity>()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}