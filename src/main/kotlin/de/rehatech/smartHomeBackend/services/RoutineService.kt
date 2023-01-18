package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RoutineService(private val routineRepository: RoutineRepository, private val routine: Routine) {

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

    fun getRoutine(routineId: Long): ResponseEntity<Routine> {
        val optionalRoutine = routineRepository.findById(routineId)
        return if (optionalRoutine.isPresent) {
            ResponseEntity.ok(optionalRoutine.get())
        } else ResponseEntity.ok(null)
    }

    fun triggerRoutineById(routineId: Long?): ResponseEntity<Routine>? {
        return null //Todo: Specify implementation
    }

    fun createRoutine(routine: Routine): ResponseEntity<Routine> {
        return ResponseEntity.ok(routineRepository.save(routine))
    }

    fun deleteRoutine(routineId: Long): ResponseEntity<String> {
        routineRepository.deleteById(routineId)
        return ResponseEntity.ok("Entity deleted")
    }
}