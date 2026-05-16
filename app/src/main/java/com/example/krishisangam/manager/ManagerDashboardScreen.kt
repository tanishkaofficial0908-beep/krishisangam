package com.example.krishisangam.manager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)

@Composable
fun ManagerDashboardScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            ManagerBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> ManagerHomeScreen()
                1 -> ManagerVerificationScreen()
                2 -> ManagerOrdersScreen()
                3 -> ManagerPaymentsScreen()
                4 -> ManagerFarmersDirectoryScreen()
                5 -> ManagerProfileScreen()
            }
        }
    }
}

@Composable
fun ManagerBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        val items = listOf(
            "Home" to "🏠",
            "Verify" to "✅",
            "Orders" to "📦",
            "Payments" to "💳",
            "Farmers" to "🧑‍🌾",
            "Profile" to "👤"
        )

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = {
                    onTabSelected(index)
                },
                icon = {
                    Text(
                        text = item.second,
                        fontSize = 18.sp
                    )
                },
                label = {
                    Text(
                        text = item.first,
                        fontSize = 9.sp,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = Color(0xFFDFF8EF),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}