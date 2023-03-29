package de.openvalue.resilience.adapter.repository

import de.openvalue.resilience.domain.OrderEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: CoroutineCrudRepository<OrderEntity, String>
