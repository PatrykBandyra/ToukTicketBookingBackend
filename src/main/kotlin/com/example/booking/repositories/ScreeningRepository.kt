package com.example.booking.repositories

import com.example.booking.models.Screening
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScreeningRepository : JpaRepository<Screening, Int> {

    fun findAllByStartTimestampBetweenOrderByMovieTitleDescStartTimestampAsc(startTimestamp: Long, endTimestamp: Long): MutableList<Screening>
}