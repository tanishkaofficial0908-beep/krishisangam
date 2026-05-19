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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TrackPrimaryGreen = Color(0xFF01AC66)
private val TrackBackground = Color(0xFF003D22)
private val TrackDeepGreen = Color(0xFF002514)
private val TrackDarkGreen = Color(0xFF005C32)
private val TrackAccentYellow = Color(0xFFFFC107)
private val TrackTextLight = Color(0xFFF5FFF9)
private val TrackTextMuted = Color(0xFFB9D8C7)
private val TrackGlassCard = Color.White.copy(alpha = 0.105f)
private val TrackBorderGlass = Color.White.copy(alpha = 0.16f)

@Composable
fun BuyerTrackOrderScreen(
    order: BuyerConfirmedOrder,
    onBackClick: () -> Unit
) {
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

            TrackTimelineCard(order = order)
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
                text = "Order Progress",
                color = TrackTextLight,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            TrackStepRow(
                title = "Order Placed",
                subtitle = "50% advance payment received.",
                isCompleted = isStepCompleted(order.orderStatus, "order_placed"),
                updatedBy = "Updated automatically after payment"
            )

            TrackStepRow(
                title = "Agro Node Verification",
                subtitle = "Agro Node manager will verify product availability.",
                isCompleted = isStepCompleted(order.orderStatus, "agro_node_verified"),
                updatedBy = "Updated by Agro Node Manager"
            )

            TrackStepRow(
                title = "Quality Check & Packaging",
                subtitle = "Quality, quantity and basic packaging will be completed.",
                isCompleted = isStepCompleted(order.orderStatus, "quality_checked"),
                updatedBy = "Updated by Agro Node Manager"
            )

            TrackStepRow(
                title = "Dispatch Planning",
                subtitle = "Truck capacity and delivery route will be planned.",
                isCompleted = isStepCompleted(order.orderStatus, "dispatch_planned"),
                updatedBy = "Updated by Agro Node Manager"
            )

            TrackStepRow(
                title = "Out for Delivery",
                subtitle = "Delivery partner has started the delivery.",
                isCompleted = isStepCompleted(order.orderStatus, "out_for_delivery"),
                updatedBy = "Updated by Delivery Partner"
            )

            TrackStepRow(
                title = "Delivered",
                subtitle = "Order delivered to buyer location.",
                isCompleted = isStepCompleted(order.orderStatus, "delivered"),
                updatedBy = "Updated by Delivery Partner"
            )

            TrackStepRow(
                title = "Remaining Payment Pending",
                subtitle = "Buyer will pay remaining 50% after delivery.",
                isCompleted = order.paymentStatus == "fully_paid",
                updatedBy = "Updated after buyer pays remaining amount"
            )

            TrackStepRow(
                title = "Completed",
                subtitle = "Order and payment completed.",
                isCompleted = order.orderStatus == "completed" && order.paymentStatus == "fully_paid",
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

fun isStepCompleted(
    currentStatus: String,
    stepStatus: String
): Boolean {
    val statusOrder = listOf(
        "order_placed",
        "agro_node_verified",
        "quality_checked",
        "dispatch_planned",
        "out_for_delivery",
        "delivered",
        "completed"
    )

    val currentIndex = statusOrder.indexOf(currentStatus)
    val stepIndex = statusOrder.indexOf(stepStatus)

    return currentIndex >= stepIndex && currentIndex != -1 && stepIndex != -1
}