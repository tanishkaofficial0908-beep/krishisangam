package com.example.krishisangam.buyer

import androidx.compose.runtime.mutableStateListOf
import com.example.krishisangam.ProductModel

data class BuyerOrderItem(
    val id: String,
    val name: String,
    val type: String,
    val emoji: String,
    val price: String,
    val quantity: Int = 1,
    val status: String = "added_to_cart"
)

object BuyerOrderStore {

    val orders = mutableStateListOf<BuyerOrderItem>()

    fun addOrderFromProduct(
        product: ProductModel,
        fallbackProductName: String = "",
        fallbackCategoryName: String = "",
        fallbackPrice: String = "₹0"
    ) {
        val productId = product.productId.ifBlank {
            "${product.name}-${product.category}-${product.price}"
        }

        val existingIndex = orders.indexOfFirst { order ->
            order.id == productId
        }

        if (existingIndex != -1) {
            val oldItem = orders[existingIndex]
            orders[existingIndex] = oldItem.copy(
                quantity = oldItem.quantity + 1
            )
        } else {
            orders.add(
                BuyerOrderItem(
                    id = productId,
                    name = product.name.ifBlank {
                        fallbackProductName
                    },
                    type = product.category.ifBlank {
                        fallbackCategoryName
                    },
                    emoji = product.emoji.ifBlank {
                        "🌾"
                    },
                    price = product.price.ifBlank {
                        fallbackPrice
                    },
                    quantity = 1
                )
            )
        }
    }

    fun increaseQuantity(order: BuyerOrderItem) {
        val index = orders.indexOfFirst { item ->
            item.id == order.id
        }

        if (index != -1) {
            val oldItem = orders[index]
            orders[index] = oldItem.copy(
                quantity = oldItem.quantity + 1
            )
        }
    }

    fun decreaseQuantity(order: BuyerOrderItem) {
        val index = orders.indexOfFirst { item ->
            item.id == order.id
        }

        if (index != -1) {
            val oldItem = orders[index]

            if (oldItem.quantity > 1) {
                orders[index] = oldItem.copy(
                    quantity = oldItem.quantity - 1
                )
            } else {
                orders.removeAt(index)
            }
        }
    }

    fun removeOrder(order: BuyerOrderItem) {
        orders.removeAll { item ->
            item.id == order.id
        }
    }

    fun clearOrders() {
        orders.clear()
    }
}