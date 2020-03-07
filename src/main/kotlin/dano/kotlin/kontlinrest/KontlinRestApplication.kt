package dano.kotlin.kontlinrest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class KontlinRestApplication

fun main(args: Array<String>) {
    runApplication<KontlinRestApplication>(*args)
}
