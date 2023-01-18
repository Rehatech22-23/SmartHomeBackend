package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
class RoutineEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var deviceId: Long? = null
    var functionsId: Long? = null

    @ManyToOne
    @JoinColumn(name = "routineEvent_id")
    private val routine: Routine? = null
}