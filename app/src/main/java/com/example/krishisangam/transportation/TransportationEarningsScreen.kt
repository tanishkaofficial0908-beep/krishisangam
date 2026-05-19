package com.example.krishisangam.transportation

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

private const val TRANSPORT_PAYMENT_COMPLETED = "Completed"
private const val TRANSPORT_PAYMENT_PENDING = "Pending"

data class TransportationPayment(
    val tripId: String,
    val orderId: String,
    val productName: String,
    val route: String,
    val distance: String,
    val amount: String,
    val status: String,
    val date: String
)

data class TransportationEarningInfo(
    val title: String,
    val emoji: String,
    val message: String
)

@Composable
fun TransportationEarningsScreen() {
    var selectedPayment by remember {
        mutableStateOf<TransportationPayment?>(null)
    }

    var selectedInfo by remember {
        mutableStateOf<TransportationEarningInfo?>(null)
    }

    val payments = listOf(
        TransportationPayment(
            tripId = "#TRP1019",
            orderId = "#ORD2197",
            productName = "Potato",
            route = "Raisen Agro Node → Bhopal Community Hub",
            distance = "37 km",
            amount = "₹1,050",
            status = TRANSPORT_PAYMENT_COMPLETED,
            date = "12 May 2026"
        ),
        TransportationPayment(
            tripId = "#TRP1020",
            orderId = "#ORD2198",
            productName = "Wheat",
            route = "Bhopal Agro Node → Bhopal Market Hub",
            distance = "18 km",
            amount = "₹650",
            status = TRANSPORT_PAYMENT_COMPLETED,
            date = "11 May 2026"
        ),
        TransportationPayment(
            tripId = "#TRP1022",
            orderId = "#ORD2202",
            productName = "Rice",
            route = "Sehore Node → Community Drop Point",
            distance = "32 km",
            amount = "₹920",
            status = TRANSPORT_PAYMENT_PENDING,
            date = "Pending release"
        ),
        TransportationPayment(
            tripId = "#TRP1023",
            orderId = "#ORD2203",
            productName = "Tomato",
            route = "Bhopal Agro Node → Raisen Drop Point",
            distance = "41 km",
            amount = "₹1,150",
            status = TRANSPORT_PAYMENT_PENDING,
            date = "After delivery confirmation"
        )
    )

    if (selectedPayment != null) {
        TransportationPaymentDetailDialog(
            payment = selectedPayment!!,
            onDismiss = {
                selectedPayment = null
            }
        )
    }

    if (selectedInfo != null) {
        TransportationEarningInfoDialog(
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
                text = "Transport Earnings",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Track delivery income, pending payouts and completed trip payments",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TransportationEarningStatCard(
                    title = "Total Earnings",
                    value = "₹8,750",
                    subtitle = "All completed trips",
                    icon = "💼",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedInfo = TransportationEarningInfo(
                            title = "Total Earnings",
                            emoji = "💼",
                            message = "This shows total income earned from completed transport trips. Later it will be calculated from real trip payments."
                        )
                    }
                )

                TransportationEarningStatCard(
                    title = "Pending Pay",
                    value = "₹2,070",
                    subtitle = "To be released",
                    icon = "🕒",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedInfo = TransportationEarningInfo(
                            title = "Pending Payments",
                            emoji = "🕒",
                            message = "These payments are pending because delivery confirmation or manager payout approval is not completed yet."
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TransportationEarningStatCard(
                    title = "Completed Trips",
                    value = "18",
                    subtitle = "Delivered orders",
                    icon = "✅",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedInfo = TransportationEarningInfo(
                            title = "Completed Trips",
                            emoji = "✅",
                            message = "Completed trips are deliveries marked as delivered successfully."
                        )
                    }
                )

                TransportationEarningStatCard(
                    title = "This Month",
                    value = "₹4,320",
                    subtitle = "Monthly earning",
                    icon = "📈",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectedInfo = TransportationEarningInfo(
                            title = "This Month",
                            emoji = "📈",
                            message = "This month value is demo data right now. Later it will show current month earnings from Firebase."
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            TransportationPaymentNoteCard()

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Payment History",
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(12.dp))

            payments.forEach { payment ->
                TransportationPaymentCard(
                    payment = payment,
                    onClick = {
                        selectedPayment = payment
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun TransportationEarningStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(142.dp)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                        .size(36.dp)
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
                        fontSize = 18.sp
                    )
                }
            }

            Text(
                text = value,
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 2,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun TransportationPaymentNoteCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            ),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(AccentYellow.copy(alpha = 0.16f))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = AccentYellow.copy(alpha = 0.24f)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💳",
                    fontSize = 25.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = "Payment Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Payments are released after delivery confirmation by Agro Node Manager.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun TransportationPaymentCard(
    payment: TransportationPayment,
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
                        .size(54.dp)
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
                        text = "🚚",
                        fontSize = 24.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = "${payment.tripId} • ${payment.productName}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Text(
                        text = payment.orderId,
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Text(
                        text = payment.date,
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                Text(
                    text = payment.amount,
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = payment.route,
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "Distance: ${payment.distance}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                TransportationPaymentStatusBadge(
                    status = payment.status
                )
            }
        }
    }
}

@Composable
fun TransportationPaymentStatusBadge(
    status: String
) {
    val isCompleted = status == TRANSPORT_PAYMENT_COMPLETED

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
        fontSize = 10.sp,
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
            .padding(horizontal = 9.dp, vertical = 5.dp),
        maxLines = 1
    )
}

@Composable
fun TransportationPaymentDetailDialog(
    payment: TransportationPayment,
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
                text = payment.tripId,
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
                    text = "Order ID: ${payment.orderId}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Route: ${payment.route}",
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Distance: ${payment.distance}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Amount: ${payment.amount}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Status: ${payment.status}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Date: ${payment.date}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Real payment data will be connected later with Firebase/payment backend.",
                    color = TextMuted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    )
}

@Composable
fun TransportationEarningInfoDialog(
    info: TransportationEarningInfo,
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