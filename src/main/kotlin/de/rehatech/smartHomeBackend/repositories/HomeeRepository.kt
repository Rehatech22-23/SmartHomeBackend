package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.HomeeDevice
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HomeeRepository : CrudRepository<HomeeDevice, Long> {

    fun findHomeeByHomeeID (homeeID: Int): HomeeDevice
}