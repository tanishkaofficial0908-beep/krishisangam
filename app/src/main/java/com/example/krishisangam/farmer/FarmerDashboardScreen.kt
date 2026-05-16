package com.example.krishisangam.farmer

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val LightGreen = Color(0xFFDFF8EF)

@Composable
fun FarmerDashboardScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddProductScreen by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            FarmerBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    showAddProductScreen = false
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
            if (showAddProductScreen) {
                FarmerAddProductScreen(
                    onBackClick = {
                        showAddProductScreen = false
                    },
                    onProductSubmitted = {
                        showAddProductScreen = false
                        selectedTab = 1
                    }
                )
            } else {
                when (selectedTab) {
                    0 -> FarmerHomeScreen()

                    1 -> FarmerProductsScreen(
                        onAddProductClick = {
                            showAddProductScreen = true
                        }
                    )

                    2 -> FarmerOrdersScreen()
                    3 -> FarmerEarningsScreen()
                    4 -> FarmerProfileScreen()
                }
            }
        }
    }
}

@Composable
fun FarmerBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        FarmerBottomBarItem(
            title = stringResource(R.string.home),
            icon = "🏠"
        ),
        FarmerBottomBarItem(
            title = stringResource(R.string.products),
            icon = "🌱"
        ),
        FarmerBottomBarItem(
            title = stringResource(R.string.orders),
            icon = "📦"
        ),
        FarmerBottomBarItem(
            title = stringResource(R.string.earnings),
            icon = "💰"
        ),
        FarmerBottomBarItem(
            title = stringResource(R.string.profile),
            icon = "👤"
        )
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = {
                    onTabSelected(index)
                },
                icon = {
                    Text(
                        text = item.icon,
                        fontSize = 20.sp
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryGreen,
                    selectedTextColor = PrimaryGreen,
                    indicatorColor = LightGreen,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

data class FarmerBottomBarItem(
    val title: String,
    val icon: String
)