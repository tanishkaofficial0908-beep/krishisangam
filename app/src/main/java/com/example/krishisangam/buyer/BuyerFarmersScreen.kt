package com.example.krishisangam.buyer

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.krishisangam.R

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Text(
            text = stringResource(R.string.farmer_directory),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.verified_farmers_trust_scores),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(22.dp))

        farmers.forEach { farmer ->
            BuyerFarmerCard(
                farmer = farmer
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun BuyerFarmerCard(
    farmer: BuyerFarmerProfile
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
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(LightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = farmer.name.first().toString(),
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
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
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "📍 ${farmer.location}",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = farmer.verificationStatus,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryGreen,
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
                color = TextDark,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = {
                    farmer.trustScore / 100f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = PrimaryGreen,
                trackColor = LightGreen
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
            .background(Color(0xFFFFF3CD)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = score.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB8860B)
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
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}