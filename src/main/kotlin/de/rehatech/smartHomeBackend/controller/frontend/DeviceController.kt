package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.services.AutomationService
import de.rehatech.smartHomeBackend.services.DeviceService
import de.rehatech2223.datamodel.DeviceDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * A class that handles API calls for Device Objects
 *
 * @param deviceService Instance gets automatically autowired into the Controller
 * @param automationService Instance gets automatically autowired into the Controller
 * @authors Sebastian Kurth, Sofia Bonas
 */
@RestController
@RequestMapping("/device")
class DeviceController @Autowired constructor(val deviceService: DeviceService, val automationService: AutomationService) {

    @GetMapping("/list")
    fun getDeviceIdList(): List<String> = deviceService.getDeviceIdList()

    @GetMapping()
    fun getDevice(@RequestParam deviceId: String): DeviceDTO {
        try {
            val tmp = deviceService.getDevice(deviceId)
                return tmp!!
        } catch (ex: NullPointerException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex)
        }
    }

    @GetMapping("/updated")
    fun getUpdatedDevices(): ResponseEntity<Nothing> {
        automationService.updateDevices()
        return ResponseEntity(null,null, HttpStatus.OK)
    }

}