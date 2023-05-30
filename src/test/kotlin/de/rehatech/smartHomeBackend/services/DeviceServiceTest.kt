package de.rehatech.smartHomeBackend.services

import com.google.gson.Gson
import de.rehatech.smartHomeBackend.response.Things
import de.rehatech.smartHomeBackend.entities.HomeeDevice
import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList


@SpringBootTest
class DeviceServiceTest {



    @MockBean
    lateinit var automationService: AutomationService

    @SpyBean
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository

    @SpyBean
    lateinit var homeeDeviceRepository: HomeeDeviceRepository

    @Autowired
    lateinit var deviceService: DeviceService


    @Test
    fun updateDevicesOpenHab() {
        val filePath = "src/test/kotlin/de/rehatech/smartHomeBackend/controller/backend/responsesClass/json/things.json"

        val countbefore= openHabDeviceRepository.count()
        val content = Files.readString(Paths.get(filePath), Charsets.UTF_8)
        val things = Gson().fromJson(content, Array<Things>::class.java)
        val arrayList = ArrayList<Things>()
        Collections.addAll(arrayList, *things)
        deviceService.updateDevicesOpenHab(arrayList)
        assertEquals(countbefore + 2,openHabDeviceRepository.count())
        val filePath2 = "src/test/kotlin/de/rehatech/smartHomeBackend/controller/backend/responsesClass/json/thing2.json"

        val content2 = Files.readString(Paths.get(filePath2), Charsets.UTF_8)
        val thing = Gson().fromJson(content2, Things::class.java)
        val arlist = ArrayList<Things>()
        arlist.add(thing)
        deviceService.updateDevicesOpenHab(arlist)
        assertEquals(countbefore+3,openHabDeviceRepository.count())
        deviceService.updateDevicesOpenHab(arrayList)
        assertEquals(countbefore+3,openHabDeviceRepository.count())

    }

    @Test
    fun getDeviceIdListTest() {
        val ohList: List<OpenHabDevice> = listOf(OpenHabDevice(1,"name1", "uid1"), OpenHabDevice(2, "name2", "uid2"))
        Mockito.`when`(openHabDeviceRepository.findAll().toList()).thenReturn(ohList)
        val hmList: List<HomeeDevice> = listOf(HomeeDevice(1,"name1", 2), HomeeDevice(2, "name2", 4))
        Mockito.`when`(homeeDeviceRepository.findAll().toList()).thenReturn(hmList)

        val result = deviceService.getDeviceIdList()
        assertEquals("OH:1", result[0])
        assertEquals("OH:2", result[1])
        assertEquals("HM:1", result[2])
        assertEquals("HM:2", result[3])

        assert((ohList.size+hmList.size)==result.size)

        verify(openHabDeviceRepository, times(1)).findAll()
        verify(homeeDeviceRepository, times(1)).findAll()
    }

    @Test //TODO
    fun getDevice() {
/*
        Mockito.`when`(getDeviceOH(tmp.get(1))).thenReturn(oh)
        Mockito.`when`(deviceId.split(":")).thenReturn(listOf("OH","1"))
        verify(deviceService, times(1)).getDeviceOH("OH:1")
*/

    }



    @Test //TODO
    fun getDeviceOH() {
    /*
        val oh = OpenHab(1,"name1","uid1")
        Mockito.`when`(openHabDeviceRepository.findById(id.toLong()).get()).thenReturn(oh)

        val result = deviceService.getDevice("OH:1")


        Mockito.`when`(openHabDeviceRepository.findById(id.toLong()).get()).thenReturn(null)
*/

    }

    @Test //TODO
    fun getDeviceHM() {

    }
}