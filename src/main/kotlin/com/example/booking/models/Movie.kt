package com.example.booking.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "MOVIE")
data class Movie(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnoreProperties("movie")
    val screenings: MutableList<Screening> = mutableListOf(),

    @Column(unique = true, nullable = false)
    val title: String,

    @Column
    val director: String?,

    @Column
    val actors: String?,

    @Column
    val duration: Int?,

    @Column
    val description: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Movie

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}
