package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.Enum.FunctionType
import de.rehatech.smartHomeBackend.entities.FunctionValues
import de.rehatech.smartHomeBackend.entities.OpenHab
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FunctionRepositoryTest {

    @Autowired
    lateinit var openHabRepository: OpenHabRepository

    @Autowired
    lateinit var functionRepository: FunctionRepository

    @Test
    fun testJoin()
    {
        val test = OpenHab(name="hallo", uid="by")
        openHabRepository.save(test)
        val t = openHabRepository.findById(1).get();
        val newFun = FunctionValues(name = "test", label= "test", type = FunctionType.Switch, deviceOpenHab = t)
        functionRepository.save(newFun)


        val t3 = openHabRepository.findById(1).get();
        println( t3.functionValuesIDS.size)
    }
}