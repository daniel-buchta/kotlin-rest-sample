package dano.kotlin.kontlinrest.adapters.persistence

import dano.kotlin.kontlinrest.adapters.persistence.model.OrderJpa
import dano.kotlin.kontlinrest.application.any
import dano.kotlin.kontlinrest.domain.model.entities.Order
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class JpaOrderDaoTest {

    @Mock
    private lateinit var repository: OrderRepository

    @InjectMocks
    private lateinit var dao: JpaOrderDao

    @Test
    fun save() {
        val order = Order(id = 1)
        `when`(repository.save(any(OrderJpa::class.java))).thenAnswer(returnsFirstArg<Order>())
        assertThat(dao.save(order)).isEqualTo(order)
    }

    @Test
    fun `findById returns null if order not found`() {
        assertThat(dao.findById(42)).isNull()
    }
}
