package com.example.booking.utils

import com.example.booking.business.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Throughout the app and database I use timestamps in seconds and convert them to milliseconds when necessary.
 */

/**
 * Checks if timestamp representing screening time is correct (according to business rules - described in README).
 */
fun isTimestampCorrect(timestamp: Long): Boolean {
    val currentTimestamp: Long = System.currentTimeMillis() / 1000L
    return currentTimestamp - Constants.SCREENING_MAX_IN_PAST_IN_SECONDS <= timestamp &&
            timestamp <= currentTimestamp + Constants.SCREENING_MAX_IN_ADVANCE_IN_SECONDS
}

/**
 *  Returns the timestamp of specified day's midnight in seconds
 */
fun getEndOfDayTimestampInSeconds(date: Date): Long {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = date
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis / 1000L
}

fun getHoursAndMinutesFromTimestampInSeconds(timestamp: Long): String {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = Date(timestamp * 1000L)
    return "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
}

/**
 *  Checks if screening with given timestamp can be reserved at this moment (according to business rules).
 */
fun canReserveScreeningWithSuchStartTimestamp(timestamp: Long): Boolean {
    val currentTimestamp: Long = System.currentTimeMillis() / 1000L
    return currentTimestamp - Constants.MINIMUM_RESERVATION_TIME_BEFORE_SCREENING <= timestamp
}

fun getDateTimeFromTimestamp(timestampInSeconds: Long): String {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        sdf.format(Date(timestampInSeconds * 1000L))
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}