package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.databuild.RoutineBuild
import de.rehatech.smartHomeBackend.entities.DeviceMethods
import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.repositories.DeviceMethodsRepository
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import de.rehatech2223.datamodel.FunctionDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.internal.verification.VerificationModeFactory.atLeastOnce
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import java.util.*

@SpringBootTest
class AutomationServiceTest {

    @Autowired
    lateinit var  automationService: AutomationService

    @Autowired
    lateinit var routineService: RoutineService

    @Autowired
    lateinit var routineRepository: RoutineRepository

    @SpyBean
    lateinit var backendController: BackendController

    @SpyBean
    lateinit var deviceMethodsRepository: DeviceMethodsRepository

    @SpyBean
    lateinit var functionService: FunctionService



    var routineBuild = RoutineBuild()

    @BeforeEach
    fun clearRepo()
    {
        routineRepository.deleteAll()
    }

    @Test
    fun testErrorRoutine()
    {
        routineService.createRoutine(routineBuild.errorRoutine())
        automationService.automation()
        assertEquals(0, routineRepository.count())
    }

    @Test
    fun testRoutinenoRangeSwitch()
    {
        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        val mockDTO= FunctionDTO.Builder("Test",1, onOff = true).build()
        Mockito.`when`(backendController.getMethodStatus("OH:1", mockDeviceMethod)).thenReturn(mockDTO)

        routineService.createRoutine(routineBuild.noRangeRoutine())
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        automationService.automation()
        Mockito.verify(functionService,atLeastOnce()).triggerFunc("HM:1",1,1F)
    }

    @Test
    fun testRoutinenoRangeSwitchFalse()
    {
        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        val mockDTO= FunctionDTO.Builder("Test",1, onOff = false).build()
        Mockito.`when`(backendController.getMethodStatus("OH:1", mockDeviceMethod)).thenReturn(mockDTO)

        routineService.createRoutine(routineBuild.noRangeRoutine())
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        automationService.automation()
        Mockito.verify(functionService,never()).triggerFunc("HM:1",1,1F)
    }


}