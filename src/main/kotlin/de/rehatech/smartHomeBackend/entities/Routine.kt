package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import org.springframework.context.annotation.Bean
/**
 * An Entity that represents the Routines devices will execute
 *
 * @author Tim Lukas Bräuker
 */
@Entity(name = "routine")
class Routine {
    @get:Bean
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    lateinit var routineName: String
    var comparisonType: Int? = null

    /**
     * When this field is set, then the Routine will be triggered by a device
     */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "triggerEventByDevice_id")
    var triggerEventByDevice: TriggerEventByDevice? = null

    /**
     * When this field is set, then the Routine will be triggered by the time stamp given by the user
     */
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "triggerTime_id")
    var triggerTime: TriggerTime? = null


    @OneToMany( cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "routine_id")
    var routineEvent = mutableListOf<RoutineEvent>()
}