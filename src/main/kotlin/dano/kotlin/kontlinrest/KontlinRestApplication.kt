package dano.kotlin.kontlinrest

import dano.kotlin.kontlinrest.application.OrderConfigProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(OrderConfigProperties::class)
class KontlinRestApplication

fun main(args: Array<String>) {
    runApplication<KontlinRestApplication>(*args)
}
