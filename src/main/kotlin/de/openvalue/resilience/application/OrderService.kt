package de.openvalue.resilience.application

import de.openvalue.resilience.adapter.repository.OrderRepository
import de.openvalue.resilience.domain.OrderEntity
import org.springframework.stereotype.Service

@Service
class OrderService(val orderRepository: OrderRepository) {

    suspend fun create(order: OrderEntity): OrderEntity = orderRepository.save(order)

}