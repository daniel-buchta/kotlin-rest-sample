package dano.kotlin.kontlinrest.domain.dao

import dano.kotlin.kontlinrest.domain.model.entities.Order

interface OrderDao {

    fun save(order: Order): Order

    fun findById(id: Long): Order?

    fun findAll(): List<Order>
}
