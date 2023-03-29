package de.rehatech.smartHomeBackend.entities

import de.rehatech.smartHomeBackend.enums.FunctionType
import jakarta.persistence.*

/**
 * An Entity Class that represents the DeviceMethods
 *
 * @author Sebastian Kurth
 */
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
    var deviceOpenHab: OpenHabDevice? = null,
    @ManyToOne
    @JoinColumn(name = "deviceHomee", nullable=true)
    var deviceHomee: HomeeDevice? = null,

    )
