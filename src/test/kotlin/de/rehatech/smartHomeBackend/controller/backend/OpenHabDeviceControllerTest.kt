package de.rehatech.smartHomeBackend.controller.backend

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OpenHabDeviceControllerTest {

    @Autowired
    lateinit var openHabController: OpenHabController;
    val itemname = "Landepunkt_SchubkarreBesen_shellyplugsef627c192168178192_Betrieb";
    val thingUID = "shelly:shellyem3:bcff4dfd0bca";
    @Test
    fun sendcommand() {
        val erg = openHabController.sendCommand(itemname, "ON");
        assertEquals(true, erg)
    }

    @Test
    fun getItemByNameTest()
    {
        val erg = openHabController.getItemByName(itemname)
        assertNotNull(erg);
    }

    @Test
    fun getDevicesTest()
    {
        val erg = openHabController.getDevices()
        assertNotNull(erg)
    }

    @Test
    fun getDeviceTest()
    {
        val erg = openHabController.getDevice(thingUID)
        assertNotNull(erg)
    }
}