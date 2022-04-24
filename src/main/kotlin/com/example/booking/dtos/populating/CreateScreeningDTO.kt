package com.example.booking.dtos.populating

data class CreateScreeningDTO(
    val movieID: Int,
    val roomID: Int,
    val startTime: Long
)
