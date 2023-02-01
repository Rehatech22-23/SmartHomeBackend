package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
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

}