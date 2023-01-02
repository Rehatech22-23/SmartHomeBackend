package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.Homee
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HomeeRepository : CrudRepository<Homee, Long> {

}