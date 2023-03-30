package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

/**
 * An Entity Class that represents HomeeDevices
 */
@Entity
data class HomeeDevice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    var homeeID: Int,
)
{
    @OneToMany(cascade = [(CascadeType.ALL)], fetch= FetchType.EAGER, mappedBy = "deviceHomee", orphanRemoval = false)
    var deviceMethodsIDS = mutableListOf<DeviceMethods>()

    fun getHomeeID():String
    {
        return "HM:${id}"
    }
}
