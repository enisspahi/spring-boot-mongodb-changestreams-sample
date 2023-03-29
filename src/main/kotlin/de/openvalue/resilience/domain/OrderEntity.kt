package de.openvalue.resilience.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("orders")
@TypeAlias("OrderEntity")
data class OrderEntity(val email: String,
                       val items: List<OrderItem>,
                       @Id val id: String = UUID.randomUUID().toString())

data class OrderItem(val name: String,
                     val count: Int,
                     @Id val id: String = UUID.randomUUID().toString())

