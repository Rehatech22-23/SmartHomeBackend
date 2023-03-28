package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.services.RoutineService
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.datamodel.util.RoutineEventDTO
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class RoutineControllerTest(@field:Autowired private val mockedService: RoutineService) {

    /**
     * Tests the getAllRoutineIds method from the Routine Controller
     */
    @Test
    fun testGetAllRoutineIds() {
        val controller = RoutineController(mockedService)
        val response = controller.getAllRoutineIds()
        assertNotNull(response)
    }

    /**
     * Tests the getRoutine method from the Routine Controller
     */
    @Test
    fun testGetRoutine() {
        val controller = RoutineController(mockedService)
        val routineId: Long = 123
        val response = controller.getRoutine(routineId)
        assertNotNull(response)
    }

    /**
     * Test the triggerRoutineById method from the Routine Controller
     */
    @Test
    fun testTriggerRoutineById() {
        val controller = RoutineController(mockedService)
        val routineId: Long = 123
        val response = controller.triggerRoutineById(routineId)
        assertNotNull(response)
    }


    /**
     * Tests the createRoutine method from the Routine Controller
     */
    @Test
    fun testCreateRoutine() {
        val controller = RoutineController(mockedService)
        val routineDTO = RoutineDTO.Builder(
            routineName="MyRoutine",
            comparisonType = 1,
            routineEventDTO = arrayListOf(RoutineEventDTO(
                deviceId="device123",
                functionId=456L,
                functionValue=1.2f,
                routineEventId=789L,
                routineId=123L
            )),
            routineId=123L).build()
        val response = controller.createRoutine(routineDTO)
        assertNotNull(response)
    }

    /**
     * Tests the deleteRoutine method from the Routine Controller
     */
    @Test
    fun testDeleteRoutine() {
        val controller = RoutineController(mockedService)
        val routineId: Long = 123
        val response = controller.deleteRoutine(routineId)
        assertNotNull(response)
    }
}