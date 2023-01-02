package de.rehatech.smartHomeBackend.repositories
import de.rehatech.smartHomeBackend.entities.Routine
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoutineRepository : CrudRepository<Routine, Long> {

}