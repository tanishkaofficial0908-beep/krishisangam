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

data class TransportationHomeInfo(
    val title: String,
    val emoji: String,
    val message: String
)

@Composable
fun TransportationHomeScreen(
    onTripsClick: () -> Unit,
    onEarningsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedInfo by remember {
        mutableStateOf<TransportationHomeInfo?>(null)
    }

    if (selectedInfo != null) {
        TransportationHomeInfoDialog(
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
            TransportationTopHeader(
                partnerName = "Transport Partner",
                onNotificationClick = {
                    selectedInfo = TransportationHomeInfo(
                        title = "Notifications",
                        emoji = "🔔",
                        message = "No new trip notifications yet. Assigned trips, pickup alerts and payment updates will appear here."
                    )
                },
                onHelpClick = {
                    selectedInfo = TransportationHomeInfo(
                        title = "Help",
                        emoji = "🤝",
                        message = "Need help? You can contact Krishi Sangam support or your assigned Agro Node Manager."
                    )
                },
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Transport Partner Hub",
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Accept nearby trips, manage pickups and complete deliveries",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TransportationStatCard(
                    title = "Nearby Orders",
                    value = "5",
                    subtitle = "Available trips",
                    icon = "📍",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTripsClick()
                    }
                )

                TransportationStatCard(
                    title = "Ongoing Trips",
                    value = "2",
                    subtitle = "In progress",
                    icon = "🚚",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTripsClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                TransportationStatCard(
                    title = "Completed",
                    value = "18",
                    subtitle = "Total deliveries",
                    icon = "✅",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onTripsClick()
                    }
                )

                TransportationStatCard(
                    title = "Pending Pay",
                    value = "₹1,250",
                    subtitle = "To be released",
                    icon = "💰",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEarningsClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            TransportationApprovalCard(
                onClick = {
                    onProfileClick()
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            TransportationLoadCapacityCard(
                onClick = {
                    selectedInfo = TransportationHomeInfo(
                        title = "Vehicle Capacity",
                        emoji = "🚛",
                        message = "Your demo vehicle capacity is shown as 70% available. Later this will be calculated from assigned load and vehicle capacity."
                    )
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            TransportationRecentTripsCard(
                onTripClick = {
                    onTripsClick()
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            TransportationActivityCard(
                onTripsClick = onTripsClick,
                onEarningsClick = onEarningsClick,
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun TransportationTopHeader(
    partnerName: String,
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
                text = "🚚",
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
                text = partnerName,
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
fun TransportationStatCard(
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
fun TransportationApprovalCard(
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
                    text = "🕒",
                    fontSize = 25.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = "Verification Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Pending manager approval. Complete your vehicle profile.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }

            Text(
                text = "›",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )
        }
    }
}

@Composable
fun TransportationLoadCapacityCard(
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
                    text = "Vehicle Load Capacity",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "70%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentYellow
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Mini Truck • 1.5 Ton capacity",
                fontSize = 13.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { 0.70f },
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
                text = "Current load: 1.05 Ton assigned for ongoing trips",
                fontSize = 13.sp,
                color = DialogText,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun TransportationRecentTripsCard(
    onTripClick: () -> Unit
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
                text = "Recent Trips",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransportationTripRow(
                tripId = "#TRP1021",
                route = "Bhopal Agro Node → Bhopal Market Hub",
                status = "Pickup Pending",
                amount = "₹650",
                onClick = onTripClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            TransportationTripRow(
                tripId = "#TRP1022",
                route = "Sehore Node → Community Drop Point",
                status = "In Transit",
                amount = "₹920",
                onClick = onTripClick
            )
        }
    }
}

@Composable
fun TransportationTripRow(
    tripId: String,
    route: String,
    status: String,
    amount: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick()
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
                text = "🚚",
                fontSize = 20.sp
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = tripId,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                maxLines = 1
            )

            Text(
                text = route,
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )

            Text(
                text = status,
                fontSize = 12.sp,
                color = AccentYellow,
                fontWeight = FontWeight.SemiBold,
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
fun TransportationActivityCard(
    onTripsClick: () -> Unit,
    onEarningsClick: () -> Unit,
    onProfileClick: () -> Unit
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

            TransportationActivityItem(
                text = "New nearby wheat delivery available",
                onClick = onTripsClick
            )

            TransportationActivityItem(
                text = "Payment for #TRP1019 has been completed",
                onClick = onEarningsClick
            )

            TransportationActivityItem(
                text = "Vehicle profile pending approval",
                onClick = onProfileClick
            )

            TransportationActivityItem(
                text = "Rice delivery marked in transit",
                onClick = onTripsClick
            )
        }
    }
}

@Composable
fun TransportationActivityItem(
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
fun TransportationHomeInfoDialog(
    info: TransportationHomeInfo,
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