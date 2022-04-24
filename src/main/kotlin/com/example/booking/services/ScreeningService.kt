package com.example.booking.services

import com.example.booking.models.Screening
import com.example.booking.models.Seat
import com.example.booking.repositories.ScreeningRepository
import com.example.booking.utils.getEndOfDayTimestampInSeconds
import org.springframework.stereotype.Service
import java.util.*

@Service
class ScreeningService(private val screeningRepository: ScreeningRepository) {

    fun save(screening: Screening): Screening = screeningRepository.save(screening)

    fun findAllByTimestampBetween(screeningTimestamp: Long, screeningDate: Date): MutableList<Screening> {
        val endTimestamp: Long = getEndOfDayTimestampInSeconds(screeningDate)
        return screeningRepository.findAllByStartTimestampBetweenOrderByMovieTitleDescStartTimestampAsc(
            screeningTimestamp, endTimestamp
        )
    }

    fun findById(id: Int): Screening? {
        val screeningOptional = screeningRepository.findById(id)
        return if (screeningOptional.isPresent) screeningOptional.get() else null
    }

    fun getSeatsNotAvailable(screening: Screening): MutableList<Seat> {
        val seatsReserved = screening.seatsReserved
        val seatsNotAvailable: MutableList<Seat> = mutableListOf()
        seatsReserved.forEach { seatReserved ->
            seatsNotAvailable.add(seatReserved.seat)
        }
        return seatsNotAvailable
    }
}