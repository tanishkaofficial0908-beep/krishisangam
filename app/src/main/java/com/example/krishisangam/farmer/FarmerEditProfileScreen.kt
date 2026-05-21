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
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

private val FarmerEditPrimaryGreen = Color(0xFF01AC66)
private val FarmerEditBackgroundColor = Color(0xFF003D22)
private val FarmerEditDeepGreen = Color(0xFF002514)
private val FarmerEditDarkGreen = Color(0xFF005C32)
private val FarmerEditAccentYellow = Color(0xFFFFC107)
private val FarmerEditTextLight = Color(0xFFF5FFF9)
private val FarmerEditTextMuted = Color(0xFFB9D8C7)
private val FarmerEditGlassCard = Color.White.copy(alpha = 0.105f)
private val FarmerEditBorderGlass = Color.White.copy(alpha = 0.16f)
private val FarmerEditError = Color(0xFFFF6B6B)

@Composable
fun FarmerEditProfileScreen(
    onBackClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    var fullName by remember {
        mutableStateOf(currentUser?.displayName ?: "")
    }

    var email by remember {
        mutableStateOf(currentUser?.email ?: "")
    }

    var phoneNumber by remember {
        mutableStateOf(currentUser?.phoneNumber ?: "")
    }

    var location by remember {
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
                fullName = document.getString("fullName") ?: currentUser.displayName ?: fullName
                email = document.getString("email") ?: currentUser.email ?: email
                phoneNumber = document.getString("phoneNumber") ?: currentUser.phoneNumber ?: phoneNumber
                location = document.getString("location") ?: ""
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        FarmerEditDarkGreen,
                        FarmerEditBackgroundColor,
                        FarmerEditDeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(FarmerEditAccentYellow.copy(alpha = 0.07f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomStart)
                .background(FarmerEditPrimaryGreen.copy(alpha = 0.10f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .padding(bottom = 110.dp)
        ) {
            FarmerEditTopBar(
                title = "Edit Profile",
                subtitle = "Update name, location and contact",
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            FarmerEditGlassCard {
                Text(
                    text = "Farmer Details",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = FarmerEditTextLight
                )

                Spacer(modifier = Modifier.height(14.dp))

                FarmerEditTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                    },
                    label = "Full Name"
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmerEditTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = "Email",
                    enabled = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmerEditTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                    },
                    label = "Phone Number"
                )

                Spacer(modifier = Modifier.height(12.dp))

                FarmerEditTextField(
                    value = location,
                    onValueChange = {
                        location = it
                    },
                    label = "Location / Village / City"
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Email is read-only. Phone number is saved in farmer profile data.",
                    color = FarmerEditTextMuted,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (message.isNotBlank()) {
                Text(
                    text = message,
                    fontSize = 13.sp,
                    color = if (message.contains("success", ignoreCase = true)) {
                        FarmerEditAccentYellow
                    } else {
                        FarmerEditError
                    },
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            FarmerEditPrimaryButton(
                text = if (isSaving) {
                    "Saving..."
                } else {
                    "Save Profile"
                },
                enabled = !isSaving,
                onClick = {
                    val uid = currentUser?.uid

                    if (uid.isNullOrBlank()) {
                        message = "User not found. Please login again."
                        return@FarmerEditPrimaryButton
                    }

                    if (fullName.isBlank()) {
                        message = "Please enter your full name."
                        return@FarmerEditPrimaryButton
                    }

                    isSaving = true
                    message = ""

                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName.trim())
                        .build()

                    currentUser.updateProfile(profileUpdate)
                        .addOnSuccessListener {
                            val farmerData = mapOf(
                                "farmerId" to uid,
                                "fullName" to fullName.trim(),
                                "email" to email,
                                "phoneNumber" to phoneNumber.trim(),
                                "location" to location.trim(),
                                "role" to "farmer"
                            )

                            firestore.collection("farmers")
                                .document(uid)
                                .set(farmerData, SetOptions.merge())
                                .addOnSuccessListener {
                                    isSaving = false
                                    message = "Profile updated successfully."
                                }
                                .addOnFailureListener { exception ->
                                    isSaving = false
                                    message = exception.message ?: "Failed to save profile data."
                                }
                        }
                        .addOnFailureListener { exception ->
                            isSaving = false
                            message = exception.message ?: "Failed to update profile."
                        }
                }
            )
        }
    }
}

@Composable
private fun FarmerEditTopBar(
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
                        color = FarmerEditBorderGlass
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
                color = FarmerEditTextLight
            )
        }

        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold,
                color = FarmerEditTextLight
            )

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = FarmerEditTextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun FarmerEditGlassCard(
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
            containerColor = FarmerEditGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = FarmerEditBorderGlass
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
private fun FarmerEditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = {
            Text(text = label)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = FarmerEditTextLight,
            unfocusedTextColor = FarmerEditTextLight,
            disabledTextColor = FarmerEditTextMuted,
            focusedLabelColor = FarmerEditAccentYellow,
            unfocusedLabelColor = FarmerEditTextMuted,
            disabledLabelColor = FarmerEditTextMuted,
            focusedBorderColor = FarmerEditAccentYellow,
            unfocusedBorderColor = FarmerEditBorderGlass,
            disabledBorderColor = FarmerEditBorderGlass,
            cursorColor = FarmerEditAccentYellow,
            focusedContainerColor = Color.White.copy(alpha = 0.06f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
            disabledContainerColor = Color.White.copy(alpha = 0.04f)
        )
    )
}

@Composable
private fun FarmerEditPrimaryButton(
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
                    FarmerEditPrimaryGreen
                } else {
                    FarmerEditPrimaryGreen.copy(alpha = 0.45f)
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