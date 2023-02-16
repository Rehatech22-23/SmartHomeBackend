package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
class TriggerTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var routineId: Long? = null
    var localTime: LocalDateTime? = null
    var repeat: Boolean? = null
    var repeatExecuted: Boolean = false //verfällt beim mappen

    @OneToOne
    @MapsId
    @JoinColumn(name = "triggerTime")
    var routine: Routine?=null
}