package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Routine(
    @Id
    @GeneratedValue
    var id: Long? = null,
    var name: String,
)
