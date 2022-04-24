package com.example.booking.controllers

import com.example.booking.business.Constants
import com.example.booking.dtos.forms.MakeSeatsReservationDTO
import com.example.booking.dtos.responses.*
import com.example.booking.enums.TicketType
import com.example.booking.models.Reservation
import com.example.booking.models.Screening
import com.example.booking.models.Seat
import com.example.booking.models.SeatReserved
import com.example.booking.services.ReservationService
import com.example.booking.services.ScreeningService
import com.example.booking.services.SeatService
import com.example.booking.utils.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.EntityNotFoundException
import kotlin.collections.HashMap

@RestController
@RequestMapping("api/reservation")
class TicketReservationController(
    private val screeningService: ScreeningService,
    private val seatService: SeatService,
    private val reservationService: ReservationService
) {

    /**
     * If no errors, endpoint returns movies with their screening times. Screenings are sorted by title and screening time.
     * Screening times have also their corresponding ids so that potential frontend can reference to specific screening
     * in later requests.
     */
    @GetMapping("movie-screenings")
    fun getMoviesAndTheirScreeningsByDate(
        @RequestParam(
            name = "date",
            required = true
        ) date: String
    ): ResponseEntity<Any> {
        return try {
            val screeningDate: Date = SimpleDateFormat("yyyy-MM-dd-HH-mm").parse(date)
            val screeningTimestamp: Long = screeningDate.time / 1000L
            if (isTimestampCorrect(screeningTimestamp)) {
                val screenings: MutableList<Screening> =
                    screeningService.findAllByTimestampBetween(screeningTimestamp, screeningDate)

                val response: SortedMap<String, MovieScreeningsDTO> = sortedMapOf()
                screenings.forEach { screening ->
                    val movieTitle: String = screening.movie.title
                    val screeningDTO = ScreeningTimeDTO(
                        screening.id,
                        getHoursAndMinutesFromTimestampInSeconds(screening.startTimestamp)
                    )

                    if (response.containsKey(movieTitle)) {
                        response[movieTitle]!!.screenings.add(screeningDTO)
                    } else {
                        response[movieTitle] = MovieScreeningsDTO(
                            MovieInfoDTO(screening.movie.actors, screening.movie.description),
                            mutableListOf()
                        )
                        response[movieTitle]!!.screenings.add(screeningDTO)
                    }
                }
                ResponseEntity.ok(response)
            } else {
                ResponseEntity.status(400).body(ErrorMessage("Specified date does not have any screenings planned"))
            }
        } catch (e: ParseException) {
            ResponseEntity.status(400).body(ErrorMessage("Wrong datetime format"))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(ErrorMessage("An error occurred"))
        }
    }

    /**
     *  If no errors, returns screening room information and available seats for specified screening.
     */
    @GetMapping("seats")
    fun getScreeningRoomAndSeats(@RequestParam(name = "id", required = true) screeningId: Int): ResponseEntity<Any> {
        return try {
            val screening: Screening? = screeningService.findById(screeningId)
            if (screening == null) {
                ResponseEntity.status(400).body(ErrorMessage("No movie screening with such id"))
            } else {
                val roomSeats = screening.room.seats
                val seatsNotAvailable: MutableList<Seat> = screeningService.getSeatsNotAvailable(screening)

                val seatsAvailable: List<Seat> = roomSeats.filterNot { seat ->
                    seatsNotAvailable.contains(seat)
                }

                val response = ScreeningRoomAndAvailableSeatsDTO(
                    RoomDTO(
                        screening.room.id,
                        screening.room.name,
                        screening.room.seatsNumber
                    ),
                    seatsAvailable
                )

                ResponseEntity.ok(response)
            }
        } catch (e: Exception) {
            ResponseEntity.status(400).body(ErrorMessage("An error occurred"))
        }
    }

    /**
     *  If no errors, returns a price and reservation expiration time (timestamp 15 min before the screening).
     */
    @PostMapping("reserve-seats")
    fun reserveSeatsForSpecificScreening(@RequestBody(required = true) body: MakeSeatsReservationDTO): ResponseEntity<Any> {
        try {
            val nameAndSurnameValidationResponse: ResponseEntity<Any>? = validateNameAndSurname(body.name, body.surname)
            if (nameAndSurnameValidationResponse != null) {
                return nameAndSurnameValidationResponse
            }

            val name: String = body.name.capitalizeName()
            val surname: String = body.surname.capitalizeSurname()

            val screening: Screening? = screeningService.findById(body.screeningId)
            if (screening == null) {
                return ResponseEntity.status(400).body(ErrorMessage("Invalid screening id"))
            } else {
                if (!canReserveScreeningWithSuchStartTimestamp(screening.startTimestamp)) {
                    return ResponseEntity.status(400).body(ErrorMessage("Cannot reserve this movie screening now"))
                }

                val seatsToBeReserved: MutableList<Seat> = seatService.findAllByIdInAndRoom(body.seatsIds, screening.room)

                val ticketNumberValidationResponse: ResponseEntity<Any>? =
                    validateTicketNumber(body.tickets, seatsToBeReserved.size)
                if (ticketNumberValidationResponse != null) {
                    return ticketNumberValidationResponse
                }

                val allSeats: MutableList<Seat> = screening.room.seats
                val seatsNotAvailable: MutableList<Seat> = screeningService.getSeatsNotAvailable(screening)

                val seatsToBeReservedValidationResponse: ResponseEntity<Any>? =
                    validateSeatsToBeReserved(seatsToBeReserved, allSeats, seatsNotAvailable)
                if (seatsToBeReservedValidationResponse != null) {
                    return seatsToBeReservedValidationResponse
                }

                val reservationExpirationTimestamp: Long =
                    screening.startTimestamp - Constants.RESERVATION_EXPIRATION_BEFORE_SCREENING
                val reservationExpirationDateTime: String = getDateTimeFromTimestamp(reservationExpirationTimestamp)

                val reservationCost: BigDecimal = calculateReservationCost(body.tickets)

                makeReservation(screening, name, surname, reservationExpirationTimestamp, seatsToBeReserved, body.tickets)

                return ResponseEntity.ok(ReservationResponseDTO(reservationCost, reservationExpirationDateTime))
            }
        } catch (e: Exception) {
            return ResponseEntity.status(400).body(ErrorMessage("An error occurred"))
        }
    }

    private fun String.capitalizeName(): String = replaceFirstChar { char -> char.titlecase() }

    private fun String.capitalizeSurname(): String =
        split("-").joinToString("-") { str -> str.trim().replaceFirstChar { char -> char.titlecase() } }

    private fun validateNameAndSurname(name: String, surname: String): ResponseEntity<Any>? {
        if (name.length < 3) {
            return ResponseEntity.status(400).body(ErrorMessage("Name is too short"))
        }
        val regexName = Regex("\\p{IsAlphabetic}+")
        if (!regexName.matches(name)) {
            return ResponseEntity.status(400).body(ErrorMessage("Name is invalid"))
        }
        if (surname.length < 3) {
            return ResponseEntity.status(400).body(ErrorMessage("Surname is too short"))
        }
        val regexSurname = Regex("^\\p{IsAlphabetic}[\\p{IsAlphabetic}-]+")
        if (!regexSurname.matches(surname)) {
            return ResponseEntity.status(400).body(ErrorMessage("Surname is invalid"))
        }
        return null
    }

    private fun validateTicketNumber(
        tickets: HashMap<TicketType, Int>,
        seatsToBeReservedSize: Int
    ): ResponseEntity<Any>? {
        var ticketsNumber = 0
        tickets.forEach { (_, number: Int) ->
            ticketsNumber += number
        }
        if (ticketsNumber != seatsToBeReservedSize) {
            return ResponseEntity.status(400)
                .body(ErrorMessage("Number of seats to be reserved is different from number of tickets to be reserved"))
        }
        return null
    }

    private fun validateSeatsToBeReserved(
        seatsToBeReserved: MutableList<Seat>,
        allSeats: MutableList<Seat>,
        seatsNotAvailable: MutableList<Seat>
    ): ResponseEntity<Any>? {
        if (seatsToBeReserved.size < 1) {
            return ResponseEntity.status(400).body(ErrorMessage("List of seats to be reserved is empty"))
        }
        seatsToBeReserved.forEach { seat ->
            if (seatsNotAvailable.contains(seat)) {
                return ResponseEntity.status(400)
                    .body(ErrorMessage("One of selected seats is already reserved"))
            }
        }
        if (!validateSeatsLayout(seatsToBeReserved, allSeats, seatsNotAvailable)) {
            return ResponseEntity.status(400)
                .body(ErrorMessage("Cannot leave single seats unoccupied between two already reserved seats"))
        }
        return null
    }

    private fun makeReservation(
        screening: Screening,
        name: String,
        surname: String,
        reservationExpirationTimestamp: Long,
        seatsToBeReserved: MutableList<Seat>,
        tickets: HashMap<TicketType, Int>
    ) {
        val reservation = Reservation(
            screening = screening,
            name = name,
            surname = surname,
            expirationTimestamp = reservationExpirationTimestamp
        )

        val seatsReserved: MutableList<SeatReserved> = mutableListOf()
        seatsToBeReserved.forEach { seat ->
            val ticketType: TicketType
            if (tickets.containsKey(TicketType.Adult) && tickets[TicketType.Adult]!! > 0) {
                ticketType = TicketType.Adult
                tickets[TicketType.Adult] = tickets[TicketType.Adult]!! - 1
            } else if (tickets.containsKey(TicketType.Student) && tickets[TicketType.Student]!! > 0) {
                ticketType = TicketType.Student
                tickets[TicketType.Student] = tickets[TicketType.Student]!! - 1
            } else if (tickets.containsKey(TicketType.Child) && tickets[TicketType.Child]!! > 0) {
                ticketType = TicketType.Child
                tickets[TicketType.Child] = tickets[TicketType.Child]!! - 1
            } else {
                return@forEach
            }

            seatsReserved.add(
                SeatReserved(
                    seat = seat,
                    screening = screening,
                    reservation = reservation,
                    ticketType = ticketType
                )
            )
        }

        reservation.seatsReserved = seatsReserved
        reservationService.save(reservation)
    }

    private fun calculateReservationCost(tickets: HashMap<TicketType, Int>): BigDecimal {
        var price: BigDecimal = BigDecimal.ZERO
        tickets.forEach { (ticketType, ticketsNumber) ->
            price += when (ticketType) {
                TicketType.Adult -> {
                    Constants.ADULT_TICKET_PRICE * BigDecimal(ticketsNumber)
                }
                TicketType.Student -> {
                    Constants.STUDENT_TICKET_PRICE * BigDecimal(ticketsNumber)
                }
                else -> {
                    Constants.CHILD_TICKET_PRICE * BigDecimal(ticketsNumber)
                }
            }
        }
        return price
    }
}