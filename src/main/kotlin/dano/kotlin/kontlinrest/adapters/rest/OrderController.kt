package dano.kotlin.kontlinrest.adapters.rest

import dano.kotlin.kontlinrest.application.OrderService
import dano.kotlin.kontlinrest.domain.model.entities.Order
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

private val log = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/orders")
@Api(value = "Order related services", tags = ["Order"])
class OrderController(val service: OrderService) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "Creates new order")
    fun createOrder(@RequestBody order: Order): Order {
        log.info { "Creating order: $order" }
        return service.createOrder(order)
    }

    @PutMapping(path = ["/id-{orderId}"],
            consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateOrder(@PathVariable orderId: Long, @RequestBody order: Order): Order {
        log.info { "Updating order #$orderId: $order" }
        return service.updateOrder(order.copy(id = orderId))
    }

    @GetMapping(path = ["/id-{orderId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getOrder(@PathVariable orderId: Long): Order {
        log.info { "Getting order #$orderId" }
        return service.getOrder(orderId)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @ApiOperation(value = "Get a list of orders")
    fun findOrders(): List<Order> {
        log.info { "Finding all order" }
        return service.findOrders()
    }
}
