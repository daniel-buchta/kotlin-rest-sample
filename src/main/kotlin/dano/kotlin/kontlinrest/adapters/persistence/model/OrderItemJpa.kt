package dano.kotlin.kontlinrest.adapters.persistence.model

import dano.kotlin.kontlinrest.domain.model.valueobjects.OrderItem
import javax.persistence.*

@Entity(name = "OrderItem")
@Table(name = "order_item")
data class OrderItemJpa(

        @Id
        @Column(name = "id_order_item", nullable = false)
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Int? = null,

        @Column(nullable = false)
        val name: String = "",

        @Column(nullable = false)
        val count: Int = 0
) {

    constructor(item: OrderItem) : this(name = item.name, count = item.count)

    fun toOrderItem(): OrderItem = OrderItem(name = this.name, count = this.count)
}
