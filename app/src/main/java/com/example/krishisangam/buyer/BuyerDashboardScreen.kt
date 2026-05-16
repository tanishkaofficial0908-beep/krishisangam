package com.example.krishisangam.buyer

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
fun BuyerDashboardScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showSearchScreen by remember { mutableStateOf(false) }
    var selectedCategoryScreen by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            BuyerBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { tabIndex ->
                    selectedTab = tabIndex
                    showSearchScreen = false
                    selectedCategoryScreen = null
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
            when {
                showSearchScreen -> {
                    BuyerSearchScreen(
                        onBackClick = {
                            showSearchScreen = false
                        }
                    )
                }

                selectedCategoryScreen != null -> {
                    BuyerCategoryListingScreen(
                        categoryName = selectedCategoryScreen ?: "",
                        onBackClick = {
                            selectedCategoryScreen = null
                        }
                    )
                }

                else -> {
                    when (selectedTab) {
                        0 -> {
                            BuyerHomeScreen(
                                onSearchClick = {
                                    showSearchScreen = true
                                },
                                onCategoryClick = { categoryName ->
                                    selectedCategoryScreen = categoryName
                                }
                            )
                        }

                        1 -> {
                            BuyerFarmersScreen()
                        }

                        2 -> {
                            BuyerMarketplaceScreen()
                        }

                        3 -> {
                            BuyerOrdersScreen()
                        }

                        4 -> {
                            BuyerProfileScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BuyerBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomBarItem(
            title = stringResource(R.string.home),
            icon = "🏠"
        ),
        BottomBarItem(
            title = stringResource(R.string.farmers),
            icon = "🧑‍🌾"
        ),
        BottomBarItem(
            title = stringResource(R.string.marketplace),
            icon = "🏪"
        ),
        BottomBarItem(
            title = stringResource(R.string.orders),
            icon = "🛒"
        ),
        BottomBarItem(
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

data class BottomBarItem(
    val title: String,
    val icon: String
)