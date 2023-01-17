package de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass

data class Device(
    var deviceName: String,
    var deviceId: String,
    var functionIds: IntArray,
)
