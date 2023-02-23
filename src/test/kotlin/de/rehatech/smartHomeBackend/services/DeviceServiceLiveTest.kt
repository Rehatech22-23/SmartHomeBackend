package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DeviceServiceLiveTest {

    @Autowired
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository

    @Autowired
    lateinit var homeeDeviceRepository: HomeeDeviceRepository

    @Autowired
    lateinit var deviceService: DeviceService


    @Test
    fun updateDevices() {
        deviceService.updateDevices()
        val l1 = openHabDeviceRepository.findAll().toList()
        //val count = deviceMethodsRepository.count()
        val l2 = homeeDeviceRepository.findAll().toList()
        val count = homeeDeviceRepository.count()
    }
}