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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
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
import com.google.firebase.auth.FirebaseAuth

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

data class FarmerHomeInfo(
    val title: String,
    val emoji: String,
    val message: String
)

@Composable
fun FarmerHomeScreen(
    totalProducts: Int = 0,
    approvedProducts: Int = 0,
    pendingProducts: Int = 0,
    activeOrders: Int = 0,
    completedOrders: Int = 0,
    totalEarnings: Int = 0,
    pendingPayment: Int = 0,
    isDashboardLoading: Boolean = false,
    onProductsClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onEarningsClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val farmerName = currentUser?.displayName ?: stringResource(R.string.farmer)

    var selectedInfo by remember {
        mutableStateOf<FarmerHomeInfo?>(null)
    }

    if (selectedInfo != null) {
        FarmerHomeInfoDialog(
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
            FarmerTopHeader(
                farmerName = farmerName,
                onNotificationClick = {
                    onNotificationClick()
                },
                onHelpClick = {
                    selectedInfo = FarmerHomeInfo(
                        title = "Help",
                        emoji = "🤝",
                        message = "Need help? Visit your nearest Agro Node Manager or contact Krishi Sangam support for listing, orders, and payment support."
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.farmer_dashboard),
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.your_farm_performance_overview),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FarmerStatCard(
                    title = stringResource(R.string.total_products),
                    value = totalProducts.toString(),
                    subtitle = "$approvedProducts approved • $pendingProducts pending",
                    icon = "🌱",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onProductsClick()
                    }
                )

                FarmerStatCard(
                    title = stringResource(R.string.active_orders),
                    value = activeOrders.toString(),
                    subtitle = "$completedOrders completed",
                    icon = "📦",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onOrdersClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FarmerStatCard(
                    title = stringResource(R.string.total_earnings),
                    value = "₹$totalEarnings",
                    subtitle = stringResource(R.string.completed_orders),
                    icon = "💰",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEarningsClick()
                    }
                )

                FarmerStatCard(
                    title = stringResource(R.string.pending_payment),
                    value = "₹$pendingPayment",
                    subtitle = stringResource(R.string.after_delivery),
                    icon = "⏳",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEarningsClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            FarmerHomeTrustScoreCard(
                onClick = {
                    selectedInfo = FarmerHomeInfo(
                        title = "Trust Score",
                        emoji = "📈",
                        message = "Your trust score improves with successful deliveries, verified products, and reliable order completion. This value is static right now."
                    )
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            FarmerRecentOrdersCard(
                onOrderClick = { orderId, product, status, amount ->
                    onOrdersClick()
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            FarmerProductStatusCard(
                onProductClick = { productName, status ->
                    onProductsClick()
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun FarmerTopHeader(
    farmerName: String,
    onNotificationClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    var showMoreMenu by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
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
                text = "👨‍🌾",
                fontSize = 26.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                fontSize = 13.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = farmerName,
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                maxLines = 1
            )
        }

        Text(
            text = "🔔",
            fontSize = 23.sp,
            modifier = Modifier.clickable {
                onNotificationClick()
            }
        )

        Spacer(modifier = Modifier.width(14.dp))

        Box {
            Text(
                text = "⋯",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                modifier = Modifier.clickable {
                    showMoreMenu = true
                }
            )

            DropdownMenu(
                expanded = showMoreMenu,
                onDismissRequest = {
                    showMoreMenu = false
                },
                containerColor = DialogGreen,
                tonalElevation = 6.dp
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Help",
                            color = TextLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onClick = {
                        showMoreMenu = false
                        onHelpClick()
                    }
                )
            }
        }
    }
}

/* Remaining UI functions unchanged (FarmerStatCard, FarmerHomeTrustScoreCard, FarmerRecentOrdersCard, FarmerOrderRow,
   FarmerProductStatusCard, FarmerProductStatusRow, FarmerHomeInfoDialog) */

@Composable
fun FarmerStatCard(
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
                .padding(15.dp),
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
fun FarmerHomeTrustScoreCard(
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
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.trust_score),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = stringResource(R.string.score_out_of_100, 70),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentYellow
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.good_improves_with_deliveries),
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { 0.70f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = AccentYellow,
                trackColor = Color.White.copy(alpha = 0.14f)
            )
        }
    }
}

@Composable
fun FarmerRecentOrdersCard(
    onOrderClick: (String, String, String, String) -> Unit
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
                text = stringResource(R.string.recent_orders),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            FarmerOrderRow(
                orderId = "#KS1234",
                product = stringResource(R.string.wheat),
                status = stringResource(R.string.ready_for_dispatch),
                amount = "₹4,300",
                onClick = onOrderClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerOrderRow(
                orderId = "#KS1235",
                product = stringResource(R.string.rice),
                status = stringResource(R.string.payment_pending),
                amount = "₹3,200",
                onClick = onOrderClick
            )
        }
    }
}

@Composable
fun FarmerOrderRow(
    orderId: String,
    product: String,
    status: String,
    amount: String,
    onClick: (String, String, String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick(orderId, product, status, amount)
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
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
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                maxLines = 1
            )

            Text(
                text = status,
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )
        }

        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentYellow
        )
    }
}

@Composable
fun FarmerProductStatusCard(
    onProductClick: (String, String) -> Unit
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
                text = stringResource(R.string.product_status),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            FarmerProductStatusRow(
                name = stringResource(R.string.sharbati_wheat),
                status = stringResource(R.string.pending_verification),
                statusColor = AccentYellow,
                bgColor = SoftYellow,
                onClick = onProductClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            FarmerProductStatusRow(
                name = stringResource(R.string.rice),
                status = stringResource(R.string.approved),
                statusColor = PrimaryGreen,
                bgColor = SoftGreen,
                onClick = onProductClick
            )
        }
    }
}

@Composable
fun FarmerProductStatusRow(
    name: String,
    status: String,
    statusColor: Color,
    bgColor: Color,
    onClick: (String, String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick(name, status)
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextLight,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )

        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = statusColor,
            modifier = Modifier
                .background(bgColor, RoundedCornerShape(20.dp))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = statusColor.copy(alpha = 0.24f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
fun FarmerHomeInfoDialog(
    info: FarmerHomeInfo,
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