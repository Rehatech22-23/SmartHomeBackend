package de.rehatech.smartHomeBackend.controller

import de.rehatech.smartHomeBackend.entities.TriggerTime
import de.rehatech.smartHomeBackend.services.TriggerTimeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class TriggerTimeController(@field:Autowired private val triggerTimeService: TriggerTimeService) {

    @get:GetMapping(value = ["get/trigger-times"])
    val allTriggerTimes: List<TriggerTime>
        get() = triggerTimeService.allTriggerTimes

    @GetMapping(value = ["get/trigger-time/{id}"])
    fun getTriggerTimeById(@PathVariable id: Long?): TriggerTime {
        return triggerTimeService.getTriggerTimeById(id)
    }

    @PostMapping(value = ["post/trigger-time"])
    fun postTriggerTimes(@RequestParam triggerTime: TriggerTime?): TriggerTime {
        return triggerTimeService.postTriggerTime(triggerTime)
    }

    @DeleteMapping(value = ["delete/trigger-times"])
    fun deleteAllTriggerTimes(): ResponseEntity<Void> {
        return triggerTimeService.deleteAllTriggerTimes()
    }

    @DeleteMapping(value = ["delete/trigger-time/{id}"])
    fun deleteTriggerTimeById(@PathVariable id: Long?): ResponseEntity<Void> {
        return triggerTimeService.deleteTriggerTimesById(id)
    }
}