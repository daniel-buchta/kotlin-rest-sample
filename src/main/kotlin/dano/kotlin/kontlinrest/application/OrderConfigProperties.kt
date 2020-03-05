package dano.kotlin.kontlinrest.application

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app.order")
data class OrderConfigProperties(
        /** Service name*/
        val name: String = "Dummy",

        /** Service version*/
        val version: String,

        /** Build */
        val build: String
)
