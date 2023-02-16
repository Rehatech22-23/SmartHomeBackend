package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
class RoutineEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var routineId: Long? = null
    lateinit var deviceId: String
    var functionId: Long? = null
    var voldemort: Float? = null // functionValue

    @ManyToOne
    @JoinColumn(name="routineEvent")
    var routineEvent: Routine?=null

}