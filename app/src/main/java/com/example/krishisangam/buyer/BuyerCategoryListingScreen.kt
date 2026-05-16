package com.example.krishisangam.buyer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.ProductModel
import com.example.krishisangam.R
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
private val ErrorRed = Color(0xFFFF6B6B)
private val WishlistRed = Color(0xFFFF4D6D)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)

@Composable
fun BuyerCategoryListingScreen(
    categoryName: String,
    onBackClick: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    val failedToLoadProductsText = stringResource(R.string.failed_to_load_products)

    var approvedProducts by remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var searchText by remember {
        mutableStateOf("")
    }

    val wishlistItems = remember {
        mutableStateListOf<String>()
    }

    DisposableEffect(Unit) {
        val listener: ListenerRegistration = db.collection("products")
            .whereEqualTo("status", "approved")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    errorMessage = error.message ?: failedToLoadProductsText
                    return@addSnapshotListener
                }

                approvedProducts = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(ProductModel::class.java)
                } ?: emptyList()
            }

        onDispose {
            listener.remove()
        }
    }

    val localizedCategoryTitle = getLocalizedBuyerCategoryTitle(categoryName)

    val categoryFilteredProducts = approvedProducts.filter { product ->
        productMatchesBuyerCategory(
            productCategory = product.category,
            selectedBuyerCategory = categoryName
        )
    }

    val displayedProducts = categoryFilteredProducts.filter { product ->
        val query = searchText.trim().lowercase()

        if (query.isBlank()) {
            true
        } else {
            product.name.lowercase().contains(query) ||
                    product.category.lowercase().contains(query) ||
                    product.quantity.lowercase().contains(query) ||
                    product.price.lowercase().contains(query)
        }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 18.dp)
        ) {
            CategoryTopBar(
                title = localizedCategoryTitle,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            CategorySearchBar(
                searchText = searchText,
                onSearchTextChange = { newValue ->
                    searchText = newValue
                },
                onClearClick = {
                    searchText = ""
                }
            )

            if (errorMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = errorMessage,
                    fontSize = 13.sp,
                    color = ErrorRed,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(
                    R.string.approved_category_listings,
                    localizedCategoryTitle
                ),
                fontSize = 21.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (displayedProducts.isEmpty()) {
                EmptyCategoryState(
                    categoryName = localizedCategoryTitle
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
                        items = displayedProducts,
                        key = { product ->
                            product.productId.ifBlank {
                                "${product.name}-${product.category}-${product.price}"
                            }
                        }
                    ) { product ->
                        ApprovedCategoryProductCard(
                            product = product,
                            isWishlisted = wishlistItems.contains(product.productId),
                            onWishlistClick = {
                                if (wishlistItems.contains(product.productId)) {
                                    wishlistItems.remove(product.productId)
                                } else {
                                    wishlistItems.add(product.productId)
                                }
                            },
                            onAddClick = {
                                BuyerOrderStore.addOrderFromProduct(product)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getLocalizedBuyerCategoryTitle(
    categoryName: String
): String {
    return when (categoryName.lowercase().trim()) {
        "grain", "grains", "rice" -> stringResource(R.string.grain)
        "fruits", "fruit" -> stringResource(R.string.fruits)
        "vegetables", "vegetable" -> stringResource(R.string.vegetables)
        "pulses", "pulse" -> stringResource(R.string.pulses)
        else -> categoryName
    }
}

fun productMatchesBuyerCategory(
    productCategory: String,
    selectedBuyerCategory: String
): Boolean {
    val product = productCategory.lowercase().trim()
    val selected = selectedBuyerCategory.lowercase().trim()

    return when (selected) {
        "grain", "grains" -> {
            product == "grain" || product == "grains" || product == "rice"
        }

        "fruits", "fruit" -> {
            product == "fruits" || product == "fruit"
        }

        "vegetables", "vegetable" -> {
            product == "vegetables" || product == "vegetable"
        }

        "pulses", "pulse" -> {
            product == "pulses" || product == "pulse"
        }

        else -> {
            product == selected
        }
    }
}

@Composable
fun CategoryTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = BorderGlass
                    ),
                    shape = CircleShape
                )
                .clickable {
                    onBackClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = TextLight
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextLight,
            maxLines = 1
        )
    }
}

@Composable
fun CategorySearchBar(
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

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = searchText,
                onValueChange = { newValue ->
                    onSearchTextChange(newValue)
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search),
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
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onClearClick()
                    }
                )
            }
        }
    }
}

@Composable
fun ApprovedCategoryProductCard(
    product: ProductModel,
    isWishlisted: Boolean,
    onWishlistClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(220.dp)
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(22.dp)
            ),
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
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.category.ifBlank {
                        stringResource(R.string.product)
                    },
                    fontSize = 10.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = if (isWishlisted) "♥" else "♡",
                    color = if (isWishlisted) WishlistRed else AccentYellow,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onWishlistClick()
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.emoji.ifBlank { "🌾" },
                    fontSize = 38.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.name.ifBlank {
                    stringResource(R.string.unnamed_product)
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = product.quantity.ifBlank {
                    stringResource(R.string.quantity_not_added)
                },
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.price.ifBlank {
                    stringResource(R.string.price_not_added)
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = stringResource(
                    R.string.trust_score_with_value,
                    product.trustScore
                ),
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow,
                maxLines = 1,
                modifier = Modifier
                    .background(SoftYellow, RoundedCornerShape(16.dp))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = AccentYellow.copy(alpha = 0.22f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.verified),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            PrimaryGreen.copy(alpha = 0.85f),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.18f)
                            ),
                            shape = CircleShape
                        )
                        .clickable {
                            onAddClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCategoryState(
    categoryName: String
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

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(
                    R.string.no_category_products_yet,
                    categoryName
                ),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.approved_products_after_verification),
                fontSize = 13.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
