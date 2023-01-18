package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.entities.TriggerByDevice
import de.rehatech.smartHomeBackend.repositories.TriggerByDeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TriggerByDeviceService {
    @Autowired
    private val triggerByDeviceRepository: TriggerByDeviceRepository? = null
    val allTriggerByDevice: List<TriggerByDevice?>
        get() {
            val result: MutableList<TriggerByDevice?> = ArrayList()
            for (triggerByDevice in triggerByDeviceRepository!!.findAll()) {
                result.add(triggerByDevice)
            }
            return result
        }

    fun getTriggerByDeviceById(id: Long): TriggerByDevice? {
        val optionalTriggerByDevice = triggerByDeviceRepository!!.findById(id)
        return optionalTriggerByDevice.orElse(null)
    }

    fun postTriggerByDevice(triggerByDevice: TriggerByDevice): TriggerByDevice {
        return triggerByDeviceRepository!!.save(triggerByDevice)
    }

    fun deleteAllTriggerByDevices(): ResponseEntity<Void> {
        triggerByDeviceRepository!!.deleteAll()
        return ResponseEntity.ok().build()
    }

    fun deleteTriggerByDevicesById(id: Long): ResponseEntity<Void> {
        triggerByDeviceRepository!!.deleteById(id)
        return ResponseEntity.ok().build()
    }
}