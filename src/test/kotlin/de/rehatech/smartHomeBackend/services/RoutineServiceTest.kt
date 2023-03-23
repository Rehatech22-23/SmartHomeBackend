package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.mapper.RoutineMapper
import de.rehatech.smartHomeBackend.repositories.RoutineEventRepository
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import de.rehatech.smartHomeBackend.repositories.TriggerEventByDeviceRepository
import de.rehatech.smartHomeBackend.repositories.TriggerTimeRepository
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.datamodel.util.RangeDTO
import de.rehatech2223.datamodel.util.RoutineEventDTO
import de.rehatech2223.datamodel.util.TriggerEventByDeviceDTO
import de.rehatech2223.datamodel.util.TriggerTimeDTO
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import java.time.LocalTime


@SpringBootTest
class RoutineServiceTest{
    @Autowired
    lateinit var routineService: RoutineService
    @Autowired
    lateinit var routineRepository: RoutineRepository
    @Autowired
    lateinit var triggerTimeRepository: TriggerTimeRepository
    @Autowired
    lateinit var triggerEventByDeviceRepository: TriggerEventByDeviceRepository
    @Autowired
    lateinit var routineEventRepository: RoutineEventRepository

    @Test
    fun testGetAllRoutineIds() {
        //Call the method to test
        val result: ResponseEntity<List<Long>> = routineService.getAllRoutineIds()
        //Check the returned value
        assertTrue(result.hasBody())
        assertEquals(0, result.body!!.size)
    }

    @Test
    fun testGetRoutine() {
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

        //Call the method to test
        val result = routineService.getRoutine(mockRoutine.routineId!!)

        //Check the returned value
        assertEquals("Es konnte keine Routine mit der angegebenen Id gefunden werden!", result.body)

        routineService.createRoutine(mockRoutine)
        val result2 = routineService.getRoutine(mockRoutine.routineId!!)
        assertNotNull(result2)

    }

    @Test
    fun testTriggerRoutineById() {
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

        //Call the method to test
        val result = routineService.triggerRoutineById(mockRoutine.routineId!!)

        //Check the returned value
        assertEquals("Es konnte keine Routine mit der angegebenen Id gefunden werden!", result.body)
    }

    @Test
    fun testCreateRoutine() {
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
        val functionId = 2L

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

        //Call the method to test
        val result = routineService.createRoutine(mockRoutine)

        val tests = routineRepository.findAll()
        for (test in tests)
        {
            println(test.id)
            println( test.routineEvent)
        }
        //Check the returned value
        assertNotNull(result)
    }

    @Test
    fun testDeleteRoutine() {
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
        val mockRoutineDTO = routineDTOBuilder.build()
        val mockRoutine = RoutineMapper.mapToEntity(mockRoutineDTO)

        try {
            routineService.deleteRoutine(1)
        } catch (_: Error) {}

        mockRoutine.id=1
        routineRepository.save(mockRoutine)
        //Call the method to test
        val result = routineService.deleteRoutine(1)

        //Check the returned value
        assertEquals("Entity deleted", result.body)
    }
}