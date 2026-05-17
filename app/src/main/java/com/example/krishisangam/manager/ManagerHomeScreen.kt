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
import androidx.compose.material3.ProgressIndicatorDefaults
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

data class ManagerHomeInfo(
    val title: String,
    val emoji: String,
    val message: String
)

@Composable
fun ManagerHomeScreen(
    onVerificationClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onSpecificOrderClick: (String) -> Unit,
    onPaymentsClick: () -> Unit,
    onFarmersClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedInfo by remember {
        mutableStateOf<ManagerHomeInfo?>(null)
    }

    if (selectedInfo != null) {
        ManagerHomeInfoDialog(
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
            ManagerTopHeader(
                managerName = "Agro Node Manager",
                onNotificationClick = {
                    selectedInfo = ManagerHomeInfo(
                        title = "Notifications",
                        emoji = "🔔",
                        message = "No new manager notifications yet. Product verification, order dispatch, and payment alerts will appear here."
                    )
                },
                onHelpClick = {
                    selectedInfo = ManagerHomeInfo(
                        title = "Help",
                        emoji = "🤝",
                        message = "Need help? Manager support can guide you with product verification, payment release, storage usage, and dispatch coordination."
                    )
                },
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Agro-Node Control Center",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Manage farmer produce, orders and dispatch",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ManagerStatCard(
                    title = "Pending Verification",
                    value = "9",
                    subtitle = "Awaiting review",
                    icon = "⏳",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onVerificationClick()
                    }
                )

                ManagerStatCard(
                    title = "Approved Products",
                    value = "11",
                    subtitle = "Live listings",
                    icon = "✅",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onVerificationClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                ManagerStatCard(
                    title = "Active Orders",
                    value = "4",
                    subtitle = "Ready to process",
                    icon = "📦",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onOrdersClick()
                    }
                )

                ManagerStatCard(
                    title = "Total Revenue",
                    value = "₹8,200",
                    subtitle = "Today",
                    icon = "📈",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onPaymentsClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            StorageUsageCard(
                onClick = {
                    selectedInfo = ManagerHomeInfo(
                        title = "Node Storage",
                        emoji = "🏢",
                        message = "Bhopal Agro Node 01 is currently shown as 65% full. Incoming today: Wheat, Rice, Tomato. This is static demo data right now."
                    )
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            RecentOrdersCard(
                onOrderClick = { orderId, _, _, _ ->
                    onSpecificOrderClick(orderId)
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            ActivityFeedCard(
                onVerificationClick = onVerificationClick,
                onPaymentsClick = onPaymentsClick,
                onOrdersClick = onOrdersClick
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ManagerTopHeader(
    managerName: String,
    onNotificationClick: () -> Unit,
    onHelpClick: () -> Unit,
    onProfileClick: () -> Unit
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
                )
                .clickable {
                    onProfileClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "👤",
                fontSize = 25.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Welcome back",
                fontSize = 13.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = managerName,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                maxLines = 1
            )
        }

        Text(
            text = "🔔",
            fontSize = 22.sp,
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
                            text = "Profile",
                            color = TextLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onClick = {
                        showMoreMenu = false
                        onProfileClick()
                    }
                )

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

@Composable
fun ManagerStatCard(
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
fun StorageUsageCard(
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
                    text = "Node Storage",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "65%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentYellow
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Bhopal Agro Node 01",
                fontSize = 13.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { 0.65f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = AccentYellow,
                trackColor = Color.White.copy(alpha = 0.14f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Incoming today: Wheat, Rice, Tomato",
                fontSize = 13.sp,
                color = DialogText,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun RecentOrdersCard(
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
                text = "Recent Orders",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            ManagerOrderRow(
                orderId = "#KS1234",
                product = "Wheat",
                status = "Ready for dispatch",
                amount = "₹4,300",
                onClick = onOrderClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            ManagerOrderRow(
                orderId = "#KS1235",
                product = "Rice",
                status = "Payment completed",
                amount = "₹6,400",
                onClick = onOrderClick
            )
        }
    }
}

@Composable
fun ManagerOrderRow(
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
                text = "$orderId • $product",
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
fun ActivityFeedCard(
    onVerificationClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onOrdersClick: () -> Unit
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
                text = "Activity Feed",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            ActivityItem(
                text = "New wheat listing submitted by Ramesh Patel",
                onClick = {
                    onVerificationClick()
                }
            )

            ActivityItem(
                text = "Rice order payment completed",
                onClick = {
                    onPaymentsClick()
                }
            )

            ActivityItem(
                text = "Tomato batch approved by Agro Node",
                onClick = {
                    onVerificationClick()
                }
            )

            ActivityItem(
                text = "Local truck assigned for delivery",
                onClick = {
                    onOrdersClick()
                }
            )
        }
    }
}

@Composable
fun ActivityItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable {
                onClick()
            }
            .padding(bottom = 12.dp, top = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(CircleShape)
                .background(PrimaryGreen)
        )

        Text(
            text = text,
            fontSize = 13.sp,
            color = DialogText,
            modifier = Modifier.padding(start = 10.dp),
            lineHeight = 18.sp
        )
    }
}

@Composable
fun ManagerHomeInfoDialog(
    info: ManagerHomeInfo,
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