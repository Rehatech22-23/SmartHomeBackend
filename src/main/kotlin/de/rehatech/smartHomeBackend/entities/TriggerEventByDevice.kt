package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
class TriggerEventByDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    val routineID: Long? = null
    var deviceId: String? = null

   /* @OneToOne
    @MapsId
    @JoinColumn(name = "routine_id")
    val functionExpectation: Function*/
}