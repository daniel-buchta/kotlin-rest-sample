package dano.kotlin.kontlinrest.domain.model.valueobjects

import com.fasterxml.jackson.annotation.JsonCreator
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

data class OrderItem @JsonCreator constructor(

        @get:Size(min = 1)
        val name: String,

        @get:Min(1)
        @get:Max(999)
        val count: Int
)
