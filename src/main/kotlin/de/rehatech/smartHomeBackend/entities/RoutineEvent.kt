package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity(name = "routineEvent")
class RoutineEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    lateinit var deviceId: String
    var functionId: Long? = null
    var voldemort: Float? = null // deviceMethods

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="routine_id")
    var routine: Routine?=null

}