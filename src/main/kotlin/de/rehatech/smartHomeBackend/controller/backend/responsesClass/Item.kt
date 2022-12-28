package de.rehatech.smartHomeBackend.controller.backend.responsesClass

import de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass.CommandDescription
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass.StateDescription

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
