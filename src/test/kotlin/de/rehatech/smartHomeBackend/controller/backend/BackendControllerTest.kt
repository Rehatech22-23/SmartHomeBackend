package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import de.rehatech.smartHomeBackend.services.DeviceService
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BackendControllerTest {

    @Autowired
    lateinit var backendController: BackendController

    @Autowired
    lateinit var openHabRepository: OpenHabRepository

    @Autowired
    lateinit var functionRepository: FunctionRepository


    @Autowired
    lateinit var homeeRepository: HomeeRepository

    @Autowired
    lateinit var deviceService: DeviceService

    @Test
    fun getFunctionStateTest()
    {
        deviceService.updateDevices()
        val l1 = openHabRepository.findAll().toList()
        //val count = functionRepository.count()
        val l2 = homeeRepository.findAll().toList()

        val opstage = backendController.getMethodStatus("OH:11", l1.get(11).deviceMethodsIDS[0])
        val hmstage = backendController.getMethodStatus("HM:11", l2.get(2).deviceMethodsIDS[0])

        println(hmstage)

    }

}