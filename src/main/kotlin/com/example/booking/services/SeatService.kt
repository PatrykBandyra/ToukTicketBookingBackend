package com.example.booking.services

import com.example.booking.models.Room
import com.example.booking.models.Seat
import com.example.booking.repositories.SeatRepository
import org.springframework.stereotype.Service

@Service
class SeatService(private val seatRepository: SeatRepository) {

    fun save(seat: Seat): Seat = seatRepository.save(seat)
    fun findAllByIdInAndRoom(ids: MutableList<Int>, room: Room) = seatRepository.findAllByIdInAndRoom(ids, room)
}