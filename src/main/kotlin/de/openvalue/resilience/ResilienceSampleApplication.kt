package de.openvalue.resilience

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResilienceSampleApplication

fun main(args: Array<String>) {
	runApplication<ResilienceSampleApplication>(*args)
}
