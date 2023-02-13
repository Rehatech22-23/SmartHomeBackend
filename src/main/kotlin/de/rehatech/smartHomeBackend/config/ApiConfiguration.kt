package de.rehatech.smartHomeBackend.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding


@ConfigurationProperties(prefix = "api")
data class ApiConfiguration(
    val openhabUrl: String,
    val openhabToken: String,
    val homeeUrl: String,
    val homeeUser: String,
    val homeePassword: String,
)
