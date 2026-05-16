package com.example.krishisangam.buyer

import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val OrderPrimaryGreen = Color(0xFF01AC66)
private val OrderBackgroundColor = Color(0xFFE8FAF6)
private val OrderTextDark = Color(0xFF111111)
private val OrderCardBg = Color(0xFFFFFFFF)
private val OrderLightGray = Color(0xFFF4F4F4)
private val OrderLightGreen = Color(0xFFDFF8EF)

@Composable
fun BuyerOrdersScreen() {
    val orders = BuyerOrderStore.orders

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrderBackgroundColor)
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        BuyerOrdersTopBar()

        Spacer(modifier = Modifier.height(18.dp))

        if (orders.isEmpty()) {
            BuyerEmptyOrdersMessage()
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
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

                BuyerCheckoutButton()
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
                fontWeight = FontWeight.Bold,
                color = OrderTextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.products_added_from_approved_farmer_listings),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(OrderLightGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🛒",
                fontSize = 22.sp
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
                    fontWeight = FontWeight.Bold,
                    color = OrderTextDark
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = stringResource(R.string.add_approved_products_from_marketplace),
                    fontSize = 13.sp,
                    color = Color.Gray
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = OrderCardBg
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
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
                    .clip(RoundedCornerShape(14.dp))
                    .background(OrderLightGray),
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
                    fontWeight = FontWeight.Bold,
                    color = OrderTextDark,
                    maxLines = 1
                )

                Text(
                    text = order.type,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = order.price,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrderPrimaryGreen,
                    maxLines = 1
                )
            }

            BuyerQuantityControl(
                order = order
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = "🗑",
                fontSize = 18.sp,
                color = Color.Red,
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
                .size(26.dp)
                .clip(CircleShape)
                .background(OrderLightGray)
                .clickable {
                    BuyerOrderStore.decreaseQuantity(order)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = OrderTextDark
            )
        }

        Text(
            text = order.quantity.toString(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = OrderTextDark,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(OrderPrimaryGreen)
                .clickable {
                    BuyerOrderStore.increaseQuantity(order)
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
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
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.bill_split),
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = OrderTextDark
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
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.total),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrderTextDark,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "₹${totalAmount.toInt()}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrderPrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.transparent_charges_message),
                fontSize = 12.sp,
                color = Color.Gray
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
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = OrderTextDark
        )
    }
}

@Composable
fun BuyerCheckoutButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(OrderPrimaryGreen)
            .clickable {
                // Later: connect mock payment / order confirmation
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.proceed_to_checkout),
            color = Color.White,
            fontWeight = FontWeight.Bold,
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