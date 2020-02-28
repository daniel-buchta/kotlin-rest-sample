package dano.kotlin.kontlinrest.adapters.persistence

import dano.kotlin.kontlinrest.adapters.persistence.model.OrderJpa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderJpa, Long>
