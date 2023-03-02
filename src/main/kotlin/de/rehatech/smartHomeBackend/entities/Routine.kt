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

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "routine")
    var triggerEventByDevice: TriggerEventByDevice? = null

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "routine")
    var triggerTime: TriggerTime? = null


    @OneToMany(fetch = FetchType.LAZY, cascade=[CascadeType.ALL], mappedBy = "routine")
    lateinit var routineEvent: ArrayList<RoutineEvent>
}