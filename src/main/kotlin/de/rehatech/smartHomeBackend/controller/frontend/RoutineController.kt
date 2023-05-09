package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.services.RoutineService
import de.rehatech2223.datamodel.RoutineDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * A Controller for the handling of RoutineDTOs*
 *
 * This class contains methods for handling API Calls for RoutineDTOs
 *
 * @param routineService Instance gets automatically autowired into the Controller
 */

@Controller
@RequestMapping("/routine")
class RoutineController(@field:Autowired private val routineService: RoutineService) {

    /**
     * Calls the getAllRoutineIds function in the RoutineService
     * @return a ResponseEntity<List<Long>> containing a list of all Routine Ids saved in the Database
     */
    @GetMapping("/list")
    fun getAllRoutineIds(): ResponseEntity<List<RoutineDTO>> {
        return routineService.getAllRoutines()
    }

    /**
     * Calls the getRoutine function in the RoutineService
     * @param routineId referencing the Routine from the Database that will be selected
     * @return a ResponseEntity containing a Json String representing a RoutineDTO
     */
    @GetMapping()
    fun getRoutine(@RequestParam routineId: Long): ResponseEntity<String> {
        return routineService.getRoutine(routineId)
    }

    /**
     * Calls the triggerRoutineById method in the triggerRoutineById function in the RoutineService
     * @param routineId referencing the Routine from the Database that will be triggered
     */
    @GetMapping("/trigger")
    fun triggerRoutineById(@RequestParam routineId: Long): ResponseEntity<String> {
        return routineService.triggerRoutineById(routineId)
    }

    /**
     * Calls the createRoutine function in the RoutineService
     * @param routineDTO that will be saved to the Database as a Routine Entity
     * @return a ResponseEntity that contains a Json String representing the Routine
     * Entity as DTO that was saved to the Database
     */
    @PostMapping("/create")
    fun createRoutine(@RequestBody routineDTO: RoutineDTO): ResponseEntity<String>? {
        return routineService.createRoutine(routineDTO)
    }

    /**
     * Calls the deleteRoutine function in the RoutineService
     * @param routineId referencing the Routine Objekt in the Database that will be deleted
     * @return a ResponseEntity containing a String either containing an OK on a successful
     * delete or an Internal Server Error on failed delete attempt
     */
    @DeleteMapping("/delete")
    fun deleteRoutine(@RequestParam routineId: Long): ResponseEntity<String>? {
        return routineService.deleteRoutine(routineId)
    }
}