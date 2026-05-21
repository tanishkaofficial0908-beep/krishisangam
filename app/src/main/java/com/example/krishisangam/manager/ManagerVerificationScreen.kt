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
import androidx.compose.runtime.mutableStateListOf
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

private const val VERIFY_SECTION_FARMER_LISTINGS = "Farmer Listings"
private const val VERIFY_SECTION_TRANSPORT_PARTNERS = "Transport Partners"

private const val STATUS_PENDING = "Pending"
private const val STATUS_APPROVED = "Approved"
private const val STATUS_REJECTED = "Rejected"

private const val CONFIRM_ACTION_APPROVE = "approve"
private const val CONFIRM_ACTION_REJECT = "reject"

data class ManagerVerificationConfirmAction(
    val product: ProductModel,
    val actionType: String
)

data class ManagerTransportPartner(
    val id: String,
    val name: String,
    val phone: String,
    val vehicleType: String,
    val vehicleNumber: String,
    val licenseNumber: String,
    val experience: String,
    val perKmCost: String,
    val loadCapacity: String,
    val location: String,
    val status: String
)

data class ManagerTransportConfirmAction(
    val transportPartner: ManagerTransportPartner,
    val actionType: String
)

@Composable
fun ManagerVerificationScreen() {
    val db = FirebaseFirestore.getInstance()

    var selectedSection by remember {
        mutableStateOf(VERIFY_SECTION_FARMER_LISTINGS)
    }

    var selectedTab by remember {
        mutableStateOf(STATUS_PENDING)
    }

    var products by remember {
        mutableStateOf<List<ProductModel>>(emptyList())
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

    var selectedTransportPartner by remember {
        mutableStateOf<ManagerTransportPartner?>(null)
    }

    var transportConfirmAction by remember {
        mutableStateOf<ManagerTransportConfirmAction?>(null)
    }

    val transportPartners = remember {
        mutableStateListOf(
            ManagerTransportPartner(
                id = "#TP101",
                name = "Ravi Sharma",
                phone = "+91 98765 43210",
                vehicleType = "Mini Truck",
                vehicleNumber = "MP04 AB 1234",
                licenseNumber = "MP-LIC-45789",
                experience = "5 years",
                perKmCost = "₹25/km",
                loadCapacity = "1.5 Ton",
                location = "Bhopal",
                status = STATUS_PENDING
            ),
            ManagerTransportPartner(
                id = "#TP102",
                name = "Imran Khan",
                phone = "+91 91234 56780",
                vehicleType = "Pickup Van",
                vehicleNumber = "MP09 CD 7788",
                licenseNumber = "MP-LIC-78451",
                experience = "7 years",
                perKmCost = "₹30/km",
                loadCapacity = "2 Ton",
                location = "Sehore",
                status = STATUS_PENDING
            ),
            ManagerTransportPartner(
                id = "#TP103",
                name = "Mahesh Patel",
                phone = "+91 99887 76655",
                vehicleType = "Tempo",
                vehicleNumber = "MP20 EF 4567",
                licenseNumber = "MP-LIC-11223",
                experience = "4 years",
                perKmCost = "₹22/km",
                loadCapacity = "1 Ton",
                location = "Raisen",
                status = STATUS_APPROVED
            ),
            ManagerTransportPartner(
                id = "#TP104",
                name = "Suresh Yadav",
                phone = "+91 88990 12345",
                vehicleType = "Truck",
                vehicleNumber = "MP04 GH 9090",
                licenseNumber = "MP-LIC-99001",
                experience = "2 years",
                perKmCost = "₹38/km",
                loadCapacity = "4 Ton",
                location = "Vidisha",
                status = STATUS_REJECTED
            )
        )
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
        STATUS_APPROVED -> approvedProducts
        STATUS_REJECTED -> rejectedProducts
        else -> pendingProducts
    }

    val pendingTransporters = transportPartners.filter {
        it.status == STATUS_PENDING
    }

    val approvedTransporters = transportPartners.filter {
        it.status == STATUS_APPROVED
    }

    val rejectedTransporters = transportPartners.filter {
        it.status == STATUS_REJECTED
    }

    val visibleTransporters = when (selectedTab) {
        STATUS_APPROVED -> approvedTransporters
        STATUS_REJECTED -> rejectedTransporters
        else -> pendingTransporters
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

    if (selectedTransportPartner != null) {
        ManagerTransportPartnerDetailDialog(
            transportPartner = selectedTransportPartner!!,
            onDismiss = {
                selectedTransportPartner = null
            }
        )
    }

    if (transportConfirmAction != null) {
        ManagerTransportConfirmDialog(
            action = transportConfirmAction!!,
            onDismiss = {
                transportConfirmAction = null
            },
            onConfirm = {
                val action = transportConfirmAction

                if (action != null) {
                    val newStatus = if (action.actionType == CONFIRM_ACTION_APPROVE) {
                        STATUS_APPROVED
                    } else {
                        STATUS_REJECTED
                    }

                    updateTransportPartnerStatus(
                        transportPartners = transportPartners,
                        partnerId = action.transportPartner.id,
                        newStatus = newStatus
                    )
                }

                transportConfirmAction = null
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
                text = "Verification Center",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Approve farmer listings and transport partner requests",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            ManagerVerificationSectionTabs(
                selectedSection = selectedSection,
                onSectionSelected = { section ->
                    selectedSection = section
                    selectedTab = STATUS_PENDING
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (selectedSection == VERIFY_SECTION_FARMER_LISTINGS) {
                ManagerFarmerListingsVerificationContent(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    pendingProducts = pendingProducts,
                    approvedProducts = approvedProducts,
                    rejectedProducts = rejectedProducts,
                    visibleProducts = visibleProducts,
                    errorMessage = errorMessage,
                    onProductClick = { product ->
                        selectedProduct = product
                    },
                    onApprove = { product ->
                        confirmAction = ManagerVerificationConfirmAction(
                            product = product,
                            actionType = CONFIRM_ACTION_APPROVE
                        )
                    },
                    onReject = { product ->
                        confirmAction = ManagerVerificationConfirmAction(
                            product = product,
                            actionType = CONFIRM_ACTION_REJECT
                        )
                    },
                    onMoveToPending = { product ->
                        moveProductToPending(product)
                    }
                )
            } else {
                ManagerTransportPartnersVerificationContent(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    pendingTransporters = pendingTransporters,
                    approvedTransporters = approvedTransporters,
                    rejectedTransporters = rejectedTransporters,
                    visibleTransporters = visibleTransporters,
                    onTransportClick = { transporter ->
                        selectedTransportPartner = transporter
                    },
                    onApprove = { transporter ->
                        transportConfirmAction = ManagerTransportConfirmAction(
                            transportPartner = transporter,
                            actionType = CONFIRM_ACTION_APPROVE
                        )
                    },
                    onReject = { transporter ->
                        transportConfirmAction = ManagerTransportConfirmAction(
                            transportPartner = transporter,
                            actionType = CONFIRM_ACTION_REJECT
                        )
                    },
                    onMoveToPending = { transporter ->
                        updateTransportPartnerStatus(
                            transportPartners = transportPartners,
                            partnerId = transporter.id,
                            newStatus = STATUS_PENDING
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ManagerVerificationSectionTabs(
    selectedSection: String,
    onSectionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ManagerVerifyFilterChip(
            title = VERIFY_SECTION_FARMER_LISTINGS,
            selectedTab = selectedSection,
            onClick = {
                onSectionSelected(VERIFY_SECTION_FARMER_LISTINGS)
            },
            modifier = Modifier.weight(1f)
        )

        ManagerVerifyFilterChip(
            title = VERIFY_SECTION_TRANSPORT_PARTNERS,
            selectedTab = selectedSection,
            onClick = {
                onSectionSelected(VERIFY_SECTION_TRANSPORT_PARTNERS)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ManagerFarmerListingsVerificationContent(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    pendingProducts: List<ProductModel>,
    approvedProducts: List<ProductModel>,
    rejectedProducts: List<ProductModel>,
    visibleProducts: List<ProductModel>,
    errorMessage: String,
    onProductClick: (ProductModel) -> Unit,
    onApprove: (ProductModel) -> Unit,
    onReject: (ProductModel) -> Unit,
    onMoveToPending: (ProductModel) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ManagerVerifyStatCard(
            title = STATUS_PENDING,
            value = pendingProducts.size.toString(),
            icon = "⏳",
            modifier = Modifier.weight(1f)
        )

        ManagerVerifyStatCard(
            title = STATUS_APPROVED,
            value = approvedProducts.size.toString(),
            icon = "✅",
            modifier = Modifier.weight(1f)
        )

        ManagerVerifyStatCard(
            title = STATUS_REJECTED,
            value = rejectedProducts.size.toString(),
            icon = "❌",
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(18.dp))

    ManagerStatusFilterRow(
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    )

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
            selectedTab = selectedTab,
            itemName = "products",
            emptyMessage = "Farmer products will appear here after listing."
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
                        onProductClick(product)
                    },
                    onApprove = {
                        onApprove(product)
                    },
                    onReject = {
                        onReject(product)
                    },
                    onMoveToPending = {
                        onMoveToPending(product)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(130.dp))
            }
        }
    }
}

@Composable
fun ManagerTransportPartnersVerificationContent(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    pendingTransporters: List<ManagerTransportPartner>,
    approvedTransporters: List<ManagerTransportPartner>,
    rejectedTransporters: List<ManagerTransportPartner>,
    visibleTransporters: List<ManagerTransportPartner>,
    onTransportClick: (ManagerTransportPartner) -> Unit,
    onApprove: (ManagerTransportPartner) -> Unit,
    onReject: (ManagerTransportPartner) -> Unit,
    onMoveToPending: (ManagerTransportPartner) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ManagerVerifyStatCard(
            title = STATUS_PENDING,
            value = pendingTransporters.size.toString(),
            icon = "🚚",
            modifier = Modifier.weight(1f)
        )

        ManagerVerifyStatCard(
            title = STATUS_APPROVED,
            value = approvedTransporters.size.toString(),
            icon = "✅",
            modifier = Modifier.weight(1f)
        )

        ManagerVerifyStatCard(
            title = STATUS_REJECTED,
            value = rejectedTransporters.size.toString(),
            icon = "❌",
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(18.dp))

    ManagerStatusFilterRow(
        selectedTab = selectedTab,
        onTabSelected = onTabSelected
    )

    Spacer(modifier = Modifier.height(18.dp))

    if (visibleTransporters.isEmpty()) {
        EmptyVerificationState(
            selectedTab = selectedTab,
            itemName = "transport partners",
            emptyMessage = "Transport partner requests will appear here after profile submission."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(
                items = visibleTransporters,
                key = { transporter ->
                    transporter.id
                }
            ) { transporter ->
                ManagerTransportPartnerCard(
                    transportPartner = transporter,
                    selectedTab = selectedTab,
                    onCardClick = {
                        onTransportClick(transporter)
                    },
                    onApprove = {
                        onApprove(transporter)
                    },
                    onReject = {
                        onReject(transporter)
                    },
                    onMoveToPending = {
                        onMoveToPending(transporter)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(130.dp))
            }
        }
    }
}

@Composable
fun ManagerStatusFilterRow(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ManagerVerifyFilterChip(
            title = STATUS_PENDING,
            selectedTab = selectedTab,
            onClick = {
                onTabSelected(STATUS_PENDING)
            },
            modifier = Modifier.weight(1f)
        )

        ManagerVerifyFilterChip(
            title = STATUS_APPROVED,
            selectedTab = selectedTab,
            onClick = {
                onTabSelected(STATUS_APPROVED)
            },
            modifier = Modifier.weight(1f)
        )

        ManagerVerifyFilterChip(
            title = STATUS_REJECTED,
            selectedTab = selectedTab,
            onClick = {
                onTabSelected(STATUS_REJECTED)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

fun updateTransportPartnerStatus(
    transportPartners: MutableList<ManagerTransportPartner>,
    partnerId: String,
    newStatus: String
) {
    val index = transportPartners.indexOfFirst { partner ->
        partner.id == partnerId
    }

    if (index != -1) {
        transportPartners[index] = transportPartners[index].copy(
            status = newStatus
        )
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
                STATUS_PENDING -> {
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

                STATUS_APPROVED -> {
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

                STATUS_REJECTED -> {
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
fun ManagerTransportPartnerCard(
    transportPartner: ManagerTransportPartner,
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
                        text = "🚚",
                        fontSize = 30.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = transportPartner.id,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentYellow,
                        maxLines = 1
                    )

                    Text(
                        text = transportPartner.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Text(
                        text = "${transportPartner.vehicleType} • ${transportPartner.vehicleNumber}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Text(
                        text = "📍 ${transportPartner.location}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                ManagerStatusBadge(
                    status = transportPartner.status
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
                    title = "Capacity",
                    value = transportPartner.loadCapacity
                )

                ManagerProductInfoBox(
                    title = "Cost",
                    value = transportPartner.perKmCost
                )

                ManagerProductInfoBox(
                    title = "Exp.",
                    value = transportPartner.experience
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Phone: ${transportPartner.phone}",
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "License: ${transportPartner.licenseNumber}",
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(15.dp))

            when (selectedTab) {
                STATUS_PENDING -> {
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

                STATUS_APPROVED -> {
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

                STATUS_REJECTED -> {
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
    selectedTab: String,
    itemName: String,
    emptyMessage: String
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
                    STATUS_APPROVED -> "✅"
                    STATUS_REJECTED -> "❌"
                    else -> "⏳"
                },
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No $selectedTab $itemName",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = emptyMessage,
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
fun ManagerTransportPartnerDetailDialog(
    transportPartner: ManagerTransportPartner,
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
                text = transportPartner.name,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "🚚",
                    fontSize = 38.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Transport ID: ${transportPartner.id}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Phone: ${transportPartner.phone}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Vehicle Type: ${transportPartner.vehicleType}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Vehicle No: ${transportPartner.vehicleNumber}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "License No: ${transportPartner.licenseNumber}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Experience: ${transportPartner.experience}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Per KM Cost: ${transportPartner.perKmCost}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Load Capacity: ${transportPartner.loadCapacity}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Location: ${transportPartner.location}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Status: ${transportPartner.status}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "This is demo UI approval. Real transporter approval will be connected later with Firebase.",
                    color = TextMuted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
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

@Composable
fun ManagerTransportConfirmDialog(
    action: ManagerTransportConfirmAction,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val isApprove = action.actionType == CONFIRM_ACTION_APPROVE

    val title = if (isApprove) {
        "Approve Transport Partner?"
    } else {
        "Reject Transport Partner?"
    }

    val emoji = if (isApprove) {
        "✅"
    } else {
        "❌"
    }

    val message = if (isApprove) {
        "Are you sure you want to approve ${action.transportPartner.name}? This transport partner will become eligible for delivery assignments."
    } else {
        "Are you sure you want to reject ${action.transportPartner.name}? This request will move to the rejected list."
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

@Composable
fun Column(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
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