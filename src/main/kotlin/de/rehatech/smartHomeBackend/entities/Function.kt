package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

/**
 * An Entity that represents the function the Routine Object it is stored in will be executed when the Routine is triggered
 */
@Entity(name = "function")
class Function {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    lateinit var functionName: String

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "range_id")
    var range: Range? = null

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "function", orphanRemoval = true)
    var triggerEventByDevice: TriggerEventByDevice? = null

    var deviceMethodsId: Long? = null

    var onOff: Boolean? = null
    var outputValue: String? = null
    var outputTrigger: Boolean? = null
}