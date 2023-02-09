package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import org.springframework.context.annotation.Bean

@Entity(name = "routine")
class Routine {
    @get:Bean
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var routineName: String? = null
    var triggerType: Int? = null

    @OneToOne(mappedBy = "routine")
    @PrimaryKeyJoinColumn
    var triggerEventByDevice: TriggerEventByDevice? = null

    @OneToOne(mappedBy = "routine")
    @PrimaryKeyJoinColumn
    var triggerTime: TriggerTime? = null


    @OneToMany(mappedBy = "routine")
    var routineEvent: ArrayList<RoutineEvent>? = null
}