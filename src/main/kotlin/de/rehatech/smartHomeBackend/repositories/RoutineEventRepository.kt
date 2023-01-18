package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.RoutineEvent
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoutineEventRepository : CrudRepository<RoutineEvent?, Long?>