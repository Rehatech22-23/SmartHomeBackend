package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.TriggerTime
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TriggerTimeRepository : CrudRepository<TriggerTime?, Long?>