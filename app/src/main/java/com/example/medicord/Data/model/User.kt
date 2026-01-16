package com.example.medicord.Data.model

data class User(
    val id: Long = 0,
    val username: String,
    val password: String,
    val role: String = "Admin"
)