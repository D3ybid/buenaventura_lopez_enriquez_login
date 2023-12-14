package com.example.buenaventura_login.data

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val favoriteFood: String,
    val email: String,
    val password: String
)
