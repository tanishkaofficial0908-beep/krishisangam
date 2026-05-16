package com.example.krishisangam.farmer

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF1C1C1C)
private val LightGreen = Color(0xFFDFF8EF)
private val LightYellow = Color(0xFFFFF5D9)
private val LightBlue = Color(0xFFEAF2FF)
private val LightGray = Color(0xFFF5F6F7)
private val SoftText = Color(0xFF7A7A7A)

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Text(
            text = stringResource(R.string.earnings),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.your_income_payment_history),
            fontSize = 14.sp,
            color = SoftText
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryCard(
                title = stringResource(R.string.total_earned),
                value = totalEarned,
                icon = "💼",
                iconBg = LightGreen,
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = stringResource(R.string.pending_release),
                value = pendingRelease,
                icon = "🕒",
                iconBg = LightYellow,
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
                iconBg = LightGreen,
                modifier = Modifier.weight(1f)
            )

            SummaryCard(
                title = stringResource(R.string.trust_score),
                value = stringResource(R.string.score_out_of_100, trustScore),
                icon = "📈",
                iconBg = LightBlue,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                PaymentHistoryCard(payments = payments)
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                FarmerEarningsTrustScoreCard(score = trustScore)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: String,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    color = SoftText,
                    fontWeight = FontWeight.SemiBold
                )

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(iconBg),
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
    }
}

@Composable
fun PaymentHistoryCard(
    payments: List<FarmerPayment>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.payment_history),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.track_received_pending_payments),
                fontSize = 13.sp,
                color = SoftText
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
                            color = SoftText
                        )
                    }
                }
            } else {
                payments.forEachIndexed { index, payment ->
                    PaymentHistoryItem(payment = payment)

                    if (index != payments.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(
                            color = LightGray,
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
    payment: FarmerPayment
) {
    Column(
        modifier = Modifier.fillMaxWidth()
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
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(
                        R.string.order_buyer_format,
                        payment.orderId,
                        payment.buyerName
                    ),
                    fontSize = 12.sp,
                    color = SoftText
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = payment.date,
                    fontSize = 12.sp,
                    color = SoftText
                )
            }

            StatusBadge(statusKey = payment.statusKey)
        }

        Spacer(modifier = Modifier.height(10.dp))

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
            color = SoftText
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark
        )
    }
}

@Composable
fun StatusBadge(
    statusKey: String
) {
    val isCompleted = statusKey == PAYMENT_STATUS_COMPLETED

    val bgColor = if (isCompleted) {
        LightGreen
    } else {
        LightYellow
    }

    val textColor = if (isCompleted) {
        PrimaryGreen
    } else {
        Color(0xFFB8860B)
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
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = statusText,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}

@Composable
fun FarmerEarningsTrustScoreCard(
    score: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
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
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(LightYellow),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = score.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = stringResource(R.string.out_of_100),
                        fontSize = 13.sp,
                        color = SoftText
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
                fontWeight = FontWeight.SemiBold,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.trust_score_description),
                fontSize = 12.sp,
                color = SoftText,
                lineHeight = 18.sp
            )
        }
    }
}