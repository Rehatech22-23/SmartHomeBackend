package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.entities.OpenHab
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import de.rehatech.smartHomeBackend.services.DeviceService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class DeviceControllerTest {

    @MockBean
    lateinit var deviceService: DeviceService

    @MockBean
    lateinit var openHabRepository : OpenHabRepository

    @Autowired
    lateinit var deviceController: DeviceController

    @Test
    fun getDeviceIdList() {
        val result = deviceController.getDeviceIdList()
        verify(deviceService, times(1)).getDeviceIdList()
    }

    @Test
    fun getDevice() {
       /* val result = deviceController.getDevice("OH:1")

        val oh = OpenHab(1,"name1", "uid1")
        Mockito.`when`(deviceService.getDevice(anyString())).thenReturn()

        verify(deviceService, times(1)).getDevice("OH:1")
        verify(deviceService, times(1)).getDevice("OH:1")*/
    }

    @Test
    fun getUpdatedDevices() {
        val result = deviceController.getUpdatedDevices()
        verify(deviceService, times(1)).updatedDevices()
    }
}