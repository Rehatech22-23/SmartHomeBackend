package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
class TriggerEventByDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var routineID: Long? = null
    lateinit var deviceId: String

    @OneToOne
    @JoinColumn(name = "routine")
    lateinit var routine: Routine

    @OneToOne
    @JoinColumn(name = "function")
    lateinit var function: Function
}