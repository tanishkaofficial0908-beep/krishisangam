package com.example.krishisangam

data class ProductModel(
    val productId: String = "",
    val name: String = "",
    val category: String = "",
    val emoji: String = "",
    val quantity: String = "",
    val cropYear: String = "",
    val description: String = "",
    val location: String = "",
    val price: String = "",

    // Old fields kept so old buyer/manager screens do not break
    val packagingCharge: String = "",
    val logisticsCharge: String = "",

    // New pricing intelligence fields
    val mandiPrice: String = "",
    val farmerExpectedPrice: String = "",
    val krishiSangamPrice: String = "",
    val priceDifferenceFromMandi: String = "",
    val priceDifferenceFromSuggested: String = "",
    val profitGainPercent: String = "",
    val fairnessScore: Int = 0,
    val priceFlag: String = "",

    val farmerId: String = "",
    val farmerName: String = "",
    val farmerEmail: String = "",
    val trustScore: Int = 70,
    val status: String = "pending",
    val createdAt: Long = 0L,
    val approvedAt: Long? = null,
    val approvedBy: String = ""
)