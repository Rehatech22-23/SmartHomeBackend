package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.model.Homee
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface HomeeRepository : CrudRepository<Homee, Long> {

}