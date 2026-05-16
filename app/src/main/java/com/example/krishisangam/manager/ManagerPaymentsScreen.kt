package com.example.krishisangam.manager

import androidx.compose.foundation.background
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftYellow = Color(0xFFFFF4D6)
private val YellowText = Color(0xFFB8860B)

data class ManagerPaymentItem(
    val orderId: String,
    val buyerName: String,
    val farmerName: String,
    val productName: String,
    val totalAmount: String,
    val firstHalf: String,
    val secondHalf: String,
    val status: String
)

@Composable
fun ManagerPaymentsScreen() {
    val payments = listOf(
        ManagerPaymentItem(
            orderId = "#KS1234",
            buyerName = "Amit Buyer",
            farmerName = "Ramesh Patel",
            productName = "Wheat",
            totalAmount = "₹4,300",
            firstHalf = "₹2,150 Paid",
            secondHalf = "₹2,150 Pending",
            status = "Delivery Pending"
        ),
        ManagerPaymentItem(
            orderId = "#KS1235",
            buyerName = "Neha Buyer",
            farmerName = "Shreya Gupta",
            productName = "Rice",
            totalAmount = "₹6,400",
            firstHalf = "₹3,200 Paid",
            secondHalf = "₹3,200 Released",
            status = "Completed"
        ),
        ManagerPaymentItem(
            orderId = "#KS1236",
            buyerName = "Rahul Buyer",
            farmerName = "Mahesh Sahu",
            productName = "Tomato",
            totalAmount = "₹2,400",
            firstHalf = "₹1,200 Paid",
            secondHalf = "₹1,200 Pending",
            status = "In Transit"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Text(
            text = "Payment Management",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Track split payments and farmer payouts",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentStatCard(
                title = "Total Collected",
                value = "₹13,100",
                icon = "📈",
                modifier = Modifier.weight(1f)
            )

            PaymentStatCard(
                title = "Pending Release",
                value = "₹4,550",
                icon = "⏳",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentStatCard(
                title = "Total Orders",
                value = "3",
                icon = "📦",
                modifier = Modifier.weight(1f)
            )

            PaymentStatCard(
                title = "Completed",
                value = "1",
                icon = "✅",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Payment Breakdown",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        payments.forEach { payment ->
            ManagerPaymentCard(payment = payment)
            Spacer(modifier = Modifier.height(14.dp))
        }

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun PaymentStatCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(115.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(LightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 17.sp)
                }
            }

            Text(
                text = value,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }
    }
}

@Composable
fun ManagerPaymentCard(
    payment: ManagerPaymentItem
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(LightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "💳", fontSize = 24.sp)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = "${payment.orderId} • ${payment.productName}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = "Buyer: ${payment.buyerName}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "Farmer: ${payment.farmerName}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = payment.totalAmount,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = Color(0xFFEDEDED))

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PaymentSplitBox(
                    title = "1st Half",
                    value = payment.firstHalf
                )

                PaymentSplitBox(
                    title = "2nd Half",
                    value = payment.secondHalf
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = payment.status,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (payment.status == "Completed") PrimaryGreen else YellowText,
                modifier = Modifier
                    .background(
                        if (payment.status == "Completed") LightGreen else SoftYellow,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
fun PaymentSplitBox(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            fontSize = 11.sp,
            color = Color.Gray
        )

        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
    }
}