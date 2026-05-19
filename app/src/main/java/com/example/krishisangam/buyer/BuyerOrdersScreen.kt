package com.example.krishisangam.buyer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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

private val OrderPrimaryGreen = Color(0xFF01AC66)
private val OrderBackgroundColor = Color(0xFF003D22)
private val OrderDeepGreen = Color(0xFF002514)
private val OrderDarkGreen = Color(0xFF005C32)
private val OrderAccentYellow = Color(0xFFFFC107)
private val OrderTextLight = Color(0xFFF5FFF9)
private val OrderTextMuted = Color(0xFFB9D8C7)
private val OrderGlassDark = Color.White.copy(alpha = 0.095f)
private val OrderGlassCard = Color.White.copy(alpha = 0.105f)
private val OrderBorderGlass = Color.White.copy(alpha = 0.16f)
private val OrderDeleteRed = Color(0xFFFF6B6B)

@Composable
fun BuyerOrdersScreen() {
    val orders = BuyerOrderStore.orders
    val confirmedOrders = BuyerConfirmedOrderStore.confirmedOrders

    var selectedTrackOrder by remember {
        mutableStateOf<BuyerConfirmedOrder?>(null)
    }

    if (selectedTrackOrder != null) {
        BuyerTrackOrderScreen(
            order = selectedTrackOrder!!,
            onBackClick = {
                selectedTrackOrder = null
            }
        )
        return
    }

    var showCheckoutScreen by remember {
        mutableStateOf(false)
    }

    if (showCheckoutScreen) {
        BuyerCheckoutScreen(
            onBackClick = {
                showCheckoutScreen = false
            },
            onOrderCompleted = {
                showCheckoutScreen = false
            }
        )
        return
    }

    val subtotal by remember {
        derivedStateOf {
            orders.fold(0.0) { total, order ->
                total + (extractOrderPrice(order.price) * order.quantity)
            }
        }
    }

    val packagingCharge = if (orders.isEmpty()) 0.0 else 35.0
    val logisticsCharge = if (orders.isEmpty()) 0.0 else 80.0
    val platformFee = if (orders.isEmpty()) 0.0 else 15.0
    val tax = subtotal * 0.05
    val totalAmount = subtotal + packagingCharge + logisticsCharge + platformFee + tax

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        OrderDarkGreen,
                        OrderBackgroundColor,
                        OrderDeepGreen
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            BuyerOrdersTopBar()

            Spacer(modifier = Modifier.height(18.dp))

            if (orders.isEmpty() && confirmedOrders.isEmpty()) {
                BuyerEmptyOrdersMessage()
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 110.dp)
                ) {
                    if (confirmedOrders.isNotEmpty()) {
                        Text(
                            text = "Confirmed Orders",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OrderTextLight
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        confirmedOrders.forEach { confirmedOrder ->
                            BuyerConfirmedOrderCard(
                                order = confirmedOrder,
                                onTrackOrderClick = {
                                    selectedTrackOrder = confirmedOrder
                                }
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (orders.isNotEmpty()) {
                        orders.forEach { order ->
                            BuyerOrderItemCard(
                                order = order
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        BuyerBillSummaryCard(
                            subtotal = subtotal,
                            packagingCharge = packagingCharge,
                            logisticsCharge = logisticsCharge,
                            platformFee = platformFee,
                            tax = tax,
                            totalAmount = totalAmount
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        BuyerCheckoutButton(
                            onClick = {
                                showCheckoutScreen = true
                            }
                        )
                    } else {
                        BuyerNewCartHintCard()
                    }
                }
            }
        }
    }
}

@Composable
fun BuyerOrdersTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.my_orders),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrderTextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.products_added_from_approved_farmer_listings),
                fontSize = 13.sp,
                color = OrderTextMuted,
                lineHeight = 19.sp
            )
        }

        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(OrderGlassDark)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = OrderBorderGlass
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🛒",
                fontSize = 23.sp
            )
        }
    }
}

@Composable
fun BuyerEmptyOrdersMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = OrderGlassCard
            ),
            border = BorderStroke(
                width = 1.dp,
                color = OrderBorderGlass
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
                    text = "🛒",
                    fontSize = 48.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.no_orders_added_yet),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OrderTextLight
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.add_approved_products_from_marketplace),
                    fontSize = 13.sp,
                    color = OrderTextMuted,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
fun BuyerOrderItemCard(
    order: BuyerOrderItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp)
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = OrderGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = OrderBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = OrderBorderGlass
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = order.emoji,
                    fontSize = 34.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = order.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OrderTextLight,
                    maxLines = 1
                )

                Text(
                    text = order.type,
                    fontSize = 11.sp,
                    color = OrderTextMuted,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = order.price,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OrderAccentYellow,
                    maxLines = 1
                )
            }

            BuyerQuantityControl(
                order = order
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "🗑",
                fontSize = 18.sp,
                color = OrderDeleteRed,
                modifier = Modifier.clickable {
                    BuyerOrderStore.removeOrder(order)
                }
            )
        }
    }
}

