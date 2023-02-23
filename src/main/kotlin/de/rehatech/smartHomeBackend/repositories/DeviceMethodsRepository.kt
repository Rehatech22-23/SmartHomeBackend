package de.rehatech.smartHomeBackend.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import de.rehatech.smartHomeBackend.entities.DeviceMethods

@Repository
interface DeviceMethodsRepository : CrudRepository<DeviceMethods, Long> {

}