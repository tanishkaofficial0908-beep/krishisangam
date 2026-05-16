package com.example.krishisangam.manager

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)

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
            text = "Farmers Directory",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Manage farmers connected to this Agro Node",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        farmers.forEach { farmer ->
            ManagerFarmerCard(farmer = farmer)
            Spacer(modifier = Modifier.height(15.dp))
        }

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun ManagerFarmerCard(
    farmer: ManagerFarmerProfile
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
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
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = farmer.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = "📍 ${farmer.location}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "Crops: ${farmer.mainCrops}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = farmer.status,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    modifier = Modifier
                        .background(LightGreen, RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Trust Score: ${farmer.trustScore}%",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = farmer.trustScore / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = PrimaryGreen,
                trackColor = LightGreen
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
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}