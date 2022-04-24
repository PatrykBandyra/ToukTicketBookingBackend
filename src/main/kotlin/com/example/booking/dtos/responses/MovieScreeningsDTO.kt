package com.example.booking.dtos.responses

data class MovieScreeningsDTO(
    val info: MovieInfoDTO,
    val screenings: MutableList<ScreeningTimeDTO>
)
