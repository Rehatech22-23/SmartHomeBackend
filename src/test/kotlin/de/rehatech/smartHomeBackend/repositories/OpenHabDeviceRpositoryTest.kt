package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest


@DataJpaTest
class OpenHabDeviceRpositoryTest  {

    @Autowired
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository


    @Test
    fun testrepo()
    {

        val test = OpenHabDevice(name="hallo", uid="by")
        val test2 = OpenHabDevice(name="hao",uid="by1")
        val count = openHabDeviceRepository.count()
        openHabDeviceRepository.save(test)

        val testObject = openHabDeviceRepository.save(test2)
        assertEquals(count+2, openHabDeviceRepository.count())

        val t = openHabDeviceRepository.findById(testObject.id!!).get()
        assertEquals("hao", t.name)
        assertEquals("OH:2",t.getOpenHabID())

        val found = openHabDeviceRepository.findOpenHabByUid("by")
        assertEquals("hallo",found.name)

        openHabDeviceRepository.delete(t)
        openHabDeviceRepository.delete(found)
        assertEquals(count,openHabDeviceRepository.count())

    }




}