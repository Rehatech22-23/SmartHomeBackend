package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
class TriggerByDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var deviceId: Long? = null
    var functionId: Long? = null

    @ManyToOne
    @JoinColumn(name = "triggerByDevice_id")
    private val routine: Routine? = null
}