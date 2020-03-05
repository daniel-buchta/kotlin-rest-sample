package dano.kotlin.kontlinrest.adapters.persistence

import dano.kotlin.kontlinrest.adapters.persistence.model.OrderJpa
import dano.kotlin.kontlinrest.domain.model.entities.Order
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional


@ExtendWith(MockKExtension::class)
internal class JpaOrderDaoTest {

    @MockK
    private lateinit var repository: OrderRepository

    @InjectMockKs
    private lateinit var dao: JpaOrderDao

    @Test
    fun save() {
        val order = Order(id = 1)

        every { repository.save(any<OrderJpa>()) } returnsArgument 0
        assertThat(dao.save(order)).isEqualTo(order)
    }

    @Test
    fun `findById returns null if order not found`() {
        every { repository.findById(any()) } returns Optional.empty()
        assertThat(dao.findById(42)).isNull()
    }
}
