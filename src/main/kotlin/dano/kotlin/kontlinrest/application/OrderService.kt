package dano.kotlin.kontlinrest.application

import dano.kotlin.kontlinrest.domain.dao.OrderDao
import dano.kotlin.kontlinrest.domain.model.entities.Order
import dano.kotlin.kontlinrest.domain.model.exceptions.InvalidOrderException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderExistsException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderNotFoundException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderStateException
import mu.KotlinLogging
import org.springframework.stereotype.Service


private val log = KotlinLogging.logger {}

@Service
class OrderService(private val dao: OrderDao) {

    /**
     * Creates new order
     * @param order order to create
     * @return created order
     * */
    fun createOrder(order: Order): Order {
        log.debug { "Creating order: $order" }

        val orderToCreate = order.prepareToCreateIfValid() ?: run {
            log.warn { "Can't create invalid order: $order" }
            throw InvalidOrderException("Order #${order.id} is invalid")
        }

        dao.findById(order.id)?.let {
            log.warn { "Order already exists: $it" }
            throw OrderExistsException("Order #${order.id} already in exists")
        }

        return dao.save(orderToCreate).apply { log.info { "Created order: $this" } }
    }

    fun updateOrder(order: Order): Order {
        log.debug { "Updating order: $order" }

        if (!order.isUpdatable()) {
            log.warn { "Can't update invalid order: $order" }
            throw InvalidOrderException("Order #${order.id} is invalid")
        }

        // throw exception if existing offer is not updatable
        dao.findById(order.id)?.takeIf { !it.isUpdatable() }?.let {
            log.warn { "Can't update order already in processing: $it" }
            throw OrderStateException("Order #${order.id} already in processing")
        }

        return dao.save(order).apply { log.info { "Updated order: $this" } }
    }

    fun getOrder(orderId: Long): Order {
        log.debug { "Getting order: $orderId" }

        return dao.findById(orderId)?.apply { log.info { "Retrieved order: $this" } }
                ?: run {
                    log.warn { "No order exists for id $orderId" }
                    throw OrderNotFoundException("Order #$orderId doesn't exist")
                }
    }

    fun findOrders(): List<Order> {
        log.debug { "Finding orders" }

        return dao.findAll().apply { log.info { "Retrieved ${this.size} orders" } }
    }
}
