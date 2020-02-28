package dano.kotlin.kontlinrest.application

import dano.kotlin.kontlinrest.domain.dao.OrderDao
import dano.kotlin.kontlinrest.domain.model.entities.Order
import dano.kotlin.kontlinrest.domain.model.enums.OrderState
import dano.kotlin.kontlinrest.domain.model.exceptions.InvalidOrderException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderExistsException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderNotFoundException
import dano.kotlin.kontlinrest.domain.model.exceptions.OrderStateException
import dano.kotlin.kontlinrest.domain.model.valueobjects.OrderItem
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class OrderServiceTest {

    @Mock
    private lateinit var dao: OrderDao

    @InjectMocks
    private lateinit var orderService: OrderService

    @Test
    fun `createOrder creates new order if order is not empty and doesn't already exist`() {
        // setup
        val order = Order(id = 1, items = listOf(OrderItem(name = "Item1", count = 3)))

        `when`(dao.save(any(Order::class.java))).thenAnswer(returnsFirstArg<Order>())

        //run
        val result = orderService.createOrder(order)

        // verify
        assertThat(result).isEqualTo(order.copy(state = OrderState.NEW))
    }

    @Test
    fun `createOrder throws exception if order is empty`() {
        // setup
        val order = Order(id = 1, items = emptyList())

        //run & verify
        assertThatThrownBy { orderService.createOrder(order) }
                .isInstanceOf(InvalidOrderException::class.java)
    }

    @Test
    fun `createOrder throws exception if order already exist`() {
        // setup
        val order = Order(id = 1, items = listOf(OrderItem(name = "Item1", count = 3)))

        `when`(dao.findById(order.id)).thenReturn(order)

        //run & verify
        assertThatThrownBy { orderService.createOrder(order) }
                .isInstanceOf(OrderExistsException::class.java)
    }

    @Test
    fun `updateOrder creates non empty order if doesn't exist`() {
        // setup
        val order = Order(id = 5, items = listOf(OrderItem(name = "Item1", count = 7)))

        `when`(dao.save(any(Order::class.java))).thenAnswer(returnsFirstArg<Order>())

        //run
        val result = orderService.updateOrder(order)

        // verify
        assertThat(result).isEqualTo(order.copy(state = OrderState.NEW))
    }

    @Test
    fun `updateOrder updates non empty order if existing order is in state NEW`() {
        // setup
        val order = Order(id = 5, items = listOf(OrderItem(name = "Item1", count = 7)))

        `when`(dao.findById(order.id))
                .thenReturn(Order(id = 5, items = listOf(OrderItem(name = "Item2", count = 5))))
        `when`(dao.save(any(Order::class.java))).thenAnswer(returnsFirstArg<Order>())

        //run
        val result = orderService.updateOrder(order)

        // verify
        assertThat(result).isEqualTo(order.copy(state = OrderState.NEW))
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, value = OrderState::class, names = ["NEW"])
    fun `updateOrder throws exception if existing order is in other state than NEW`(state: OrderState) {
        // setup
        val order = Order(id = 5, items = listOf(OrderItem(name = "Item1", count = 7)))

        `when`(dao.findById(order.id))
                .thenReturn(Order(id = 5, state = state, items = listOf(OrderItem(name = "Item2", count = 5))))

        //run & verify
        assertThatThrownBy { orderService.updateOrder(order) }
                .isInstanceOf(OrderStateException::class.java)
    }

    @Test
    fun `getOrder returns order if exist`() {
        // setup
        val expected = Order(id = 5, items = listOf(OrderItem(name = "Item2", count = 5)))
        `when`(dao.findById(expected.id)).thenReturn(expected)

        // run
        val result = orderService.getOrder(expected.id)

        // verify
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getOrder throws exception if order doesn't exist`() {
        // setup

        // run & verify
        assertThatThrownBy { orderService.getOrder(42) }
                .isInstanceOf(OrderNotFoundException::class.java)
    }

}

fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
