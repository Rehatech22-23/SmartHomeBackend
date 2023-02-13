package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DeviceServiceLiveTest {

    @Autowired
    lateinit var openHabRepository: OpenHabRepository

    @Autowired
    lateinit var homeeRepository: HomeeRepository

    @Autowired
    lateinit var deviceService: DeviceService


    @Test
    fun updateDevices() {
        deviceService.updateDevices()
        val l1 = openHabRepository.findAll().toList()
        //val count = functionRepository.count()
        val l2 = homeeRepository.findAll().toList()
        val count = homeeRepository.count()
    }
}