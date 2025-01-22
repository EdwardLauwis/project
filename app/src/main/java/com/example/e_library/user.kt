package com.example.e_library

data class User(
    val username: String = "",
    val password: String = "",
    val phoneNumber: String = "",
    val booksRead: Int = 0
)