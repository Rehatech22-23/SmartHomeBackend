package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.services.DeviceService
import de.rehatech2223.datamodel.Device
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
class DeviceController @Autowired constructor(val deviceService: DeviceService) {

    @GetMapping("/device/list")
    fun getDeviceIdList(): List<String> = deviceService.getDeviceIdList()

    @GetMapping("/device")
    fun getDevice(@RequestParam deviceID: String): Device {
        try {
            val tmp = deviceService.getDevice(deviceID)
                return tmp!!
        } catch (ex: NullPointerException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex)
        }
    }

    @GetMapping("/device/updatedDevices")
    fun getUpdatedDevices(): List<String> = deviceService.updatedDevices()

}