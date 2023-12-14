package com.example.buenaventura_login.data

object UserRepository {
    val users = listOf(
        User(1, "John", "Doe", "Pizza", "john.doe@example.com", "password123"),
        User(2, "Alice", "Smith", "Sushi", "alice.smith@example.com", "securepass456"),
        User(3, "Michael", "Johnson", "Tacos", "michael.johnson@example.com", "mysecretpw"),
        User(4, "Emily", "Davis", "Pasta", "emily.davis@example.com", "pastaLover789"),
        User(5, "Sarah", "Wilson", "Burgers", "sarah.wilson@example.com", "burger1234")
    )

    fun findUserByEmail(email: String): User? {
        return users.find { it.email == email }
    }
}
