package dano.kotlin.kontlinrest.application

import dano.kotlin.kontlinrest.domain.dao.OrderDao
import dano.kotlin.kontlinrest.domain.model.entities.Order
import dano.kotlin.kontlinrest.domain.model.enums.OrderState
import dano.kotlin.kontlinrest.domain.model.exceptions.InvalidOrderException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderExistsException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderNotFoundException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderStateException
import dano.kotlin.kontlinrest.domain.model.valueobjects.OrderItem
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(MockKExtension::class)
internal class OrderServiceTest {

    @RelaxedMockK
    private lateinit var dao: OrderDao

    @RelaxedMockK
    private lateinit var config: OrderConfigProperties

    @InjectMockKs
    private lateinit var orderService: OrderService

    @Test
    fun `createOrder creates new order if order is not empty and doesn't already exist`() {
        // setup
        val order = Order(id = 1, items = listOf(OrderItem(name = "Item1", count = 3)))

        every { dao.findById(any()) } returns null
        every { dao.save(any()) } returnsArgument 0

        // run
        val result = orderService.createOrder(order)

        // verify
        assertThat(result).isEqualTo(order.copy(state = OrderState.NEW))
    }

    @Test
    fun `createOrder throws exception if order is empty`() {
        // setup
        val order = Order(id = 1, items = emptyList())

        // run & verify
        assertThatThrownBy { orderService.createOrder(order) }
                .isInstanceOf(InvalidOrderException::class.java)
    }

    @Test
    fun `createOrder throws exception if order already exist`() {
        // setup
        val order = Order(id = 1, items = listOf(OrderItem(name = "Item1", count = 3)))

        every { dao.findById(order.id) } returns order

        // run & verify
        assertThatThrownBy { orderService.createOrder(order) }
                .isInstanceOf(OrderExistsException::class.java)
    }

    @Test
    fun `updateOrder creates non empty order if doesn't exist`() {
        // setup
        val order = Order(id = 5, items = listOf(OrderItem(name = "Item1", count = 7)))

        every { dao.findById(any()) } returns null
        every { dao.save(any()) } returnsArgument 0

        // run
        val result = orderService.updateOrder(order)

        // verify
        assertThat(result).isEqualTo(order.copy(state = OrderState.NEW))
    }

    @Test
    fun `updateOrder updates non empty order if existing order is in state NEW`() {
        // setup
        val order = Order(id = 5, items = listOf(OrderItem(name = "Item1", count = 7)))

        every { dao.findById(order.id) } returns Order(id = 5, items = listOf(OrderItem(name = "Item2", count = 5)))
        every { dao.save(any()) } returnsArgument 0

        // run
        val result = orderService.updateOrder(order)

        // verify
        assertThat(result).isEqualTo(order.copy(state = OrderState.NEW))
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, value = OrderState::class, names = ["NEW"])
    fun `updateOrder throws exception if existing order is in other state than NEW`(state: OrderState) {
        // setup
        val order = Order(id = 5, items = listOf(OrderItem(name = "Item1", count = 7)))

        every { dao.findById(order.id) } returns
                Order(id = 5, state = state, items = listOf(OrderItem(name = "Item2", count = 5)))

        // run & verify
        assertThatThrownBy { orderService.updateOrder(order) }
                .isInstanceOf(OrderStateException::class.java)
    }

    @Test
    fun `getOrder returns order if exist`() {
        // setup
        val expected = Order(id = 5, items = listOf(OrderItem(name = "Item2", count = 5)))
        every { dao.findById(expected.id) } returns expected

        // run
        val result = orderService.getOrder(expected.id)

        // verify
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getOrder throws exception if order doesn't exist`() {
        // run & verify
        every { dao.findById(any()) } returns null
        assertThatThrownBy { orderService.getOrder(42) }
                .isInstanceOf(OrderNotFoundException::class.java)
    }
}
