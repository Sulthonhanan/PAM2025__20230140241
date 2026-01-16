package com.example.medicord.Data.repository

import com.example.medicord.Data.database.dao.UserDao
import com.example.medicord.Data.database.entity.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun login(username: String, password: String): UserEntity? =
        userDao.login(username, password)

    suspend fun getUserByUsername(username: String): UserEntity? =
        userDao.getUserByUsername(username)

    suspend fun insertUser(user: UserEntity): Long = userDao.insertUser(user)

    suspend fun getUserCount(): Int = userDao.getUserCount()
}