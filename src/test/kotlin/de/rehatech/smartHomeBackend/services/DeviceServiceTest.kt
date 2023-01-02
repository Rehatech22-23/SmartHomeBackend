package de.rehatech.smartHomeBackend.services

import com.google.gson.Gson
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Things
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList

@SpringBootTest
class DeviceServiceTest {

    @Autowired
    lateinit var deviceService: DeviceService

    @Autowired
    lateinit var openHabRepository: OpenHabRepository
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
}