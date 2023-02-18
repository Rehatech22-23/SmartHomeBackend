package de.rehatech.smartHomeBackend.response.util

data class StateDescription(
    var minimum: Double,
    var maximum: Double,
    var step: Double,
    var pattern: String,
    var readOnly: Boolean,
    var options: ArrayList<Options> ?= null,
)
