package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.attributes
import de.rehatech.homeekt.model.nodes
import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.controller.backend.HomeeController
import de.rehatech.smartHomeBackend.controller.backend.OpenHabController
import de.rehatech.smartHomeBackend.databuild.RoutineBuild
import de.rehatech.smartHomeBackend.entities.DeviceMethods
import de.rehatech.smartHomeBackend.entities.HomeeDevice
import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.repositories.DeviceMethodsRepository
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import de.rehatech.smartHomeBackend.response.Things
import de.rehatech.smartHomeBackend.response.util.Channels
import de.rehatech.smartHomeBackend.response.util.FirmwareStatus
import de.rehatech.smartHomeBackend.response.util.Prop
import de.rehatech.smartHomeBackend.response.util.StatusInfo
import de.rehatech2223.datamodel.FunctionDTO
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.internal.verification.VerificationModeFactory.atLeastOnce
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ExtendWith(OutputCaptureExtension::class)
@ActiveProfiles("integration-test")
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
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository

    @SpyBean
    lateinit var homeeDeviceRepository: HomeeDeviceRepository

    @SpyBean
    lateinit var functionService: FunctionService

    @SpyBean
    lateinit var homeeController: HomeeController

    @SpyBean
    lateinit var openHabController: OpenHabController



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
    fun testTimeRoutineInRange()
    {

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))

        routineService.createRoutine(routineBuild.triggertimeRoutine())
        automationService.automation()
        Mockito.verify(functionService,atLeastOnce()).triggerFunc("HM:1",1,1F)
    }

    @Test
    fun testTimeRoutineInRangeNoRepeat()
    {


        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        routineService.createRoutine(routineBuild.triggertimeRoutineNoRepeat())
        automationService.automation()
        Mockito.verify(functionService,atLeastOnce()).triggerFunc("HM:1",1,1F)
    }

    @Test
    fun testTimeRoutineNotInRange()
    {


        routineService.createRoutine(routineBuild.triggertimeBigRangeRoutine())
        automationService.automation()
        Mockito.verify(functionService, never()).triggerFunc("HM:1",1,1F)
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

    @Test
    fun testTriggerFuncThrowNoSuchException(output: CapturedOutput)
    {
        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        val mockDTO= FunctionDTO.Builder("Test",1, onOff = true).build()
        Mockito.`when`(backendController.getMethodStatus("OH:1", mockDeviceMethod)).thenReturn(mockDTO)

        routineService.createRoutine(routineBuild.noRangeRoutine())
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        Mockito.`when`(functionService.triggerFunc("HM:1",1,1F)).thenThrow(NoSuchMethodError::class.java)
        automationService.automation()

        //Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        //Mockito.`when`(functionService.triggerFunc("HM:1",1,1F)).thenThrow(IllegalArgumentException::class.java)

        assertTrue(output.out.contains("Routine Error"))
    }


    @Test
    fun testTriggerFuncThrowIllegalArgumentException(output: CapturedOutput)
    {
        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        val mockDTO= FunctionDTO.Builder("Test",1, onOff = true).build()
        Mockito.`when`(backendController.getMethodStatus("OH:1", mockDeviceMethod)).thenReturn(mockDTO)

        routineService.createRoutine(routineBuild.noRangeRoutine())
        Mockito.`when`( deviceMethodsRepository.findById(1)).thenReturn(Optional.of(mockDeviceMethod))
        Mockito.`when`(functionService.triggerFunc("HM:1",1,1F)).thenThrow(IllegalArgumentException::class.java)
        automationService.automation()

        assertTrue(output.out.contains("Routine Error"))
    }

    @Test
    fun deleteOpenHabDeviceTest()
    {
        openHabDeviceRepository.deleteAll()
        val openHabDevicemokito = OpenHabDevice(name = "Test", uid = "jsfw1e")
        val openHabDevice = openHabDeviceRepository.save(openHabDevicemokito)
        val deviceMethods = DeviceMethods(name = "Test", label = "Test", type = FunctionType.Switch,deviceOpenHab = openHabDevice)
        deviceMethodsRepository.save(deviceMethods)
        automationService.removeOldDevicesAndMethods()
        assertTrue( openHabDeviceRepository.findById(openHabDevice.id!!).isEmpty)


    }

    @Test
    fun deleteOpenHabDeviceMethodsTest() {
        deviceMethodsRepository.deleteAll()
        openHabDeviceRepository.deleteAll()
        automationService.updateDevices()
        val openHabDevicemokito = OpenHabDevice(name = "Test", uid = "jsfw1e")
        val openHabDevice = openHabDeviceRepository.save(openHabDevicemokito)
        val deviceMethods =
            DeviceMethods(name = "Test", label = "Test", type = FunctionType.Switch, deviceOpenHab = openHabDevice)
        deviceMethodsRepository.save(deviceMethods)
        val openhabList = openHabController.getDevices()
        val listchangel = arrayListOf<Channels>()
        openhabList!!.add(Things(label = "Test", bridgeUID = "ff",
            configuration = Prop("g","r","r"),
            UID = "jsfw1e", location = "d",
            statusInfo = StatusInfo("r","f","gf"),
            properties = Prop("f","d","r"),
            firmwareStatus = FirmwareStatus("d","d"),
            editable = true,
            thingTypeUID = "jsfw1e",
            channels = listchangel
        ))
        Mockito.`when`(openHabController.getDevices()).thenReturn(openhabList)
        Mockito.`when`(openHabController.getDevices()).thenReturn(openhabList)
        Mockito.`when`(openHabController.getDevices()).thenReturn(openhabList)

        automationService.removeOldDevicesAndMethods()
        assertFalse(openHabDeviceRepository.findById(openHabDevice.id!!).isEmpty)

    }
    @Test
    fun deleteHomeeDeviceTest()
    {
        homeeDeviceRepository.deleteAll()
        homeeController.getNodes()
        Thread.sleep(1000)
        automationService.updateDevices()
        Thread.sleep(3000)
        val mockHomee = HomeeDevice(name = "Test", homeeID = 5)
        val mockHomeeSave = homeeDeviceRepository.save(mockHomee)
        val deviceMethods = DeviceMethods(name = "Test", label = "Test", type = FunctionType.Switch, deviceHomee = mockHomeeSave)
        deviceMethodsRepository.save(deviceMethods)
        automationService.removeOldDevicesAndMethods()
        Thread.sleep(30000)
        assertTrue( homeeDeviceRepository.findById(mockHomeeSave.id!!).isEmpty)


    }

    @Test
    fun deleteHomeeDeviceMethodeTest()
    {
        homeeDeviceRepository.deleteAll()
        deviceMethodsRepository.deleteAll()
        automationService.updateDevices()
        Thread.sleep(3000)
        automationService.updateDevices()
        Thread.sleep(3000)
        val mockHomee = HomeeDevice(name = "Test", homeeID = 5)
        val mockHomeeSave = homeeDeviceRepository.save(mockHomee)
        val deviceMethods = DeviceMethods(name = "Test", label = "Test", type = FunctionType.Switch, deviceHomee = mockHomeeSave, homeeattrID = 2)
        val deviceMethodsSave = deviceMethodsRepository.save(deviceMethods)
        val listnodes = homeeController.getNodes()
        val listarr = arrayListOf<attributes>()
        listnodes!!.add(nodes(name = "Test", attributes = listarr, id = 5, profile = 0, protocol = 0, owner = 0, routing = 0, security = 0, state = 0, image = "f", cube_type = 0, favorite = 0, history = 0, note = "h", added = 0, state_change = 0, order = 0, phonetic_name = "g", services = 0 ))
        Mockito.`when`(homeeController.getNodes()).thenReturn(listnodes)
        Mockito.`when`(homeeController.getNodes()).thenReturn(listnodes)
        Mockito.`when`(homeeController.getNodes()).thenReturn(listnodes)
        Mockito.`when`(homeeController.getNodes()).thenReturn(listnodes)
        automationService.removeOldDevicesAndMethods()
        Thread.sleep(30000)
        assertFalse( homeeDeviceRepository.findById(mockHomeeSave.id!!).isEmpty)
        assertTrue(deviceMethodsRepository.findById(deviceMethodsSave.id!!).isEmpty)


    }


}