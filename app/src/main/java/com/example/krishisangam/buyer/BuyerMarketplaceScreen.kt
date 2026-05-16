package com.example.krishisangam.buyer

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
import androidx.compose.material3.Text
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
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftYellow = Color(0xFFFFF4D6)
private val YellowText = Color(0xFFB8860B)

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

    val wishlistedItems = remember {
        mutableStateListOf<String>()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Text(
            text = stringResource(R.string.marketplace_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = stringResource(R.string.verified_farmer_listings_approved_by_agro_node),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(18.dp))

        MarketplaceOfferBanner()

        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.approved_products),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(
                    R.string.items_count,
                    approvedProducts.size
                ),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (approvedProducts.isEmpty()) {
            EmptyMarketplaceState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = approvedProducts,
                    key = { product ->
                        product.productId.ifBlank {
                            "${product.name}-${product.category}-${product.price}"
                        }
                    }
                ) { product ->
                    BuyerMarketplaceProductCard(
                        product = product,
                        isWishlisted = wishlistedItems.contains(product.productId),
                        onWishlistClick = {
                            if (wishlistedItems.contains(product.productId)) {
                                wishlistedItems.remove(product.productId)
                            } else {
                                wishlistedItems.add(product.productId)
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

@Composable
fun MarketplaceOfferBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryGreen
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.verified_bulk_deals),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.up_to_20_off),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(R.string.on_agro_node_approved_listings),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "🏪🌾",
                fontSize = 44.sp
            )
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
        modifier = Modifier.height(255.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
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
                Text(
                    text = stringResource(R.string.verified),
                    fontSize = 10.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(LightGreen, RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = if (isWishlisted) "♥" else "♡",
                    fontSize = 22.sp,
                    color = if (isWishlisted) Color.Red else PrimaryGreen,
                    modifier = Modifier.clickable {
                        onWishlistClick()
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.emoji.ifBlank { "🌾" },
                    fontSize = 44.sp
                )
            }

            Text(
                text = product.name.ifBlank {
                    stringResource(R.string.unnamed_product)
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
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
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = product.price.ifBlank {
                    stringResource(R.string.price_not_added)
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(
                    R.string.farmer_name_with_icon,
                    product.farmerName.ifBlank {
                        stringResource(R.string.farmer)
                    }
                ),
                fontSize = 11.sp,
                color = TextDark,
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
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(
                    R.string.trust_score_with_value,
                    product.trustScore
                ),
                fontSize = 10.sp,
                color = YellowText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(SoftYellow, RoundedCornerShape(18.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.agro_node_approved),
                    color = PrimaryGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(LightGreen, RoundedCornerShape(18.dp))
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                        .clickable {
                            onAddClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMarketplaceState() {
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
                text = "🏪",
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.no_approved_products_yet),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.products_after_agro_node_approval),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}