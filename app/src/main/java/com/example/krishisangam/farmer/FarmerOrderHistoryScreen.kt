package com.example.krishisangam.farmer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

private val OrderHistoryPrimaryGreen = Color(0xFF01AC66)
private val OrderHistoryBackgroundColor = Color(0xFF003D22)
private val OrderHistoryDeepGreen = Color(0xFF002514)
private val OrderHistoryDarkGreen = Color(0xFF005C32)
private val OrderHistoryAccentYellow = Color(0xFFFFC107)
private val OrderHistoryTextLight = Color(0xFFF5FFF9)
private val OrderHistoryTextMuted = Color(0xFFB9D8C7)
private val OrderHistoryGlassCard = Color.White.copy(alpha = 0.105f)
private val OrderHistoryBorderGlass = Color.White.copy(alpha = 0.16f)
private val OrderHistorySoftGreen = Color(0xFF01AC66).copy(alpha = 0.18f)
private val OrderHistorySoftYellow = Color(0xFFFFC107).copy(alpha = 0.18f)
private val OrderHistorySoftRed = Color(0xFFFF6B6B).copy(alpha = 0.18f)
private val OrderHistoryRedText = Color(0xFFFF6B6B)

private data class FarmerOrderHistoryItem(
    val orderId: String,
    val productName: String,
    val buyerName: String,
    val dateText: String,
    val totalAmount: Int,
    val advanceAmount: Int,
    val remainingAmount: Int,
    val orderStatus: String,
    val paymentStatus: String
)

