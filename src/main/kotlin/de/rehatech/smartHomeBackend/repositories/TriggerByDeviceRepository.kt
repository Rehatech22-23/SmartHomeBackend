package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.TriggerByDevice
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TriggerByDeviceRepository :
    CrudRepository<TriggerByDevice?, Long?>