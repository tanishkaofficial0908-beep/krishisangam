package com.example.krishisangam.farmer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.ProductModel
import com.example.krishisangam.R
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
private val GlassDark = Color.White.copy(alpha = 0.095f)
private val GlassCard = Color.White.copy(alpha = 0.105f)
private val BorderGlass = Color.White.copy(alpha = 0.16f)
private val DialogGreen = Color(0xFF123D2B)
private val DialogText = Color(0xFFD8EDE3)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val SoftRed = Color(0xFFFF6B6B).copy(alpha = 0.16f)
private val RedText = Color(0xFFFF6B6B)
private val SoftBlue = Color(0xFF2F6FED).copy(alpha = 0.18f)
private val BlueText = Color(0xFF7FB2FF)

private const val CATEGORY_ALL = "All"
private const val CATEGORY_GRAINS = "Grains"
private const val CATEGORY_VEGETABLES = "Vegetables"
private const val CATEGORY_FRUITS = "Fruits"
private const val CATEGORY_PULSES = "Pulses"

@Composable
fun FarmerProductsScreen(
    onAddProductClick: () -> Unit = {}
) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser

    val failedToLoadProductsMessage = stringResource(R.string.failed_to_load_products)
    val userNotLoggedInMessage = stringResource(R.string.user_not_logged_in)

    var products by remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf(CATEGORY_ALL)
    }

    var categoryMenuExpanded by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var selectedProduct by remember {
        mutableStateOf<ProductModel?>(null)
    }

    DisposableEffect(currentUser?.uid) {
        var listener: ListenerRegistration? = null

        if (currentUser != null) {
            listener = db.collection("products")
                .whereEqualTo("farmerId", currentUser.uid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        errorMessage = error.message ?: failedToLoadProductsMessage
                        return@addSnapshotListener
                    }

                    products = snapshot?.documents?.mapNotNull { document ->
                        document.toObject(ProductModel::class.java)
                    } ?: emptyList()
                }
        } else {
            errorMessage = userNotLoggedInMessage
        }

        onDispose {
            listener?.remove()
        }
    }

    val filteredProducts = products.filter { product ->
        val query = searchText.trim().lowercase()

        val matchesSearch = if (query.isBlank()) {
            true
        } else {
            product.name.lowercase().contains(query) ||
                    product.category.lowercase().contains(query) ||
                    product.price.lowercase().contains(query) ||
                    product.quantity.lowercase().contains(query) ||
                    product.status.lowercase().contains(query) ||
                    product.location.lowercase().contains(query)
        }

        val matchesCategory = selectedCategory == CATEGORY_ALL ||
                product.category.equals(selectedCategory, ignoreCase = true)

        matchesSearch && matchesCategory
    }

    val totalProducts = products.size
    val approvedProducts = products.count { it.status.equals("approved", ignoreCase = true) }
    val pendingProducts = products.count { it.status.equals("pending", ignoreCase = true) }
    val rejectedProducts = products.count { it.status.equals("rejected", ignoreCase = true) }

    if (selectedProduct != null) {
        FarmerProductDetailDialog(
            product = selectedProduct!!,
            onDismiss = {
                selectedProduct = null
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
            FarmerProductsHeader(
                onAddProductClick = onAddProductClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            FarmerProductsSearchAndFilter(
                searchText = searchText,
                onSearchChange = { searchText = it },
                onClearSearchClick = {
                    searchText = ""
                },
                selectedCategory = selectedCategory,
                categoryMenuExpanded = categoryMenuExpanded,
                onCategoryMenuChange = { categoryMenuExpanded = it },
                onCategorySelected = { category ->
                    selectedCategory = category
                    categoryMenuExpanded = false
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FarmerProductsSummaryRow(
                total = totalProducts,
                approved = approvedProducts,
                pending = pendingProducts,
                rejected = rejectedProducts
            )

            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = errorMessage,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RedText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoftRed, RoundedCornerShape(14.dp))
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = RedText.copy(alpha = 0.25f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (searchText.isBlank() && selectedCategory == CATEGORY_ALL) {
                    "${filteredProducts.size} products"
                } else {
                    "${filteredProducts.size} matching products"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (filteredProducts.isEmpty()) {
                FarmerProductsEmptyState(
                    isSearching = searchText.isNotBlank() || selectedCategory != CATEGORY_ALL
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 130.dp)
                ) {
                    items(
                        items = filteredProducts,
                        key = { product ->
                            product.productId.ifBlank {
                                "${product.name}-${product.category}-${product.price}"
                            }
                        }
                    ) { product ->
                        FarmerProductCard(
                            product = product,
                            onClick = {
                                selectedProduct = product
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FarmerProductsHeader(
    onAddProductClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.my_products),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.manage_product_listings),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )
        }

        Box(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryGreen,
                            Color(0xFF00985B),
                            Color(0xFF007A49)
                        )
                    ),
                    RoundedCornerShape(16.dp)
                )
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.18f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {
                    onAddProductClick()
                }
                .padding(horizontal = 14.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.add_with_plus),
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun FarmerProductsSearchAndFilter(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onClearSearchClick: () -> Unit,
    selectedCategory: String,
    categoryMenuExpanded: Boolean,
    onCategoryMenuChange: (Boolean) -> Unit,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
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
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔍",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.size(8.dp))

                TextField(
                    value = searchText,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_products),
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
                        focusedTextColor = TextLight,
                        unfocusedTextColor = TextLight,
                        focusedPlaceholderColor = TextMuted,
                        unfocusedPlaceholderColor = TextMuted,
                        cursorColor = AccentYellow
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
                            onClearSearchClick()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        Box {
            Card(
                modifier = Modifier
                    .height(56.dp)
                    .clickable {
                        onCategoryMenuChange(true)
                    },
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
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = localizedFarmerProductCategory(selectedCategory),
                        fontSize = 13.sp,
                        color = TextLight,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.size(6.dp))

                    Text(
                        text = "⌄",
                        fontSize = 15.sp,
                        color = AccentYellow
                    )
                }
            }

            DropdownMenu(
                expanded = categoryMenuExpanded,
                onDismissRequest = {
                    onCategoryMenuChange(false)
                },
                containerColor = DialogGreen,
                tonalElevation = 6.dp
            ) {
                listOf(
                    CATEGORY_ALL,
                    CATEGORY_GRAINS,
                    CATEGORY_VEGETABLES,
                    CATEGORY_FRUITS,
                    CATEGORY_PULSES
                ).forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = localizedFarmerProductCategory(category),
                                color = TextLight,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        onClick = {
                            onCategorySelected(category)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun localizedFarmerProductCategory(
    category: String
): String {
    return when (category) {
        CATEGORY_ALL -> stringResource(R.string.all)
        CATEGORY_GRAINS -> stringResource(R.string.grains)
        CATEGORY_VEGETABLES -> stringResource(R.string.vegetables)
        CATEGORY_FRUITS -> stringResource(R.string.fruits)
        CATEGORY_PULSES -> stringResource(R.string.pulses)
        else -> category
    }
}

@Composable
private fun FarmerProductsSummaryRow(
    total: Int,
    approved: Int,
    pending: Int,
    rejected: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FarmerProductSummaryCard(
            modifier = Modifier.weight(1f),
            icon = "📦",
            value = total.toString(),
            label = stringResource(R.string.total)
        )

        FarmerProductSummaryCard(
            modifier = Modifier.weight(1f),
            icon = "✅",
            value = approved.toString(),
            label = stringResource(R.string.approved)
        )

        FarmerProductSummaryCard(
            modifier = Modifier.weight(1f),
            icon = "⏳",
            value = pending.toString(),
            label = stringResource(R.string.pending)
        )

        FarmerProductSummaryCard(
            modifier = Modifier.weight(1f),
            icon = "❌",
            value = rejected.toString(),
            label = stringResource(R.string.rejected)
        )
    }
}

@Composable
private fun FarmerProductSummaryCard(
    modifier: Modifier = Modifier,
    icon: String,
    value: String,
    label: String
) {
    Card(
        modifier = modifier
            .height(88.dp)
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
                text = label,
                fontSize = 10.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FarmerProductCard(
    product: ProductModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(260.dp)
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(22.dp),
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
                .fillMaxSize()
                .padding(13.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FarmerProductStatusBadge(
                    status = product.status
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = product.emoji.ifBlank { "🌾" },
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.emoji.ifBlank { "🌾" },
                    fontSize = 44.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = product.category.uppercase().ifBlank {
                    stringResource(R.string.product).uppercase()
                },
                fontSize = 11.sp,
                color = AccentYellow,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.name.ifBlank {
                    stringResource(R.string.unnamed_product)
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(
                    R.string.farmer_trust_with_score,
                    product.trustScore
                ),
                fontSize = 11.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.price.ifBlank {
                    "₹0"
                },
                fontSize = 16.sp,
                color = AccentYellow,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = stringResource(
                    R.string.available_quantity_format,
                    product.quantity.ifBlank { "0" }
                ),
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FarmerProductStatusBadge(
    status: String
) {
    val normalizedStatus = status.lowercase().trim()

    val backgroundColor = when (normalizedStatus) {
        "approved" -> SoftGreen
        "rejected" -> SoftRed
        else -> SoftBlue
    }

    val textColor = when (normalizedStatus) {
        "approved" -> PrimaryGreen
        "rejected" -> RedText
        else -> BlueText
    }

    val label = when (normalizedStatus) {
        "approved" -> stringResource(R.string.approved)
        "rejected" -> stringResource(R.string.rejected)
        else -> stringResource(R.string.pending)
    }

    Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.ExtraBold,
        color = textColor,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = textColor.copy(alpha = 0.24f)
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun FarmerProductsEmptyState(
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
                .padding(vertical = 45.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White.copy(alpha = 0.12f), CircleShape)
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
                    text = if (isSearching) "🔍" else "🌱",
                    fontSize = 34.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (isSearching) {
                    "No matching products found"
                } else {
                    stringResource(R.string.no_products_yet)
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = if (isSearching) {
                    "Try searching by crop name, category, status, price, or quantity."
                } else {
                    stringResource(R.string.tap_add_to_list_first_product)
                },
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun FarmerProductDetailDialog(
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
                text = product.name.ifBlank {
                    stringResource(R.string.unnamed_product)
                },
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
                    text = "Category: ${product.category.ifBlank { stringResource(R.string.product) }}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Quantity: ${product.quantity.ifBlank { "0" }}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Price: ${product.price.ifBlank { "₹0" }}",
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
                    text = "Location: ${product.location.ifBlank { "Not added" }}",
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Trust score: ${product.trustScore}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    )
}