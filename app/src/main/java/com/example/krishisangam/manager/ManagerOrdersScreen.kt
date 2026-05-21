package com.example.krishisangam.manager

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

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
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val SoftBlue = Color(0xFF2F6FED).copy(alpha = 0.18f)
private val BlueText = Color(0xFF7FB2FF)
private val DividerGlass = Color.White.copy(alpha = 0.14f)

private const val MANAGER_ORDER_FILTER_ALL = "All"
private const val MANAGER_ORDER_STATUS_READY = "Ready"
private const val MANAGER_ORDER_STATUS_TRANSIT = "In Transit"
private const val MANAGER_ORDER_STATUS_DELIVERED = "Delivered"

data class ManagerOrderItem(
    val documentId: String,
    val orderId: String,
    val buyerName: String,
    val farmerName: String,
    val productName: String,
    val productEmoji: String,
    val quantity: String,
    val amount: String,
    val status: String,
    val orderStatus: String,
    val trackingStep: Int,
    val nodeVerificationDone: Boolean,
    val qualityCheckDone: Boolean,
    val packagingDone: Boolean,
    val dispatchPlanningDone: Boolean,
    val outForDeliveryDone: Boolean,
    val deliveredDone: Boolean,
    val pickupNode: String,
    val dropPoint: String,
    val orderDate: String
)

