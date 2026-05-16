package com.example.krishisangam.manager

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
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)

@Composable
fun ManagerHomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        ManagerTopHeader(
            managerName = "Agro Node Manager"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Agro-Node Control Center",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Manage farmer produce, orders and dispatch",
            fontSize = 14.sp,
            color = Color.Gray
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
                modifier = Modifier.weight(1f)
            )

            ManagerStatCard(
                title = "Approved Products",
                value = "11",
                subtitle = "Live listings",
                icon = "✅",
                modifier = Modifier.weight(1f)
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
                modifier = Modifier.weight(1f)
            )

            ManagerStatCard(
                title = "Total Revenue",
                value = "₹8,200",
                subtitle = "Today",
                icon = "📈",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        StorageUsageCard()

        Spacer(modifier = Modifier.height(22.dp))

        RecentOrdersCard()

        Spacer(modifier = Modifier.height(22.dp))

        ActivityFeedCard()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ManagerTopHeader(
    managerName: String
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
                text = "👤",
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Welcome back",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Text(
                text = managerName,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Text(
            text = "🔔",
            fontSize = 22.sp
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
fun ManagerStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(135.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = value,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StorageUsageCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Node Storage",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "65%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Bhopal Agro Node 01",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
            progress = { 0.65f },
            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(20.dp)),
            color = PrimaryGreen,
            trackColor = LightGreen,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Incoming today: Wheat, Rice, Tomato",
                fontSize = 13.sp,
                color = TextDark
            )
        }
    }
}

@Composable
fun RecentOrdersCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Recent Orders",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            ManagerOrderRow(
                orderId = "#KS1234",
                product = "Wheat",
                status = "Ready for dispatch",
                amount = "₹4,300"
            )

            Spacer(modifier = Modifier.height(12.dp))

            ManagerOrderRow(
                orderId = "#KS1235",
                product = "Rice",
                status = "Payment completed",
                amount = "₹6,400"
            )
        }
    }
}

@Composable
fun ManagerOrderRow(
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
                text = "$orderId • $product",
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
fun ActivityFeedCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Activity Feed",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            ActivityItem("New wheat listing submitted by Ramesh Patel")
            ActivityItem("Rice order payment completed")
            ActivityItem("Tomato batch approved by Agro Node")
            ActivityItem("Local truck assigned for delivery")
        }
    }
}

@Composable
fun ActivityItem(
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
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
            color = TextDark,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}