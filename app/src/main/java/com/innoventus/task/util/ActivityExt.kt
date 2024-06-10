package com.innoventus.task.util

import android.content.Intent
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.innoventus.task.BuildConfig
import com.innoventus.task.base.BaseActivity

fun AppCompatActivity.onBackPress(onBackPressed: () -> Unit = { onBackPressedDispatcher.onBackPressed() }) {
    onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed.invoke()
            }
        }
    )
}

fun AppCompatActivity.loge(message: String?) {
    if (BuildConfig.DEBUG) message?.let { Log.e(this::class.java.simpleName, it) }
}

inline fun <reified T : BaseActivity> AppCompatActivity.openActivity(block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}