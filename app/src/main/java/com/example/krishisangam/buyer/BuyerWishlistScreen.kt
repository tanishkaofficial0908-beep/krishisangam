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

private val WishlistPrimaryGreen = Color(0xFF01AC66)
private val WishlistBackground = Color(0xFFE8FAF6)
private val WishlistTextDark = Color(0xFF111111)
private val WishlistLightGreen = Color(0xFFDFF8EF)

@Composable
fun BuyerWishlistScreen(
    onBackClick: () -> Unit
) {
    val wishlistProducts = BuyerWishlistStore.wishlistProducts

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WishlistBackground)
            .padding(horizontal = 18.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "‹",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = WishlistTextDark,
                modifier = Modifier.clickable {
                    onBackClick()
                }
            )

            Text(
                text = stringResource(R.string.wishlist),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = WishlistTextDark,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.saved_products_for_later),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (wishlistProducts.isEmpty()) {
            EmptyWishlistState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = wishlistProducts,
                    key = { product ->
                        product.productId.ifBlank {
                            "${product.name}-${product.category}-${product.price}"
                        }
                    }
                ) { product ->
                    WishlistProductCard(
                        product = product
                    )
                }
            }
        }
    }
}

@Composable
fun WishlistProductCard(
    product: ProductModel
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.saved),
                    fontSize = 10.sp,
                    color = WishlistPrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(WishlistLightGreen, RoundedCornerShape(18.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "♥",
                    fontSize = 23.sp,
                    color = Color.Red,
                    modifier = Modifier.clickable {
                        BuyerWishlistStore.removeFromWishlist(product)
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
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
                color = WishlistTextDark,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = product.category.ifBlank {
                    stringResource(R.string.product)
                },
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = product.quantity.ifBlank {
                    stringResource(R.string.quantity_not_added)
                },
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
                color = WishlistPrimaryGreen,
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
                color = WishlistTextDark,
                maxLines = 1
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(WishlistPrimaryGreen)
                    .clickable {
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
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.add_to_order),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyWishlistState() {
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
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(WishlistLightGreen),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "♡",
                    fontSize = 42.sp,
                    color = WishlistPrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.no_wishlist_products_yet),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = WishlistTextDark
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(R.string.tap_heart_to_save_products),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}