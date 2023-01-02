package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.OpenHab
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Assertions.*


@SpringBootTest
class OpenHabRpositoryTest  {

    @Autowired
    lateinit var openHabRepository: OpenHabRepository

    @Test
    fun testrepo()
    {

        val test = OpenHab(name="hallo", uid="by");
        val test2 = OpenHab(name="hao",uid="by1");
        openHabRepository.save(test)

        println(openHabRepository.count())
        val li = openHabRepository.findAll()
        openHabRepository.save(test2)
        val li2 = openHabRepository.findAll()
        val t = openHabRepository.findById(2).get();
        assertEquals("hao", t.name)

    }

    @Test
    fun testFindbyUid()
    {
        val test = OpenHab(name="hallo", uid="by");
        openHabRepository.save(test)
        val found = openHabRepository.findOpenHabByUid("by")
        println(found.name)
    }


}