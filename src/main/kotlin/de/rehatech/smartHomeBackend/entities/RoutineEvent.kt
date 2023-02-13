package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
class RoutineEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var routineId: Long? = null
    var deviceId: String? = null
    var functionId: Long? = null
    var functionValue: Float? = null

    @ManyToOne
    @JoinColumn(name="routineEvent")
    var routineEvent: Routine?=null

}