@Composable
fun BuyerQuantityControl(
    order: BuyerOrderItem
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(27.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.12f))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = OrderBorderGlass
                    ),
                    shape = CircleShape
                )
                .clickable {
                    BuyerOrderStore.decreaseQuantity(order)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrderTextLight
            )
        }

        Text(
            text = order.quantity.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = OrderTextLight,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Box(
            modifier = Modifier
                .size(27.dp)
                .clip(CircleShape)
                .background(OrderPrimaryGreen)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.18f)
                    ),
                    shape = CircleShape
                )
                .clickable {
                    BuyerOrderStore.increaseQuantity(order)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun BuyerBillSummaryCard(
    subtotal: Double,
    packagingCharge: Double,
    logisticsCharge: Double,
    platformFee: Double,
    tax: Double,
    totalAmount: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = OrderGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = OrderBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.bill_split),
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrderTextLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            BuyerBillRow(
                title = stringResource(R.string.product_subtotal),
                amount = "₹${subtotal.toInt()}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            BuyerBillRow(
                title = stringResource(R.string.packaging_charge),
                amount = "₹${packagingCharge.toInt()}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            BuyerBillRow(
                title = stringResource(R.string.logistics_charge),
                amount = "₹${logisticsCharge.toInt()}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            BuyerBillRow(
                title = stringResource(R.string.platform_fee),
                amount = "₹${platformFee.toInt()}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            BuyerBillRow(
                title = stringResource(R.string.tax_5),
                amount = "₹${tax.toInt()}"
            )

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = Color.White.copy(alpha = 0.16f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.total),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OrderTextLight,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "₹${totalAmount.toInt()}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OrderAccentYellow
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.transparent_charges_message),
                fontSize = 12.sp,
                color = OrderTextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun BuyerBillRow(
    title: String,
    amount: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = OrderTextMuted,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = OrderTextLight
        )
    }
}

@Composable
fun BuyerCheckoutButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        OrderPrimaryGreen,
                        Color(0xFF00985B),
                        Color(0xFF007A49)
                    )
                )
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.18f)
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.proceed_to_checkout),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        )
    }
}

fun extractOrderPrice(priceText: String): Double {
    val cleanedPrice = priceText
        .replace("₹", "")
        .replace(",", "")
        .replace("/Qtl", "")
        .replace("/ Qtl", "")
        .replace("/kg", "")
        .replace("/ kg", "")
        .replace("Qtl", "")
        .replace("kg", "")
        .trim()

    return cleanedPrice.toDoubleOrNull() ?: 0.0
}

@Composable
fun BuyerConfirmedOrderCard(
    order: BuyerConfirmedOrder,
    onTrackOrderClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = OrderGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = OrderBorderGlass
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Confirmed Order",
                fontSize = 13.sp,
                color = OrderTextMuted
            )

            Text(
                text = order.orderId,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrderAccentYellow
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Status: ${formatOrderStatus(order.orderStatus)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = OrderTextLight
            )

            Spacer(modifier = Modifier.height(10.dp))

            order.items.forEach { item ->
                Text(
                    text = "${item.emoji} ${item.name} × ${item.quantity}",
                    fontSize = 13.sp,
                    color = OrderTextMuted,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Advance Paid",
                    fontSize = 13.sp,
                    color = OrderTextMuted,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "₹${order.advanceAmount}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrderAccentYellow
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Remaining",
                    fontSize = 13.sp,
                    color = OrderTextMuted,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "₹${order.remainingAmount}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrderTextLight
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(OrderPrimaryGreen)
                    .clickable {
                        onTrackOrderClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Track Order",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun BuyerNewCartHintCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = OrderGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = OrderBorderGlass
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "Add more products",
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = OrderTextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your previous order is confirmed. Add new products from Marketplace to start a new bill below this order.",
                fontSize = 13.sp,
                color = OrderTextMuted,
                lineHeight = 19.sp
            )
        }
    }
}

fun formatOrderStatus(status: String): String {
    return when (status) {
        "order_placed" -> "Order Placed"
        "agro_node_verified" -> "Agro Node Verified"
        "quality_checked" -> "Quality Checked & Packaging"
        "dispatch_planned" -> "Dispatch Planned"
        "out_for_delivery" -> "Out for Delivery"
        "delivered" -> "Delivered"
        "completed" -> "Completed"
        else -> "Order Placed"
    }
}