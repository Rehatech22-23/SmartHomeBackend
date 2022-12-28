package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.model.OpenHab
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OpenHabRepository : CrudRepository<OpenHab, Long> {


}