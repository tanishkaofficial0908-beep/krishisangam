package com.example.krishisangam.farmer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NotificationPrimaryGreen = Color(0xFF01AC66)
private val NotificationBackgroundColor = Color(0xFF003D22)
private val NotificationDeepGreen = Color(0xFF002514)
private val NotificationDarkGreen = Color(0xFF005C32)
private val NotificationAccentYellow = Color(0xFFFFC107)
private val NotificationTextLight = Color(0xFFF5FFF9)
private val NotificationTextMuted = Color(0xFFB9D8C7)
private val NotificationGlassCard = Color.White.copy(alpha = 0.105f)
private val NotificationBorderGlass = Color.White.copy(alpha = 0.16f)

@Composable
fun FarmerNotificationScreen(
    onBackClick: () -> Unit
) {
    val notifications = FarmerNotificationStore.notifications

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NotificationDarkGreen,
                        NotificationBackgroundColor,
                        NotificationDeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(NotificationAccentYellow.copy(alpha = 0.07f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomStart)
                .background(NotificationPrimaryGreen.copy(alpha = 0.10f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .padding(bottom = 110.dp)
        ) {
            FarmerNotificationTopBar(
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (notifications.isEmpty()) {
                FarmerEmptyNotificationCard()
            } else {
                notifications.forEach { notification ->
                    FarmerNotificationCard(
                        notification = notification
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
private fun FarmerNotificationTopBar(
    onBackClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = NotificationBorderGlass
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
                fontWeight = FontWeight.ExtraBold,
                color = NotificationTextLight
            )
        }

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = "Notifications",
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NotificationTextLight
            )

            Text(
                text = "Product approval, order updates and payment alerts",
                fontSize = 13.sp,
                color = NotificationTextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun FarmerNotificationCard(
    notification: FarmerNotification
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
            containerColor = NotificationGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = NotificationBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(NotificationPrimaryGreen.copy(alpha = 0.18f))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = NotificationPrimaryGreen.copy(alpha = 0.35f)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = notification.icon,
                    fontSize = 25.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NotificationTextLight,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = notification.time,
                        fontSize = 11.sp,
                        color = NotificationAccentYellow,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = NotificationTextMuted,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
private fun FarmerEmptyNotificationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = NotificationGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = NotificationBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔔",
                fontSize = 42.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No notifications yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NotificationTextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Product approval, order updates and payment alerts will appear here.",
                fontSize = 13.sp,
                color = NotificationTextMuted,
                lineHeight = 19.sp
            )
        }
    }
}