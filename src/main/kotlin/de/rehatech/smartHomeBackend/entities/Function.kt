package de.rehatech.smartHomeBackend.entities

import de.rehatech2223.datamodel.util.RangeDTO
import jakarta.persistence.*

@Entity(name = "function")
class Function {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    lateinit var functionName: String

    @OneToOne(mappedBy = "range")
    var range: RangeDTO? = null


    var onOff : Boolean? = null
    var outputValue: String? = null
    var outputTrigger: Boolean? = null
}