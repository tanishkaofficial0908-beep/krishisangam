// ManagerDashboardScreen.kt
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
import androidx.compose.runtime.DisposableEffect
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

private val ManagerDashPrimaryGreen = Color(0xFF01AC66)
private val ManagerDashBackgroundColor = Color(0xFF003D22)
private val ManagerDashDeepGreen = Color(0xFF002514)
private val ManagerDashDarkGreen = Color(0xFF005C32)
private val ManagerDashAccentYellow = Color(0xFFFFC107)
private val ManagerDashTextMuted = Color(0xFFB9D8C7)
private val ManagerDashGlassDark = Color.White.copy(alpha = 0.095f)
private val ManagerDashGlassSelected = Color(0xFF01AC66).copy(alpha = 0.22f)
private val ManagerDashBorderGlass = Color.White.copy(alpha = 0.16f)

data class ManagerDashboardStats(
    val pendingVerification: Int = 0,
    val approvedProducts: Int = 0,
    val activeOrders: Int = 0,
    val totalRevenue: Int = 0
)

@Composable
fun ManagerDashboardScreen(
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    var showEditProfileScreen by remember {
        mutableStateOf(false)
    }

    var showNodeDetailsScreen by remember {
        mutableStateOf(false)
    }

    var dashboardStats by remember {
        mutableStateOf(ManagerDashboardStats())
    }

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    DisposableEffect(Unit) {
        var pendingCount = 0
        var approvedCount = 0
        var activeOrderCount = 0
        var revenueAmount = 0

        fun updateStats() {
            dashboardStats = ManagerDashboardStats(
                pendingVerification = pendingCount,
                approvedProducts = approvedCount,
                activeOrders = activeOrderCount,
                totalRevenue = revenueAmount
            )
        }

        val productsListener = firestore
            .collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    pendingCount = 0
                    approvedCount = 0
                    updateStats()
                    return@addSnapshotListener
                }

                val products = snapshot.documents

                pendingCount = products.count { document ->
                    document.getString("status").equals("pending", ignoreCase = true)
                }

                approvedCount = products.count { document ->
                    document.getString("status").equals("approved", ignoreCase = true)
                }

                updateStats()
            }

        val ordersListener = firestore
            .collection("orders")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    activeOrderCount = 0
                    revenueAmount = 0
                    updateStats()
                    return@addSnapshotListener
                }

                val orders = snapshot.documents

                activeOrderCount = orders.count { document ->
                    val orderStatus = document.getString("orderStatus") ?: ""
                    !orderStatus.equals("delivered", ignoreCase = true) &&
                            !orderStatus.equals("cancelled", ignoreCase = true)
                }

                var revenue = 0.0

                orders.forEach { document ->
                    val paymentStatus = document.getString("paymentStatus") ?: ""

                    if (
                        paymentStatus.equals("advance_paid", ignoreCase = true) ||
                        paymentStatus.equals("fully_paid", ignoreCase = true)
                    ) {
                        val platformFee = document.getDoubleValueSafely(listOf("platformFee"))
                        val packagingCharge = document.getDoubleValueSafely(listOf("packagingCharge"))
                        val logisticsCharge = document.getDoubleValueSafely(listOf("logisticsCharge"))

                        revenue += platformFee + packagingCharge + logisticsCharge
                    }
                }

                revenueAmount = revenue.roundToInt()
                updateStats()
            }

        onDispose {
            productsListener.remove()
            ordersListener.remove()
        }
    }

    Scaffold(
        containerColor = ManagerDashBackgroundColor,
        bottomBar = {
            ManagerBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    showEditProfileScreen = false
                    showNodeDetailsScreen = false
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
                            ManagerDashDarkGreen,
                            ManagerDashBackgroundColor,
                            ManagerDashDeepGreen
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            when {
                showEditProfileScreen -> {
                    ManagerEditProfileScreen(
                        onBackClick = {
                            showEditProfileScreen = false
                        }
                    )
                }

                showNodeDetailsScreen -> {
                    ManagerNodeDetailsScreen(
                        onBackClick = {
                            showNodeDetailsScreen = false
                        }
                    )
                }

                else -> {
                    when (selectedTab) {
                        0 -> {
                            ManagerHomeScreen(
                                pendingVerification = dashboardStats.pendingVerification,
                                approvedProducts = dashboardStats.approvedProducts,
                                activeOrders = dashboardStats.activeOrders,
                                totalRevenue = dashboardStats.totalRevenue,
                                onVerificationClick = {
                                    selectedTab = 1
                                },
                                onOrdersClick = {
                                    selectedTab = 2
                                },
                                onSpecificOrderClick = { _ ->
                                    selectedTab = 2
                                },
                                onPaymentsClick = {
                                    selectedTab = 3
                                },
                                onFarmersClick = {
                                    selectedTab = 4
                                },
                                onProfileClick = {
                                    selectedTab = 5
                                }
                            )
                        }

                        1 -> {
                            ManagerVerificationScreen()
                        }

                        2 -> {
                            ManagerOrdersScreen()
                        }

                        3 -> {
                            ManagerPaymentsScreen()
                        }

                        4 -> {
                            ManagerFarmersDirectoryScreen()
                        }

                        5 -> {
                            // Keep UI same; only remove unsupported params.
                            ManagerProfileScreen(
                                onLogoutConfirm = {
                                    onLogout()
                                },

                                onEditProfileClick = {
                                    showEditProfileScreen = true
                                },

                                onNodeDetailsClick = {
                                    showNodeDetailsScreen = true
                                }
                            )
                        }
                    }
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
        ManagerTabItem("Home", "🏠"),
        ManagerTabItem("Verify", "✅"),
        ManagerTabItem("Orders", "📦"),
        ManagerTabItem("Payments", "💳"),
        ManagerTabItem("Farmers", "👨‍🌾"),
        ManagerTabItem("Profile", "👤")
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(28.dp)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(ManagerDashGlassDark)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = ManagerDashBorderGlass
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

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        onTabSelected(index)
                    },
                    icon = {
                        Text(
                            text = item.icon,
                            fontSize = if (isSelected) 20.sp else 18.sp
                        )
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 9.sp,
                            maxLines = 1,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ManagerDashAccentYellow,
                        selectedTextColor = ManagerDashAccentYellow,
                        indicatorColor = ManagerDashGlassSelected,
                        unselectedIconColor = ManagerDashTextMuted,
                        unselectedTextColor = ManagerDashTextMuted
                    )
                )
            }
        }
    }
}

private data class ManagerTabItem(
    val title: String,
    val icon: String
)

private fun DocumentSnapshot.getDoubleValueSafely(
    keys: List<String>
): Double {
    keys.forEach { key ->
        val value = get(key)

        when (value) {
            is Number -> return value.toDouble()
            is String -> {
                val cleanedValue = value
                    .replace("₹", "")
                    .replace(",", "")
                    .trim()

                cleanedValue.toDoubleOrNull()?.let {
                    return it
                }
            }
        }
    }

    return 0.0
}