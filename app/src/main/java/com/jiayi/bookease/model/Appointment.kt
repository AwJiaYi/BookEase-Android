package com.jiayi.bookease.model

data class Appointment(
    val id: String = "",
    val userId: String = "",
    val serviceId: String = "",
    val serviceName: String = "",
    val date: String = "",
    val time: String = "",
    val price: Double = 0.0,
    val status: String = "confirmed",
    val createdAt: Long = 0L
)