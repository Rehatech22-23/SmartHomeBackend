package de.rehatech.smartHomeBackend.response

import de.rehatech.smartHomeBackend.response.util.Channels
import de.rehatech.smartHomeBackend.response.util.FirmwareStatus
import de.rehatech.smartHomeBackend.response.util.Prop
import de.rehatech.smartHomeBackend.response.util.StatusInfo

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
