package de.rehatech.smartHomeBackend.dataDB.model

import jakarta.persistence.*


@Entity
@Table(name ="user")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)