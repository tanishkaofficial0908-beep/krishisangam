package com.example.krishisangam.buyer

import androidx.compose.runtime.mutableStateListOf
import kotlin.math.roundToInt

data class BuyerConfirmedOrder(
    val orderId: String,
    val items: List<BuyerOrderItem>,
    val totalAmount: Int,
    val advanceAmount: Int,
    val remainingAmount: Int,
    val paymentId: String,
    val orderStatus: String = "order_placed",
    val paymentStatus: String = "advance_paid",
    val deliveryEstimate: String = "Within 7 days after Agro Node verification"
)

object BuyerConfirmedOrderStore {

    val confirmedOrders = mutableStateListOf<BuyerConfirmedOrder>()

    fun addConfirmedOrder(
        items: List<BuyerOrderItem>,
        totalAmount: Double,
        advanceAmount: Double,
        remainingAmount: Double,
        paymentId: String
    ) {
        val orderId = "KS-${System.currentTimeMillis().toString().takeLast(6)}"

        confirmedOrders.add(
            index = 0,
            element = BuyerConfirmedOrder(
                orderId = orderId,
                items = items,
                totalAmount = totalAmount.roundToInt(),
                advanceAmount = advanceAmount.roundToInt(),
                remainingAmount = remainingAmount.roundToInt(),
                paymentId = paymentId,
                orderStatus = "order_placed",
                paymentStatus = "advance_paid"
            )
        )
    }
}