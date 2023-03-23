package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.services.RoutineService
import de.rehatech2223.datamodel.RoutineDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/routine")
class RoutineController(@field:Autowired private val routineService: RoutineService) {

    @GetMapping("/list")
    fun getAllRoutineIds(): ResponseEntity<List<Long>> {
        return routineService.getAllRoutineIds()
    }


    @GetMapping()
    fun getRoutine(@RequestParam routineId: Long): ResponseEntity<String> {
        return routineService.getRoutine(routineId)
    }

    @GetMapping("/trigger")
    fun triggerRoutineById(@RequestParam routineId: Long): ResponseEntity<String> {
        return routineService.triggerRoutineById(routineId)
    }

    @PostMapping("/create")
    fun createRoutine(@RequestBody routineDTO: RoutineDTO): ResponseEntity<String>? {
        return routineService.createRoutine(routineDTO)
    }

    @DeleteMapping("/delete")
    fun deleteRoutine(@RequestParam routineId: Long): ResponseEntity<String>? {
        return routineService.deleteRoutine(routineId)
    }
}