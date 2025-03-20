package com.example.summary.sample.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import com.example.summary.R
import com.example.summary.databinding.ActivityUserBinding
import com.example.summary.sample.vo.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    private val userViewModel: UserViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            add.setOnClickListener {
                userViewModel.addUser(User(name=userEdit.text.toString()))
            }
        }

        userViewModel.users.observe(this
        ) { value ->
            val show:StringBuilder=StringBuilder()
            value.forEach {
                show.append("$it \n")
            }
            binding.result.text=show
        }

    }
}