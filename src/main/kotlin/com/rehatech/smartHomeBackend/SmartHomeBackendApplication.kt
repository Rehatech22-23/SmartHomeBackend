package com.rehatech.smartHomeBackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmartHomeBackendApplication

fun main(args: Array<String>) {
	runApplication<SmartHomeBackendApplication>(*args)
}
