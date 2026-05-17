package com.example.krishisangam.farmer

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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFF003D22)
private val DeepGreen = Color(0xFF002514)
private val DarkGreen = Color(0xFF005C32)
private val AccentYellow = Color(0xFFFFC107)
private val TextLight = Color(0xFFF5FFF9)
private val TextMuted = Color(0xFFB9D8C7)
private val GlassDark = Color.White.copy(alpha = 0.095f)
private val GlassCard = Color.White.copy(alpha = 0.105f)
private val BorderGlass = Color.White.copy(alpha = 0.16f)
private val DialogGreen = Color(0xFF123D2B)
private val DialogText = Color(0xFFD8EDE3)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val SoftBlue = Color(0xFF2F6FED).copy(alpha = 0.18f)
private val BlueText = Color(0xFF7FB2FF)
private val DividerGlass = Color.White.copy(alpha = 0.14f)

private const val ORDER_FILTER_ALL = "all"
private const val ORDER_STATUS_PENDING = "pending"
private const val ORDER_STATUS_READY = "ready"
private const val ORDER_STATUS_TRANSIT = "in_transit"
private const val ORDER_STATUS_DELIVERED = "delivered"

data class FarmerOrder(
    val id: String,
    val buyerName: String,
    val productName: String,
    val productEmoji: String,
    val quantity: String,
    val amount: String,
    val firstPayment: String,
    val pendingPayment: String,
    val statusKey: String,
    val deliveryLocation: String,
    val orderDate: String
)

