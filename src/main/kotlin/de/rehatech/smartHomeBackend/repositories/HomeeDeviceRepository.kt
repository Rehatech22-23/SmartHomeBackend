package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.HomeeDevice
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * An Interface class extending a CrudRepository in order to handle CRUD operations on a repository for HomeeDevices
 *
 * @author Sebastian Kurth, Sofia Bonas
 */
@Repository
interface HomeeDeviceRepository : CrudRepository<HomeeDevice, Long> {

    fun findHomeeByHomeeID (homeeID: Int): HomeeDevice
}