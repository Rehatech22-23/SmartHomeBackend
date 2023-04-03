package de.rehatech.smartHomeBackend.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * A data class hosting the ApiConfiguration for OpenHab and Homee
 *
 * @param openhabUrl String stating the URL to connect to the openHab Api
 * @param openhabToken String stating the Security Token necessary to access the OpenHab Api
 * @param homeeUrl String stating the URL to connect to the homee Api
 * @param homeeUser String stating the Security Username necessary to access the OpenHab Api
 * @param homeePassword String stating the Security password necessary to access the OpenHab Api
 */
@ConfigurationProperties(prefix = "api")
data class ApiConfiguration(
    val openhabUrl: String,
    val openhabToken: String,
    val homeeUrl: String,
    val homeeUser: String,
    val homeePassword: String,
    val deviceHomeeName:String
)
