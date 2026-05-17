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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.krishisangam.ProductModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

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
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val SoftRed = Color(0xFFFF6B6B).copy(alpha = 0.16f)
private val RedText = Color(0xFFFF6B6B)
private val DividerGlass = Color.White.copy(alpha = 0.14f)

private const val CONFIRM_ACTION_APPROVE = "approve"
private const val CONFIRM_ACTION_REJECT = "reject"

data class ManagerVerificationConfirmAction(
    val product: ProductModel,
    val actionType: String
)

@Composable
fun ManagerVerificationScreen() {
    val db = FirebaseFirestore.getInstance()

    var products by remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    var selectedTab by remember {
        mutableStateOf("Pending")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var selectedProduct by remember {
        mutableStateOf<ProductModel?>(null)
    }

    var confirmAction by remember {
        mutableStateOf<ManagerVerificationConfirmAction?>(null)
    }

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

    val pendingProducts = products.filter {
        it.status.equals("pending", ignoreCase = true)
    }

    val approvedProducts = products.filter {
        it.status.equals("approved", ignoreCase = true)
    }

    val rejectedProducts = products.filter {
        it.status.equals("rejected", ignoreCase = true)
    }

    val visibleProducts = when (selectedTab) {
        "Approved" -> approvedProducts
        "Rejected" -> rejectedProducts
        else -> pendingProducts
    }

    if (selectedProduct != null) {
        ManagerVerificationDetailDialog(
            product = selectedProduct!!,
            onDismiss = {
                selectedProduct = null
            }
        )
    }

    if (confirmAction != null) {
        ManagerVerificationConfirmDialog(
            action = confirmAction!!,
            onDismiss = {
                confirmAction = null
            },
            onConfirm = {
                val action = confirmAction

                if (action != null) {
                    if (action.actionType == CONFIRM_ACTION_APPROVE) {
                        approveProduct(action.product)
                    } else if (action.actionType == CONFIRM_ACTION_REJECT) {
                        rejectProduct(action.product)
                    }
                }

                confirmAction = null
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
                text = "Product Verification",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Review and approve farmer product listings",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
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
                    onClick = {
                        selectedTab = "Pending"
                    },
                    modifier = Modifier.weight(1f)
                )

                ManagerVerifyFilterChip(
                    title = "Approved",
                    selectedTab = selectedTab,
                    onClick = {
                        selectedTab = "Approved"
                    },
                    modifier = Modifier.weight(1f)
                )

                ManagerVerifyFilterChip(
                    title = "Rejected",
                    selectedTab = selectedTab,
                    onClick = {
                        selectedTab = "Rejected"
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = errorMessage,
                    fontSize = 13.sp,
                    color = RedText,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoftRed, RoundedCornerShape(14.dp))
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = RedText.copy(alpha = 0.24f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (visibleProducts.isEmpty()) {
                EmptyVerificationState(
                    selectedTab = selectedTab
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = visibleProducts,
                        key = { product ->
                            product.productId.ifBlank {
                                "${product.name}-${product.category}-${product.price}"
                            }
                        }
                    ) { product ->
                        ManagerProductVerificationCard(
                            product = product,
                            selectedTab = selectedTab,
                            onCardClick = {
                                selectedProduct = product
                            },
                            onApprove = {
                                confirmAction = ManagerVerificationConfirmAction(
                                    product = product,
                                    actionType = CONFIRM_ACTION_APPROVE
                                )
                            },
                            onReject = {
                                confirmAction = ManagerVerificationConfirmAction(
                                    product = product,
                                    actionType = CONFIRM_ACTION_REJECT
                                )
                            },
                            onMoveToPending = {
                                moveProductToPending(product)
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
fun ManagerVerifyStatCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(92.dp)
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
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
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
                    fontSize = 15.sp
                )
            }

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Text(
                text = title,
                fontSize = 10.sp,
                color = TextMuted,
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
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (selected) Color.White else TextLight,
            maxLines = 1
        )
    }
}

@Composable
fun ManagerProductVerificationCard(
    product: ProductModel,
    selectedTab: String,
    onCardClick: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onMoveToPending: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                onCardClick()
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
                        text = product.category.uppercase().ifBlank {
                            "PRODUCT"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentYellow,
                        maxLines = 1
                    )

                    Text(
                        text = product.name.ifBlank { "Unnamed Product" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Text(
                        text = "Farmer: ${product.farmerName.ifBlank { "Farmer" }}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Text(
                        text = "📍 ${product.location.ifBlank { "No location" }}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                ManagerStatusBadge(
                    status = product.status
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
                    color = TextMuted,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "Crop Year: ${product.cropYear.ifBlank { "Not added" }}",
                fontSize = 12.sp,
                color = TextMuted
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
                                fontWeight = FontWeight.ExtraBold
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
                                fontWeight = FontWeight.ExtraBold
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
                            contentColor = AccentYellow
                        )
                    ) {
                        Text(
                            text = "Move Back To Pending",
                            fontWeight = FontWeight.ExtraBold
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
                                contentColor = AccentYellow
                            )
                        ) {
                            Text(
                                text = "Pending",
                                fontWeight = FontWeight.ExtraBold
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
                                fontWeight = FontWeight.ExtraBold
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
fun ManagerStatusBadge(
    status: String
) {
    val normalizedStatus = status.lowercase().trim()

    val bgColor = when (normalizedStatus) {
        "approved" -> SoftGreen
        "rejected" -> SoftRed
        else -> SoftYellow
    }

    val textColor = when (normalizedStatus) {
        "approved" -> PrimaryGreen
        "rejected" -> RedText
        else -> AccentYellow
    }

    val statusText = normalizedStatus.ifBlank { "pending" }
        .replaceFirstChar { it.uppercase() }

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
fun EmptyVerificationState(
    selectedTab: String
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
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Farmer products will appear here after listing.",
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ManagerVerificationDetailDialog(
    product: ProductModel,
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
                text = product.name.ifBlank { "Unnamed Product" },
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = product.emoji.ifBlank { "🌾" },
                    fontSize = 38.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Category: ${product.category.ifBlank { "Product" }}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Farmer: ${product.farmerName.ifBlank { "Farmer" }}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Location: ${product.location.ifBlank { "No location" }}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Quantity: ${product.quantity.ifBlank { "-" }}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Price: ${product.price.ifBlank { "-" }}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Trust: ${product.trustScore}/100",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Status: ${product.status.ifBlank { "pending" }}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Crop Year: ${product.cropYear.ifBlank { "Not added" }}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                if (product.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = product.description,
                        color = DialogText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    )
}

@Composable
fun ManagerVerificationConfirmDialog(
    action: ManagerVerificationConfirmAction,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val isApprove = action.actionType == CONFIRM_ACTION_APPROVE

    val title = if (isApprove) {
        "Approve Product?"
    } else {
        "Reject Product?"
    }

    val emoji = if (isApprove) {
        "✅"
    } else {
        "❌"
    }

    val message = if (isApprove) {
        "Are you sure you want to approve ${action.product.name.ifBlank { "this product" }}? It will become visible to buyers."
    } else {
        "Are you sure you want to reject ${action.product.name.ifBlank { "this product" }}? It will move to the rejected list."
    }

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
                    onConfirm()
                }
            ) {
                Text(
                    text = if (isApprove) "Approve" else "Reject",
                    color = if (isApprove) PrimaryGreen else RedText,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = "Cancel",
                    color = AccentYellow,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        title = {
            Text(
                text = title,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = emoji,
                    fontSize = 38.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    )
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