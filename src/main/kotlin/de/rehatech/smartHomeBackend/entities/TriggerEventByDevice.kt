package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
class TriggerEventByDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    val routineID: Long? = null
    lateinit var deviceId: String

    @OneToOne
    @JoinColumn(name = "triggerEventByDevice")
    lateinit var triggerEventByDevice: Routine
}