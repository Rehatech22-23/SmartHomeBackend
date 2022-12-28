package de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass

data class StateDescription(
    var minimum: Double,
    var maximum: Double,
    var step: Double,
    var pattern: String,
    var readOnly: Boolean,
    var options: Options,
)
