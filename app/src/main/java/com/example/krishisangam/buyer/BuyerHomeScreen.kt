package com.example.krishisangam.buyer

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.krishisangam.R

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

data class BuyerOfferData(
    val title: String,
    val headline: String,
    val subtitle: String,
    val emoji: String,
    val detail: String
)

@Composable
fun BuyerHomeScreen(
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onCategoriesSeeAllClick: () -> Unit
) {
    var showAllOffers by remember { mutableStateOf(false) }
    var selectedOffer by remember { mutableStateOf<BuyerOfferData?>(null) }

    val bulkCropOffer = BuyerOfferData(
        title = stringResource(R.string.bulk_crop_deal),
        headline = stringResource(R.string.up_to_lower_logistics_cost),
        subtitle = stringResource(R.string.for_community_orders),
        emoji = "🚚🌾",
        detail = "This offer is designed for bulk/community crop orders where logistics are optimized through Agro Nodes and shared delivery planning."
    )

    val verifiedNodeOffer = BuyerOfferData(
        title = "Verified Node Deal",
        headline = "Fresh crops from verified Agro Nodes",
        subtitle = "Better quality check before dispatch",
        emoji = "🏢✅",
        detail = "Products linked with verified Agro Nodes go through basic quality and quantity checks before dispatch, helping buyers trust the listing."
    )

    val bulkBuyerOffer = BuyerOfferData(
        title = "Bulk Buyer Benefit",
        headline = "Save more on community bulk orders",
        subtitle = "Ideal for shops, hostels and local groups",
        emoji = "🛒📦",
        detail = "This benefit is suitable for buyers placing larger orders together, reducing repeated delivery effort and improving order coordination."
    )

    if (selectedOffer != null) {
        AlertDialog(
            onDismissRequest = {
                selectedOffer = null
            },
            containerColor = DialogGreen,
            titleContentColor = TextLight,
            textContentColor = TextLight,
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedOffer = null
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
                    text = selectedOffer?.title ?: "",
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = selectedOffer?.emoji ?: "",
                        fontSize = 34.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = selectedOffer?.headline ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextLight
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = selectedOffer?.subtitle ?: "",
                        fontSize = 14.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = selectedOffer?.detail ?: "",
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = DialogText
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
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopEnd)
                .background(AccentYellow.copy(alpha = 0.08f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.BottomStart)
                .background(PrimaryGreen.copy(alpha = 0.12f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 22.dp, bottom = 120.dp)
        ) {
            BuyerTopHeader(
                onProfileClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            BuyerSearchBar(
                onSearchClick = onSearchClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            BuyerSectionHeader(
                title = stringResource(R.string.categories),
                action = stringResource(R.string.see_all),
                onActionClick = onCategoriesSeeAllClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BuyerCategoryItem(
                    emoji = "🌾",
                    title = stringResource(R.string.grain),
                    onClick = {
                        onCategoryClick("Grain")
                    }
                )

                BuyerCategoryItem(
                    emoji = "🍎",
                    title = stringResource(R.string.fruits),
                    onClick = {
                        onCategoryClick("Fruits")
                    }
                )

                BuyerCategoryItem(
                    emoji = "🥦",
                    title = stringResource(R.string.vegetables),
                    onClick = {
                        onCategoryClick("Vegetables")
                    }
                )

                BuyerCategoryItem(
                    emoji = "🫘",
                    title = stringResource(R.string.pulses),
                    onClick = {
                        onCategoryClick("Pulses")
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            BuyerSectionHeader(
                title = stringResource(R.string.todays_offers),
                action = if (showAllOffers) "Show Less" else stringResource(R.string.see_all),
                onActionClick = {
                    showAllOffers = !showAllOffers
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            BuyerOfferCard(
                title = bulkCropOffer.title,
                headline = bulkCropOffer.headline,
                subtitle = bulkCropOffer.subtitle,
                emoji = bulkCropOffer.emoji,
                onClick = {
                    selectedOffer = bulkCropOffer
                }
            )

            if (showAllOffers) {
                Spacer(modifier = Modifier.height(16.dp))

                BuyerOfferCard(
                    title = verifiedNodeOffer.title,
                    headline = verifiedNodeOffer.headline,
                    subtitle = verifiedNodeOffer.subtitle,
                    emoji = verifiedNodeOffer.emoji,
                    onClick = {
                        selectedOffer = verifiedNodeOffer
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                BuyerOfferCard(
                    title = bulkBuyerOffer.title,
                    headline = bulkBuyerOffer.headline,
                    subtitle = bulkBuyerOffer.subtitle,
                    emoji = bulkBuyerOffer.emoji,
                    onClick = {
                        selectedOffer = bulkBuyerOffer
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.tap_category_to_view_listings),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BuyerTopHeader(
    onProfileClick: () -> Unit
) {
    var showMoreMenu by remember { mutableStateOf(false) }
    var showWishlistDialog by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    if (showWishlistDialog) {
        AlertDialog(
            onDismissRequest = {
                showWishlistDialog = false
            },
            containerColor = DialogGreen,
            titleContentColor = TextLight,
            textContentColor = DialogText,
            confirmButton = {
                TextButton(
                    onClick = {
                        showWishlistDialog = false
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
                    text = "Wishlist",
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "❤",
                        fontSize = 34.sp,
                        color = AccentYellow
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Wishlist coming soon.",
                        color = DialogText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        )
    }

    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = {
                showNotificationDialog = false
            },
            containerColor = DialogGreen,
            titleContentColor = TextLight,
            textContentColor = DialogText,
            confirmButton = {
                TextButton(
                    onClick = {
                        showNotificationDialog = false
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
                    text = "Notifications",
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "🔔",
                        fontSize = 34.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "No notifications yet.",
                        color = DialogText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        )
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = {
                showHelpDialog = false
            },
            containerColor = DialogGreen,
            titleContentColor = TextLight,
            textContentColor = DialogText,
            confirmButton = {
                TextButton(
                    onClick = {
                        showHelpDialog = false
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
                    text = "Help",
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "🤝",
                        fontSize = 34.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Need help? You can contact Krishi Sangam support or visit your nearest Agro Node Manager.",
                        color = DialogText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
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
                text = "👤",
                fontSize = 25.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                fontSize = 13.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = stringResource(R.string.buyer),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )
        }

        Text(
            text = "❤",
            fontSize = 23.sp,
            color = AccentYellow,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                showWishlistDialog = true
            }
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = "🔔",
            fontSize = 23.sp,
            modifier = Modifier.clickable {
                showNotificationDialog = true
            }
        )

        Spacer(modifier = Modifier.width(14.dp))

        Box {
            Text(
                text = "⋯",
                fontSize = 28.sp,
                color = TextLight,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    showMoreMenu = true
                }
            )

            DropdownMenu(
                expanded = showMoreMenu,
                onDismissRequest = {
                    showMoreMenu = false
                },
                containerColor = DialogGreen,
                tonalElevation = 6.dp
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Profile",
                            color = TextLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onClick = {
                        showMoreMenu = false
                        onProfileClick()
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Help",
                            color = TextLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onClick = {
                        showMoreMenu = false
                        showHelpDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun BuyerSearchBar(
    onSearchClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable {
                onSearchClick()
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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔍",
                fontSize = 19.sp
            )

            Spacer(modifier = Modifier.width(11.dp))

            Text(
                text = stringResource(R.string.search_wheat_rice_vegetables),
                color = TextMuted,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
        }
    }
}

@Composable
fun BuyerSectionHeader(
    title: String,
    action: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = TextLight,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = action,
            color = AccentYellow,
            fontSize = 13.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.clickable {
                onActionClick()
            }
        )
    }
}

@Composable
fun BuyerCategoryItem(
    emoji: String,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Card(
            modifier = Modifier.size(66.dp),
            shape = RoundedCornerShape(18.dp),
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 31.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextLight,
            maxLines = 1
        )
    }
}

@Composable
fun BuyerOfferCard(
    title: String,
    headline: String,
    subtitle: String,
    emoji: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp)
            .shadow(
                elevation = 12.dp,
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
                    text = title,
                    color = Color.White.copy(alpha = 0.92f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = headline,
                    color = Color.White,
                    fontSize = 21.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.78f),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        Color.White.copy(alpha = 0.12f),
                        RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 36.sp
                )
            }
        }
    }
}