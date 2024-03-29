package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*

/**
 * An Entity Class that represents OpenHabDevices
 */
@Entity(name = "openhabDevice")
data class OpenHabDevice(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String,
    @Column(unique = true)
    var uid: String,

    ) {
    @OneToMany(
        cascade = [(CascadeType.ALL)],
        fetch = FetchType.EAGER,
        mappedBy = "deviceOpenHab",
        orphanRemoval = false
    )
    var deviceMethodsIDS = mutableListOf<DeviceMethods>()

    fun getOpenHabID(): String {
        return "OH:${id}"
    }
}
