package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.smartHomeBackend.repositories.DeviceMethodsRepository
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import de.rehatech.smartHomeBackend.services.AutomationService
import de.rehatech.smartHomeBackend.services.DeviceService
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BackendControllerTest {

    @Autowired
    private lateinit var automationService: AutomationService

    @Autowired
    lateinit var backendController: BackendController

    @Autowired
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository

    @Autowired
    lateinit var deviceMethodsRepository: DeviceMethodsRepository


    @Autowired
    lateinit var homeeDeviceRepository: HomeeDeviceRepository

    @Autowired
    lateinit var deviceService: DeviceService

    @Test
    fun getFunctionStateTest()
    {
        automationService.updateDevices()
        val l1 = openHabDeviceRepository.findAll().toList()
        //val count = deviceMethodsRepository.count()
        val l2 = homeeDeviceRepository.findAll().toList()

        val opstage = backendController.getMethodStatus("OH:11", l1.get(11).deviceMethodsIDS[0])
        val hmstage = backendController.getMethodStatus("HM:11", l2.get(2).deviceMethodsIDS[0])

        println(opstage)
        println(hmstage)

    }

}