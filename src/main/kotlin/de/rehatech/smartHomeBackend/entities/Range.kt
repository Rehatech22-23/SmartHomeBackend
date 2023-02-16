package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
@Entity
class Range {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var minValue: Double?= null
    var maxValue: Double?= null
    var currentValue: Double?= null

    @OneToOne
    @MapsId
    @JoinColumn(name = "range")
    var function: Function?=null
}