package com.example.booking.repositories

import com.example.booking.models.Room
import com.example.booking.models.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatRepository : JpaRepository<Seat, Int> {

    fun findAllByIdInAndRoom(ids: MutableList<Int>, room: Room): MutableList<Seat>
}