package dano.kotlin.kontlinrest.adapters.persistence.model

import dano.kotlin.kontlinrest.domain.model.entities.Order
import javax.persistence.*

@Entity(name = "Order")
@Table(name = "`order`")
data class OrderJpa(

        @Id
        @Column(name = "id_order", nullable = false)
        val id: Long = -1,

        @JoinColumn(name = "id_order", referencedColumnName = "id_order", nullable = false)
        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
        val items: List<OrderItemJpa> = mutableListOf()
) {
    constructor(order: Order) : this(id = order.id, items = order.items.map { OrderItemJpa(it) })

    fun toOrder(): Order = Order(id = this.id, items = this.items.map { it.toOrderItem() })
}
