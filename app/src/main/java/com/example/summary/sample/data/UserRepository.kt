package com.example.summary.sample.data

import com.example.summary.sample.data.db.UserDao
import com.example.summary.sample.vo.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    fun getUsers() = userDao.getAllUser()

    suspend fun addUser(user: User) {
        userDao.insertUser(user)
    }

}