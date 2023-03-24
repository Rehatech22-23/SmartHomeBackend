package de.rehatech.smartHomeBackend.controller.backend

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class HomeeDeviceControllerTest

{
    @Autowired
    lateinit var homeeController: HomeeController;
    @Test
    fun getNodeTest()
    {
        val list = homeeController.getNodes()
        println(list)
    }

    @Test
    fun setNode2off()
    {
        homeeController.sendCommand(2,26,1F)
    }
}