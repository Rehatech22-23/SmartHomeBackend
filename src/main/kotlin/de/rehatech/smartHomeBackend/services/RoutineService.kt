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

/**
 * A Service for the business logic concerning Routine Entities*
 *
 * This class contains methods the business logic concerning Routine Entities
 *
 * @param  routineRepository Interface gets automatically autowired into the Service
 * @author Tim Lukas Bräuker
 */
@Service
class RoutineService(private val routineRepository: RoutineRepository) {

    /**
     * Finds all Routine Objects stored in the Database and filters out their Ids
     * @returns returns a ResponseEntity containing a List with Ids of all Routine Ids
     */
    fun getAllRoutineIds(): ResponseEntity<List<Long>> {
        val ids: ArrayList<Long> = ArrayList()
        routineRepository.findAll().iterator().forEachRemaining {
            ids.add(it.id!!)
        }
        return ResponseEntity(ids, null, HttpStatus.OK)
    }

    /**
     * Finds the Routine referenced by the parameter
     * @param routineId referencing the Routine Object stored in the DataBase
     * @return  a ResponseEntity containing a Json String representing a RoutineDTO
     */
    fun getRoutine(routineId: Long): ResponseEntity<String> {
        val optionalRoutine = routineRepository.findById(routineId)
        if(optionalRoutine.isPresent) {
            return ResponseEntity(Json.encodeToString(RoutineMapper.mapToDTO(optionalRoutine.get())),null, HttpStatus.OK)
        }
        return ResponseEntity("Es konnte keine Routine mit der angegebenen Id gefunden werden!", null, HttpStatus.NOT_FOUND)
    }

    /**
     * Triggers the Routine referenced by the routineId
     * @param routineId referencing the Routine from the Database that will be triggered
     */
    fun triggerRoutineById(routineId: Long): ResponseEntity<String> {
        val optionalRoutine = routineRepository.findById(routineId)
        if (optionalRoutine.isPresent){
            return ResponseEntity(Json.encodeToString(RoutineMapper.mapToDTO(optionalRoutine.get())), null, HttpStatus.OK)
            //Todo: Specify implementation -> routine.active = true (?)
            //Todo: Actually trigger a routine
        }
        return  ResponseEntity("Es konnte keine Routine mit der angegebenen Id gefunden werden!", null, HttpStatus.NOT_FOUND)
    }

    /**
     * Maps the RoutineDTO parameter to a Routine Entity and saves it to the DataBase
     * @param routineDTO that will be saved to the Database as a Routine Entity
     * @return a ResponseEntity that contains a Json String representing the Routine
     * Entity as DTO that was saved to the Database
     */
    @Transactional
    fun createRoutine(routineDTO: RoutineDTO): ResponseEntity<String> {
        //return ResponseEntity(Json.encodeToString((RoutineMapper.mapToDTO(routineRepository.save(RoutineMapper.mapToEntity(routineDTO))))), null, HttpStatus.OK)
        var routine = RoutineMapper.mapToEntity(routineDTO)
        val savedRoutine = routineRepository.save(routine)
        val savedRoutineDTO = RoutineMapper.mapToDTO(savedRoutine)
        val s = Json.encodeToString(savedRoutineDTO)
        return ResponseEntity(s, null, HttpStatus.OK)
    }

    /**
     * Deletes the Routine specified by the parameter
     * @param routineId referencing the Routine object in the Database that will be deleted
     * @return a ResponseEntity containing a String either constituting an OK on a successful
     * delete or an Internal Server Error on a failed deletion attempt
     */
    fun deleteRoutine(routineId: Long): ResponseEntity<String> {
        return try {
            routineRepository.deleteById(routineId)
            ResponseEntity.ok("Entity deleted")
        }catch (error: Error) {
            ResponseEntity.internalServerError().build()
        }

    }
}