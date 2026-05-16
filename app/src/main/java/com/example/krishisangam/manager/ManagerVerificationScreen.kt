package com.example.krishisangam.manager

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftYellow = Color(0xFFFFF4D6)
private val YellowText = Color(0xFFB8860B)
private val SoftRed = Color(0xFFFFE3E3)
private val RedText = Color(0xFFD64B4B)

@Composable
fun ManagerVerificationScreen() {
    val db = FirebaseFirestore.getInstance()

    var products by remember { mutableStateOf<List<ProductModel>>(emptyList()) }
    var selectedTab by remember { mutableStateOf("Pending") }
    var errorMessage by remember { mutableStateOf("") }

    DisposableEffect(Unit) {
        val listener: ListenerRegistration = db.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    errorMessage = error.message ?: "Failed to load products"
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    products = snapshot.documents.mapNotNull { document ->
                        document.toObject(ProductModel::class.java)
                    }
                }
            }

        onDispose {
            listener.remove()
        }
    }

    val pendingProducts = products.filter { it.status == "pending" }
    val approvedProducts = products.filter { it.status == "approved" }
    val rejectedProducts = products.filter { it.status == "rejected" }

    val visibleProducts = when (selectedTab) {
        "Approved" -> approvedProducts
        "Rejected" -> rejectedProducts
        else -> pendingProducts
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Text(
            text = "Product Verification",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Review and approve farmer product listings",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ManagerVerifyStatCard(
                title = "Pending",
                value = pendingProducts.size.toString(),
                icon = "⏳",
                modifier = Modifier.weight(1f)
            )

            ManagerVerifyStatCard(
                title = "Approved",
                value = approvedProducts.size.toString(),
                icon = "✅",
                modifier = Modifier.weight(1f)
            )

            ManagerVerifyStatCard(
                title = "Rejected",
                value = rejectedProducts.size.toString(),
                icon = "❌",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ManagerVerifyFilterChip(
                title = "Pending",
                selectedTab = selectedTab,
                onClick = { selectedTab = "Pending" },
                modifier = Modifier.weight(1f)
            )

            ManagerVerifyFilterChip(
                title = "Approved",
                selectedTab = selectedTab,
                onClick = { selectedTab = "Approved" },
                modifier = Modifier.weight(1f)
            )

            ManagerVerifyFilterChip(
                title = "Rejected",
                selectedTab = selectedTab,
                onClick = { selectedTab = "Rejected" },
                modifier = Modifier.weight(1f)
            )
        }

        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage,
                fontSize = 13.sp,
                color = RedText,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (visibleProducts.isEmpty()) {
            EmptyVerificationState(selectedTab = selectedTab)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(visibleProducts) { product ->
                    ManagerProductVerificationCard(
                        product = product,
                        selectedTab = selectedTab,
                        onApprove = {
                            approveProduct(product)
                        },
                        onReject = {
                            rejectProduct(product)
                        },
                        onMoveToPending = {
                            moveProductToPending(product)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ManagerVerifyStatCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(92.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(LightGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 15.sp
                )
            }

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Text(
                text = title,
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ManagerVerifyFilterChip(
    title: String,
    selectedTab: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = title == selectedTab

    Box(
        modifier = modifier
            .background(
                color = if (selected) PrimaryGreen else Color.White,
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
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else TextDark,
            maxLines = 1
        )
    }
}

@Composable
fun ManagerProductVerificationCard(
    product: ProductModel,
    selectedTab: String,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onMoveToPending: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
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
                        text = product.emoji.ifBlank { "🌾" },
                        fontSize = 30.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = product.category.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )

                    Text(
                        text = product.name.ifBlank { "Unnamed Product" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = "Farmer: ${product.farmerName.ifBlank { "Farmer" }}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "📍 ${product.location.ifBlank { "No location" }}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                ManagerStatusBadge(status = product.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = Color(0xFFEDEDED))

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ManagerProductInfoBox(
                    title = "Quantity",
                    value = product.quantity.ifBlank { "-" }
                )

                ManagerProductInfoBox(
                    title = "Price",
                    value = product.price.ifBlank { "-" }
                )

                ManagerProductInfoBox(
                    title = "Trust",
                    value = "${product.trustScore}/100"
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (product.description.isNotBlank()) {
                Text(
                    text = product.description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "Crop Year: ${product.cropYear.ifBlank { "Not added" }}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(15.dp))

            when (selectedTab) {
                "Pending" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftRed,
                                contentColor = RedText
                            )
                        ) {
                            Text(
                                text = "Reject",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onApprove,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryGreen,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Approve",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                "Approved" -> {
                    Button(
                        onClick = onMoveToPending,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftYellow,
                            contentColor = YellowText
                        )
                    ) {
                        Text(
                            text = "Move Back To Pending",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                "Rejected" -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onMoveToPending,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftYellow,
                                contentColor = YellowText
                            )
                        ) {
                            Text(
                                text = "Pending",
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onApprove,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryGreen,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Approve",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ManagerProductInfoBox(
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
fun ManagerStatusBadge(
    status: String
) {
    val bgColor = when (status) {
        "approved" -> LightGreen
        "rejected" -> SoftRed
        else -> SoftYellow
    }

    val textColor = when (status) {
        "approved" -> PrimaryGreen
        "rejected" -> RedText
        else -> YellowText
    }

    Text(
        text = status.replaceFirstChar { it.uppercase() },
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
fun EmptyVerificationState(
    selectedTab: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 45.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (selectedTab) {
                    "Approved" -> "✅"
                    "Rejected" -> "❌"
                    else -> "⏳"
                },
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No $selectedTab products",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Farmer products will appear here after listing.",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

fun approveProduct(product: ProductModel) {
    val db = FirebaseFirestore.getInstance()
    val managerEmail = FirebaseAuth.getInstance().currentUser?.email ?: "manager"

    db.collection("products")
        .document(product.productId)
        .update(
            mapOf(
                "status" to "approved",
                "approvedAt" to System.currentTimeMillis(),
                "approvedBy" to managerEmail
            )
        )
}

fun rejectProduct(product: ProductModel) {
    val db = FirebaseFirestore.getInstance()

    db.collection("products")
        .document(product.productId)
        .update(
            mapOf(
                "status" to "rejected",
                "approvedAt" to null,
                "approvedBy" to ""
            )
        )
}

fun moveProductToPending(product: ProductModel) {
    val db = FirebaseFirestore.getInstance()

    db.collection("products")
        .document(product.productId)
        .update(
            mapOf(
                "status" to "pending",
                "approvedAt" to null,
                "approvedBy" to ""
            )
        )
}