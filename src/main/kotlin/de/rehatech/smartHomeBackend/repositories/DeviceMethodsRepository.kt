package de.rehatech.smartHomeBackend.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import de.rehatech.smartHomeBackend.entities.DeviceMethods

/**
 * An Interface class extending a CrudRepository in order to handle CRUD operations on a repository for DeviceMethods
 *
 * @author Sebastian Kurth
 */
@Repository
interface DeviceMethodsRepository : CrudRepository<DeviceMethods, Long> {

}