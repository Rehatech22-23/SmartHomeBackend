package de.rehatech.smartHomeBackend.entities

import jakarta.persistence.*
import java.time.LocalTime
/**
 * An Entity Class that represents the TriggerTime
 *
 * @author Tim Lukas Bräuker
 */
@Entity(name = "triggerTime")
class TriggerTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var localTime: LocalTime? = null
    var repeat: Boolean? = null
    var repeatExecuted: Boolean = false //verfällt beim mappen


}