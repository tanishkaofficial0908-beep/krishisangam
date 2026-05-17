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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

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
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val DividerGlass = Color.White.copy(alpha = 0.14f)

private const val PAYMENT_STATUS_COMPLETED = "completed"
private const val PAYMENT_STATUS_PENDING_RELEASE = "pending_release"

data class FarmerPayment(
    val orderId: String,
    val productName: String,
    val buyerName: String,
    val totalAmount: String,
    val firstHalf: String,
    val secondHalf: String,
    val statusKey: String,
    val date: String
)

@Composable
fun FarmerEarningsScreen() {
    var selectedPayment by remember {
        mutableStateOf<FarmerPayment?>(null)
    }

    var showTrustDialog by remember {
        mutableStateOf(false)
    }

    val payments = listOf(
        FarmerPayment(
            orderId = "#KS1021",
            productName = stringResource(R.string.sharbati_wheat),
            buyerName = "Amit Traders",
            totalAmount = "₹4,300",
            firstHalf = "₹2,150",
            secondHalf = "₹2,150",
            statusKey = PAYMENT_STATUS_COMPLETED,
            date = "11 May 2026"
        ),
        FarmerPayment(
            orderId = "#KS1022",
            productName = stringResource(R.string.rice),
            buyerName = "Bhopal Food Hub",
            totalAmount = "₹3,200",
            firstHalf = "₹1,600",
            secondHalf = "₹1,600",
            statusKey = PAYMENT_STATUS_PENDING_RELEASE,
            date = "10 May 2026"
        ),
        FarmerPayment(
            orderId = "#KS1023",
            productName = stringResource(R.string.tomato),
            buyerName = "Fresh Basket",
            totalAmount = "₹1,760",
            firstHalf = "₹880",
            secondHalf = "₹880",
            statusKey = PAYMENT_STATUS_COMPLETED,
            date = "09 May 2026"
        )
    )

    val totalEarned = "₹6,060"
    val pendingRelease = "₹1,600"
    val completedOrders = 2
    val trustScore = 70

    if (selectedPayment != null) {
        PaymentDetailDialog(
            payment = selectedPayment!!,
            onDismiss = {
                selectedPayment = null
            }
        )
    }

    if (showTrustDialog) {
        AlertDialog(
            onDismissRequest = {
                showTrustDialog = false
            },
            containerColor = DialogGreen,
            titleContentColor = TextLight,
            textContentColor = DialogText,
            confirmButton = {
                TextButton(
                    onClick = {
                        showTrustDialog = false
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
                    text = stringResource(R.string.trust_score),
                    color = TextLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "📈",
                        fontSize = 36.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Current trust score: $trustScore/100",
                        color = AccentYellow,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.trust_score_description),
                        color = DialogText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
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
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .padding(bottom = 110.dp)
        ) {
            Text(
                text = stringResource(R.string.earnings),
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.your_income_payment_history),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    title = stringResource(R.string.total_earned),
                    value = totalEarned,
                    icon = "💼",
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = stringResource(R.string.pending_release),
                    value = pendingRelease,
                    icon = "🕒",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    title = stringResource(R.string.completed_orders),
                    value = completedOrders.toString(),
                    icon = "✅",
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = stringResource(R.string.trust_score),
                    value = stringResource(R.string.score_out_of_100, trustScore),
                    icon = "📈",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        showTrustDialog = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            PaymentHistoryCard(
                payments = payments,
                onPaymentClick = { payment ->
                    selectedPayment = payment
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            FarmerEarningsTrustScoreCard(
                score = trustScore,
                onClick = {
                    showTrustDialog = true
                }
            )

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .height(118.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable {
                        onClick()
                    }
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = GlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 16.sp,
                    modifier = Modifier.weight(1f)
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
                        fontSize = 16.sp
                    )
                }
            }

            Text(
                text = value,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PaymentHistoryCard(
    payments: List<FarmerPayment>,
    onPaymentClick: (FarmerPayment) -> Unit
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
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.payment_history),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.track_received_pending_payments),
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (payments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "💳",
                            fontSize = 42.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.no_completed_payments_yet),
                            fontSize = 15.sp,
                            color = TextMuted
                        )
                    }
                }
            } else {
                payments.forEachIndexed { index, payment ->
                    PaymentHistoryItem(
                        payment = payment,
                        onClick = {
                            onPaymentClick(payment)
                        }
                    )

                    if (index != payments.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                            color = DividerGlass,
                            thickness = 1.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentHistoryItem(
    payment: FarmerPayment,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = payment.productName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(
                        R.string.order_buyer_format,
                        payment.orderId,
                        payment.buyerName
                    ),
                    fontSize = 12.sp,
                    color = TextMuted,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = payment.date,
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }

            StatusBadge(statusKey = payment.statusKey)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PaymentInfoColumn(
                title = stringResource(R.string.total),
                value = payment.totalAmount
            )

            PaymentInfoColumn(
                title = stringResource(R.string.first_half),
                value = payment.firstHalf
            )

            PaymentInfoColumn(
                title = stringResource(R.string.second_half),
                value = payment.secondHalf
            )
        }
    }
}

@Composable
fun PaymentInfoColumn(
    title: String,
    value: String
) {
    Column {
        Text(
            text = title,
            fontSize = 12.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextLight
        )
    }
}

@Composable
fun StatusBadge(
    statusKey: String
) {
    val isCompleted = statusKey == PAYMENT_STATUS_COMPLETED

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

    val statusText = if (isCompleted) {
        stringResource(R.string.completed)
    } else {
        stringResource(R.string.pending_release)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = textColor.copy(alpha = 0.24f)
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = statusText,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    }
}

@Composable
fun FarmerEarningsTrustScoreCard(
    score: Int,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.trust_score),
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(126.dp)
                    .clip(CircleShape)
                    .background(SoftYellow)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = AccentYellow.copy(alpha = 0.25f)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = score.toString(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentYellow
                    )

                    Text(
                        text = stringResource(R.string.out_of_100),
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (score >= 70) {
                    stringResource(R.string.good)
                } else {
                    stringResource(R.string.improving)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.trust_score_description),
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun PaymentDetailDialog(
    payment: FarmerPayment,
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
                text = payment.productName,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "💳",
                    fontSize = 36.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Order: ${payment.orderId}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Buyer: ${payment.buyerName}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Date: ${payment.date}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Total amount: ${payment.totalAmount}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "First half: ${payment.firstHalf}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Second half: ${payment.secondHalf}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (payment.statusKey == PAYMENT_STATUS_COMPLETED) {
                        stringResource(R.string.completed)
                    } else {
                        stringResource(R.string.pending_release)
                    },
                    color = if (payment.statusKey == PAYMENT_STATUS_COMPLETED) {
                        PrimaryGreen
                    } else {
                        AccentYellow
                    },
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    )
}