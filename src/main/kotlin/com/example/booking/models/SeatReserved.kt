package com.example.booking.models

import com.example.booking.enums.TicketType
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "SEAT_RESERVED")
data class SeatReserved(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SEAT_ID")
    val seat: Seat,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCREENING_ID")
    val screening: Screening,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RESERVATION_ID")
    val reservation: Reservation,

    @Column
    @Enumerated(EnumType.STRING)
    val ticketType: TicketType

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as SeatReserved

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}

