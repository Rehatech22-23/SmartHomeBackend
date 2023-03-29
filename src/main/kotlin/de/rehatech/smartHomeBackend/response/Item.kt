package de.rehatech.smartHomeBackend.response

import de.rehatech.smartHomeBackend.response.util.CommandDescription
import de.rehatech.smartHomeBackend.response.util.StateDescription

/**
 * A data class storing the required values for Items concerning OpenHab Devices
 */
data class Item(
    var type: String,
    var name: String,
    var label: String,
    var category: String,
    var tags: ArrayList<String>,
    var groupNames: ArrayList<String>,
    var link: String,
    var state: String,
    var transformedState: String,
    var stateDescription: StateDescription,
    var commandDescription: CommandDescription,
    var editable: Boolean,
)
