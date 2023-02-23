package de.rehatech.smartHomeBackend.response.util

data class Channels(
    var uid: String,
    var id: String,
    var channelTypeUID: String,
    var itemType: String,
    var kind: String,
    var label: String,
    var description: String,
    var defaultTags: ArrayList<String>,
    var properties: Prop,
    var configuration: Prop,
    var autoUpdatePolicy: String,
    var linkedItems: ArrayList<String>,
)
