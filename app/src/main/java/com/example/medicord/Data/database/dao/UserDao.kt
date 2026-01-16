package com.example.medicord.Data.database.dao

import androidx.room.*
import com.example.medicord.Data.database.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserEntity?

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT COUNT(*) FROM user")
    suspend fun getUserCount(): Int
}