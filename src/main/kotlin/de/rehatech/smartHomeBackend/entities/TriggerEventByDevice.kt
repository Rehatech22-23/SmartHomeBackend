package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity(name = "triggerEventByDevice")
class TriggerEventByDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var routineID: Long? = null
    lateinit var deviceId: String

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "routine_id", nullable = false)
    lateinit var routine: Routine

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "function_id", nullable = false)
    lateinit var function: Function
}