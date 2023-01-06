package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
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
    @Test
    fun updateDevices() {

        backendController.updateDevices()
        val l1 = openHabRepository.findAll().toList()
        val count = functionRepository.count()
        


    }
}