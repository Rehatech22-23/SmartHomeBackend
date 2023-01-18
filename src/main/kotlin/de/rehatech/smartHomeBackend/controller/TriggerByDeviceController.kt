package de.rehatech.smartHomeBackend.controller

import de.rehatech.smartHomeBackend.entities.TriggerByDevice
import de.rehatech.smartHomeBackend.services.TriggerByDeviceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class TriggerByDeviceController(@field:Autowired private val triggerByDeviceService: TriggerByDeviceService) {

    @get:GetMapping(value = ["get/triggerByDevices"])
    val allTriggerByDevices: List<TriggerByDevice>
        get() = triggerByDeviceService.allTriggerByDevice

    @GetMapping(value = ["get/triggerbydevice/{id}"])
    fun getTriggerByDeviceById(@PathVariable id: Long?): TriggerByDevice {
        return triggerByDeviceService.getTriggerByDeviceById(id)
    }

    @PostMapping(value = ["post/triggerbydevice"])
    fun postTriggerByDevices(@RequestParam triggerByDevice: TriggerByDevice?): TriggerByDevice {
        return triggerByDeviceService.postTriggerByDevice(triggerByDevice)
    }

    @DeleteMapping(value = ["delete/triggerbydevices"])
    fun deleteAllTriggerByDevices(): ResponseEntity<Void> {
        return triggerByDeviceService.deleteAllTriggerByDevices()
    }

    @DeleteMapping(value = ["delete/triggerbydevices/{id}"])
    fun deleteTriggerByDeviceById(@PathVariable id: Long?): ResponseEntity<Void> {
        return triggerByDeviceService.deleteTriggerByDevicesById(id)
    }
}