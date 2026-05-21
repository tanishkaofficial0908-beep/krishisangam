package com.example.krishisangam.farmer

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFF003D22)
private val DeepGreen = Color(0xFF002514)
private val DarkGreen = Color(0xFF005C32)
private val AccentYellow = Color(0xFFFFC107)
private val TextMuted = Color(0xFFB9D8C7)
private val GlassDark = Color.White.copy(alpha = 0.095f)
private val GlassSelected = Color(0xFF01AC66).copy(alpha = 0.22f)
private val BorderGlass = Color.White.copy(alpha = 0.16f)

data class FarmerDashboardStats(
    val isLoading: Boolean = true,
    val totalProducts: Int = 0,
    val approvedProducts: Int = 0,
    val pendingProducts: Int = 0,
    val rejectedProducts: Int = 0,
    val activeOrders: Int = 0,
    val completedOrders: Int = 0,
    val totalEarnings: Int = 0,
    val pendingPayment: Int = 0
)

@Composable
fun FarmerDashboardScreen(
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    var showAddProductScreen by remember {
        mutableStateOf(false)
    }

    var showNotificationScreen by remember {
        mutableStateOf(false)
    }

    var showEditProfileScreen by remember {
        mutableStateOf(false)
    }

    var showFarmDetailsScreen by remember {
        mutableStateOf(false)
    }

    var showOrderHistoryScreen by remember {
        mutableStateOf(false)
    }

    var dashboardStats by remember {
        mutableStateOf(FarmerDashboardStats())
    }

    val farmerId = FirebaseAuth.getInstance().currentUser?.uid
    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    DisposableEffect(farmerId) {
        if (farmerId.isNullOrBlank()) {
            dashboardStats = FarmerDashboardStats(isLoading = false)
            return@DisposableEffect onDispose { }
        }

        var latestProductStats = ProductStats()
        var latestOrderStats = OrderStats()

        fun updateDashboardStats() {
            dashboardStats = FarmerDashboardStats(
                isLoading = false,
                totalProducts = latestProductStats.totalProducts,
                approvedProducts = latestProductStats.approvedProducts,
                pendingProducts = latestProductStats.pendingProducts,
                rejectedProducts = latestProductStats.rejectedProducts,
                activeOrders = latestOrderStats.activeOrders,
                completedOrders = latestOrderStats.completedOrders,
                totalEarnings = latestOrderStats.totalEarnings,
                pendingPayment = latestOrderStats.pendingPayment
            )
        }

        val productsListener = firestore
            .collection("products")
            .whereEqualTo("farmerId", farmerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    latestProductStats = ProductStats()
                    updateDashboardStats()
                    return@addSnapshotListener
                }

                val products = snapshot.documents

                latestProductStats = ProductStats(
                    totalProducts = products.size,
                    approvedProducts = products.count {
                        it.getString("status").equals("approved", ignoreCase = true)
                    },
                    pendingProducts = products.count {
                        it.getString("status").equals("pending", ignoreCase = true)
                    },
                    rejectedProducts = products.count {
                        it.getString("status").equals("rejected", ignoreCase = true)
                    }
                )

                updateDashboardStats()
            }

        val ordersListener = firestore
            .collection("orders")
            .whereEqualTo("farmerId", farmerId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    latestOrderStats = OrderStats()
                    updateDashboardStats()
                    return@addSnapshotListener
                }

                val orders = snapshot.documents

                val activeOrders = orders.count { document ->
                    val orderStatus = document.getString("orderStatus") ?: ""
                    !orderStatus.equals("delivered", ignoreCase = true) &&
                            !orderStatus.equals("cancelled", ignoreCase = true)
                }

                val completedOrders = orders.count { document ->
                    document.getString("orderStatus").equals("delivered", ignoreCase = true)
                }

                var totalEarnings = 0.0
                var pendingPayment = 0.0

                orders.forEach { document ->
                    val paymentStatus = document.getString("paymentStatus") ?: ""

                    val farmerAdvanceAmount = document.getDoubleValueSafely(
                        keys = listOf(
                            "farmerAdvanceAmount",
                            "advanceAmountPaidByBuyer",
                            "advanceAmount"
                        )
                    )

                    val farmerRemainingAmount = document.getDoubleValueSafely(
                        keys = listOf(
                            "farmerRemainingAmount",
                            "remainingAmountFromBuyer",
                            "remainingAmount"
                        )
                    )

                    if (
                        paymentStatus.equals("advance_paid", ignoreCase = true) ||
                        paymentStatus.equals("fully_paid", ignoreCase = true)
                    ) {
                        totalEarnings += farmerAdvanceAmount
                    }

                    if (paymentStatus.equals("fully_paid", ignoreCase = true)) {
                        totalEarnings += farmerRemainingAmount
                    }

                    if (paymentStatus.equals("advance_paid", ignoreCase = true)) {
                        pendingPayment += farmerRemainingAmount
                    }
                }

                latestOrderStats = OrderStats(
                    activeOrders = activeOrders,
                    completedOrders = completedOrders,
                    totalEarnings = totalEarnings.roundToInt(),
                    pendingPayment = pendingPayment.roundToInt()
                )

                updateDashboardStats()
            }

        onDispose {
            productsListener.remove()
            ordersListener.remove()
        }
    }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            FarmerBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    showAddProductScreen = false
                    showNotificationScreen = false
                    showEditProfileScreen = false
                    showFarmDetailsScreen = false
                    showOrderHistoryScreen = false
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
            when {
                showNotificationScreen -> {
                    FarmerNotificationScreen(
                        onBackClick = {
                            showNotificationScreen = false
                        }
                    )
                }

                showEditProfileScreen -> {
                    FarmerEditProfileScreen(
                        onBackClick = {
                            showEditProfileScreen = false
                        }
                    )
                }

                showFarmDetailsScreen -> {
                    FarmerFarmDetailsScreen(
                        onBackClick = {
                            showFarmDetailsScreen = false
                        }
                    )
                }

                showOrderHistoryScreen -> {
                    FarmerOrderHistoryScreen(
                        onBackClick = {
                            showOrderHistoryScreen = false
                        }
                    )
                }

                showAddProductScreen -> {
                    FarmerAddProductScreen(
                        onBackClick = {
                            showAddProductScreen = false
                        },
                        onProductSubmitted = {
                            showAddProductScreen = false
                            selectedTab = 1
                        }
                    )
                }

                else -> {
                    when (selectedTab) {
                        0 -> {
                            FarmerHomeScreen(
                                totalProducts = dashboardStats.totalProducts,
                                approvedProducts = dashboardStats.approvedProducts,
                                pendingProducts = dashboardStats.pendingProducts,
                                activeOrders = dashboardStats.activeOrders,
                                completedOrders = dashboardStats.completedOrders,
                                totalEarnings = dashboardStats.totalEarnings,
                                pendingPayment = dashboardStats.pendingPayment,
                                isDashboardLoading = dashboardStats.isLoading,
                                onProductsClick = {
                                    selectedTab = 1
                                    showAddProductScreen = false
                                },
                                onOrdersClick = {
                                    selectedTab = 2
                                    showAddProductScreen = false
                                },
                                onEarningsClick = {
                                    selectedTab = 3
                                    showAddProductScreen = false
                                },
                                onNotificationClick = {
                                    showNotificationScreen = true
                                }
                            )
                        }

                        1 -> {
                            FarmerProductsScreen(
                                onAddProductClick = {
                                    showAddProductScreen = true
                                }
                            )
                        }

                        2 -> {
                            FarmerOrdersScreen()
                        }

                        3 -> {
                            FarmerEarningsScreen()
                        }

                        4 -> {
                            FarmerProfileScreen(
                                onEditProfileClick = {
                                    showEditProfileScreen = true
                                },
                                onFarmDetailsClick = {
                                    showFarmDetailsScreen = true
                                },
                                onOrdersClick = {
                                    showOrderHistoryScreen = true
                                },
                                onLogoutConfirm = {
                                    onLogout()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class ProductStats(
    val totalProducts: Int = 0,
    val approvedProducts: Int = 0,
    val pendingProducts: Int = 0,
    val rejectedProducts: Int = 0
)

private data class OrderStats(
    val activeOrders: Int = 0,
    val completedOrders: Int = 0,
    val totalEarnings: Int = 0,
    val pendingPayment: Int = 0
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
                    .replace("/Kg", "")
                    .replace("/kg", "")
                    .replace("Kg", "")
                    .replace("kg", "")
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

@Composable
fun FarmerBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        Pair(stringResource(R.string.home), "🏠"),
        Pair(stringResource(R.string.products), "🌱"),
        Pair(stringResource(R.string.orders), "📦"),
        Pair(stringResource(R.string.earnings), "💰"),
        Pair(stringResource(R.string.profile), "👤")
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 10.dp)
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
                                21.sp
                            } else {
                                19.sp
                            }
                        )
                    },
                    label = {
                        Text(
                            text = title,
                            fontSize = 10.sp,
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