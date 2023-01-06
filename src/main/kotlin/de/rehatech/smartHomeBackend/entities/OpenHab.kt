package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*


@Entity(name = "openhab")
data class OpenHab(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String,
    @Column(unique = true)
    var uid: String,

)
{
    @OneToMany(cascade = [(CascadeType.ALL)], fetch= FetchType.EAGER, mappedBy = "deviceOpenHab", orphanRemoval = false)
    var functionValuesIDS = mutableListOf<FunctionValues>()

    fun getOpenHabID():String
    {
        return "OH:${id}"
    }
}
