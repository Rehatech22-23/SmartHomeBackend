package de.rehatech.smartHomeBackend.entities

import de.rehatech.smartHomeBackend.Enum.FunctionType
import jakarta.persistence.*

@Entity(name = "functions")
data class FunctionValues(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String,
    var type: FunctionType,
    var label: String,
    @ManyToOne
    @JoinColumn(name = "deviceOpenHab", nullable=true)
    var deviceOpenHab: OpenHab? = null,
    @ManyToOne
    @JoinColumn(name = "deviceHomee", nullable=true)
    var deviceHomee: Homee? = null,

)
