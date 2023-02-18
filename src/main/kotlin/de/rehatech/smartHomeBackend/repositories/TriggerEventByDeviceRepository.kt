package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.TriggerEventByDevice
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TriggerEventByDeviceRepository :
    CrudRepository<TriggerEventByDevice?, Long?>