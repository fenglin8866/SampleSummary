package com.sample.summary.test
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.sample.summary.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            viewModel.handleIntent(UIIntent.OnLoginClicked)
        }

        observeState()

        observeEvents()
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                LoginViewStateBindingAdapter.bind(binding, state)
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is UIEvent.Toast -> {
                        Toast.makeText(this@LoginActivity, event.message, Toast.LENGTH_SHORT).show()
                    }
                    is UIEvent.Snackbar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is UIEvent.Navigate -> {
                        Toast.makeText(this@LoginActivity, "跳转至：${event.route}", Toast.LENGTH_SHORT).show()
                        // startActivity(Intent(this, HomeActivity::class.java))
                    }
                    is UIEvent.Back -> finish()
                }
            }
        }
    }
}
