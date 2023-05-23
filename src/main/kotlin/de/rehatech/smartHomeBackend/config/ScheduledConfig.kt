package de.rehatech.smartHomeBackend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile("!integration-test")
@EnableScheduling
@ComponentScan("de.rehatech.smartHomeBackend.services")
class ScheduledConfig {
}