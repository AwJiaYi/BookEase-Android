package com.jiayi.bookease.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "customer"
)