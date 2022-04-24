package com.example.booking.services

import com.example.booking.models.Room
import com.example.booking.repositories.RoomRepository
import org.springframework.stereotype.Service

@Service
class RoomService(private val roomRepository: RoomRepository) {

    fun save(room: Room): Room = roomRepository.save(room)
    fun getById(id: Int): Room = roomRepository.getById(id)
}