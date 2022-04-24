package com.example.booking.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "ROOM")
data class Room(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @OneToMany(mappedBy = "room", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnoreProperties("room")
    val seats: MutableList<Seat> = mutableListOf(),

    @OneToMany(mappedBy = "room", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val screenings: MutableList<Screening> = mutableListOf(),

    @Column
    val name: String,

    @Column
    val seatsNumber: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Room

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
