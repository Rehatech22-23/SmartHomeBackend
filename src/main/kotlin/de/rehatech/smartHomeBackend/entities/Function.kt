package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity(name = "function")
class Function {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    lateinit var functionName: String

    @OneToOne(mappedBy = "range")
    var range: Range? = null


    var onOff : Boolean? = null
    var outputValue: String? = null
    var outputTrigger: Boolean? = null
}