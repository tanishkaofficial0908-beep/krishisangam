package com.example.krishisangam.buyer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val NotificationPrimaryGreen = Color(0xFF01AC66)
private val NotificationBackground = Color(0xFF003D22)
private val NotificationDeepGreen = Color(0xFF002514)
private val NotificationDarkGreen = Color(0xFF005C32)
private val NotificationAccentYellow = Color(0xFFFFC107)
private val NotificationTextLight = Color(0xFFF5FFF9)
private val NotificationTextMuted = Color(0xFFB9D8C7)
private val NotificationGlassCard = Color.White.copy(alpha = 0.105f)
private val NotificationBorderGlass = Color.White.copy(alpha = 0.16f)
private val NotificationSoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)

@Composable
fun BuyerNotificationScreen(
    onBackClick: () -> Unit
) {
    val notifications = BuyerNotificationStore.notifications

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NotificationDarkGreen,
                        NotificationBackground,
                        NotificationDeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopEnd)
                .background(NotificationAccentYellow.copy(alpha = 0.07f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.BottomStart)
                .background(NotificationPrimaryGreen.copy(alpha = 0.12f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .padding(top = 18.dp)
        ) {
            NotificationTopBar(
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.buyer_notifications_subtitle),
                fontSize = 14.sp,
                color = NotificationTextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (notifications.isEmpty()) {
                EmptyNotificationState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 130.dp)
                ) {
                    items(
                        items = notifications,
                        key = { notification ->
                            notification.id
                        }
                    ) { notification ->
                        BuyerNotificationCard(
                            notification = notification
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationTopBar(
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
                fontWeight = FontWeight.Bold,
                color = NotificationTextLight
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = stringResource(R.string.notifications),
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NotificationTextLight,
            maxLines = 1
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(NotificationSoftYellow)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = NotificationAccentYellow.copy(alpha = 0.35f)
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🔔",
                fontSize = 21.sp
            )
        }
    }
}

@Composable
fun BuyerNotificationCard(
    notification: BuyerNotificationItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp)
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = NotificationGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = NotificationBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(NotificationSoftYellow)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = NotificationAccentYellow.copy(alpha = 0.35f)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = notification.icon,
                    fontSize = 22.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 13.dp)
            ) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NotificationTextLight,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = NotificationTextMuted,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = notification.time,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NotificationAccentYellow
                )
            }
        }
    }
}

@Composable
fun EmptyNotificationState() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
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
                .padding(vertical = 45.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(NotificationSoftYellow),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔔",
                    fontSize = 38.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.no_notifications_yet),
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NotificationTextLight
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = stringResource(R.string.notifications_will_appear_here),
                fontSize = 13.sp,
                color = NotificationTextMuted,
                lineHeight = 19.sp
            )
        }
    }
}