package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.Range
import org.springframework.data.repository.CrudRepository

interface RangeRepository: CrudRepository<Range?, Long> {
}