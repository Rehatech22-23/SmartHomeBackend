package de.rehatech.smartHomeBackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EntityScan("de.rehatech.smartHomeBackend.entities")
@ConfigurationPropertiesScan("de.rehatech.smartHomeBackend.config")
@EnableJpaRepositories("de.rehatech.smartHomeBackend.repositories")
class SmartHomeBackendApplication

fun main(args: Array<String>) {
	runApplication<SmartHomeBackendApplication>(*args)
}
