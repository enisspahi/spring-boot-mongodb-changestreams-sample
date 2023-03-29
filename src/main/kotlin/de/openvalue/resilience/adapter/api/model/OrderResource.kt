package de.openvalue.resilience.adapter.api.model

data class OrderResource(val email: String, val items: List<OrderItem>, val id: String? = null) {
    data class OrderItem(val name: String, val count: Int)
}