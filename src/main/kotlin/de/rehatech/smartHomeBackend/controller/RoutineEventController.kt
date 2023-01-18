package de.rehatech.smartHomeBackend.controller

import de.rehatech.smartHomeBackend.entities.RoutineEvent
import de.rehatech.smartHomeBackend.services.RoutineEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class RoutineEventController(@field:Autowired private val routineEventService: RoutineEventService) {

    @get:GetMapping(value = ["get/routineEvents"])
    val allRoutineEvents: List<RoutineEvent>
        get() = routineEventService.allRoutines

    @GetMapping(value = ["get/routineEvent/{id}"])
    fun getRoutineEventById(@PathVariable id: Long?): RoutineEvent {
        return routineEventService.getRoutineById(id)
    }

    @PostMapping(value = ["post/routineEvent"])
    fun postRoutineEvents(@RequestParam routineEvent: RoutineEvent?): RoutineEvent {
        return routineEventService.postRoutineEvent(routineEvent)
    }

    @DeleteMapping(value = ["delete/routineEvents"])
    fun deleteAllRoutineEvents(): ResponseEntity<Void> {
        return routineEventService.deleteAllRoutineEvents()
    }

    @DeleteMapping(value = ["delete/routineEvents/{id}"])
    fun deleteRoutineEventById(@PathVariable id: Long?): ResponseEntity<Void> {
        return routineEventService.deleteRoutineEventsById(id)
    }
}