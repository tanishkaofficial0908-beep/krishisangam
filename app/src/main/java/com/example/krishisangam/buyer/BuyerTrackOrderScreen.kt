package com.example.krishisangam.buyer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

private val TrackPrimaryGreen = Color(0xFF01AC66)
private val TrackBackground = Color(0xFF003D22)
private val TrackDeepGreen = Color(0xFF002514)
private val TrackDarkGreen = Color(0xFF005C32)
private val TrackAccentYellow = Color(0xFFFFC107)
private val TrackTextLight = Color(0xFFF5FFF9)
private val TrackTextMuted = Color(0xFFB9D8C7)
private val TrackGlassCard = Color.White.copy(alpha = 0.105f)
private val TrackBorderGlass = Color.White.copy(alpha = 0.16f)

data class BuyerTrackingState(
    val orderStatus: String = "order_placed",
    val paymentStatus: String = "advance_paid",
    val trackingStep: Int = 0,
    val nodeVerificationDone: Boolean = false,
    val qualityCheckDone: Boolean = false,
    val packagingDone: Boolean = false,
    val dispatchPlanningDone: Boolean = false,
    val outForDeliveryDone: Boolean = false,
    val deliveredDone: Boolean = false
)

@Composable
fun BuyerTrackOrderScreen(
    order: BuyerConfirmedOrder,
    onBackClick: () -> Unit
) {
    var trackingState by remember {
        mutableStateOf(
            BuyerTrackingState(
                orderStatus = order.orderStatus,
                paymentStatus = order.paymentStatus,
                trackingStep = buyerOrderStatusToStep(order.orderStatus)
            )
        )
    }

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    DisposableEffect(order.orderId) {
        val cleanOrderId = order.orderId.removePrefix("#")

        val listener = firestore
            .collection("orders")
            .whereEqualTo("orderId", order.orderId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || snapshot.documents.isEmpty()) {
                    firestore
                        .collection("orders")
                        .whereEqualTo("orderId", cleanOrderId)
                        .get()
                        .addOnSuccessListener { fallbackSnapshot ->
                            val document = fallbackSnapshot.documents.firstOrNull()

                            if (document != null) {
                                trackingState = document.toBuyerTrackingState(
                                    fallbackOrderStatus = order.orderStatus,
                                    fallbackPaymentStatus = order.paymentStatus
                                )
                            }
                        }

                    return@addSnapshotListener
                }

                val document = snapshot.documents.first()

                trackingState = document.toBuyerTrackingState(
                    fallbackOrderStatus = order.orderStatus,
                    fallbackPaymentStatus = order.paymentStatus
                )
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
                        TrackDarkGreen,
                        TrackBackground,
                        TrackDeepGreen
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(top = 18.dp, bottom = 120.dp)
        ) {
            TrackTopBar(
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            OrderSummaryCard(order = order)

            Spacer(modifier = Modifier.height(16.dp))

            TrackTimelineCard(
                order = order,
                trackingState = trackingState
            )
        }
    }
}

