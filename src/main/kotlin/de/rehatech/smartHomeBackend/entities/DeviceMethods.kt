package de.rehatech.smartHomeBackend.entities

import de.rehatech.smartHomeBackend.enum.FunctionType
import jakarta.persistence.*

@Entity
data class DeviceMethods(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var homeeattrID: Int? = null, // only for homee
    var name: String,
    var type: FunctionType,
    var label: String,
    @ManyToOne
    @JoinColumn(name = "deviceOpenHab", nullable=true)
    var deviceOpenHabDevice: OpenHabDevice? = null,
    @ManyToOne
    @JoinColumn(name = "deviceHomee", nullable=true)
    var deviceHomeeDevice: HomeeDevice? = null,

    )
