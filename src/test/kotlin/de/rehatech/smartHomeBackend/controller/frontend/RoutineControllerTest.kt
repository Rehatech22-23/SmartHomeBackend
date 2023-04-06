package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech.smartHomeBackend.mapper.RoutineMapper
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import de.rehatech.smartHomeBackend.services.RoutineService
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.datamodel.util.RoutineEventDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.util.AssertionErrors.assertEquals


@SpringBootTest
class RoutineControllerTest(
        @Autowired
        var mockedService: RoutineService,
        @Autowired
        val routineRepository: RoutineRepository,
) {


    @BeforeEach
    fun deleteAllExistingRoutinesFromTestDB() {
        routineRepository.deleteAll();
    }

    fun createMockRoutine(): Routine {
        val controller = RoutineController(mockedService)

        val routineDTO = RoutineDTO.Builder(
                routineName = "MyRoutine",
                comparisonType = 1,
                routineEventDTO = arrayListOf(RoutineEventDTO(
                        deviceId = "OH:device123",
                        functionId = 456L,
                        functionValue = 1.2f,
                        routineEventId = 789L,
                        routineId = 123L
                )),
                routineId = 123L).build()
        return RoutineMapper.mapToEntity(Json.decodeFromString<RoutineDTO>(controller.createRoutine(routineDTO)!!.body!!))
    }

    /**
     * Tests the getAllRoutineIds method from the Routine Controller
     */
    @Test
    fun testGetAllRoutineIds() {
        val controller = RoutineController(mockedService)

        createMockRoutine()

        val response = controller.getAllRoutineIds()
        assertNotNull(response.body!!.isNotEmpty())
    }

    /**
     * Tests the getRoutine method from the Routine Controller
     */
    @Test
    fun testGetRoutine() {
        val controller = RoutineController(mockedService)
        createMockRoutine()
        val response = controller.getRoutine(123)

        assertNotNull(response)
    }

    /**
     * Tests the createRoutine method from the Routine Controller
     */
    @Test
    fun testCreateRoutine() {
        val controller = RoutineController(mockedService)
        val routineDTO = RoutineDTO.Builder(
                routineName = "MyRoutine",
                comparisonType = 1,
                routineEventDTO = arrayListOf(RoutineEventDTO(
                        deviceId = "device123",
                        functionId = 456L,
                        functionValue = 1.2f,
                        routineEventId = 789L,
                        routineId = 123L
                )),
                routineId = 123L).build()
        val response = controller.createRoutine(routineDTO)
        assertEquals("HTTP Status of createRoutine()", HttpStatus.OK, response!!.statusCode)
    }

    /**
     * Tests the deleteRoutine method from the Routine Controller
     */
    @Test
    fun testDeleteRoutine() {
        val controller = RoutineController(mockedService)
        val routine = createMockRoutine()
        val response = controller.deleteRoutine(routine.id!!)
        assertEquals("HTTP Message of deleteRoutine()", "Entity deleted successfully", response!!.body)
    }
}