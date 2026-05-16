package com.example.krishisangam.farmer

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftYellow = Color(0xFFFFF4D6)
private val YellowText = Color(0xFFB8860B)
private val SoftBlue = Color(0xFFE6F0FF)
private val BlueText = Color(0xFF2F6FED)

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

    val visibleOrders = if (selectedFilter == ORDER_FILTER_ALL) {
        orders
    } else {
        orders.filter { order ->
            order.statusKey == selectedFilter
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.my_orders),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.orders_received_for_your_products),
            fontSize = 14.sp,
            color = Color.Gray
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

        Spacer(modifier = Modifier.height(18.dp))

        if (visibleOrders.isEmpty()) {
            EmptyFarmerOrderState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(visibleOrders) { order ->
                    FarmerOrderCard(order = order)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
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
        modifier = modifier.height(84.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
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
                    .size(27.dp)
                    .clip(CircleShape)
                    .background(LightGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 14.sp
                )
            }

            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Text(
                text = title,
                fontSize = 9.sp,
                color = Color.Gray,
                maxLines = 1
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
                    Color.White
                }
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
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else TextDark,
            maxLines = 1
        )
    }
}

@Composable
fun FarmerOrderCard(
    order: FarmerOrder
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
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
                        .background(LightGreen),
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
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = stringResource(
                            R.string.buyer_name_format,
                            order.buyerName
                        ),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = stringResource(
                            R.string.date_format,
                            order.orderDate
                        ),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                FarmerOrderStatusBadge(
                    statusKey = order.statusKey
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = Color(0xFFEDEDED)
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
                    containerColor = LightGreen
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
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(
                            R.string.first_half_format,
                            order.firstPayment
                        ),
                        fontSize = 12.sp,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = stringResource(
                            R.string.second_half_format,
                            order.pendingPayment
                        ),
                        fontSize = 12.sp,
                        color = TextDark
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
                color = Color.Gray
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
            fontWeight = FontWeight.Bold,
            color = TextDark,
            maxLines = 1
        )

        Text(
            text = title,
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun FarmerOrderStatusBadge(
    statusKey: String
) {
    val bgColor = when (statusKey) {
        ORDER_STATUS_READY -> LightGreen
        ORDER_STATUS_DELIVERED -> LightGreen
        ORDER_STATUS_TRANSIT -> SoftBlue
        else -> SoftYellow
    }

    val textColor = when (statusKey) {
        ORDER_STATUS_READY -> PrimaryGreen
        ORDER_STATUS_DELIVERED -> PrimaryGreen
        ORDER_STATUS_TRANSIT -> BlueText
        else -> YellowText
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
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 9.dp, vertical = 5.dp),
        maxLines = 1
    )
}

@Composable
fun EmptyFarmerOrderState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📦",
                fontSize = 42.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.no_orders_yet),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.orders_for_your_products),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}