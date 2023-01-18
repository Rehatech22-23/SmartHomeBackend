package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.entities.TriggerTime
import de.rehatech.smartHomeBackend.repositories.TriggerTimeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TriggerTimeService {
    @Autowired
    private val triggerTimeRepository: TriggerTimeRepository? = null
    val allTriggerTimes: List<TriggerTime?>
        get() {
            val result: MutableList<TriggerTime?> = ArrayList()
            for (triggerTime in triggerTimeRepository!!.findAll()) {
                result.add(triggerTime)
            }
            return result
        }

    fun getTriggerTimeById(id: Long): TriggerTime? {
        val optionalTriggerTime = triggerTimeRepository!!.findById(id)
        return optionalTriggerTime.orElse(null)
    }

    fun postTriggerTime(triggerTime: TriggerTime): TriggerTime {
        return triggerTimeRepository!!.save(triggerTime)
    }

    fun deleteAllTriggerTimes(): ResponseEntity<Void> {
        triggerTimeRepository!!.deleteAll()
        return ResponseEntity.ok().build()
    }

    fun deleteTriggerTimesById(id: Long): ResponseEntity<Void> {
        triggerTimeRepository!!.deleteById(id)
        return ResponseEntity.ok().build()
    }
}