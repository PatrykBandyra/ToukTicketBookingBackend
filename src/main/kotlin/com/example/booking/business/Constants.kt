package com.example.booking.business

import java.math.BigDecimal

object Constants {
    const val MINIMUM_RESERVATION_TIME_BEFORE_SCREENING: Long = 15 * 60
    const val SCREENING_MAX_IN_PAST_IN_SECONDS: Long = 15 * 60
    const val SCREENING_MAX_IN_ADVANCE_IN_SECONDS: Long = 365 * 24 * 60 * 60  // 1 year

    // The reservation expiration timestamp is created by subtracting this value from screening start timestamp
    const val RESERVATION_EXPIRATION_BEFORE_SCREENING: Long = 15 * 60

    // Prices - PLN
    val ADULT_TICKET_PRICE: BigDecimal = BigDecimal("25")
    val STUDENT_TICKET_PRICE: BigDecimal = BigDecimal("18")
    val CHILD_TICKET_PRICE: BigDecimal = BigDecimal("12.50")
}