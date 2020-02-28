package dano.kotlin.kontlinrest.domain.model.valueobjects

import com.fasterxml.jackson.annotation.JsonCreator

data class OrderItem @JsonCreator constructor(val name: String, val count: Int)
