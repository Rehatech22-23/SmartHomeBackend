package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
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

    fun getRoutine(routineId: Long): Routine? {
        val optionalRoutine = routineRepository.findById(routineId)
        return optionalRoutine.get()
    }

    fun triggerRoutineById(routineId: Long?): Routine? {
        //Todo: Specify implementation
        val routine = routineId?.let { getRoutine(it) }
        if (routine != null) {
            return routine
        }else{
            return null
        }
    }

    fun createRoutine(routine: Routine): Routine {
            return routineRepository.save(routine)
    }

    fun deleteRoutine(routineId: Long): ResponseEntity<String> {
        routineRepository.deleteById(routineId)
        return ResponseEntity.ok("Entity deleted")
    }
}