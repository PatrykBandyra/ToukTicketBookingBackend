package com.example.booking.dtos.responses

import java.math.BigDecimal

data class ReservationResponseDTO(
    val reservationCost: BigDecimal,
    val reservationExpirationDateTime: String
)
