package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity(name = "function")
class Function {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    lateinit var functionName: String

    @OneToOne
    @JoinColumn(name = "range")
    var range: Range? = null

    @OneToOne
    @JoinColumn(name = "triggerEventByDevice")
    var triggerEventByDevice: TriggerEventByDevice? = null

    var deviceMethodsId: Long? = null

    var onOff: Boolean? = null
    var outputValue: String? = null
    var outputTrigger: Boolean? = null
}