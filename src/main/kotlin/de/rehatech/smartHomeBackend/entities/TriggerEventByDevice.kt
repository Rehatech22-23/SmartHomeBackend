package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*


/**
 * An Entity that states, that the Routine Event of the Routine object it is stored in,
 * will be triggered by a Device instead of a specific time
 *
 * @author Tim Lukas Bräuker
 */
@Entity(name = "triggerEventByDevice")
class TriggerEventByDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    lateinit var deviceId: String


    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "function_id")
    lateinit var function: Function
}