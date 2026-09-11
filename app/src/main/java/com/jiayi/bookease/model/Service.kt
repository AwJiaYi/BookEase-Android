package com.jiayi.bookease.model

data class Service(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val duration: Long = 0,
    val price: Double = 0.0,
    val active: Boolean = true
)