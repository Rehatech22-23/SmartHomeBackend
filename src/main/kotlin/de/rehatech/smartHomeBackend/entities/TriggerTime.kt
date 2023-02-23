package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity(name = "triggerTime")
class TriggerTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var routineId: Long? = null
    var localTime: LocalDateTime? = null
    var repeat: Boolean? = null
    var repeatExecuted: Boolean = false //verfällt beim mappen

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "triggerTime", orphanRemoval = false)
    var routine: Routine?=null
}