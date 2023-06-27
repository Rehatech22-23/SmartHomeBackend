package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.entities.DeviceMethods
import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.repositories.DeviceMethodsRepository
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import java.util.*

@SpringBootTest
class FunctionServiceTest {

    @Autowired
    lateinit var functionService: FunctionService

    @SpyBean
    lateinit var backendController: BackendController

    @SpyBean
    lateinit var deviceMethodsRepository: DeviceMethodsRepository

    @Autowired
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository

    @Autowired
    lateinit var homeeDeviceRepository: HomeeDeviceRepository

    @Autowired
    lateinit var functionTypeService: FunctionTypeService

    fun setup() {
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun testTriggerFuncValidDeviceIdOH() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )

        //-----------valid Device Id OH----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "OFF")
    }
    @Test
    fun testTriggerFuncValidDeviceIdHM(){

        val validDeviceIdHM = "HM:3"
        val mockOH1 = null
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )

        //-----------valid Device Id HM----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdHM, 1L, 1F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdHM, mockDeviceMethod, "1.0")
    }
    @Test
    fun testTriggerFuncInvalidDeviceId(){

        val invalidDeviceId = "KJO:O4"//IllegalArgumentException with log: 'DeviceId: $deviceId is illegal'
        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )

        //-----------invalid Device Id: IllegalArgumentException thrown? "DeviceId: $deviceId is illegal"----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try {
            functionService.triggerFunc(invalidDeviceId, 1L, 0F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "DeviceId: $deviceId is illegal"
        }
        Mockito.verify(backendController, VerificationModeFactory.times(0)).sendCommand(invalidDeviceId, mockDeviceMethod, "OFF")
    }
    @Test
    fun testTriggerFuncInvalidBody(){
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )

        //-----------invalid body value---------------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try{
            functionService.triggerFunc(validDeviceIdOH, 1L, 8F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        Mockito.verify(backendController, VerificationModeFactory.times(0)).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")

    }
    @Test
    fun testTriggerFuncOpen() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Contact, "Test",mockOH1,null )

        //-----------FunctionType.Contact, body: 1F, command soll "OPEN" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 1F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "OPEN")
    }
    @Test
    fun testTriggerFuncClosed() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Contact, "Test",mockOH1,null )

        //-----------FunctionType.Contact, body: 0F, command soll "CLOSED" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "CLOSED")
    }
    @Test
    fun testTriggerFuncInvalidOpenClosed() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Contact, "Test",mockOH1,null )

        //-----------FunctionType.Contact, body: 8F, command soll "invalidBody" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try{
            functionService.triggerFunc(validDeviceIdOH, 1L, 8F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        //Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")
    }
    @Test
    fun testTriggerFuncValidPercentRightBound() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Dimmer, "Test",mockOH1,null )

        //-----------FunctionType.Dimmer, body: 0F, command soll "0.0" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "0.0")
    }
    @Test
    fun testTriggerFuncValidPercentLeftBound() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Dimmer, "Test",mockOH1,null )

        //-----------FunctionType.Dimmer, body: 100F, command soll "100.0" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 100F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "100.0")
    }
    @Test
    fun testTriggerFuncInvalidPercent() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Dimmer, "Test",mockOH1,null )

        //-----------FunctionType.Dimmer, body: 101F, command soll "invalidBody" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try{
            functionService.triggerFunc(validDeviceIdOH, 1L, 101F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        //Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")
    }
    @Test
    fun testTriggerFuncOn() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )

        //-----------FunctionType.Switch, body: 1F, command soll "ON" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 1F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "ON")
    }
    @Test
    fun testTriggerFuncOff() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )

        //-----------FunctionType.Switch, body: 0F, command soll "OFF" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "OFF")
    }
    @Test
    fun testTriggerFuncInvalidOnOff() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockOH1,null )

        //-----------FunctionType.Switch, body: 8F, command soll "invalidBody" sein & bei Aufruf von triggerFunc soll eine ILLEGALARGUMENTEXCEPTION geworfen werden----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try{
            functionService.triggerFunc(validDeviceIdOH, 1L, 8F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        //Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")
    }
    @Test
    fun testTriggerFuncPlay() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Player, body: 0F, command soll "PLAY" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "PLAY")
    }
    @Test
    fun testTriggerFuncNext() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Player, body: 1F, command soll "NEXT" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 1F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "NEXT")
    }
    @Test
    fun testTriggerFuncPrevious() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Player, body: 2F, command soll "PREVIOUS" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 2F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "PREVIOUS")
    }
    @Test
    fun testTriggerFuncPause() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Player, body: 3F, command soll "PAUSE" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 3F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "PAUSE")
    }
    @Test
    fun testTriggerFuncRewind() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Player, body: 4F, command soll "REWIND" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 4F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "REWIND")
    }
    @Test
    fun testTriggerFuncFastforward() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Player, body: 5F, command soll "FASTFORWARD" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 5F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "FASTFORWARD")
    }
    @Test
    fun testTriggerFuncRefresh() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Player, body: 6F, command soll "REFRESH" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 6F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "REFRESH")
    }
    @Test
    fun testTriggerFuncInvalidPlayPause() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Player, body: 101F, command soll "invalidBody" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try{
            functionService.triggerFunc(validDeviceIdOH, 1L, 101F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        //Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")
    }
    @Test
    fun testTriggerFuncAirPurifierS() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Air, "Test",mockOH1,null )

        //-----------FunctionType.Air, body: 0F, command soll "s" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "s")
    }
    @Test
    fun testTriggerFuncAirPurifier1() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Air, "Test",mockOH1,null )

        //-----------FunctionType.Air, body: 1F, command soll "1" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 1F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "1")
    }
    @Test
    fun testTriggerFuncAirPurifier2() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Air, "Test",mockOH1,null )

        //-----------FunctionType.Air, body: 2F, command soll "2" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 2F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "2")
    }
    @Test
    fun testTriggerFuncAirPurifier3() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Air, "Test",mockOH1,null )

        //-----------FunctionType.Air, body: 3F, command soll "3" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 3F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "3")
    }
    @Test
    fun testTriggerFuncAirPurifierT() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Air, "Test",mockOH1,null )

        //-----------FunctionType.Air, body: 4F, command soll "t" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 4F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "t")
    }
    @Test
    fun testTriggerFuncInvalidAirPurifier() {
        //create Mock
        val validDeviceIdOH = "OH:14"

        val mockOH1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Player, "Test",mockOH1,null )

        //-----------FunctionType.Air, body: 5F, command soll "invalidBody" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try{
            functionService.triggerFunc(validDeviceIdOH, 1L, 5F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        //Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")
    }
    @Test
    fun testTriggerFuncCheckOn() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockHM1,null )

        //-----------FunctionType.Switch, body: 0F, command soll "0" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "0.0")
    }
    @Test
    fun testTriggerFuncCheckOff() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockHM1,null )

        //-----------FunctionType.Switch, body: 1F, command soll "1" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 1F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "1.0")
    }
    @Test
    fun testTriggerFuncInvalidCheckOnOff() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Switch, "Test",mockHM1,null )

        //-----------FunctionType.Switch, body: 18F, command soll "invalidBody" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try {
            functionService.triggerFunc(validDeviceIdOH, 1L, 18F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        //Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")
    }
    @Test
    fun testTriggerFuncCheckDimmingLevelLeftBound() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Dimmer, "Test",mockHM1,null )

        //-----------FunctionType.Dimmer, body: 0F, command soll "0.0" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "0.0")
    }
    @Test
    fun testTriggerFuncCheckDimmingLevelRightBound() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Dimmer, "Test",mockHM1,null )

        //-----------FunctionType.Dimmer, body: 100F, command soll "100.0" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 100F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "100.0")
    }
    @Test
    fun testTriggerFuncCheckDimmingLevel() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Dimmer, "Test",mockHM1,null )

        //-----------FunctionType.Dimmer, body: 50.5F, command soll "50.5" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 50.5F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "50.5")
    }
    @Test
    fun testTriggerFuncInvalidCheckDimmingLevel() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Dimmer, "Test",mockHM1,null )

        //-----------FunctionType.Dimmer, body: 104F, command soll "invalidBody" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try {
            functionService.triggerFunc(validDeviceIdOH, 1L, 104F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        //Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")
    }
    @Test
    fun testTriggerFuncCheckValueColorLeftBound() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Color, "Test",mockHM1,null )

        //-----------FunctionType.Color, body: 0F, command soll "0" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 0F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "0.0")
    }
    @Test
    fun testTriggerFuncCheckValueColorRightBound() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Color, "Test",mockHM1,null )

        //-----------FunctionType.Color, body: 16777215F, command soll "0" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 16777215F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "1.6777215E7")
    }
    @Test
    fun testTriggerFuncCheckValueColor() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Color, "Test",mockHM1,null )

        //-----------FunctionType.Color, body: 15732F, command soll "15732.0" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        functionService.triggerFunc(validDeviceIdOH, 1L, 15732F)
        Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "15732.0")
    }
    @Test
    fun testTriggerFuncInvalidCheckValueColor() {
        //create Mock
        val validDeviceIdOH = "HM:12"

        val mockHM1 = OpenHabDevice(1,"Test", "Test1")
        val mockDeviceMethod = DeviceMethods(1,null, "test", FunctionType.Color, "Test",mockHM1,null )

        //-----------FunctionType.Color, body: -1F, command soll "invalidBody" sein----------------------
        Mockito.`when`( deviceMethodsRepository.findById(1L)).thenReturn(Optional.of(mockDeviceMethod))
        try {
            functionService.triggerFunc(validDeviceIdOH, 1L, -1F)
        }catch (ex: IllegalArgumentException){
            //hier soll eine IllegalArgumentException geworfen werden
            //mit log.Error = "illegal value in request body"
        }
        //Mockito.verify(backendController, VerificationModeFactory.atLeastOnce()).sendCommand(validDeviceIdOH, mockDeviceMethod, "invalidBody")
    }
}