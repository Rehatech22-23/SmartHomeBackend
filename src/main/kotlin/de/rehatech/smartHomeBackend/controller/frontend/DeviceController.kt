package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass.Device
import de.rehatech.smartHomeBackend.services.DeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class DeviceController @Autowired constructor(val deviceService: DeviceService) {

    @GetMapping("/device/list")
    fun getDeviceIdList(): List<String> = deviceService.getDeviceIdList()

    @GetMapping("/device")
    fun getDevice(@RequestParam deviceID: String): Device? = deviceService.getDevice(deviceID)

    @GetMapping("/device/updatedDevices")
    fun getUpdatedDevices(): List<String> = deviceService.updatedDevices()

}