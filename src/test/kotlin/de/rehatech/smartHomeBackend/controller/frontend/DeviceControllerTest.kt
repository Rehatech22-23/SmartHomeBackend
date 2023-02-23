package de.rehatech.smartHomeBackend.controller.frontend;

import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import de.rehatech.smartHomeBackend.services.DeviceService
import de.rehatech2223.datamodel.DeviceDTO
import org.junit.jupiter.api.Test;
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.mock.http.client.MockClientHttpRequest
import java.net.URI
import java.net.http.HttpResponse

@SpringBootTest
@WebMvcTest
class DeviceControllerTest {

    @MockBean
    lateinit var deviceService: DeviceService

    @Autowired
    lateinit var deviceController: DeviceController

    @Autowired
    lateinit var openHabRepository: OpenHabRepository

    @Test //TODO
    fun getDeviceIdList() {
        //responsecode 200 ok
        //response type == Application/json & UTF 8
        //verify dass deviceService.getDeviceIdList() nur einmal aufgerufen wird
    }

    @Test //TODO
    fun getDevice() {
        deviceService.updateDevices()
            //responsecode 200
            //responsetype == applicaiton/json & UTF-8 (?)
            //verify service.findAll() aufgerufen once
            //verify dass sonst keine anderen methoden aufgerufen werden
            //mock der null ist
            //NullPointerException? wird HttpStatus.BAD_REQUEST?
        val id = "OH:14"
        val req = MockClientHttpRequest(HttpMethod.GET, URI("http://localhost:9000/device"))
        val resp200 = Mockito.mock(HttpResponse::class.java)
        val dev: Device = Device("deviceName",id, arrayListOf(1,2,3))
        Mockito.`when`(req).thenReturn()

        Mockito.verify(deviceService.getDevice(id), Mockito.times(1)).getDevice(id)

        /*val request = Request.Builder()
            .url("http://localhost:9000/device?deviceID=OH:1")
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(2500, TimeUnit.MILLISECONDS)
            .build()

        val respond = client.newCall(request).execute()
        val json = respond.body?.string()
        if(respond.isSuccessful) {
            //val arr = Gson().fromJson(json, Things::class.java)
        }*/
    }

    @Test //TODO
    fun getUpdatedDevices() {
        //responsecode 200 ok
        //response type == Application/json & UTF 8
        //verify dass deviceService.updatedDevices() nur einmal aufgerufen wird
    }
}