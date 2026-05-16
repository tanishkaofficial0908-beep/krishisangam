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
import androidx.compose.material3.LinearProgressIndicator
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
import com.google.firebase.auth.FirebaseAuth

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftYellow = Color(0xFFFFF4D6)
private val YellowText = Color(0xFFB8860B)

@Composable
fun FarmerHomeScreen() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val farmerName = currentUser?.displayName ?: stringResource(R.string.farmer)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        FarmerTopHeader(
            farmerName = farmerName
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.farmer_dashboard),
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = stringResource(R.string.your_farm_performance_overview),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FarmerStatCard(
                title = stringResource(R.string.total_products),
                value = "10",
                subtitle = stringResource(R.string.approved_pending_summary, 1, 9),
                icon = "🌱",
                modifier = Modifier.weight(1f)
            )

            FarmerStatCard(
                title = stringResource(R.string.active_orders),
                value = "4",
                subtitle = stringResource(R.string.completed_count, 2),
                icon = "📦",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FarmerStatCard(
                title = stringResource(R.string.total_earnings),
                value = "₹8,200",
                subtitle = stringResource(R.string.completed_orders),
                icon = "💰",
                modifier = Modifier.weight(1f)
            )

            FarmerStatCard(
                title = stringResource(R.string.pending_payment),
                value = "₹4,100",
                subtitle = stringResource(R.string.after_delivery),
                icon = "⏳",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        FarmerHomeTrustScoreCard()

        Spacer(modifier = Modifier.height(22.dp))

        FarmerRecentOrdersCard()

        Spacer(modifier = Modifier.height(22.dp))

        FarmerProductStatusCard()

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun FarmerTopHeader(
    farmerName: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "👨‍🌾",
                fontSize = 25.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                fontSize = 13.sp,
                color = Color.Gray
            )

            Text(
                text = farmerName,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Text(
            text = "🔔",
            fontSize = 23.sp
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = "⋯",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
    }
}

@Composable
fun FarmerStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(135.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
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
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(LightGreen),
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
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun FarmerHomeTrustScoreCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.trust_score),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = stringResource(R.string.score_out_of_100, 70),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = YellowText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.good_improves_with_deliveries),
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { 0.70f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = PrimaryGreen,
                trackColor = LightGreen
            )
        }
    }
}

@Composable
fun FarmerRecentOrdersCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.recent_orders),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            FarmerOrderRow(
                orderId = "#KS1234",
                product = stringResource(R.string.wheat),
                status = stringResource(R.string.ready_for_dispatch),
                amount = "₹4,300"
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerOrderRow(
                orderId = "#KS1235",
                product = stringResource(R.string.rice),
                status = stringResource(R.string.payment_pending),
                amount = "₹3,200"
            )
        }
    }
}

@Composable
fun FarmerOrderRow(
    orderId: String,
    product: String,
    status: String,
    amount: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(LightGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "📦",
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.order_product_format, orderId, product),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = status,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )
    }
}

@Composable
fun FarmerProductStatusCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.product_status),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            FarmerProductStatusRow(
                name = stringResource(R.string.sharbati_wheat),
                status = stringResource(R.string.pending_verification),
                statusColor = YellowText,
                bgColor = SoftYellow
            )

            Spacer(modifier = Modifier.height(10.dp))

            FarmerProductStatusRow(
                name = stringResource(R.string.rice),
                status = stringResource(R.string.approved),
                statusColor = PrimaryGreen,
                bgColor = LightGreen
            )
        }
    }
}

@Composable
fun FarmerProductStatusRow(
    name: String,
    status: String,
    statusColor: Color,
    bgColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = statusColor,
            modifier = Modifier
                .background(bgColor, RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}