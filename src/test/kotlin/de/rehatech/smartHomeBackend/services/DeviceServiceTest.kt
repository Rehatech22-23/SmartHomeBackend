package de.rehatech.smartHomeBackend.services

import com.google.gson.Gson
import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Things
import de.rehatech.smartHomeBackend.entities.Homee
import de.rehatech.smartHomeBackend.entities.OpenHab
import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList


@SpringBootTest
class DeviceServiceTest {

    @MockBean
    lateinit var openHabRepository: OpenHabRepository

    @MockBean
    lateinit var homeeRepository: HomeeRepository

    @Autowired
    lateinit var deviceService: DeviceService


    @Test
    fun updateDevicesOpenHab() {
        val filePath = "src/test/kotlin/de/rehatech/smartHomeBackend/controller/backend/responsesClass/json/things.json"

        val content = Files.readString(Paths.get(filePath), Charsets.UTF_8)
        val things = Gson().fromJson(content, Array<Things>::class.java)
        val arrayList = ArrayList<Things>()
        Collections.addAll(arrayList, *things)
        deviceService.updateDevicesOpenHab(arrayList)
        assertEquals(2,openHabRepository.count())
        val filePath2 = "src/test/kotlin/de/rehatech/smartHomeBackend/controller/backend/responsesClass/json/thing2.json"

        val content2 = Files.readString(Paths.get(filePath2), Charsets.UTF_8)
        val thing = Gson().fromJson(content2, Things::class.java)
        val arlist = ArrayList<Things>()
        arlist.add(thing)
        deviceService.updateDevicesOpenHab(arlist)
        assertEquals(3,openHabRepository.count())
        deviceService.updateDevicesOpenHab(arrayList)
        assertEquals(3,openHabRepository.count())

    }

    @Test
    fun getDeviceIdListTest() {
        val ohList: List<OpenHab> = listOf(OpenHab(1,"name1", "uid1"), OpenHab(2, "name2", "uid2"))
        Mockito.`when`(openHabRepository.findAll().toList()).thenReturn(ohList)
        val hmList: List<Homee> = listOf(Homee(1,"name1"), Homee(2, "name2"))
        Mockito.`when`(homeeRepository.findAll().toList()).thenReturn(hmList)

        val result = deviceService.getDeviceIdList()
        assertEquals("OH:1", result[0])
        assertEquals("OH:2", result[1])
        assertEquals("HM:1", result[2])
        assertEquals("HM:2", result[3])

        assert((ohList.size+hmList.size)==result.size)

        verify(openHabRepository, times(1)).findAll()
        verify(homeeRepository, times(1)).findAll()
    }

    @Test //TODO
    fun getDevice() {
/*
        Mockito.`when`(getDeviceOH(tmp.get(1))).thenReturn(oh)
        Mockito.`when`(deviceId.split(":")).thenReturn(listOf("OH","1"))
        verify(deviceService, times(1)).getDeviceOH("OH:1")
*/

    }

    @Test
    fun updateDevices() {
        deviceService.updateDevices()
        val l1 = openHabRepository.findAll().toList()
        //val count = functionRepository.count()
    }

    @Test //TODO
    fun getDeviceOH() {
    /*
        val oh = OpenHab(1,"name1","uid1")
        Mockito.`when`(openHabRepository.findById(id.toLong()).get()).thenReturn(oh)

        val result = deviceService.getDevice("OH:1")


        Mockito.`when`(openHabRepository.findById(id.toLong()).get()).thenReturn(null)
*/

    }

    @Test //TODO
    fun getDeviceHM() {

    }
}