package com.example.krishisangam.buyer

import androidx.compose.runtime.mutableStateListOf
import com.example.krishisangam.ProductModel

object BuyerWishlistStore {

    val wishlistProducts = mutableStateListOf<ProductModel>()

    private fun getProductKey(product: ProductModel): String {
        return product.productId.ifBlank {
            "${product.name}-${product.category}-${product.price}"
        }
    }

    fun isWishlisted(product: ProductModel): Boolean {
        val productKey = getProductKey(product)

        return wishlistProducts.any { wishlistProduct ->
            getProductKey(wishlistProduct) == productKey
        }
    }

    fun toggleWishlist(product: ProductModel) {
        val productKey = getProductKey(product)

        val existingProduct = wishlistProducts.firstOrNull { wishlistProduct ->
            getProductKey(wishlistProduct) == productKey
        }

        if (existingProduct != null) {
            wishlistProducts.remove(existingProduct)
        } else {
            wishlistProducts.add(product)
        }
    }

    fun removeFromWishlist(product: ProductModel) {
        val productKey = getProductKey(product)

        wishlistProducts.removeAll { wishlistProduct ->
            getProductKey(wishlistProduct) == productKey
        }
    }

    fun clearWishlist() {
        wishlistProducts.clear()
    }
}