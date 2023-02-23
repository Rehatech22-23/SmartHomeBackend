package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Assertions.*


@SpringBootTest
class OpenHabDeviceRpositoryTest  {

    @Autowired
    lateinit var openHabRepository: OpenHabRepository

    @Test
    fun testrepo()
    {

        val test = OpenHabDevice(name="hallo", uid="by");
        val test2 = OpenHabDevice(name="hao",uid="by1");
        openHabRepository.save(test)

        println(openHabRepository.count())
        val li = openHabRepository.findAll()
        openHabRepository.save(test2)
        val li2 = openHabRepository.findAll().toList()
        assertEquals(2, li2.size)
        val t = openHabRepository.findById(2).get();
        assertEquals("hao", t.name)
        assertEquals("OH:2",t.getOpenHabID())

    }

    @Test
    fun testFindbyUid()
    {
        val test = OpenHabDevice(name="hallo", uid="by");
        openHabRepository.save(test)
        val found = openHabRepository.findOpenHabByUid("by")
        println(found.name)
    }


}