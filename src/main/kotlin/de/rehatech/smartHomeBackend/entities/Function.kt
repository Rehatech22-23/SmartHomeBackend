package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity(name = "function")
class Function {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    lateinit var functionName: String

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "function", orphanRemoval = true)
    var range: Range? = null

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "function", orphanRemoval = true)
    var triggerEventByDevice: TriggerEventByDevice? = null

    var deviceMethodsId: Long? = null

    var onOff: Boolean? = null
    var outputValue: String? = null
    var outputTrigger: Boolean? = null
}