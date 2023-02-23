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

    @OneToOne
    @JoinColumn(name = "triggerEventByDevice")
    var triggerEventByDevice: TriggerEventByDevice? = null

    @OneToOne
    @JoinColumn(name = "triggerTime")
    var triggerTime: TriggerTime? = null


    @OneToMany(mappedBy = "routine", cascade=[CascadeType.ALL])
    lateinit var routineEvent: ArrayList<RoutineEvent>
}