package com.example.booking.controllers

import com.example.booking.dtos.populating.CreateMovieDTO
import com.example.booking.dtos.populating.CreateRoomDTO
import com.example.booking.dtos.populating.CreateScreeningDTO
import com.example.booking.dtos.populating.CreateSeatDTO
import com.example.booking.dtos.responses.Message
import com.example.booking.models.Movie
import com.example.booking.models.Room
import com.example.booking.models.Screening
import com.example.booking.models.Seat
import com.example.booking.services.MovieService
import com.example.booking.services.RoomService
import com.example.booking.services.ScreeningService
import com.example.booking.services.SeatService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Data Population Controller is only used after application start-up. It allows to populate DB from REST endpoints.
 * In real life this controller should be protected from unauthenticated access.
 *
 * I wanted to have all ids auto-generated and be able to populate DB with 100% certainty that ids will be consistent
 * even if DB has some data inside before running data population script. That's why those endpoints return ids of newly
 * created objects so that a data population script can handle logic on its side with assigning ids to foreign keys.
 */
@RestController
@RequestMapping("api/populate")
class DataPopulationController(
    private val movieService: MovieService,
    private val roomService: RoomService,
    private val seatService: SeatService,
    private val screeningService: ScreeningService
) {

    @PostMapping("movie")
    fun createMovie(@RequestBody body: CreateMovieDTO): ResponseEntity<Message> {
        return try {
            val movie = Movie(
                title = body.title,
                director = body.director,
                actors = body.actors,
                duration = body.duration,
                description = body.description
            )
            val savedMovie = movieService.save(movie)
            ResponseEntity.ok(Message("Movie created", savedMovie.id))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(Message("Something went wrong"))
        }
    }

    @PostMapping("room")
    fun createRoom(@RequestBody body: CreateRoomDTO): ResponseEntity<Message> {
        return try {
            val room = Room(
                name = body.name,
                seatsNumber = body.seatsNumber,
            )
            val savedRoom = roomService.save(room)
            ResponseEntity.ok(Message("Room created", savedRoom.id))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(Message("Something went wrong"))
        }
    }

    @PostMapping("seat")
    fun createSeat(@RequestBody body: CreateSeatDTO): ResponseEntity<Message> {
        return try {
            val seat = Seat(
                room = roomService.getById(body.roomID),
                row = body.row,
                number = body.number
            )
            val savedSeat = seatService.save(seat)
            ResponseEntity.ok(Message("Seat created", savedSeat.id))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(Message("Something went wrong"))
        }
    }

    @PostMapping("screening")
    fun createScreening(@RequestBody body: CreateScreeningDTO): ResponseEntity<Message> {
        return try {
            val screening = Screening(
                movie = movieService.getById(body.movieID),
                room = roomService.getById(body.roomID),
                startTimestamp = body.startTime
            )
            val savedScreening = screeningService.save(screening)
            ResponseEntity.ok(Message("Screening created", savedScreening.id))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(Message("Something went wrong"))
        }
    }
}