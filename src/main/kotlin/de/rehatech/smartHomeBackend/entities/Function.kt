package de.rehatech.smartHomeBackend.entities

import de.rehatech2223.datamodel.util.Range
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity(name = "function")
class Function {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null
    val functionName: String? = null
    val range: String? = null // Serialized Range Object
    val onOff : Boolean? = null
    val outputValue: String? = null
    val outputTrigger: Boolean? = null
}