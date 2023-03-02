package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity(name = "triggerEventByDevice")
class TriggerEventByDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    lateinit var deviceId: String

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    lateinit var routine: Routine

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id")
    lateinit var function: Function
}