package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
@Entity(name ="range")
class Range {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var minValue: Double?= null
    var maxValue: Double?= null
    var currentValue: Double?= null

    @OneToOne
    @MapsId
    @JoinColumn(name = "function")
    var function: Function?=null
}