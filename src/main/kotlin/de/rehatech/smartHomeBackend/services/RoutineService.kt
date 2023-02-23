package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech.smartHomeBackend.mapper.RoutineMapper
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import de.rehatech2223.datamodel.RoutineDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RoutineService(private val routineRepository: RoutineRepository) {

    fun getAllRoutineIds(): ResponseEntity<List<Long>> {
        val ids: ArrayList<Long> = ArrayList()
        routineRepository.findAll().iterator().forEachRemaining {
            ids.add(it.id!!)
        }
        return ResponseEntity(ids, null, HttpStatus.OK)
    }

    fun getRoutine(routineId: Long): ResponseEntity<String> {
        val optionalRoutine = routineRepository.findById(routineId)
        if(optionalRoutine.isPresent) {
            return ResponseEntity(Json.encodeToString(RoutineMapper.mapToDTO(optionalRoutine.get())),null, HttpStatus.OK)
        }
        return ResponseEntity("Es konnte keine Routine mit der angegebenen Id gefunden werden!", null, HttpStatus.NOT_FOUND)
    }

    fun triggerRoutineById(routineId: Long): ResponseEntity<String> {
        val optionalRoutine = routineRepository.findById(routineId)
        if (optionalRoutine.isPresent){
            return ResponseEntity(Json.encodeToString(RoutineMapper.mapToDTO(optionalRoutine.get())), null, HttpStatus.OK)
            //Todo: Specify implementation
            //Todo: Actually trigger a routine
        }
        return  ResponseEntity("Es konnte keine Routine mit der angegebenen Id gefunden werden!", null, HttpStatus.NOT_FOUND)
    }

    fun createRoutine(routineDTO: RoutineDTO): ResponseEntity<String> {
        return ResponseEntity(Json.encodeToString((RoutineMapper.mapToDTO(routineRepository.save(RoutineMapper.mapToEntity(routineDTO))))), null, HttpStatus.OK)
    }

    fun deleteRoutine(routineId: Long): ResponseEntity<String> {
        return try {
            routineRepository.deleteById(routineId)
            ResponseEntity.ok("Entity deleted")
        }catch (error: Error) {
            ResponseEntity.internalServerError().build()
        }

    }
}