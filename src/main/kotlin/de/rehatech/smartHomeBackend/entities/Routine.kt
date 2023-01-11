package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.context.annotation.Bean

@Entity
class Routine(
    @field:GeneratedValue(strategy = GenerationType.AUTO)
    @field:Id val id: Long,
    var name: String
) {
}