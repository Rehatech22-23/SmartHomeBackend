package de.rehatech.smartHomeBackend.controller.backend.responsesClass

import de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass.Channels
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass.FirmwareStatus
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass.Prop
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass.StatusInfo

data class Things(
    var label: String,
    var bridgeUID: String,
    var configuration: Prop,
    var properties: Prop,
    var UID: String,
    var thingTypeUID: String,
    var location: String,
    var channels: ArrayList<Channels>,
    var statusInfo: StatusInfo,
    var firmwareStatus: FirmwareStatus,
    var editable: Boolean,
)