@Composable
fun FarmerOrdersScreen() {
    var selectedFilter by remember {
        mutableStateOf(ORDER_FILTER_ALL)
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedOrder by remember {
        mutableStateOf<FarmerOrder?>(null)
    }

    val pendingText = stringResource(R.string.pending)
    val readyText = stringResource(R.string.ready)
    val transitText = stringResource(R.string.in_transit)
    val deliveredText = stringResource(R.string.delivered)

    val orders = listOf(
        FarmerOrder(
            id = "#KS1234",
            buyerName = "Amit Buyer",
            productName = stringResource(R.string.sharbati_wheat),
            productEmoji = "🌾",
            quantity = "2 Quintal",
            amount = "₹4,300",
            firstPayment = stringResource(R.string.amount_received, "₹2,150"),
            pendingPayment = stringResource(R.string.amount_after_delivery, "₹2,150"),
            statusKey = ORDER_STATUS_READY,
            deliveryLocation = "Bhopal Drop Point",
            orderDate = stringResource(R.string.today)
        ),
        FarmerOrder(
            id = "#KS1235",
            buyerName = "Neha Buyer",
            productName = stringResource(R.string.rice),
            productEmoji = "🍚",
            quantity = "1 Quintal",
            amount = "₹3,200",
            firstPayment = stringResource(R.string.amount_received, "₹1,600"),
            pendingPayment = stringResource(R.string.amount_after_delivery, "₹1,600"),
            statusKey = ORDER_STATUS_TRANSIT,
            deliveryLocation = "Sehore Community Hub",
            orderDate = stringResource(R.string.yesterday)
        ),
        FarmerOrder(
            id = "#KS1236",
            buyerName = "Rohit Buyer",
            productName = stringResource(R.string.tomato),
            productEmoji = "🍅",
            quantity = "80 kg",
            amount = "₹1,760",
            firstPayment = stringResource(R.string.amount_received, "₹880"),
            pendingPayment = stringResource(R.string.amount_after_delivery, "₹880"),
            statusKey = ORDER_STATUS_PENDING,
            deliveryLocation = "Bhopal Agro Node",
            orderDate = "10 May"
        ),
        FarmerOrder(
            id = "#KS1237",
            buyerName = "Suman Buyer",
            productName = stringResource(R.string.capsicum),
            productEmoji = "🫑",
            quantity = "120 kg",
            amount = "₹4,200",
            firstPayment = stringResource(R.string.amount_received, "₹2,100"),
            pendingPayment = stringResource(R.string.released),
            statusKey = ORDER_STATUS_DELIVERED,
            deliveryLocation = "Raisen Drop Point",
            orderDate = "9 May"
        )
    )

    val filterMatchedOrders = if (selectedFilter == ORDER_FILTER_ALL) {
        orders
    } else {
        orders.filter { order ->
            order.statusKey == selectedFilter
        }
    }

    val visibleOrders = filterMatchedOrders.filter { order ->
        val query = searchText.trim().lowercase()

        if (query.isBlank()) {
            true
        } else {
            val statusText = when (order.statusKey) {
                ORDER_STATUS_PENDING -> pendingText
                ORDER_STATUS_READY -> readyText
                ORDER_STATUS_TRANSIT -> transitText
                ORDER_STATUS_DELIVERED -> deliveredText
                else -> ""
            }

            order.id.lowercase().contains(query) ||
                    order.buyerName.lowercase().contains(query) ||
                    order.productName.lowercase().contains(query) ||
                    order.quantity.lowercase().contains(query) ||
                    order.amount.lowercase().contains(query) ||
                    order.deliveryLocation.lowercase().contains(query) ||
                    order.orderDate.lowercase().contains(query) ||
                    statusText.lowercase().contains(query)
        }
    }

    if (selectedOrder != null) {
        FarmerOrderDetailDialog(
            order = selectedOrder!!,
            onDismiss = {
                selectedOrder = null
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
                .padding(horizontal = 18.dp, vertical = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.my_orders),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.orders_received_for_your_products),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            FarmerOrderSearchBar(
                searchText = searchText,
                onSearchTextChange = { newValue ->
                    searchText = newValue
                },
                onClearClick = {
                    searchText = ""
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            FarmerOrderSummaryRow(
                totalOrders = orders.size,
                readyOrders = orders.count { it.statusKey == ORDER_STATUS_READY },
                transitOrders = orders.count { it.statusKey == ORDER_STATUS_TRANSIT },
                deliveredOrders = orders.count { it.statusKey == ORDER_STATUS_DELIVERED }
            )

            Spacer(modifier = Modifier.height(18.dp))

            FarmerOrderFilterRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (searchText.isBlank()) {
                    "${visibleOrders.size} orders"
                } else {
                    "${visibleOrders.size} matching orders"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (visibleOrders.isEmpty()) {
                EmptyFarmerOrderState(
                    isSearching = searchText.isNotBlank()
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(visibleOrders) { order ->
                        FarmerOrderCard(
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
fun FarmerOrderSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = GlassDark
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔍",
                fontSize = 19.sp
            )

            Spacer(modifier = Modifier.size(8.dp))

            TextField(
                value = searchText,
                onValueChange = { newValue ->
                    onSearchTextChange(newValue)
                },
                placeholder = {
                    Text(
                        text = "Search order, buyer, crop, location",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = AccentYellow,
                    focusedTextColor = TextLight,
                    unfocusedTextColor = TextLight,
                    focusedPlaceholderColor = TextMuted,
                    unfocusedPlaceholderColor = TextMuted
                ),
                modifier = Modifier.weight(1f)
            )

            if (searchText.isNotBlank()) {
                Text(
                    text = "×",
                    color = TextMuted,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable {
                        onClearClick()
                    }
                )
            }
        }
    }
}

@Composable
fun FarmerOrderSummaryRow(
    totalOrders: Int,
    readyOrders: Int,
    transitOrders: Int,
    deliveredOrders: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FarmerOrderSummaryCard(
            title = stringResource(R.string.total),
            value = totalOrders.toString(),
            icon = "📦",
            modifier = Modifier.weight(1f)
        )

        FarmerOrderSummaryCard(
            title = stringResource(R.string.ready),
            value = readyOrders.toString(),
            icon = "✅",
            modifier = Modifier.weight(1f)
        )

        FarmerOrderSummaryCard(
            title = stringResource(R.string.transit),
            value = transitOrders.toString(),
            icon = "🚚",
            modifier = Modifier.weight(1f)
        )

        FarmerOrderSummaryCard(
            title = stringResource(R.string.done),
            value = deliveredOrders.toString(),
            icon = "🎯",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FarmerOrderSummaryCard(
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
            Box(
                modifier = Modifier
                    .size(28.dp)
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
                    text = icon,
                    fontSize = 14.sp
                )
            }

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
                maxLines = 1,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun FarmerOrderFilterRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FarmerOrderFilterChip(
            title = stringResource(R.string.all),
            filterKey = ORDER_FILTER_ALL,
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected
        )

        FarmerOrderFilterChip(
            title = stringResource(R.string.pending),
            filterKey = ORDER_STATUS_PENDING,
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected
        )

        FarmerOrderFilterChip(
            title = stringResource(R.string.ready),
            filterKey = ORDER_STATUS_READY,
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected
        )

        FarmerOrderFilterChip(
            title = stringResource(R.string.in_transit),
            filterKey = ORDER_STATUS_TRANSIT,
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected
        )
    }
}

@Composable
fun FarmerOrderFilterChip(
    title: String,
    filterKey: String,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val selected = filterKey == selectedFilter

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (selected) {
                    PrimaryGreen
                } else {
                    GlassCard
                }
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
                onFilterSelected(filterKey)
            }
            .padding(horizontal = 11.dp, vertical = 8.dp),
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
fun FarmerOrderCard(
    order: FarmerOrder,
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
                        fontSize = 31.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.order_product_format,
                            order.id,
                            order.productName
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = stringResource(
                            R.string.buyer_name_format,
                            order.buyerName
                        ),
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Text(
                        text = stringResource(
                            R.string.date_format,
                            order.orderDate
                        ),
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                FarmerOrderStatusBadge(
                    statusKey = order.statusKey
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = DividerGlass
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FarmerOrderInfoBox(
                    title = stringResource(R.string.quantity),
                    value = order.quantity
                )

                FarmerOrderInfoBox(
                    title = stringResource(R.string.amount),
                    value = order.amount
                )

                FarmerOrderInfoBox(
                    title = stringResource(R.string.location),
                    value = stringResource(R.string.node)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PrimaryGreen.copy(alpha = 0.14f)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = PrimaryGreen.copy(alpha = 0.24f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.payment_split),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(
                            R.string.first_half_format,
                            order.firstPayment
                        ),
                        fontSize = 12.sp,
                        color = AccentYellow,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = stringResource(
                            R.string.second_half_format,
                            order.pendingPayment
                        ),
                        fontSize = 12.sp,
                        color = DialogText
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(
                    R.string.drop_point_format,
                    order.deliveryLocation
                ),
                fontSize = 12.sp,
                color = TextMuted,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
fun FarmerOrderInfoBox(
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
            color = TextMuted
        )
    }
}

@Composable
fun FarmerOrderStatusBadge(
    statusKey: String
) {
    val bgColor = when (statusKey) {
        ORDER_STATUS_READY -> SoftGreen
        ORDER_STATUS_DELIVERED -> SoftGreen
        ORDER_STATUS_TRANSIT -> SoftBlue
        else -> SoftYellow
    }

    val textColor = when (statusKey) {
        ORDER_STATUS_READY -> PrimaryGreen
        ORDER_STATUS_DELIVERED -> PrimaryGreen
        ORDER_STATUS_TRANSIT -> BlueText
        else -> AccentYellow
    }

    val statusText = when (statusKey) {
        ORDER_STATUS_READY -> stringResource(R.string.ready)
        ORDER_STATUS_DELIVERED -> stringResource(R.string.delivered)
        ORDER_STATUS_TRANSIT -> stringResource(R.string.in_transit)
        else -> stringResource(R.string.pending)
    }

    Text(
        text = statusText,
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
fun EmptyFarmerOrderState(
    isSearching: Boolean
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
                .padding(vertical = 40.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isSearching) "🔍" else "📦",
                fontSize = 42.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isSearching) {
                    "No matching orders found"
                } else {
                    stringResource(R.string.no_orders_yet)
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isSearching) {
                    "Try searching buyer name, crop, order ID, location, or status."
                } else {
                    stringResource(R.string.orders_for_your_products)
                },
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun FarmerOrderDetailDialog(
    order: FarmerOrder,
    onDismiss: () -> Unit
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
                text = order.id,
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
                    text = order.productName,
                    color = TextLight,
                    fontSize = 17.sp,
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
                    text = "Date: ${order.orderDate}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Drop point: ${order.deliveryLocation}",
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Payment split",
                    color = TextLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = order.firstPayment,
                    color = AccentYellow,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = order.pendingPayment,
                    color = DialogText,
                    fontSize = 14.sp
                )
            }
        }
    )
}