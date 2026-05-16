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
import androidx.compose.foundation.layout.width
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
private val FieldColor = Color(0xFFF4F4F4)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftYellow = Color(0xFFFFF4D6)
private val YellowText = Color(0xFFB8860B)

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

    val filteredProducts = approvedProducts.filter { product ->
        productMatchesBuyerCategory(
            productCategory = product.category,
            selectedBuyerCategory = categoryName
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        CategoryTopBar(
            title = localizedCategoryTitle,
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(18.dp))

        CategorySearchBar()

        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = errorMessage,
                fontSize = 13.sp,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(
                R.string.approved_category_listings,
                localizedCategoryTitle
            ),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(
                R.string.products_available_count,
                filteredProducts.size
            ),
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredProducts.isEmpty()) {
            EmptyCategoryState(
                categoryName = localizedCategoryTitle
            )
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
        Text(
            text = "‹",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.clickable {
                onBackClick()
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            maxLines = 1
        )
    }
}

@Composable
fun CategorySearchBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = FieldColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
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
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = stringResource(R.string.search),
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )

                Text(
                    text = "🎙️",
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = "＋",
            color = PrimaryGreen,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
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
        modifier = Modifier.height(235.dp),
        shape = RoundedCornerShape(18.dp),
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
            Text(
                text = if (isWishlisted) "♥" else "♡",
                color = if (isWishlisted) Color.Red else PrimaryGreen,
                fontSize = 21.sp,
                modifier = Modifier.clickable {
                    onWishlistClick()
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.emoji.ifBlank { "🌾" },
                    fontSize = 42.sp
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = product.name.ifBlank {
                    stringResource(R.string.unnamed_product)
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                maxLines = 1
            )

            Text(
                text = product.category.ifBlank {
                    stringResource(R.string.product)
                },
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Text(
                text = product.quantity.ifBlank {
                    stringResource(R.string.quantity_not_added)
                },
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.price.ifBlank {
                    stringResource(R.string.price_not_added)
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(
                    R.string.trust_score_with_value,
                    product.trustScore
                ),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = YellowText,
                modifier = Modifier
                    .background(SoftYellow, RoundedCornerShape(16.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.verified),
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
                        .size(30.dp)
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
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
                text = "📦",
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(
                    R.string.no_category_products_yet,
                    categoryName
                ),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.approved_products_after_verification),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}