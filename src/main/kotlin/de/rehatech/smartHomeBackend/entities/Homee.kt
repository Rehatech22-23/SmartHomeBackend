package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
data class Homee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
)
{
    @OneToMany(cascade = [(CascadeType.ALL)], fetch= FetchType.EAGER, mappedBy = "deviceHomee", orphanRemoval = false)
    var functionValuesIDS = mutableListOf<FunctionValues>()

    fun getHomeeID():String
    {
        return "HM:${id}"
    }
}