@Composable
fun TrackTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = TrackBorderGlass
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
                fontWeight = FontWeight.Bold,
                color = TrackTextLight
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Track Order",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TrackTextLight
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Live order progress from Agro Node to delivery",
                fontSize = 13.sp,
                color = TrackTextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun OrderSummaryCard(
    order: BuyerConfirmedOrder
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = TrackGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = TrackBorderGlass
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Order ID",
                color = TrackTextMuted,
                fontSize = 13.sp
            )

            Text(
                text = order.orderId,
                color = TrackAccentYellow,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Items",
                color = TrackTextLight,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            order.items.forEach { item ->
                Text(
                    text = "${item.emoji} ${item.name}  × ${item.quantity}",
                    color = TrackTextMuted,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = Color.White.copy(alpha = 0.15f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            TrackAmountRow(
                title = "Total Amount",
                value = "₹${order.totalAmount}"
            )

            TrackAmountRow(
                title = "Advance Paid",
                value = "₹${order.advanceAmount}"
            )

            TrackAmountRow(
                title = "Remaining",
                value = "₹${order.remainingAmount}"
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = order.deliveryEstimate,
                color = TrackAccentYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun TrackAmountRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = TrackTextMuted,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            color = TrackTextLight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TrackTimelineCard(
    order: BuyerConfirmedOrder,
    trackingState: BuyerTrackingState
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = TrackGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = TrackBorderGlass
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Order Progress",
                color = TrackTextLight,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            TrackStepRow(
                title = "Order Placed",
                subtitle = "50% advance payment received.",
                isCompleted = true,
                updatedBy = "Updated automatically after payment"
            )

            TrackStepRow(
                title = "Agro Node Verification",
                subtitle = "Agro Node manager verified product availability.",
                isCompleted = trackingState.nodeVerificationDone || trackingState.trackingStep >= 1,
                updatedBy = "Updated by Agro Node Manager"
            )

            TrackStepRow(
                title = "Quality Check",
                subtitle = "Quality and quantity check completed.",
                isCompleted = trackingState.qualityCheckDone || trackingState.trackingStep >= 2,
                updatedBy = "Updated by Agro Node Manager"
            )

            TrackStepRow(
                title = "Packaging",
                subtitle = "Basic packaging completed at Agro Node.",
                isCompleted = trackingState.packagingDone || trackingState.trackingStep >= 3,
                updatedBy = "Updated by Agro Node Manager"
            )

            TrackStepRow(
                title = "Dispatch Planning",
                subtitle = "Truck capacity and delivery route planned.",
                isCompleted = trackingState.dispatchPlanningDone || trackingState.trackingStep >= 4,
                updatedBy = "Updated by Agro Node Manager"
            )

            TrackStepRow(
                title = "Out for Delivery",
                subtitle = "Delivery partner has started the delivery.",
                isCompleted = trackingState.outForDeliveryDone || trackingState.trackingStep >= 5,
                updatedBy = "Updated by Delivery Partner"
            )

            TrackStepRow(
                title = "Delivered",
                subtitle = "Order delivered to buyer location.",
                isCompleted = trackingState.deliveredDone || trackingState.trackingStep >= 6,
                updatedBy = "Updated by Delivery Partner"
            )

            TrackStepRow(
                title = "Remaining Payment Pending",
                subtitle = "Buyer will pay remaining 50% after delivery.",
                isCompleted = trackingState.paymentStatus == "fully_paid",
                updatedBy = "Updated after buyer pays remaining amount"
            )

            TrackStepRow(
                title = "Completed",
                subtitle = "Order and payment completed.",
                isCompleted = (
                        trackingState.orderStatus == "completed" ||
                                trackingState.orderStatus == "delivered"
                        ) && trackingState.paymentStatus == "fully_paid",
                updatedBy = "Updated automatically after final payment"
            )
        }
    }
}

@Composable
fun TrackStepRow(
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    updatedBy: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 18.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) {
                        TrackPrimaryGreen
                    } else {
                        Color.White.copy(alpha = 0.12f)
                    }
                )
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isCompleted) {
                            TrackPrimaryGreen.copy(alpha = 0.55f)
                        } else {
                            TrackBorderGlass
                        }
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isCompleted) "✓" else "•",
                color = if (isCompleted) Color.White else TrackTextMuted,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                color = if (isCompleted) TrackTextLight else TrackTextMuted,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                color = TrackTextMuted,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = updatedBy,
                color = TrackAccentYellow,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun DocumentSnapshot.toBuyerTrackingState(
    fallbackOrderStatus: String,
    fallbackPaymentStatus: String
): BuyerTrackingState {
    val orderStatus = getString("orderStatus") ?: fallbackOrderStatus
    val trackingStep = getLong("trackingStep")?.toInt() ?: buyerOrderStatusToStep(orderStatus)

    return BuyerTrackingState(
        orderStatus = orderStatus,
        paymentStatus = getString("paymentStatus") ?: fallbackPaymentStatus,
        trackingStep = trackingStep,
        nodeVerificationDone = getBoolean("nodeVerificationDone") ?: (trackingStep >= 1),
        qualityCheckDone = getBoolean("qualityCheckDone") ?: (trackingStep >= 2),
        packagingDone = getBoolean("packagingDone") ?: (trackingStep >= 3),
        dispatchPlanningDone = getBoolean("dispatchPlanningDone") ?: (trackingStep >= 4),
        outForDeliveryDone = getBoolean("outForDeliveryDone") ?: (trackingStep >= 5),
        deliveredDone = getBoolean("deliveredDone") ?: (trackingStep >= 6)
    )
}

private fun buyerOrderStatusToStep(
    orderStatus: String
): Int {
    return when {
        orderStatus.equals("agro_node_verified", ignoreCase = true) -> 1
        orderStatus.equals("quality_checked", ignoreCase = true) -> 2
        orderStatus.equals("packaging", ignoreCase = true) -> 3
        orderStatus.equals("dispatch_planned", ignoreCase = true) -> 4
        orderStatus.equals("out_for_delivery", ignoreCase = true) -> 5
        orderStatus.equals("delivered", ignoreCase = true) -> 6
        orderStatus.equals("completed", ignoreCase = true) -> 6
        else -> 0
    }
}