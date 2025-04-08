package com.sample.summary.test
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.sample.summary.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private val viewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            viewModel.onLoginClicked()
        }

        observeState()

        observeEvents()
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                binding.loadingBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is UIEvent.Toast -> {
                        Toast.makeText(this@TestActivity, event.message, Toast.LENGTH_SHORT).show()
                    }
                    is UIEvent.Snackbar -> {
                        Snackbar.make(binding.root, event.message, Snackbar.LENGTH_SHORT).show()
                    }
                    is UIEvent.Navigate -> {
                        Toast.makeText(this@TestActivity, "跳转至：${event.route}", Toast.LENGTH_SHORT).show()
                        // startActivity(Intent(this, HomeActivity::class.java))
                    }
                    is UIEvent.Back -> finish()
                }
            }
        }
    }
}
