package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
/**
 * An Entity Class that represents the RoutineEvent
 *
 * @author Tim Lukas Bräuker
 */
@Entity(name = "routineEvent")
class RoutineEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    lateinit var deviceId: String
    var functionId: Long? = null
    var voldemort: Float? = null // deviceMethods

    @ManyToOne
    @JoinColumn(name = "routine_id")
    var routineId: Routine? = null

}