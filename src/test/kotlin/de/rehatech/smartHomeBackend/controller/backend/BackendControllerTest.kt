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

        val opstage = backendController.getFunctionState("OH:11", l1.get(11).functionValuesIDS[0])
        val hmstage = backendController.getFunctionState("HM:11", l2.get(2).functionValuesIDS[0])

        println(hmstage)

    }

}