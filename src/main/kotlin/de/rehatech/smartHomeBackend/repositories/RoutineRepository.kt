package de.rehatech.smartHomeBackend.repositories
import de.rehatech.smartHomeBackend.entities.Routine
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * An Interface class extending a CrudRepository in order to handle CRUD operations on a repository for Routines
 *
 * @author  Tim Bräuker, Sebastian Kurth
 */
@Repository
interface RoutineRepository : CrudRepository<Routine, Long> {

}