package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import org.springframework.context.annotation.Bean

@Entity
class Routine {
    @get:Bean
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var routineName: String? = null
    var triggerType: Long? = null

    @OneToMany
    @JoinColumn(name = "triggerByDevice_id")
    var triggerEventByDevice: ArrayList<TriggerByDevice>? = null

    @OneToMany
    @JoinColumn(name = "routineEvent_id")
    var routineEvent: ArrayList<RoutineEvent>? = null

    @OneToMany
    @JoinColumn(name = "triggerTime_id")
    var triggerTime: ArrayList<TriggerTime>? = null
}