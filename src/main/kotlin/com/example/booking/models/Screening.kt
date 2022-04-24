package com.example.booking.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "SCREENING")
data class Screening(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MOVIE_ID")
    @JsonIgnoreProperties("screenings")
    val movie: Movie,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROOM_ID")
    @JsonIgnoreProperties("seats", "screenings")
    val room: Room,

    @OneToMany(mappedBy = "screening", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    val reservations: MutableList<Reservation> = mutableListOf(),

    @OneToMany(mappedBy = "screening", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnoreProperties("screening", "reservation", "ticketType")
    val seatsReserved: MutableList<SeatReserved> = mutableListOf(),

    @Column
    val startTimestamp: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Screening

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
