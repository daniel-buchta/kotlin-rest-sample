package dano.kotlin.kontlinrest.adapters.persistence

import dano.kotlin.kontlinrest.adapters.persistence.model.OrderJpa
import dano.kotlin.kontlinrest.domain.dao.OrderDao
import dano.kotlin.kontlinrest.domain.model.entities.Order
import mu.KotlinLogging
import org.springframework.stereotype.Component


private val log = KotlinLogging.logger {}

@Component
class JpaOrderDao(private val repository: OrderRepository) : OrderDao {

    override fun save(order: Order): Order =
            repository.save(OrderJpa(order)).toOrder().apply { log.info { "save($order) -> $this" } }

    override fun findById(id: Long): Order? =
            repository.findById(id).map { it.toOrder() }
                    .orElse(null)
                    .apply { log.info { "findById($id) -> $this" } }

    override fun findAll(): List<Order> =  repository.findAll().map { it.toOrder() }

}
