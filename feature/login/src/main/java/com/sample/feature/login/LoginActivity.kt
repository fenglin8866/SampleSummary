package com.sample.feature.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.sample.core.basic.ui.event.UIEvent
import com.sample.feature.login.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeState()

        observeEvents()

        userIntent()

    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    LoginViewBinder().bind(binding, state)
                }
            }
        }
    }

    private fun observeEvents() {
        // Create a new coroutine in the lifecycleScope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // This happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                viewModel.eventFlow.collect {
                    // Process item
                }
            }
        }

        lifecycleScope.launch {
            viewModel.eventFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { event ->
                    when (event) {
                        is UIEvent.Toast -> {
                            Toast.makeText(this@LoginActivity, event.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is UIEvent.Snackbar -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                        }

                        is UIEvent.Navigate -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "跳转至：${event.route}",
                                Toast.LENGTH_SHORT
                            ).show()
                            //startActivity(Intent(this@LoginActivity, UpgradeActivity::class.java))
                        }

                        UIEvent.Back -> finish()

                        else -> {

                        }
                    }
                }

        }
    }

    private fun userIntent() {
        binding.loginButton.setOnClickListener {
            viewModel.handleIntent(LoginUIIntent.OnLoginClicked)
        }

        binding.upgradeButton.setOnClickListener {
            viewModel.handleIntent(LoginUIIntent.OnNavigateToHome)
        }
    }
}
