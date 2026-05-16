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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)

data class BuyerFarmerProfile(
    val name: String,
    val location: String,
    val trustScore: Int,
    val productsListed: Int,
    val completedOrders: Int,
    val mainCrop: String,
    val verificationStatus: String
)

@Composable
fun BuyerFarmersScreen() {
    var searchText by remember {
        mutableStateOf("")
    }

    var selectedFarmer by remember {
        mutableStateOf<BuyerFarmerProfile?>(null)
    }

    val farmers = listOf(
        BuyerFarmerProfile(
            name = "Ramesh Patel",
            location = "Bhopal",
            trustScore = 86,
            productsListed = 8,
            completedOrders = 24,
            mainCrop = stringResource(R.string.wheat_rice),
            verificationStatus = stringResource(R.string.verified_farmer)
        ),
        BuyerFarmerProfile(
            name = "Shreya Gupta",
            location = "Sehore",
            trustScore = 78,
            productsListed = 5,
            completedOrders = 16,
            mainCrop = stringResource(R.string.rice_pulses),
            verificationStatus = stringResource(R.string.verified_farmer)
        ),
        BuyerFarmerProfile(
            name = "Mahesh Sahu",
            location = "Raisen",
            trustScore = 91,
            productsListed = 11,
            completedOrders = 32,
            mainCrop = stringResource(R.string.wheat_vegetables),
            verificationStatus = stringResource(R.string.premium_farmer)
        )
    )

    val filteredFarmers = farmers.filter { farmer ->
        val query = searchText.trim().lowercase()

        if (query.isBlank()) {
            true
        } else {
            farmer.name.lowercase().contains(query) ||
                    farmer.location.lowercase().contains(query) ||
                    farmer.mainCrop.lowercase().contains(query) ||
                    farmer.verificationStatus.lowercase().contains(query) ||
                    farmer.trustScore.toString().contains(query)
        }
    }

    if (selectedFarmer != null) {
        FarmerDetailDialog(
            farmer = selectedFarmer!!,
            onDismiss = {
                selectedFarmer = null
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 22.dp)
                .padding(bottom = 110.dp)
        ) {
            Text(
                text = stringResource(R.string.farmer_directory),
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.verified_farmers_trust_scores),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            FarmerSearchBar(
                searchText = searchText,
                onSearchTextChange = { newValue ->
                    searchText = newValue
                },
                onClearClick = {
                    searchText = ""
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            if (filteredFarmers.isEmpty()) {
                EmptyFarmersState()
            } else {
                filteredFarmers.forEach { farmer ->
                    BuyerFarmerCard(
                        farmer = farmer,
                        onClick = {
                            selectedFarmer = farmer
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun FarmerSearchBar(
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
                        text = "Search farmer, crop, location",
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
fun BuyerFarmerCard(
    farmer: BuyerFarmerProfile,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                onClick()
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
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
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
                        text = farmer.name.first().toString(),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentYellow
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp)
                ) {
                    Text(
                        text = farmer.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "📍 ${farmer.location}",
                        fontSize = 13.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = farmer.verificationStatus,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentYellow,
                        maxLines = 1
                    )
                }

                TrustScoreBadge(
                    score = farmer.trustScore
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(
                    R.string.main_crops_with_value,
                    farmer.mainCrop
                ),
                fontSize = 13.sp,
                color = TextLight,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = {
                    farmer.trustScore / 100f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = AccentYellow,
                trackColor = Color.White.copy(alpha = 0.14f)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FarmerStatBox(
                    icon = "📦",
                    value = farmer.productsListed.toString(),
                    label = stringResource(R.string.products)
                )

                FarmerStatBox(
                    icon = "✅",
                    value = farmer.completedOrders.toString(),
                    label = stringResource(R.string.orders)
                )

                FarmerStatBox(
                    icon = "⭐",
                    value = stringResource(
                        R.string.percentage_value,
                        farmer.trustScore
                    ),
                    label = stringResource(R.string.trust)
                )
            }
        }
    }
}

@Composable
fun TrustScoreBadge(
    score: Int
) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(CircleShape)
            .background(SoftYellow)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = AccentYellow.copy(alpha = 0.22f)
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = score.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentYellow
        )
    }
}

@Composable
fun FarmerStatBox(
    icon: String,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextLight
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = TextMuted,
            maxLines = 1
        )
    }
}

@Composable
fun FarmerDetailDialog(
    farmer: BuyerFarmerProfile,
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
                text = farmer.name,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "🧑‍🌾",
                    fontSize = 36.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Location: ${farmer.location}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Main crops: ${farmer.mainCrop}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Verification: ${farmer.verificationStatus}",
                    color = AccentYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Products listed: ${farmer.productsListed}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Completed orders: ${farmer.completedOrders}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Trust score: ${farmer.trustScore}%",
                    color = AccentYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    )
}

@Composable
fun EmptyFarmersState() {
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
                text = "🔍",
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "No farmers found",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Try searching by farmer name, crop, city, or verification status.",
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 19.sp
            )
        }
    }
}