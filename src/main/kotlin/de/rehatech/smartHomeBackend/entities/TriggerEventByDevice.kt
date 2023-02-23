package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity(name = "triggerEventByDevice")
class TriggerEventByDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var routineID: Long? = null
    lateinit var deviceId: String

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "triggerEventByDevice")
    lateinit var routine: Routine

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, mappedBy = "triggerEventByDevice")
    lateinit var function: Function
}