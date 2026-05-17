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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val SoftRed = Color(0xFFFF6B6B).copy(alpha = 0.16f)
private val RedText = Color(0xFFFF6B6B)

private const val FARMER_FILTER_ALL = "All"
private const val FARMER_FILTER_VERIFIED = "Verified"
private const val FARMER_FILTER_PREMIUM = "Premium"
private const val FARMER_FILTER_LOW_TRUST = "Low Trust"

data class ManagerFarmerProfile(
    val name: String,
    val location: String,
    val trustScore: Int,
    val productsListed: Int,
    val approvedProducts: Int,
    val rejectedProducts: Int,
    val mainCrops: String,
    val status: String
)

@Composable
fun ManagerFarmersDirectoryScreen() {
    var selectedFarmer by remember {
        mutableStateOf<ManagerFarmerProfile?>(null)
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedFilter by remember {
        mutableStateOf(FARMER_FILTER_ALL)
    }

    var filterMenuExpanded by remember {
        mutableStateOf(false)
    }

    val farmers = listOf(
        ManagerFarmerProfile(
            name = "Ramesh Patel",
            location = "Bhopal",
            trustScore = 86,
            productsListed = 8,
            approvedProducts = 6,
            rejectedProducts = 1,
            mainCrops = "Wheat, Rice",
            status = "Verified"
        ),
        ManagerFarmerProfile(
            name = "Shreya Gupta",
            location = "Sehore",
            trustScore = 78,
            productsListed = 5,
            approvedProducts = 3,
            rejectedProducts = 1,
            mainCrops = "Rice, Corn",
            status = "Verified"
        ),
        ManagerFarmerProfile(
            name = "Mahesh Sahu",
            location = "Raisen",
            trustScore = 91,
            productsListed = 11,
            approvedProducts = 10,
            rejectedProducts = 0,
            mainCrops = "Wheat, Tomato",
            status = "Premium"
        ),
        ManagerFarmerProfile(
            name = "Kishan Verma",
            location = "Vidisha",
            trustScore = 58,
            productsListed = 4,
            approvedProducts = 2,
            rejectedProducts = 2,
            mainCrops = "Onion, Potato",
            status = "Verified"
        )
    )

    val filteredFarmers = farmers.filter { farmer ->
        val matchesSearch =
            farmer.name.contains(searchText, ignoreCase = true) ||
                    farmer.location.contains(searchText, ignoreCase = true) ||
                    farmer.mainCrops.contains(searchText, ignoreCase = true) ||
                    farmer.status.contains(searchText, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            FARMER_FILTER_VERIFIED -> farmer.status.equals("Verified", ignoreCase = true)
            FARMER_FILTER_PREMIUM -> farmer.status.equals("Premium", ignoreCase = true)
            FARMER_FILTER_LOW_TRUST -> farmer.trustScore < 70
            else -> true
        }

        matchesSearch && matchesFilter
    }

    if (selectedFarmer != null) {
        ManagerFarmerDetailDialog(
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 22.dp)
                .padding(bottom = 110.dp)
        ) {
            Text(
                text = "Farmers Directory",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Manage farmers connected to this Agro Node",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            ManagerFarmersSummaryRow(
                total = farmers.size,
                verified = farmers.count { it.status.equals("Verified", ignoreCase = true) },
                premium = farmers.count { it.status.equals("Premium", ignoreCase = true) }
            )

            Spacer(modifier = Modifier.height(18.dp))

            ManagerFarmerSearchAndFilter(
                searchText = searchText,
                onSearchChange = {
                    searchText = it
                },
                selectedFilter = selectedFilter,
                filterMenuExpanded = filterMenuExpanded,
                onFilterMenuChange = {
                    filterMenuExpanded = it
                },
                onFilterSelected = { filter ->
                    selectedFilter = filter
                    filterMenuExpanded = false
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${filteredFarmers.size} farmers found",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredFarmers.isEmpty()) {
                EmptyManagerFarmersState()
            } else {
                filteredFarmers.forEach { farmer ->
                    ManagerFarmerCard(
                        farmer = farmer,
                        onClick = {
                            selectedFarmer = farmer
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun ManagerFarmerSearchAndFilter(
    searchText: String,
    onSearchChange: (String) -> Unit,
    selectedFilter: String,
    filterMenuExpanded: Boolean,
    onFilterMenuChange: (Boolean) -> Unit,
    onFilterSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .height(54.dp),
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
            TextField(
                value = searchText,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = "Search farmer, crop, location...",
                        color = TextMuted,
                        fontSize = 13.sp
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
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = TextLight,
                    unfocusedTextColor = TextLight,
                    cursorColor = AccentYellow
                )
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Box {
            Card(
                modifier = Modifier
                    .height(54.dp)
                    .clickable {
                        onFilterMenuChange(true)
                    },
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
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedFilter,
                        fontSize = 12.sp,
                        color = TextLight,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1
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
                expanded = filterMenuExpanded,
                onDismissRequest = {
                    onFilterMenuChange(false)
                },
                containerColor = DialogGreen
            ) {
                listOf(
                    FARMER_FILTER_ALL,
                    FARMER_FILTER_VERIFIED,
                    FARMER_FILTER_PREMIUM,
                    FARMER_FILTER_LOW_TRUST
                ).forEach { filter ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = filter,
                                color = TextLight,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        onClick = {
                            onFilterSelected(filter)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ManagerFarmersSummaryRow(
    total: Int,
    verified: Int,
    premium: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ManagerFarmerSummaryCard(
            value = total.toString(),
            label = "Total",
            icon = "🧑‍🌾",
            modifier = Modifier.weight(1f)
        )

        ManagerFarmerSummaryCard(
            value = verified.toString(),
            label = "Verified",
            icon = "✅",
            modifier = Modifier.weight(1f)
        )

        ManagerFarmerSummaryCard(
            value = premium.toString(),
            label = "Premium",
            icon = "⭐",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ManagerFarmerSummaryCard(
    value: String,
    label: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(90.dp)
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
                fontSize = 19.sp
            )

            Text(
                text = value,
                fontSize = 19.sp,
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
fun ManagerFarmerCard(
    farmer: ManagerFarmerProfile,
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
            modifier = Modifier.padding(17.dp)
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
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = farmer.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Text(
                        text = "📍 ${farmer.location}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Text(
                        text = "Crops: ${farmer.mainCrops}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                ManagerFarmerStatusBadge(
                    status = farmer.status,
                    trustScore = farmer.trustScore
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Trust Score: ${farmer.trustScore}%",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = {
                    farmer.trustScore / 100f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = if (farmer.trustScore < 70) RedText else AccentYellow,
                trackColor = Color.White.copy(alpha = 0.14f)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FarmerDirectoryStat(
                    value = farmer.productsListed.toString(),
                    label = "Products"
                )

                FarmerDirectoryStat(
                    value = farmer.approvedProducts.toString(),
                    label = "Approved"
                )

                FarmerDirectoryStat(
                    value = farmer.rejectedProducts.toString(),
                    label = "Rejected"
                )
            }
        }
    }
}

@Composable
fun ManagerFarmerStatusBadge(
    status: String,
    trustScore: Int
) {
    val isLowTrust = trustScore < 70
    val isPremium = status.equals("Premium", ignoreCase = true)

    val finalStatus = if (isLowTrust) {
        "Low Trust"
    } else {
        status
    }

    val bgColor = when {
        isLowTrust -> SoftRed
        isPremium -> SoftYellow
        else -> SoftGreen
    }

    val textColor = when {
        isLowTrust -> RedText
        isPremium -> AccentYellow
        else -> PrimaryGreen
    }

    Text(
        text = finalStatus,
        fontSize = 11.sp,
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
            .padding(horizontal = 10.dp, vertical = 5.dp),
        maxLines = 1
    )
}

@Composable
fun FarmerDirectoryStat(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentYellow
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
fun EmptyManagerFarmersState() {
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
                text = "🧑‍🌾",
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No farmers found",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Try searching another name, crop, location or filter.",
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ManagerFarmerDetailDialog(
    farmer: ManagerFarmerProfile,
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
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "🧑‍🌾",
                    fontSize = 38.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Location: ${farmer.location}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Main crops: ${farmer.mainCrops}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Trust score: ${farmer.trustScore}%",
                    color = if (farmer.trustScore < 70) RedText else AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Products listed: ${farmer.productsListed}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Approved products: ${farmer.approvedProducts}",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Rejected products: ${farmer.rejectedProducts}",
                    color = if (farmer.rejectedProducts > 0) RedText else DialogText,
                    fontWeight = if (farmer.rejectedProducts > 0) {
                        FontWeight.ExtraBold
                    } else {
                        FontWeight.Normal
                    },
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Status: ${if (farmer.trustScore < 70) "Low Trust" else farmer.status}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    )
}