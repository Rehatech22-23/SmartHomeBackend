package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import java.util.*

@Entity
class TriggerTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var localTime: Date? = null
    var repeat: Boolean? = null

    @ElementCollection
    var routineIds: ArrayList<Long>? = null

    @ManyToOne
    @JoinColumn(name = "triggerTime_id")
    private val routine: Routine? = null
}