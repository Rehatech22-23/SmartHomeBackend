package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Assertions.*


@SpringBootTest
class OpenHabDeviceRpositoryTest  {

    @Autowired
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository

    @Test
    fun testrepo()
    {

        val test = OpenHabDevice(name="hallo", uid="by")
        val test2 = OpenHabDevice(name="hao",uid="by1")
        openHabDeviceRepository.save(test)

        println(openHabDeviceRepository.count())
        val li = openHabDeviceRepository.findAll()
        openHabDeviceRepository.save(test2)
        val li2 = openHabDeviceRepository.findAll().toList()
        assertEquals(2, li2.size)
        val t = openHabDeviceRepository.findById(2).get()
        assertEquals("hao", t.name)
        assertEquals("OH:2",t.getOpenHabID())

    }

    @Test
    fun testFindbyUid()
    {
        val test = OpenHabDevice(name="hallo", uid="by");
        openHabDeviceRepository.save(test)
        val found = openHabDeviceRepository.findOpenHabByUid("by")
        println(found.name)
    }


}