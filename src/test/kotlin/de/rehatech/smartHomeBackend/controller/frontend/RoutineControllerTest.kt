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

    @Test // Test the getAllRoutineIds() method
    fun testGetAllRoutineIds() {
        val controller = RoutineController(mockedService)
        val response = controller.getAllRoutineIds()
        assertNotNull(response)
    }


    @Test // Test the getRoutine() method
    fun testGetRoutine() {
        val controller = RoutineController(mockedService)
        val routineId: Long = 123
        val response = controller.getRoutine(routineId)
        assertNotNull(response)
    }


    @Test // Test the triggerRoutineById() method
    fun testTriggerRoutineById() {
        val controller = RoutineController(mockedService)
        val routineId: Long = 123
        val response = controller.triggerRoutineById(routineId)
        assertNotNull(response)
    }



    @Test // Test the createRoutine() method
    fun testCreateRoutine() {
        val controller = RoutineController(mockedService)
        val routineDTO = RoutineDTO.Builder(
            routineName="MyRoutine",
            triggerType=1,
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


    @Test // Test the deleteRoutine() method
    fun testDeleteRoutine() {
        val controller = RoutineController(mockedService)
        val routineId: Long = 123
        val response = controller.deleteRoutine(routineId)
        assertNotNull(response)
    }
}