@Composable
fun ManagerOrdersScreen(
    openOrderId: String? = null
) {
    var selectedFilter by remember {
        mutableStateOf(MANAGER_ORDER_FILTER_ALL)
    }

    var selectedOrder by remember {
        mutableStateOf<ManagerOrderItem?>(null)
    }

    var orders by remember {
        mutableStateOf<List<ManagerOrderItem>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    DisposableEffect(Unit) {
        val listener = firestore
            .collection("orders")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    isLoading = false
                    errorMessage = error?.message ?: "Unable to load orders."
                    orders = emptyList()
                    return@addSnapshotListener
                }

                orders = snapshot.documents.map { document ->
                    document.toManagerOrderItem()
                }

                selectedOrder = selectedOrder?.let { selected ->
                    orders.firstOrNull { it.documentId == selected.documentId }
                }

                isLoading = false
                errorMessage = ""
            }

        onDispose {
            listener.remove()
        }
    }

    LaunchedEffect(openOrderId, orders) {
        if (!openOrderId.isNullOrBlank() && orders.isNotEmpty()) {
            selectedOrder = orders.firstOrNull { order ->
                order.orderId == openOrderId || order.documentId == openOrderId
            }

            selectedFilter = MANAGER_ORDER_FILTER_ALL
        }
    }

    val visibleOrders = when (selectedFilter) {
        MANAGER_ORDER_STATUS_READY -> {
            orders.filter { it.status == MANAGER_ORDER_STATUS_READY }
        }

        MANAGER_ORDER_STATUS_TRANSIT -> {
            orders.filter { it.status == MANAGER_ORDER_STATUS_TRANSIT }
        }

        MANAGER_ORDER_STATUS_DELIVERED -> {
            orders.filter { it.status == MANAGER_ORDER_STATUS_DELIVERED }
        }

        else -> orders
    }

    if (selectedOrder != null) {
        ManagerOrderDetailDialog(
            order = selectedOrder!!,
            onDismiss = {
                selectedOrder = null
            },
            onUpdateTrackingStep = { documentId, step ->
                updateOrderTrackingStep(
                    firestore = firestore,
                    documentId = documentId,
                    step = step
                )
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
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Text(
                text = "Order Management",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Track buyer orders, dispatch and delivery status",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            ManagerOrderSummaryRow(
                total = orders.size,
                ready = orders.count { it.status == MANAGER_ORDER_STATUS_READY },
                transit = orders.count { it.status == MANAGER_ORDER_STATUS_TRANSIT },
                delivered = orders.count { it.status == MANAGER_ORDER_STATUS_DELIVERED }
            )

            Spacer(modifier = Modifier.height(18.dp))

            ManagerOrderFilterRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { filter ->
                    selectedFilter = filter
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = when {
                    isLoading -> "Loading orders..."
                    errorMessage.isNotBlank() -> errorMessage
                    else -> "${visibleOrders.size} orders"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (!isLoading && visibleOrders.isEmpty()) {
                EmptyManagerOrdersState(
                    selectedFilter = selectedFilter
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = visibleOrders,
                        key = { order ->
                            order.documentId
                        }
                    ) { order ->
                        ManagerOrderCard(
                            order = order,
                            onClick = {
                                selectedOrder = order
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(130.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ManagerOrderSummaryRow(
    total: Int,
    ready: Int,
    transit: Int,
    delivered: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ManagerOrderSummaryCard(
            title = "Total",
            value = total.toString(),
            icon = "📦",
            modifier = Modifier.weight(1f)
        )

        ManagerOrderSummaryCard(
            title = "Ready",
            value = ready.toString(),
            icon = "✅",
            modifier = Modifier.weight(1f)
        )

        ManagerOrderSummaryCard(
            title = "Transit",
            value = transit.toString(),
            icon = "🚚",
            modifier = Modifier.weight(1f)
        )

        ManagerOrderSummaryCard(
            title = "Done",
            value = delivered.toString(),
            icon = "🎯",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ManagerOrderSummaryCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = GlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = icon,
                fontSize = 18.sp
            )

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Text(
                text = title,
                fontSize = 9.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ManagerOrderFilterRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ManagerOrderFilterChip(
            title = MANAGER_ORDER_FILTER_ALL,
            selectedFilter = selectedFilter,
            onClick = {
                onFilterSelected(MANAGER_ORDER_FILTER_ALL)
            },
            modifier = Modifier.weight(1f)
        )

        ManagerOrderFilterChip(
            title = MANAGER_ORDER_STATUS_READY,
            selectedFilter = selectedFilter,
            onClick = {
                onFilterSelected(MANAGER_ORDER_STATUS_READY)
            },
            modifier = Modifier.weight(1f)
        )

        ManagerOrderFilterChip(
            title = MANAGER_ORDER_STATUS_TRANSIT,
            selectedFilter = selectedFilter,
            onClick = {
                onFilterSelected(MANAGER_ORDER_STATUS_TRANSIT)
            },
            modifier = Modifier.weight(1f)
        )

        ManagerOrderFilterChip(
            title = MANAGER_ORDER_STATUS_DELIVERED,
            selectedFilter = selectedFilter,
            onClick = {
                onFilterSelected(MANAGER_ORDER_STATUS_DELIVERED)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ManagerOrderFilterChip(
    title: String,
    selectedFilter: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = title == selectedFilter

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (selected) {
                    PrimaryGreen
                } else {
                    GlassCard
                },
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (selected) {
                        Color.White.copy(alpha = 0.18f)
                    } else {
                        BorderGlass
                    }
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (selected) Color.White else TextLight,
            maxLines = 1
        )
    }
}

@Composable
fun ManagerOrderCard(
    order: ManagerOrderItem,
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
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
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
                        text = order.productEmoji,
                        fontSize = 30.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = "${order.orderId} • ${order.productName}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Text(
                        text = "Buyer: ${order.buyerName}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Text(
                        text = "Farmer: ${order.farmerName}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                ManagerOrderStatusBadge(
                    status = order.status
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = DividerGlass)

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ManagerOrderInfoBox(
                    title = "Quantity",
                    value = order.quantity
                )

                ManagerOrderInfoBox(
                    title = "Amount",
                    value = order.amount
                )

                ManagerOrderInfoBox(
                    title = "Date",
                    value = order.orderDate
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Pickup: ${order.pickupNode}",
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Drop: ${order.dropPoint}",
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ManagerOrderInfoBox(
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextLight,
            maxLines = 1
        )

        Text(
            text = title,
            fontSize = 10.sp,
            color = TextMuted,
            maxLines = 1
        )
    }
}

@Composable
fun ManagerOrderStatusBadge(
    status: String
) {
    val bgColor = when (status) {
        MANAGER_ORDER_STATUS_DELIVERED -> SoftGreen
        MANAGER_ORDER_STATUS_TRANSIT -> SoftBlue
        else -> SoftYellow
    }

    val textColor = when (status) {
        MANAGER_ORDER_STATUS_DELIVERED -> PrimaryGreen
        MANAGER_ORDER_STATUS_TRANSIT -> BlueText
        else -> AccentYellow
    }

    Text(
        text = status,
        fontSize = 10.sp,
        fontWeight = FontWeight.ExtraBold,
        color = textColor,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = textColor.copy(alpha = 0.24f)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 9.dp, vertical = 5.dp),
        maxLines = 1
    )
}

@Composable
fun EmptyManagerOrdersState(
    selectedFilter: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                .fillMaxWidth()
                .padding(vertical = 45.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📦",
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No $selectedFilter orders",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Orders will appear here after buyers place orders.",
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ManagerOrderDetailDialog(
    order: ManagerOrderItem,
    onDismiss: () -> Unit,
    onUpdateTrackingStep: (String, Int) -> Unit
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
                text = order.orderId,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = order.productEmoji,
                    fontSize = 38.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Product: ${order.productName}",
                    color = TextLight,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Buyer: ${order.buyerName}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Farmer: ${order.farmerName}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Quantity: ${order.quantity}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Amount: ${order.amount}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Status: ${order.status}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Pickup: ${order.pickupNode}",
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Drop: ${order.dropPoint}",
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Date: ${order.orderDate}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Update Tracking",
                    color = TextLight,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                ManagerTrackingActionButton(
                    title = "✓ Agro Node Verified",
                    enabled = !order.nodeVerificationDone,
                    onClick = {
                        onUpdateTrackingStep(order.documentId, 1)
                    }
                )

                ManagerTrackingActionButton(
                    title = "✓ Quality Checked",
                    enabled = order.nodeVerificationDone && !order.qualityCheckDone,
                    onClick = {
                        onUpdateTrackingStep(order.documentId, 2)
                    }
                )

                ManagerTrackingActionButton(
                    title = "✓ Packaged",
                    enabled = order.qualityCheckDone && !order.packagingDone,
                    onClick = {
                        onUpdateTrackingStep(order.documentId, 3)
                    }
                )

                ManagerTrackingActionButton(
                    title = "✓ Dispatch Planned",
                    enabled = order.packagingDone && !order.dispatchPlanningDone,
                    onClick = {
                        onUpdateTrackingStep(order.documentId, 4)
                    }
                )

                ManagerTrackingActionButton(
                    title = "✓ Out For Delivery",
                    enabled = order.dispatchPlanningDone && !order.outForDeliveryDone,
                    onClick = {
                        onUpdateTrackingStep(order.documentId, 5)
                    }
                )

                ManagerTrackingActionButton(
                    title = "✓ Delivered",
                    enabled = order.outForDeliveryDone && !order.deliveredDone,
                    onClick = {
                        onUpdateTrackingStep(order.documentId, 6)
                    }
                )
            }
        }
    )
}

@Composable
private fun ManagerTrackingActionButton(
    title: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (enabled) {
                    PrimaryGreen.copy(alpha = 0.28f)
                } else {
                    Color.White.copy(alpha = 0.08f)
                }
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (enabled) {
                        PrimaryGreen.copy(alpha = 0.45f)
                    } else {
                        BorderGlass
                    }
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(enabled = enabled) {
                onClick()
            }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = if (enabled) TextLight else TextMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

private fun updateOrderTrackingStep(
    firestore: FirebaseFirestore,
    documentId: String,
    step: Int
) {
    val updateMap = when (step) {
        1 -> mapOf(
            "orderStatus" to "agro_node_verified",
            "trackingStep" to 1,
            "nodeVerificationDone" to true
        )

        2 -> mapOf(
            "orderStatus" to "quality_checked",
            "trackingStep" to 2,
            "nodeVerificationDone" to true,
            "qualityCheckDone" to true
        )

        3 -> mapOf(
            "orderStatus" to "packaging",
            "trackingStep" to 3,
            "nodeVerificationDone" to true,
            "qualityCheckDone" to true,
            "packagingDone" to true
        )

        4 -> mapOf(
            "orderStatus" to "dispatch_planned",
            "trackingStep" to 4,
            "nodeVerificationDone" to true,
            "qualityCheckDone" to true,
            "packagingDone" to true,
            "dispatchPlanningDone" to true
        )

        5 -> mapOf(
            "orderStatus" to "out_for_delivery",
            "trackingStep" to 5,
            "nodeVerificationDone" to true,
            "qualityCheckDone" to true,
            "packagingDone" to true,
            "dispatchPlanningDone" to true,
            "outForDeliveryDone" to true
        )

        6 -> mapOf(
            "orderStatus" to "delivered",
            "trackingStep" to 6,
            "nodeVerificationDone" to true,
            "qualityCheckDone" to true,
            "packagingDone" to true,
            "dispatchPlanningDone" to true,
            "outForDeliveryDone" to true,
            "deliveredDone" to true
        )

        else -> emptyMap()
    }

    if (updateMap.isNotEmpty()) {
        firestore
            .collection("orders")
            .document(documentId)
            .update(updateMap)
    }
}

private fun DocumentSnapshot.toManagerOrderItem(): ManagerOrderItem {
    val orderStatus = getString("orderStatus") ?: "order_placed"
    val trackingStep = getLong("trackingStep")?.toInt() ?: orderStatusToStep(orderStatus)

    return ManagerOrderItem(
        documentId = id,
        orderId = getOrderIdText(),
        buyerName = getString("buyerName") ?: "Buyer",
        farmerName = getString("farmerName") ?: "Farmer",
        productName = getString("productName")
            ?: getString("name")
            ?: "Product",
        productEmoji = getString("productEmoji") ?: getProductEmoji(),
        quantity = getString("quantity")
            ?: getString("quantityText")
            ?: "1",
        amount = getAmountText(
            keys = listOf(
                "totalAmount",
                "orderTotal",
                "amount",
                "subtotal"
            )
        ),
        status = orderStatus.toManagerDisplayStatus(),
        orderStatus = orderStatus,
        trackingStep = trackingStep,
        nodeVerificationDone = getBoolean("nodeVerificationDone") ?: (trackingStep >= 1),
        qualityCheckDone = getBoolean("qualityCheckDone") ?: (trackingStep >= 2),
        packagingDone = getBoolean("packagingDone") ?: (trackingStep >= 3),
        dispatchPlanningDone = getBoolean("dispatchPlanningDone") ?: (trackingStep >= 4),
        outForDeliveryDone = getBoolean("outForDeliveryDone") ?: (trackingStep >= 5),
        deliveredDone = getBoolean("deliveredDone") ?: (trackingStep >= 6),
        pickupNode = getString("pickupNode")
            ?: getString("agroNode")
            ?: "Bhopal Agro Node 01",
        dropPoint = getString("dropPoint")
            ?: getString("deliveryAddress")
            ?: getString("address")
            ?: "Buyer Drop Point",
        orderDate = getReadableDate()
    )
}

private fun String.toManagerDisplayStatus(): String {
    return when {
        equals("out_for_delivery", ignoreCase = true) -> MANAGER_ORDER_STATUS_TRANSIT
        equals("delivered", ignoreCase = true) -> MANAGER_ORDER_STATUS_DELIVERED
        else -> MANAGER_ORDER_STATUS_READY
    }
}

private fun orderStatusToStep(
    orderStatus: String
): Int {
    return when {
        orderStatus.equals("agro_node_verified", ignoreCase = true) -> 1
        orderStatus.equals("quality_checked", ignoreCase = true) -> 2
        orderStatus.equals("packaging", ignoreCase = true) -> 3
        orderStatus.equals("dispatch_planned", ignoreCase = true) -> 4
        orderStatus.equals("out_for_delivery", ignoreCase = true) -> 5
        orderStatus.equals("delivered", ignoreCase = true) -> 6
        orderStatus.equals("completed", ignoreCase = true) -> 6
        else -> 0
    }
}

private fun DocumentSnapshot.getOrderIdText(): String {
    val rawOrderId = getString("orderId") ?: id.takeLast(6).uppercase()

    return if (rawOrderId.startsWith("#")) {
        rawOrderId
    } else {
        "#$rawOrderId"
    }
}

private fun DocumentSnapshot.getAmountText(
    keys: List<String>
): String {
    keys.forEach { key ->
        val value = get(key)

        when (value) {
            is Number -> return "₹${value.toDouble().roundToInt()}"

            is String -> {
                return if (value.startsWith("₹")) {
                    value
                } else {
                    "₹$value"
                }
            }
        }
    }

    return "₹0"
}

private fun DocumentSnapshot.getProductEmoji(): String {
    val productName = (
            getString("productName")
                ?: getString("name")
                ?: ""
            ).lowercase()

    return when {
        productName.contains("rice") -> "🍚"
        productName.contains("tomato") -> "🍅"
        productName.contains("capsicum") -> "🫑"
        productName.contains("wheat") -> "🌾"
        productName.contains("grain") -> "🌾"
        else -> "📦"
    }
}

private fun DocumentSnapshot.getReadableDate(): String {
    val rawCreatedAt = get("createdAt")

    return when (rawCreatedAt) {
        is Timestamp -> {
            try {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(rawCreatedAt.toDate())
            } catch (exception: Exception) {
                "Today"
            }
        }

        is Number -> {
            try {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(rawCreatedAt.toLong())
            } catch (exception: Exception) {
                "Today"
            }
        }

        is String -> rawCreatedAt.ifBlank { "Today" }

        else -> "Today"
    }
}