package com.example.krishisangam.manager

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFF003D22)
private val DeepGreen = Color(0xFF002514)
private val DarkGreen = Color(0xFF005C32)
private val AccentYellow = Color(0xFFFFC107)
private val TextLight = Color(0xFFF5FFF9)
private val TextMuted = Color(0xFFB9D8C7)
private val GlassCard = Color.White.copy(alpha = 0.105f)
private val BorderGlass = Color.White.copy(alpha = 0.16f)
private val DialogGreen = Color(0xFF123D2B)
private val DialogText = Color(0xFFD8EDE3)
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val DividerGlass = Color.White.copy(alpha = 0.14f)

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

data class ManagerPaymentInfo(
    val title: String,
    val emoji: String,
    val message: String
)

@Composable
fun ManagerPaymentsScreen() {
    var selectedPayment by remember {
        mutableStateOf<ManagerPaymentItem?>(null)
    }

    var selectedInfo by remember {
        mutableStateOf<ManagerPaymentInfo?>(null)
    }

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

    if (selectedPayment != null) {
        ManagerPaymentDetailDialog(
            payment = selectedPayment!!,
            onDismiss = {
                selectedPayment = null
            }
        )
    }

    if (selectedInfo != null) {
        ManagerPaymentInfoDialog(
            info = selectedInfo!!,
            onDismiss = {
                selectedInfo = null
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        DarkGreen,
                        BackgroundColor,
                        DeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(AccentYellow.copy(alpha = 0.07f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomStart)
                .background(PrimaryGreen.copy(alpha = 0.10f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 22.dp)
                .padding(bottom = 110.dp)
        ) {
            Text(
                text = "Payment Management",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Track split payments and farmer payouts",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
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
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedInfo = ManagerPaymentInfo(
                            title = "Total Collected",
                            emoji = "📈",
                            message = "This shows total amount collected from buyers. Current value is static demo data and should later be calculated from real orders."
                        )
                    }
                )

                PaymentStatCard(
                    title = "Pending Release",
                    value = "₹4,550",
                    icon = "⏳",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedInfo = ManagerPaymentInfo(
                            title = "Pending Release",
                            emoji = "⏳",
                            message = "This shows second-half payments pending release to farmers after delivery confirmation."
                        )
                    }
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
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedInfo = ManagerPaymentInfo(
                            title = "Total Orders",
                            emoji = "📦",
                            message = "This shows total payment-linked orders. Current value is static demo data."
                        )
                    }
                )

                PaymentStatCard(
                    title = "Completed",
                    value = "1",
                    icon = "✅",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedInfo = ManagerPaymentInfo(
                            title = "Completed Payments",
                            emoji = "✅",
                            message = "This shows orders where the farmer payout is fully completed."
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Payment Breakdown",
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(12.dp))

            payments.forEach { payment ->
                ManagerPaymentCard(
                    payment = payment,
                    onClick = {
                        selectedPayment = payment
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun PaymentStatCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(122.dp)
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = GlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
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
                    fontWeight = FontWeight.ExtraBold,
                    color = TextMuted,
                    modifier = Modifier.weight(1f),
                    lineHeight = 15.sp
                )

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = BorderGlass
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = icon,
                        fontSize = 17.sp
                    )
                }
            }

            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )
        }
    }
}

@Composable
fun ManagerPaymentCard(
    payment: ManagerPaymentItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = GlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = BorderGlass
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "💳",
                        fontSize = 24.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = "${payment.orderId} • ${payment.productName}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Text(
                        text = "Buyer: ${payment.buyerName}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Text(
                        text = "Farmer: ${payment.farmerName}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                Text(
                    text = payment.totalAmount,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentYellow
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = DividerGlass)

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

            ManagerPaymentStatusBadge(
                status = payment.status
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
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextLight
        )
    }
}

@Composable
fun ManagerPaymentStatusBadge(
    status: String
) {
    val isCompleted = status.equals("Completed", ignoreCase = true)

    val bgColor = if (isCompleted) {
        SoftGreen
    } else {
        SoftYellow
    }

    val textColor = if (isCompleted) {
        PrimaryGreen
    } else {
        AccentYellow
    }

    Text(
        text = status,
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = textColor,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = textColor.copy(alpha = 0.24f)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp),
        maxLines = 1
    )
}

@Composable
fun ManagerPaymentDetailDialog(
    payment: ManagerPaymentItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        containerColor = DialogGreen,
        titleContentColor = TextLight,
        textContentColor = DialogText,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = "OK",
                    color = AccentYellow,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        title = {
            Text(
                text = payment.orderId,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "💳",
                    fontSize = 38.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Product: ${payment.productName}",
                    color = TextLight,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Buyer: ${payment.buyerName}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Farmer: ${payment.farmerName}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Total Amount: ${payment.totalAmount}",
                    color = AccentYellow,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "First Half: ${payment.firstHalf}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Second Half: ${payment.secondHalf}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Status: ${payment.status}",
                    color = if (payment.status.equals("Completed", ignoreCase = true)) {
                        PrimaryGreen
                    } else {
                        AccentYellow
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    )
}

@Composable
fun ManagerPaymentInfoDialog(
    info: ManagerPaymentInfo,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        containerColor = DialogGreen,
        titleContentColor = TextLight,
        textContentColor = DialogText,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = "OK",
                    color = AccentYellow,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        title = {
            Text(
                text = info.title,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = info.emoji,
                    fontSize = 36.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = info.message,
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    )
}