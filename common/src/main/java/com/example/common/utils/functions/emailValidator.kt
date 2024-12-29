package com.example.common.utils.functions

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$".toRegex()
    return email.matches(emailRegex)
}