package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
/**
 * An Entity that represents the Range of certain parameters stored in a Routine*
 *
 * The fields of this class represent values tahr determine the value which will
 * be applied to the settings of the device
 */
@Entity(name ="range")
class Range {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var minValue: Double?= null
    var maxValue: Double?= null
    var currentValue: Double?= null

}