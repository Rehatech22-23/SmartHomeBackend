package de.rehatech.smartHomeBackend.controller

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech.smartHomeBackend.services.RoutineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class RoutineController(@field:Autowired private val routineService: RoutineService) {

    @get:GetMapping("routine/list")
    val allDeviceIds: ResponseEntity<List<Long>>
        get() = routineService.allDeviceIds

    @GetMapping("routine/{routineId}")
    fun getRoutine(@PathVariable routineId: Long?): ResponseEntity<Routine> {
        return routineService.getRoutine(routineId)
    }

    @PostMapping("routine/trigger")
    fun triggerRoutineById(routineId: Long?): ResponseEntity<Routine> {
        return routineService.triggerRoutineById(routineId)
    }

    @PostMapping("routine/create")
    fun createRoutine(@RequestParam routine: Routine?): ResponseEntity<Routine> {
        return routineService.createRoutine(routine)
    }

    @DeleteMapping("routine/delete/{routineId}")
    fun deleteRoutine(@PathVariable routineId: Long?): ResponseEntity<String> {
        return routineService.deleteRoutine(routineId)
    }
}