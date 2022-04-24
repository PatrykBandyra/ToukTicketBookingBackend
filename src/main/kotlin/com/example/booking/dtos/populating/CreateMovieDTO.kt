package com.example.booking.dtos.populating

data class CreateMovieDTO(
    val title: String,
    val director: String?,
    val actors: String?,
    val duration: Int?,
    val description: String?
)
