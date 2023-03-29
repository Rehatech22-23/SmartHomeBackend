package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.TriggerTime
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * An Interface class extending a CrudRepository in order to handle CRUD operations on a repository for TriggerTimes
 *
 * @author Sebastian Kurth
 */
@Repository
interface TriggerTimeRepository : CrudRepository<TriggerTime?, Long?>