package com.example.krishisangam.manager

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
private val TextMuted = Color(0xFFB9D8C7)
private val GlassDark = Color.White.copy(alpha = 0.095f)
private val GlassSelected = Color(0xFF01AC66).copy(alpha = 0.22f)
private val BorderGlass = Color.White.copy(alpha = 0.16f)

@Composable
fun ManagerDashboardScreen() {
    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    var selectedManagerOrderId by remember {
        mutableStateOf<String?>(null)
    }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            ManagerBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    selectedManagerOrderId = null
                }
            )
        }
    ) { innerPadding ->

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
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    ManagerHomeScreen(
                        onVerificationClick = {
                            selectedTab = 1
                            selectedManagerOrderId = null
                        },
                        onOrdersClick = {
                            selectedTab = 2
                            selectedManagerOrderId = null
                        },
                        onSpecificOrderClick = { orderId ->
                            selectedTab = 2
                            selectedManagerOrderId = orderId
                        },
                        onPaymentsClick = {
                            selectedTab = 3
                            selectedManagerOrderId = null
                        },
                        onFarmersClick = {
                            selectedTab = 4
                            selectedManagerOrderId = null
                        },
                        onProfileClick = {
                            selectedTab = 5
                            selectedManagerOrderId = null
                        }
                    )
                }

                1 -> {
                    ManagerVerificationScreen()
                }

                2 -> {
                    ManagerOrdersScreen(
                        openOrderId = selectedManagerOrderId
                    )
                }

                3 -> {
                    ManagerPaymentsScreen()
                }

                4 -> {
                    ManagerFarmersDirectoryScreen()
                }

                5 -> {
                    ManagerProfileScreen(
                        onVerificationClick = {
                            selectedTab = 1
                            selectedManagerOrderId = null
                        },
                        onOrdersClick = {
                            selectedTab = 2
                            selectedManagerOrderId = null
                        },
                        onPaymentsClick = {
                            selectedTab = 3
                            selectedManagerOrderId = null
                        },
                        onFarmersClick = {
                            selectedTab = 4
                            selectedManagerOrderId = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ManagerBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        Pair("Home", "🏠"),
        Pair("Verify", "✅"),
        Pair("Orders", "📦"),
        Pair("Payments", "💳"),
        Pair("Farmers", "🧑‍🌾"),
        Pair("Profile", "👤")
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(GlassDark)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = BorderGlass
                ),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedTab == index
                val title = item.first
                val icon = item.second

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        onTabSelected(index)
                    },
                    icon = {
                        Text(
                            text = icon,
                            fontSize = if (isSelected) {
                                20.sp
                            } else {
                                18.sp
                            }
                        )
                    },
                    label = {
                        Text(
                            text = title,
                            fontSize = 9.sp,
                            maxLines = 1,
                            fontWeight = if (isSelected) {
                                FontWeight.ExtraBold
                            } else {
                                FontWeight.SemiBold
                            }
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentYellow,
                        selectedTextColor = AccentYellow,
                        indicatorColor = GlassSelected,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    )
                )
            }
        }
    }
}