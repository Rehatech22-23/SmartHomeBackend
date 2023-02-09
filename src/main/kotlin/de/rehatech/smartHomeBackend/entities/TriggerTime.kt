package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import java.util.*

@Entity
class TriggerTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var routineId: Long? = null
    var localTime: Date? = null
    var repeat: Boolean? = null

    @OneToOne
    @MapsId
    @JoinColumn(name = "routine_id")
    var routine: Routine?=null
}