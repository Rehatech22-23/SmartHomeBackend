package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.mapper.RoutineMapper
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import de.rehatech2223.datamodel.RoutineDTO
import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 * A Service for the business logic concerning Routine Entities*
 *
 * This class contains methods the business logic concerning Routine Entities
 *
 * @param  routineRepository Interface gets automatically autowired into the Service
 */
@Service
class RoutineService(
        val routineRepository: RoutineRepository,
        val functionService: FunctionService,
) {

    private val log: Logger = LoggerFactory.getLogger(RoutineService::class.java)

    /**
     * Finds all Routine Objects stored in the Database and filters out their Ids
     * @returns returns a ResponseEntity containing a List with Ids of all Routine Ids
     */
    fun getAllRoutineIds(): ResponseEntity<List<Long>> {
        val ids: ArrayList<Long> = ArrayList()
        routineRepository.findAll().iterator().forEachRemaining {
            ids.add(it.id!!)
        }
        log.info("All Routines are retrieved from DB")
        return ResponseEntity(ids, null, HttpStatus.OK)
    }

    /**
     * Finds the Routine referenced by the parameter
     * @param routineId referencing the Routine Object stored in the DataBase
     * @return  a ResponseEntity containing a Json String representing a RoutineDTO
     */
    fun getRoutine(routineId: Long): ResponseEntity<String> {
        val optionalRoutine = routineRepository.findById(routineId)
        if (optionalRoutine.isPresent) {
            val routine = optionalRoutine.get()
            log.info("Routine with id: ${routine.id} was retrieved successfully")
            return ResponseEntity(Json.encodeToString(RoutineMapper.mapToDTO(routine)), null, HttpStatus.OK)
        }
        log.error("Unable to find Routine with id: $routineId")
        return ResponseEntity("Es konnte keine Routine mit der angegebenen Id gefunden werden!", null, HttpStatus.NOT_FOUND)
    }

    /**
     * Triggers the Routine referenced by the routineId
     * @param routineId referencing the Routine from the Database that will be triggered
     */
    fun triggerRoutineById(routineId: Long): ResponseEntity<String> {
        val optionalRoutine = routineRepository.findById(routineId)
        if (optionalRoutine.isPresent) {
            val routine = optionalRoutine.get()
            val routineEvents = routine.routineEvent
            for (routineEvent in routineEvents) {
                functionService.triggerFunc(
                        routineEvent.deviceId,
                        routineEvent.functionId!!,
                        routineEvent.voldemort!!
                )
            }
            log.info("Routine with id ${routine.id} was triggered successfully")
            return ResponseEntity("Routine mit der angegebenen Id wurde getriggert!", null, HttpStatus.OK)
        } else {
            log.error("Unable to find Routine with id: $routineId")
            return ResponseEntity("Es konnte keine Routine mit der angegebenen Id gefunden werden!", null, HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Maps the RoutineDTO parameter to a Routine Entity and saves it to the DataBase
     * @param routineDTO that will be saved to the Database as a Routine Entity
     * @return a ResponseEntity that contains a Json String representing the Routine
     * Entity as DTO that was saved to the Database
     */
    @Transactional
    fun createRoutine(routineDTO: RoutineDTO): ResponseEntity<String> {
        val routine = RoutineMapper.mapToEntity(routineDTO)
        val savedRoutine = routineRepository.save(routine)
        log.info("Routine: ${savedRoutine.routineName} with id: ${savedRoutine.id} was created successfully")

        val savedRoutineDTO = RoutineMapper.mapToDTO(savedRoutine)
        val s = Json.encodeToString(savedRoutineDTO)
        return ResponseEntity(s, null, HttpStatus.OK)
    }

    /**
     * Deletes the Routine specified by the parameter
     * @param id referencing the Routine object in the Database that will be deleted
     * @return a ResponseEntity containing a String either constituting an OK on a successful
     * delete or an Internal Server Error on a failed deletion attempt
     */
    fun deleteRoutine(id: Long): ResponseEntity<String> {
        return try {
            routineRepository.deleteById(id)
            log.info("Routine with id $id was deleted successfully")
            ResponseEntity.ok("Entity deleted successfully")
        } catch (error: Error) {
            log.error("Unable to delete Routine with id $id")
            ResponseEntity.internalServerError().build()
        }

    }
}