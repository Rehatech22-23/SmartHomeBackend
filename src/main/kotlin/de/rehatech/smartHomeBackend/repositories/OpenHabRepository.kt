package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.OpenHab
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OpenHabRepository : CrudRepository<OpenHab, Long> {

    fun findOpenHabByUid(uid: String):OpenHab


}