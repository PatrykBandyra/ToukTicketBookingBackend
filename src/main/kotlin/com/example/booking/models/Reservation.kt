package com.example.booking.models

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "RESERVATION")
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCREENING_ID")
    val screening: Screening,

    @OneToMany(mappedBy = "reservation", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var seatsReserved: MutableList<SeatReserved> = mutableListOf(),

    @Column
    val name: String,

    @Column
    val surname: String,

    @Column
    val expirationTimestamp: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Reservation

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
