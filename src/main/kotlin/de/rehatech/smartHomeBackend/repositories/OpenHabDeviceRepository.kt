package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * An Interface class extending a CrudRepository in order to handle CRUD operations on a repository for OpenHabDevices
 */
@Repository
interface OpenHabDeviceRepository : CrudRepository<OpenHabDevice, Long> {

    fun findOpenHabByUid(uid: String):OpenHabDevice


}