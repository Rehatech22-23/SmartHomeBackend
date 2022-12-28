package de.rehatech.smartHomeBackend.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import de.rehatech.smartHomeBackend.model.Function

@Repository
interface FunctionRepository : CrudRepository<Function, Long> {
}