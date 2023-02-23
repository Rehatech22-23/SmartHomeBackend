package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

@Entity
data class HomeeDevice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var homeeID: Int,
)
{
    @OneToMany(cascade = [(CascadeType.ALL)], fetch= FetchType.EAGER, mappedBy = "deviceHomeeDevice", orphanRemoval = false)
    var functionValuesIDS = mutableListOf<FunctionValues>()

    fun getHomeeID():String
    {
        return "HM:${id}"
    }
}
