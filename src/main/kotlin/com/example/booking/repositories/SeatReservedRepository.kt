package com.example.booking.repositories

import com.example.booking.models.SeatReserved
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeatReservedRepository : JpaRepository<SeatReserved, Int>