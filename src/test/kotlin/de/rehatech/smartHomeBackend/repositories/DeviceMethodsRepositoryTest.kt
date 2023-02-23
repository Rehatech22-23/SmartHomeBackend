package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.entities.DeviceMethods
import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DeviceMethodsRepositoryTest {

    @Autowired
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository

    @Autowired
    lateinit var deviceMethodsRepository: DeviceMethodsRepository

    @Test
    fun testJoin()
    {
        val test = OpenHabDevice(name="hallo", uid="by")
        openHabDeviceRepository.save(test)
        val t = openHabDeviceRepository.findById(1).get()
        val newFun = DeviceMethods(name = "test", label= "test", type = FunctionType.Switch, deviceOpenHabDevice = t)
        deviceMethodsRepository.save(newFun)


        val t3 = openHabDeviceRepository.findById(1).get()
        println( t3.deviceMethodsIDS.size)
    }
}