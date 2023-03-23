package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import org.springframework.context.annotation.Bean

@Entity(name = "routine")
class Routine {
    @get:Bean
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    lateinit var routineName: String
    var triggerType: Int? = null

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "triggerEventByDevice_id")
    var triggerEventByDevice: TriggerEventByDevice? = null

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "triggerTime_id")
    var triggerTime: TriggerTime? = null


    @OneToMany( cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "routine_id")
    var routineEvent = mutableListOf<RoutineEvent>()
}