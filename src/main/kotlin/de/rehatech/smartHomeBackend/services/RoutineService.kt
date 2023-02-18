package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech.smartHomeBackend.mapper.RoutineMapper
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RoutineService(private val routineRepository: RoutineRepository) {

    val allDeviceIds: ResponseEntity<List<Long>>
        get() {
            val result: MutableList<Long> = ArrayList()
            val it: Iterator<Routine> = routineRepository.findAll().iterator()
            it.forEachRemaining { r: Routine ->
                r.id?.let { it1 ->
                    result.add(
                        it1.toLong()
                    )
                }
            }
            return ResponseEntity.ok(result)
        }

    /**
     * //TODO: Docs
     * @param routineId
     * @return Routine?
     */
    fun getRoutine(routineId: Long): ResponseEntity<String> {
        val optionalRoutine = routineRepository.findById(routineId)
        if(optionalRoutine.isPresent) {
            return ResponseEntity(Json.encodeToString(RoutineMapper.mapToDTO(optionalRoutine.get())),null, HttpStatus.OK)
        }
        return ResponseEntity("Es konnte keine Routine mit der angegebenen Id gefunden werden!", null, HttpStatus.NOT_FOUND)
    }

    /**
     * //TODO: Docs
     * @param routineId
     * @return Routine?
     */

    fun triggerRoutineById(routineId: Long): ResponseEntity<String> {
        val optionalRoutine = routineRepository.findById(routineId)
        if (optionalRoutine.isPresent){
            return ResponseEntity(Json.encodeToString(RoutineMapper.mapToDTO(optionalRoutine.get())), null, HttpStatus.OK)
            //Todo: Specify implementation
            //Todo: Actually trigger a routine
        }
        return  ResponseEntity("Es konnte keine Routine mit der angegebenen Id gefunden werden!", null, HttpStatus.NOT_FOUND)
    }

    /**
     * //TODO: Docs
     * @param routine
     * @return Routine
     */
    fun createRoutine(routine: Routine): ResponseEntity<String> {
        return ResponseEntity(Json.encodeToString((RoutineMapper.mapToDTO(routineRepository.save(routine)))), null, HttpStatus.OK)
    }

    /**
     * //TODO: Docs
     * @param routineId
     * @return ResponseEntity<String>
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