package com.example.booking.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "SEAT")
data class Seat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROOM_ID")
    @JsonIgnore
    val room: Room,

    @OneToMany(mappedBy = "seat", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    val seatsReserved: MutableList<SeatReserved> = mutableListOf(),

    @Column
    val row: Int,

    @Column
    val number: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Seat

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
