package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech.smartHomeBackend.mapper.RoutineMapper
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.datamodel.util.RangeDTO
import de.rehatech2223.datamodel.util.RoutineEventDTO
import de.rehatech2223.datamodel.util.TriggerEventByDeviceDTO
import de.rehatech2223.datamodel.util.TriggerTimeDTO
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import java.time.LocalTime

/**
 * A Testing Class for the RoutineService
 *
 * This class contains methods for testing the business logic concerning Routine Entities
 */

@SpringBootTest
class RoutineServiceTest {
    @Autowired
    lateinit var routineService: RoutineService

    @Autowired
    lateinit var routineRepository: RoutineRepository

    @BeforeEach
    fun deleteAllExistingRoutinesFromDB() {

        routineRepository.deleteAll()
    }

    fun createMockRoutine(): Routine {
        //Create a mock routine
        val routineName = "My Routine"
        val triggerType = 1
        val routineEventDTO = arrayListOf<RoutineEventDTO>()

        val routineId = 0L
        val time = LocalTime.now()
        val repeat = true
        val triggerTimeId = 0L
        val triggerTimeDTO = TriggerTimeDTO(time, repeat, triggerTimeId, routineId)

        val deviceId = "MyDeviceId"

        val functionName = "MyFunction"
        val functionId = 0L

        val minValue = 0.0
        val maxValue = 1.0
        val currentValue = 0.5
        val rangeDTO = RangeDTO(minValue, maxValue, currentValue)

        val onOff = true
        val outputValue = "Some output value"
        val outputTrigger = true

        val functionDTOExpectationBuilder = FunctionDTO.Builder(functionName, functionId, rangeDTO, onOff, outputValue, outputTrigger)
        val functionDTOExpectation = functionDTOExpectationBuilder.build()

        val triggerEventByDeviceId = 0L

        val triggerEventByDeviceDTO = TriggerEventByDeviceDTO(deviceId, functionDTOExpectation, triggerEventByDeviceId, routineId)

        val routineDTOBuilder = RoutineDTO.Builder(routineName, triggerType, routineEventDTO, routineId, triggerTimeDTO, triggerEventByDeviceDTO)
        val mockRoutine = routineDTOBuilder.build()
        return RoutineMapper.mapToEntity(Json.decodeFromString<RoutineDTO>(routineService.createRoutine(mockRoutine).body!!))
    }

    /**
     * This method tests the method getAllRoutines from the RoutineService
     */
    @Test
    fun testGetAllRoutines() {
        //Call the method to test
        val result: ResponseEntity<List<RoutineDTO>> = routineService.getAllRoutines()
        //Check the returned value
        assertTrue(result.hasBody())
        assertEquals(0, result.body!!.size)
    }

    /**
     * This method tests the method getRoutine from the RoutineService
     */
    @Test
    fun testGetRoutine() {
        //Call the method to test
        val result = routineService.getRoutine(0)

        //Check the returned value
        assertEquals("Es konnte keine Routine mit der angegebenen Id gefunden werden!", result.body)

        val routine = createMockRoutine()
        val result2 = routineService.getRoutine(routine.id!!)
        assertNotNull(result2)

    }

    /**
     * This method tests the method triggerRoutineById from the RoutineService
     */
    @Test
    fun testTriggerRoutineById() {
        val routine = createMockRoutine()

        //Call the method to test
        val result = routineService.triggerRoutineById(routine.id!!)

        //Check the returned value
        assertEquals("Routine mit der angegebenen Id wurde getriggert!", result.body)
    }

    /**
     * This method tests the method createRoutine from the RoutineService
     */
    @Test
    fun testCreateRoutine() {
        val result = createMockRoutine()
        assertNotNull(result)
        val tests = routineRepository.findAll()
        for (test in tests) {
            println(test.id)
            println(test.routineEvent)
        }
    }

    @Test
    fun testTriggerTime() {
        val trigger = TriggerTimeDTO(LocalTime.now().plusMinutes(1), false, null, null)
        val event = RoutineEventDTO("OH:9", 18, 0.0F, null, null)
        val eventlist = arrayListOf<RoutineEventDTO>()
        eventlist.add(event)
        val routineTest = RoutineDTO.Builder("TriggerTest", 0, eventlist, -1, trigger, null).build()
        val savedRoutineDTO = Json.decodeFromString<RoutineDTO>(routineService.createRoutine(routineTest).body!!)
        assertNotEquals(routineTest.routineId, savedRoutineDTO.routineId)
        assertEquals(routineTest.routineName, savedRoutineDTO.routineName)
        assertEquals(routineTest.comparisonType, savedRoutineDTO.comparisonType)
    }

    /**
     * This method tests the method deleteRoutine from the RoutineService
     */
    @Test
    fun testDeleteRoutine() {
        val routine = createMockRoutine()
        val result: ResponseEntity<String> = routineService.deleteRoutine(routine.id!!)

        //Check the returned value
        assertEquals("Entity deleted successfully", result.body)
    }
}