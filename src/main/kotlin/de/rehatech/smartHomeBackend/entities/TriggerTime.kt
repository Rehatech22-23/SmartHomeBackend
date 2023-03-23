package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import java.time.LocalTime

@Entity(name = "triggerTime")
class TriggerTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var localTime: LocalTime? = null
    var repeat: Boolean? = null
    var repeatExecuted: Boolean = false //verfällt beim mappen


}