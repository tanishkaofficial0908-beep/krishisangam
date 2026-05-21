package com.example.krishisangam.farmer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

private val FarmPrimaryGreen = Color(0xFF01AC66)
private val FarmBackgroundColor = Color(0xFF003D22)
private val FarmDeepGreen = Color(0xFF002514)
private val FarmDarkGreen = Color(0xFF005C32)
private val FarmAccentYellow = Color(0xFFFFC107)
private val FarmTextLight = Color(0xFFF5FFF9)
private val FarmTextMuted = Color(0xFFB9D8C7)
private val FarmGlassCard = Color.White.copy(alpha = 0.105f)
private val FarmBorderGlass = Color.White.copy(alpha = 0.16f)
private val FarmError = Color(0xFFFF6B6B)

@Composable
fun FarmerFarmDetailsScreen(
    onBackClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    var landSize by remember {
        mutableStateOf("")
    }

    var farmLocation by remember {
        mutableStateOf("")
    }

    var mainCrops by remember {
        mutableStateOf("")
    }

    var farmingType by remember {
        mutableStateOf("")
    }

    var irrigationType by remember {
        mutableStateOf("")
    }

    var soilType by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(currentUser?.uid) {
        val uid = currentUser?.uid ?: return@LaunchedEffect

        firestore.collection("farmers")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                landSize = document.getString("landSize") ?: ""
                farmLocation = document.getString("farmLocation") ?: ""
                mainCrops = document.getString("mainCrops") ?: ""
                farmingType = document.getString("farmingType") ?: ""
                irrigationType = document.getString("irrigationType") ?: ""
                soilType = document.getString("soilType") ?: ""
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        FarmDarkGreen,
                        FarmBackgroundColor,
                        FarmDeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(FarmAccentYellow.copy(alpha = 0.07f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomStart)
                .background(FarmPrimaryGreen.copy(alpha = 0.10f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .padding(bottom = 110.dp)
        ) {
            FarmerFarmTopBar(
                title = "Farm Details",
                subtitle = "Land size, crops and farming type",
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            FarmerFarmGlassCard {
                Text(
                    text = "Your Farm Information",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = FarmTextLight
                )

                Spacer(modifier = Modifier.height(14.dp))

                FarmerFarmTextField(
                    value = landSize,
                    onValueChange = {
                        landSize = it
                    },
                    label = "Land Size e.g. 2 acres"
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmerFarmTextField(
                    value = farmLocation,
                    onValueChange = {
                        farmLocation = it
                    },
                    label = "Farm Location / Village"
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmerFarmTextField(
                    value = mainCrops,
                    onValueChange = {
                        mainCrops = it
                    },
                    label = "Main Crops e.g. Wheat, Rice, Tomato"
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmerFarmTextField(
                    value = farmingType,
                    onValueChange = {
                        farmingType = it
                    },
                    label = "Farming Type e.g. Organic / Traditional"
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmerFarmTextField(
                    value = irrigationType,
                    onValueChange = {
                        irrigationType = it
                    },
                    label = "Irrigation Type e.g. Canal / Borewell"
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmerFarmTextField(
                    value = soilType,
                    onValueChange = {
                        soilType = it
                    },
                    label = "Soil Type e.g. Black soil / Loamy soil"
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    fontSize = 13.sp,
                    color = if (message.contains("success", ignoreCase = true)) {
                        FarmAccentYellow
                    } else {
                        FarmError
                    },
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            FarmerFarmPrimaryButton(
                text = if (isSaving) {
                    "Saving..."
                } else {
                    "Save Farm Details"
                },
                enabled = !isSaving,
                onClick = {
                    val uid = currentUser?.uid

                    if (uid.isNullOrBlank()) {
                        message = "User not found. Please login again."
                        return@FarmerFarmPrimaryButton
                    }

                    if (landSize.isBlank() || farmLocation.isBlank() || mainCrops.isBlank()) {
                        message = "Please fill land size, farm location and main crops."
                        return@FarmerFarmPrimaryButton
                    }

                    isSaving = true
                    message = ""

                    val farmData = mapOf(
                        "farmerId" to uid,
                        "landSize" to landSize.trim(),
                        "farmLocation" to farmLocation.trim(),
                        "mainCrops" to mainCrops.trim(),
                        "farmingType" to farmingType.trim(),
                        "irrigationType" to irrigationType.trim(),
                        "soilType" to soilType.trim()
                    )

                    firestore.collection("farmers")
                        .document(uid)
                        .set(farmData, SetOptions.merge())
                        .addOnSuccessListener {
                            isSaving = false
                            message = "Farm details saved successfully."
                        }
                        .addOnFailureListener { exception ->
                            isSaving = false
                            message = exception.message ?: "Failed to save farm details."
                        }
                }
            )
        }
    }
}

@Composable
private fun FarmerFarmTopBar(
    title: String,
    subtitle: String,
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
                        color = FarmBorderGlass
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
                fontWeight = FontWeight.ExtraBold,
                color = FarmTextLight
            )
        }

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FarmTextLight
            )

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = FarmTextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun FarmerFarmGlassCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = FarmGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = FarmBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}

@Composable
private fun FarmerFarmTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = FarmTextLight,
            unfocusedTextColor = FarmTextLight,
            focusedLabelColor = FarmAccentYellow,
            unfocusedLabelColor = FarmTextMuted,
            focusedBorderColor = FarmAccentYellow,
            unfocusedBorderColor = FarmBorderGlass,
            cursorColor = FarmAccentYellow,
            focusedContainerColor = Color.White.copy(alpha = 0.06f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
        )
    )
}

@Composable
private fun FarmerFarmPrimaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (enabled) {
                    FarmPrimaryGreen
                } else {
                    FarmPrimaryGreen.copy(alpha = 0.45f)
                }
            )
            .clickable(enabled = enabled) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}