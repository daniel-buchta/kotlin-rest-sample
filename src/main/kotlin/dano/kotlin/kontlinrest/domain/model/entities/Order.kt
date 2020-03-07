package dano.kotlin.kontlinrest.domain.model.entities

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import dano.kotlin.kontlinrest.domain.model.enums.OrderState
import dano.kotlin.kontlinrest.domain.model.valueobjects.OrderItem
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.Size

data class Order @JsonCreator constructor(

        @get:Min(1)
        val id: Long,

        val state: OrderState = OrderState.NEW,

        @get:Size(min = 1)
        @get:Valid
        val items: List<OrderItem> = emptyList()
) {

    @JsonIgnore
    fun isUpdatable() = state == OrderState.NEW && isValid()

    fun prepareToCreateIfValid() = if (isValid()) copy(state = OrderState.NEW) else null

    private fun isValid() = items.isNotEmpty()
}
