package com.example.booking.dtos.forms

import com.example.booking.enums.TicketType

data class MakeSeatsReservationDTO(
    val name: String,
    val surname: String,
    val screeningId: Int,
    val seatsIds: MutableList<Int>,
    val tickets: HashMap<TicketType, Int>
)
