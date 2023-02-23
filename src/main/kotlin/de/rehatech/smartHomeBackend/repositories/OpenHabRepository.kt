package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OpenHabRepository : CrudRepository<OpenHabDevice, Long> {

    fun findOpenHabByUid(uid: String):OpenHabDevice


}