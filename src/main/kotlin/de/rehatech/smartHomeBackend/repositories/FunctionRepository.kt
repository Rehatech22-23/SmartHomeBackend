package de.rehatech.smartHomeBackend.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import de.rehatech.smartHomeBackend.entities.FunctionValues

@Repository
interface FunctionRepository : CrudRepository<FunctionValues, Long> {
}