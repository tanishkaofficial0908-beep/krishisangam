package com.example.krishisangam.buyer

import androidx.compose.runtime.mutableStateOf

object BuyerPaymentStore {

    val paymentSuccessId = mutableStateOf<String?>(null)
    val paymentErrorMessage = mutableStateOf<String?>(null)

    fun markSuccess(paymentId: String?) {
        paymentSuccessId.value = paymentId
        paymentErrorMessage.value = null
    }

    fun markFailure(message: String?) {
        paymentErrorMessage.value = message ?: "Payment failed. Please try again."
        paymentSuccessId.value = null
    }

    fun clear() {
        paymentSuccessId.value = null
        paymentErrorMessage.value = null
    }
}