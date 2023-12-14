package com.example.buenaventura_login.data

data class Credentials(
    var login: String = "",
    var pwd: String = "",
    var isVisible: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return login.isNotEmpty() && pwd.isNotEmpty()
    }
}