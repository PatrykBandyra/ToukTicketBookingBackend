package com.example.booking.services

import com.example.booking.models.Movie
import com.example.booking.models.Screening
import com.example.booking.repositories.MovieRepository
import com.example.booking.utils.getEndOfDayTimestampInSeconds
import org.springframework.stereotype.Service
import java.util.*

@Service
class MovieService(private val movieRepository: MovieRepository) {

    fun save(movie: Movie): Movie = movieRepository.save(movie)
    fun getById(id: Int): Movie = movieRepository.getById(id)
}