package com.example.booking.services

import com.example.booking.models.Reservation
import com.example.booking.repositories.ReservationRepository
import org.springframework.stereotype.Service

@Service
class ReservationService(private val reservationRepository: ReservationRepository) {

    fun save(reservation: Reservation): Reservation = reservationRepository.save(reservation)
}