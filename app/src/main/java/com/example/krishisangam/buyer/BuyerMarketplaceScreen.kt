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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
private val ErrorRed = Color(0xFFFF6B6B)
private val WishlistRed = Color(0xFFFF4D6D)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)

@Composable
fun BuyerMarketplaceScreen() {
    val db = FirebaseFirestore.getInstance()

    val failedToLoadMarketplaceProductsText = stringResource(
        R.string.failed_to_load_marketplace_products
    )

    var approvedProducts by remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var showBannerDialog by remember {
        mutableStateOf(false)
    }

    DisposableEffect(Unit) {
        val listener: ListenerRegistration =
            db.collection("products")
                .whereEqualTo("status", "approved")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        errorMessage = error.message ?: failedToLoadMarketplaceProductsText
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

    val displayedProducts = approvedProducts.filter { product ->
        val query = searchText.trim().lowercase()

        if (query.isBlank()) {
            true
        } else {
            product.name.lowercase().contains(query) ||
                    product.category.lowercase().contains(query) ||
                    product.quantity.lowercase().contains(query) ||
                    product.price.lowercase().contains(query) ||
                    product.farmerName.lowercase().contains(query) ||
                    product.location.lowercase().contains(query)
        }
    }

    if (showBannerDialog) {
        AlertDialog(
            onDismissRequest = {
                showBannerDialog = false
            },
            containerColor = DialogGreen,
            titleContentColor = TextLight,
            textContentColor = DialogText,
            confirmButton = {
                TextButton(
                    onClick = {
                        showBannerDialog = false
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
                    text = stringResource(R.string.verified_bulk_deals),
                    color = TextLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "🏪🌾",
                        fontSize = 36.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.up_to_20_off),
                        color = TextLight,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.on_agro_node_approved_listings),
                        color = TextMuted,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "This marketplace shows approved farmer listings verified through Agro Node checks. Buyers can explore bulk-ready crops and add products to orders.",
                        color = DialogText,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .padding(top = 18.dp)
        ) {
            Text(
                text = stringResource(R.string.marketplace_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.verified_farmer_listings_approved_by_agro_node),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            MarketplaceSearchBar(
                searchText = searchText,
                onSearchTextChange = { newValue ->
                    searchText = newValue
                },
                onClearClick = {
                    searchText = ""
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 130.dp)
            ) {
                item(
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    MarketplaceOfferBanner(
                        onClick = {
                            showBannerDialog = true
                        }
                    )
                }

                if (errorMessage.isNotBlank()) {
                    item(
                        span = {
                            GridItemSpan(maxLineSpan)
                        }
                    ) {
                        Text(
                            text = errorMessage,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ErrorRed,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                item(
                    span = {
                        GridItemSpan(maxLineSpan)
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.approved_products),
                            fontSize = 21.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextLight,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = if (searchText.isBlank()) {
                                stringResource(
                                    R.string.items_count,
                                    approvedProducts.size
                                )
                            } else {
                                "${displayedProducts.size} found"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentYellow
                        )
                    }
                }

                if (displayedProducts.isEmpty()) {
                    item(
                        span = {
                            GridItemSpan(maxLineSpan)
                        }
                    ) {
                        EmptyMarketplaceState(
                            isSearching = searchText.isNotBlank()
                        )
                    }
                } else {
                    items(
                        items = displayedProducts,
                        key = { product ->
                            product.productId.ifBlank {
                                "${product.name}-${product.category}-${product.price}"
                            }
                        }
                    ) { product ->
                        BuyerMarketplaceProductCard(
                            product = product,
                            isWishlisted = BuyerWishlistStore.isWishlisted(product),
                            onWishlistClick = {
                                BuyerWishlistStore.toggleWishlist(product)
                            },
                            onAddClick = {
                                BuyerOrderStore.addOrderFromProduct(
                                    product = product,
                                    fallbackProductName = product.name.ifBlank {
                                        "Product"
                                    },
                                    fallbackCategoryName = product.category.ifBlank {
                                        "Category"
                                    },
                                    fallbackPrice = product.price.ifBlank {
                                        "₹0"
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MarketplaceSearchBar(
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
                        text = stringResource(R.string.search_wheat_rice_vegetables),
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
fun MarketplaceOfferBanner(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryGreen
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.18f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryGreen,
                            Color(0xFF00985B),
                            Color(0xFF007A49)
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.verified_bulk_deals),
                    color = Color.White.copy(alpha = 0.92f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.up_to_20_off),
                    color = Color.White,
                    fontSize = 27.sp,
                    lineHeight = 31.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.on_agro_node_approved_listings),
                    color = Color.White.copy(alpha = 0.78f),
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(66.dp)
                    .background(
                        Color.White.copy(alpha = 0.12f),
                        RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏪🌾",
                    fontSize = 36.sp
                )
            }
        }
    }
}

@Composable
fun BuyerMarketplaceProductCard(
    product: ProductModel,
    isWishlisted: Boolean,
    onWishlistClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(270.dp)
            .shadow(
                elevation = 6.dp,
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
            defaultElevation = 2.dp
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
                    text = stringResource(R.string.verified),
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .background(
                            PrimaryGreen.copy(alpha = 0.85f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = if (isWishlisted) "♥" else "♡",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isWishlisted) WishlistRed else AccentYellow,
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
                    fontSize = 40.sp
                )
            }

            Text(
                text = product.name.ifBlank {
                    stringResource(R.string.unnamed_product)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = stringResource(
                    R.string.category_quantity_format,
                    product.category.ifBlank {
                        stringResource(R.string.product)
                    },
                    product.quantity.ifBlank {
                        stringResource(R.string.quantity_not_added)
                    }
                ),
                fontSize = 11.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.price.ifBlank {
                    stringResource(R.string.price_not_added)
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = stringResource(
                    R.string.farmer_name_with_icon,
                    product.farmerName.ifBlank {
                        stringResource(R.string.farmer)
                    }
                ),
                fontSize = 11.sp,
                color = TextLight,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            Text(
                text = stringResource(
                    R.string.location_with_icon,
                    product.location.ifBlank {
                        stringResource(R.string.no_location)
                    }
                ),
                fontSize = 10.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(
                    R.string.trust_score_with_value,
                    product.trustScore
                ),
                fontSize = 10.sp,
                color = AccentYellow,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                modifier = Modifier
                    .background(SoftYellow, RoundedCornerShape(18.dp))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = AccentYellow.copy(alpha = 0.22f)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.agro_node_approved),
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    modifier = Modifier
                        .background(
                            PrimaryGreen.copy(alpha = 0.85f),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(horizontal = 7.dp, vertical = 5.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.22f)
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
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMarketplaceState(
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
            Text(
                text = if (isSearching) "🔍" else "🏪",
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isSearching) {
                    "No matching products found"
                } else {
                    stringResource(R.string.no_approved_products_yet)
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isSearching) {
                    "Try searching wheat, rice, onion, farmer name, or location."
                } else {
                    stringResource(R.string.products_after_agro_node_approval)
                },
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 19.sp
            )
        }
    }
}