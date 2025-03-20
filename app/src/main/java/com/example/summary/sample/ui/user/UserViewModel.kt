package com.example.summary.sample.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.summary.sample.vo.User
import com.example.summary.sample.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    val users: LiveData<List<User>> = repository.getUsers().asLiveData()


    fun addUser(user: User) {
        viewModelScope.launch {
            repository.addUser(user)
        }
    }

}