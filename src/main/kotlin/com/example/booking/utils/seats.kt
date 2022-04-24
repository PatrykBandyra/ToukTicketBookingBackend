package com.example.booking.utils

import com.example.booking.models.Seat

/**
 *  Assumption: all rooms have seat layouts in form of rectangle (each row has the same number of seats)
 */
fun validateSeatsLayout(
    seatsToBeReserved: MutableList<Seat>,
    allSeats: MutableList<Seat>,
    seatsNotAvailable: MutableList<Seat>
): Boolean {
    var isOkay = true

    val maxRow: Int = allSeats.maxOf { seat ->
        seat.row
    }
    val maxNumber: Int = allSeats.maxOf { seat ->
        seat.number
    }

    // mark already reserved seats
    val seatArray = array2DofBoolean(maxRow + 1, maxNumber + 1)
    seatsNotAvailable.forEach { seat ->
        seatArray[seat.row][seat.number] = true
    }
    // make all reservations
    seatsToBeReserved.forEach { seat ->
        seatArray[seat.row][seat.number] = true
    }
    // validate layout
    seatsToBeReserved.forEach { seat ->
        try {  // left side
            if ((!seatArray[seat.row][seat.number - 1] && seatArray[seat.row][seat.number - 2])) {
                isOkay = false
                return@forEach
            }
        } catch (_: ArrayIndexOutOfBoundsException) {
        }
        try {  // right side
            if (!seatArray[seat.row][seat.number + 1] && seatArray[seat.row][seat.number + 2]) {
                isOkay = false
                return@forEach
            }
        } catch (_: ArrayIndexOutOfBoundsException) {
        }
    }
    return isOkay
}

fun array2DofBoolean(sizeOuter: Int, sizeInner: Int): Array<BooleanArray> =
    Array(sizeOuter) { BooleanArray(sizeInner) { false } }