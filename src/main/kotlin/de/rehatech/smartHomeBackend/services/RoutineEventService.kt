package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.entities.RoutineEvent
import de.rehatech.smartHomeBackend.repositories.RoutineEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RoutineEventService {
    @Autowired
    private val routineEventRepository: RoutineEventRepository? = null
    val allRoutines: List<RoutineEvent?>
        get() {
            val result: MutableList<RoutineEvent?> = ArrayList()
            for (routineEvent in routineEventRepository!!.findAll()) {
                result.add(routineEvent)
            }
            return result
        }

    fun getRoutineById(id: Long): RoutineEvent? {
        val optionalRoutineEvent = routineEventRepository!!.findById(id)
        return optionalRoutineEvent.orElse(null)
    }

    fun postRoutineEvent(routineEvent: RoutineEvent): RoutineEvent {
        return routineEventRepository!!.save(routineEvent)
    }

    fun deleteAllRoutineEvents(): ResponseEntity<Void> {
        routineEventRepository!!.deleteAll()
        return ResponseEntity.ok().build()
    }

    fun deleteRoutineEventsById(id: Long): ResponseEntity<Void> {
        routineEventRepository!!.deleteById(id)
        return ResponseEntity.ok().build()
    }
}