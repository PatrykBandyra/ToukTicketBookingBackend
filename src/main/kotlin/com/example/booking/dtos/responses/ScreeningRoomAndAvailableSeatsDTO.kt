package com.example.booking.dtos.responses

import com.example.booking.models.Seat

data class ScreeningRoomAndAvailableSeatsDTO(
    val room: RoomDTO,
    val seatsAvailable: List<Seat>
)
