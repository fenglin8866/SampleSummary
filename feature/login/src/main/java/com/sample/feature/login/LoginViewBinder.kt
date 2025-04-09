package com.sample.feature.login

import android.view.View
import com.sample.core.basic.ui.ViewStateBinder
import com.sample.feature.login.databinding.ActivityLoginBinding

class LoginViewBinder : ViewStateBinder<ActivityLoginBinding, LoginUIState> {

    override fun bind(binding: ActivityLoginBinding, state: LoginUIState) {
        binding.apply {
            // 1. 控制加载中
            loadingBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            // 2. 控制按钮状态
            loginButton.isEnabled = !state.isLoading

            // 3. 错误信息
            if (!state.errorMessage.isNullOrEmpty()) {
                errorTextView.text = state.errorMessage
                errorTextView.visibility = View.VISIBLE
            } else {
                errorTextView.visibility = View.GONE
            }

            // 4. 登录成功提示
            if (state.isLoginSuccess) {
                statusTextView.text = "登录成功！"
            }
        }

    }
}
