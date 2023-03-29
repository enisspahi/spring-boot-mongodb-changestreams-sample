package de.openvalue.resilience

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootMongodbChangestreamsSampleApplication

fun main(args: Array<String>) {
	runApplication<SpringBootMongodbChangestreamsSampleApplication>(*args)
}
