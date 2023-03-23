package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "triggerTime")
class TriggerTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var localTime: LocalDateTime? = null
    var repeat: Boolean? = null
    var repeatExecuted: Boolean = false //verfällt beim mappen


}