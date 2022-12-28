package de.rehatech.smartHomeBackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories



@SpringBootApplication
@EntityScan("de.rehatech.smartHomeBackend.model")
@EnableJpaRepositories("de.rehatech.smartHomeBackend.repositories")
class SmartHomeBackendApplication

fun main(args: Array<String>) {
	runApplication<SmartHomeBackendApplication>(*args)
}
