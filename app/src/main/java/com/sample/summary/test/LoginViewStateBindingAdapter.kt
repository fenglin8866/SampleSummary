package com.sample.summary.test

import android.view.View
import com.sample.summary.databinding.ActivityLoginBinding

object LoginViewStateBindingAdapter {

    fun bind(binding: ActivityLoginBinding, state: LoginUIState) {
        // 1. 控制加载中
        binding.loadingBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        // 2. 控制按钮状态
        binding.loginButton.isEnabled = !state.isLoading

        // 3. 错误信息
        if (!state.errorMessage.isNullOrEmpty()) {
            binding.errorTextView.text = state.errorMessage
            binding.errorTextView.visibility = View.VISIBLE
        } else {
            binding.errorTextView.visibility = View.GONE
        }

        // 4. 登录成功提示
        if (state.isLoginSuccess) {
            binding.statusTextView.text = "登录成功！"
        }
    }
}