@Composable
fun FarmerOrderHistoryScreen(
    onBackClick: () -> Unit
) {
    var isLoading by remember {
        mutableStateOf(true)
    }

    var orders by remember {
        mutableStateOf<List<FarmerOrderHistoryItem>>(emptyList())
    }

    val farmerId = FirebaseAuth.getInstance().currentUser?.uid
    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    DisposableEffect(farmerId) {
        if (farmerId.isNullOrBlank()) {
            isLoading = false
            orders = emptyList()
            return@DisposableEffect onDispose { }
        }

        val listener = firestore
            .collection("orders")
            .whereEqualTo("farmerId", farmerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    isLoading = false
                    orders = emptyList()
                    return@addSnapshotListener
                }

                orders = snapshot.documents.map { document ->
                    val advanceAmount = document.getDoubleValueSafely(
                        keys = listOf(
                            "farmerAdvanceAmount",
                            "advanceAmountPaidByBuyer",
                            "advanceAmount"
                        )
                    )

                    val remainingAmount = document.getDoubleValueSafely(
                        keys = listOf(
                            "farmerRemainingAmount",
                            "remainingAmountFromBuyer",
                            "remainingAmount"
                        )
                    )

                    val totalAmount = document.getDoubleValueSafely(
                        keys = listOf(
                            "totalAmount",
                            "subtotal"
                        )
                    ).let { total ->
                        if (total > 0.0) {
                            total
                        } else {
                            advanceAmount + remainingAmount
                        }
                    }

                    FarmerOrderHistoryItem(
                        orderId = document.getOrderIdText(),
                        productName = document.getString("productName")
                            ?: document.getString("name")
                            ?: "Product",
                        buyerName = document.getString("buyerName")
                            ?: "Buyer",
                        dateText = document.getReadableDate(),
                        totalAmount = totalAmount.roundToInt(),
                        advanceAmount = advanceAmount.roundToInt(),
                        remainingAmount = remainingAmount.roundToInt(),
                        orderStatus = document.getString("orderStatus") ?: "order_placed",
                        paymentStatus = document.getString("paymentStatus") ?: "pending"
                    )
                }

                isLoading = false
            }

        onDispose {
            listener.remove()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        OrderHistoryDarkGreen,
                        OrderHistoryBackgroundColor,
                        OrderHistoryDeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(OrderHistoryAccentYellow.copy(alpha = 0.07f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomStart)
                .background(OrderHistoryPrimaryGreen.copy(alpha = 0.10f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .padding(bottom = 110.dp)
        ) {
            FarmerOrderHistoryTopBar(
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isLoading -> {
                    FarmerOrderHistoryEmptyCard(
                        icon = "⏳",
                        title = "Loading orders...",
                        message = "Please wait while we fetch your order history."
                    )
                }

                orders.isEmpty() -> {
                    FarmerOrderHistoryEmptyCard(
                        icon = "📦",
                        title = "No order history yet",
                        message = "Orders placed by buyers for your approved products will appear here."
                    )
                }

                else -> {
                    orders.forEach { order ->
                        FarmerOrderHistoryCard(
                            order = order
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FarmerOrderHistoryTopBar(
    onBackClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = OrderHistoryBorderGlass
                    ),
                    shape = CircleShape
                )
                .clickable {
                    onBackClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrderHistoryTextLight
            )
        }

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = "Order History",
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrderHistoryTextLight
            )

            Text(
                text = "View all buyer orders and payment progress",
                fontSize = 13.sp,
                color = OrderHistoryTextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun FarmerOrderHistoryCard(
    order: FarmerOrderHistoryItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = OrderHistoryGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = OrderHistoryBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = order.productName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = OrderHistoryTextLight
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${order.orderId} • Buyer: ${order.buyerName}",
                        fontSize = 12.sp,
                        color = OrderHistoryTextMuted,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = order.dateText,
                        fontSize = 12.sp,
                        color = OrderHistoryTextMuted
                    )
                }

                FarmerOrderStatusBadge(
                    orderStatus = order.orderStatus,
                    paymentStatus = order.paymentStatus
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FarmerOrderAmountBlock(
                    title = "Total",
                    value = "₹${order.totalAmount}"
                )

                FarmerOrderAmountBlock(
                    title = "1st Half",
                    value = "₹${order.advanceAmount}"
                )

                FarmerOrderAmountBlock(
                    title = "2nd Half",
                    value = "₹${order.remainingAmount}"
                )
            }
        }
    }
}

@Composable
private fun FarmerOrderStatusBadge(
    orderStatus: String,
    paymentStatus: String
) {
    val statusText = when {
        paymentStatus.equals("fully_paid", ignoreCase = true) -> "Completed"
        paymentStatus.equals("advance_paid", ignoreCase = true) -> "1st Half Done"
        orderStatus.equals("delivered", ignoreCase = true) -> "Delivered"
        orderStatus.equals("cancelled", ignoreCase = true) -> "Cancelled"
        else -> "Active"
    }

    val textColor = when (statusText) {
        "Completed", "Delivered" -> OrderHistoryPrimaryGreen
        "Cancelled" -> OrderHistoryRedText
        else -> OrderHistoryAccentYellow
    }

    val bgColor = when (statusText) {
        "Completed", "Delivered" -> OrderHistorySoftGreen
        "Cancelled" -> OrderHistorySoftRed
        else -> OrderHistorySoftYellow
    }

    Text(
        text = statusText,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        color = textColor,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = textColor.copy(alpha = 0.24f)
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}

@Composable
private fun FarmerOrderAmountBlock(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            fontSize = 12.sp,
            color = OrderHistoryTextMuted,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 16.sp,
            color = OrderHistoryTextLight,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun FarmerOrderHistoryEmptyCard(
    icon: String,
    title: String,
    message: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = OrderHistoryGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = OrderHistoryBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 42.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrderHistoryTextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = message,
                fontSize = 13.sp,
                color = OrderHistoryTextMuted,
                lineHeight = 19.sp
            )
        }
    }
}

private fun DocumentSnapshot.getOrderIdText(): String {
    val orderId = getString("orderId") ?: id.takeLast(6).uppercase()

    return if (orderId.startsWith("#")) {
        orderId
    } else {
        "#$orderId"
    }
}

private fun DocumentSnapshot.getReadableDate(): String {
    val rawCreatedAt = get("createdAt")

    return when (rawCreatedAt) {
        is Timestamp -> {
            try {
                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                formatter.format(rawCreatedAt.toDate())
            } catch (exception: Exception) {
                "Today"
            }
        }

        is Number -> {
            try {
                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                formatter.format(rawCreatedAt.toLong())
            } catch (exception: Exception) {
                "Today"
            }
        }

        is String -> {
            rawCreatedAt.ifBlank {
                "Today"
            }
        }

        else -> "Today"
    }
}

private fun DocumentSnapshot.getDoubleValueSafely(
    keys: List<String>
): Double {
    keys.forEach { key ->
        val value = get(key)

        when (value) {
            is Number -> return value.toDouble()
            is String -> {
                val cleanedValue = value
                    .replace("₹", "")
                    .replace("/Kg", "")
                    .replace("/kg", "")
                    .replace("Kg", "")
                    .replace("kg", "")
                    .replace(",", "")
                    .trim()

                cleanedValue.toDoubleOrNull()?.let {
                    return it
                }
            }
        }
    }

    return 0.0
}