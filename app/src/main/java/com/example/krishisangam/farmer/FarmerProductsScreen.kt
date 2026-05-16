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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
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

private val FarmerProductsPrimaryGreen = Color(0xFF01AC66)
private val FarmerProductsBackground = Color(0xFFE8FAF6)
private val FarmerProductsTextDark = Color(0xFF111111)
private val FarmerProductsCardColor = Color.White
private val FarmerProductsLightGreen = Color(0xFFDFF8EF)
private val FarmerProductsFieldColor = Color(0xFFF4F4F4)
private val FarmerProductsSoftRed = Color(0xFFFFE3E3)
private val FarmerProductsRedText = Color(0xFFD64B4B)
private val FarmerProductsSoftBlue = Color(0xFFE7F0FF)
private val FarmerProductsBlueText = Color(0xFF3B6FD8)

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
        val matchesSearch = product.name.contains(searchText, ignoreCase = true) ||
                product.category.contains(searchText, ignoreCase = true)

        val matchesCategory = selectedCategory == CATEGORY_ALL ||
                product.category.equals(selectedCategory, ignoreCase = true)

        matchesSearch && matchesCategory
    }

    val totalProducts = products.size
    val approvedProducts = products.count { it.status.equals("approved", ignoreCase = true) }
    val pendingProducts = products.count { it.status.equals("pending", ignoreCase = true) }
    val rejectedProducts = products.count { it.status.equals("rejected", ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FarmerProductsBackground)
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {
        FarmerProductsHeader(
            onAddProductClick = onAddProductClick
        )

        Spacer(modifier = Modifier.height(18.dp))

        FarmerProductsSearchAndFilter(
            searchText = searchText,
            onSearchChange = { searchText = it },
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
                fontWeight = FontWeight.Bold,
                color = FarmerProductsRedText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredProducts.isEmpty()) {
            FarmerProductsEmptyState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        product = product
                    )
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
                fontWeight = FontWeight.Bold,
                color = FarmerProductsTextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.manage_product_listings),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Box(
            modifier = Modifier
                .background(FarmerProductsPrimaryGreen, RoundedCornerShape(16.dp))
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
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FarmerProductsSearchAndFilter(
    searchText: String,
    onSearchChange: (String) -> Unit,
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
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            TextField(
                value = searchText,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_products),
                        color = Color.Gray
                    )
                },
                leadingIcon = {
                    Text(
                        text = "🔍",
                        fontSize = 17.sp
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = FarmerProductsTextDark,
                    unfocusedTextColor = FarmerProductsTextDark,
                    cursorColor = FarmerProductsPrimaryGreen
                )
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Box {
            Card(
                modifier = Modifier
                    .height(52.dp)
                    .clickable {
                        onCategoryMenuChange(true)
                    },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = localizedFarmerProductCategory(selectedCategory),
                        fontSize = 13.sp,
                        color = FarmerProductsTextDark,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.size(6.dp))

                    Text(
                        text = "⌄",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            DropdownMenu(
                expanded = categoryMenuExpanded,
                onDismissRequest = {
                    onCategoryMenuChange(false)
                }
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
                                text = localizedFarmerProductCategory(category)
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
        modifier = modifier.height(82.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 18.sp
            )

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = FarmerProductsPrimaryGreen
            )

            Text(
                text = label,
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FarmerProductCard(
    product: ProductModel
) {
    Card(
        modifier = Modifier.height(250.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = FarmerProductsCardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
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
                    fontSize = 30.sp
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
                color = FarmerProductsPrimaryGreen,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.name.ifBlank {
                    stringResource(R.string.unnamed_product)
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = FarmerProductsTextDark,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(
                    R.string.farmer_trust_with_score,
                    product.trustScore
                ),
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.price.ifBlank {
                    "₹0"
                },
                fontSize = 16.sp,
                color = FarmerProductsPrimaryGreen,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = stringResource(
                    R.string.available_quantity_format,
                    product.quantity.ifBlank { "0" }
                ),
                fontSize = 12.sp,
                color = Color.Gray,
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
        "approved" -> FarmerProductsLightGreen
        "rejected" -> FarmerProductsSoftRed
        else -> FarmerProductsSoftBlue
    }

    val textColor = when (normalizedStatus) {
        "approved" -> FarmerProductsPrimaryGreen
        "rejected" -> FarmerProductsRedText
        else -> FarmerProductsBlueText
    }

    val label = when (normalizedStatus) {
        "approved" -> stringResource(R.string.approved)
        "rejected" -> stringResource(R.string.rejected)
        else -> stringResource(R.string.pending)
    }

    Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun FarmerProductsEmptyState() {
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
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(FarmerProductsLightGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🌱",
                    fontSize = 34.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.no_products_yet),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = FarmerProductsTextDark
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.tap_add_to_list_first_product),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}