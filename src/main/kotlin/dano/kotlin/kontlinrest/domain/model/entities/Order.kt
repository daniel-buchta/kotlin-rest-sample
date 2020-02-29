package dano.kotlin.kontlinrest.domain.model.entities

import com.fasterxml.jackson.annotation.JsonCreator
import dano.kotlin.kontlinrest.domain.model.enums.OrderState
import dano.kotlin.kontlinrest.domain.model.valueobjects.OrderItem

data class Order @JsonCreator constructor(
        val id: Long,
        val state: OrderState = OrderState.NEW,
        val items: List<OrderItem> = emptyList()
) {

    fun isUpdatable() = state == OrderState.NEW && isValid()

    fun prepareToCreateIfValid() = if (isValid()) copy(state = OrderState.NEW) else null

    private fun isValid() = items.isNotEmpty()
}
