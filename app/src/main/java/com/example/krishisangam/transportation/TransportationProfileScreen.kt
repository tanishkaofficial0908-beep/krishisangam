package com.example.krishisangam.transportation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

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
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val SoftRed = Color(0xFFFF6B6B).copy(alpha = 0.16f)
private val RedText = Color(0xFFFF6B6B)

data class TransportationProfileInfo(
    val title: String,
    val emoji: String,
    val message: String
)

@Composable
fun TransportationProfileScreen() {
    val currentUser = FirebaseAuth.getInstance().currentUser

    var partnerName by remember {
        mutableStateOf(currentUser?.displayName ?: "")
    }

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var vehicleType by remember {
        mutableStateOf("Mini Truck")
    }

    var vehicleNumber by remember {
        mutableStateOf("")
    }

    var licenseNumber by remember {
        mutableStateOf("")
    }

    var experienceYears by remember {
        mutableStateOf("")
    }

    var perKmCost by remember {
        mutableStateOf("")
    }

    var loadCapacity by remember {
        mutableStateOf("")
    }

    var selectedInfo by remember {
        mutableStateOf<TransportationProfileInfo?>(null)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    if (selectedInfo != null) {
        TransportationProfileInfoDialog(
            info = selectedInfo!!,
            onDismiss = {
                selectedInfo = null
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            containerColor = DialogGreen,
            titleContentColor = TextLight,
            textContentColor = DialogText,
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        FirebaseAuth.getInstance().signOut()
                    }
                ) {
                    Text(
                        text = "Logout",
                        color = RedText,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text(
                        text = "Cancel",
                        color = AccentYellow,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            title = {
                Text(
                    text = "Logout",
                    color = TextLight,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "🚪",
                        fontSize = 36.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Are you sure you want to logout from your Transport Partner account?",
                        color = DialogText,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
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
                text = "Transport Partner Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Create your driver ID and list your vehicle for Agro Node deliveries",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            TransportationVerificationStatusCard()

            Spacer(modifier = Modifier.height(18.dp))

            TransportationProfileHeaderCard(
                email = currentUser?.email ?: "transport@krishisangam.com"
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Driver Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(12.dp))

            TransportationInputField(
                label = "Full Name",
                value = partnerName,
                onValueChange = {
                    partnerName = it
                },
                placeholder = "Enter driver / owner name"
            )

            Spacer(modifier = Modifier.height(14.dp))

            TransportationInputField(
                label = "Phone Number",
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },
                placeholder = "Enter mobile number"
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Vehicle Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(12.dp))

            TransportationInputField(
                label = "Vehicle Type",
                value = vehicleType,
                onValueChange = {
                    vehicleType = it
                },
                placeholder = "Mini Truck / Pickup / Tempo"
            )

            Spacer(modifier = Modifier.height(14.dp))

            TransportationInputField(
                label = "Vehicle Number",
                value = vehicleNumber,
                onValueChange = {
                    vehicleNumber = it
                },
                placeholder = "Example: MP04 AB 1234"
            )

            Spacer(modifier = Modifier.height(14.dp))

            TransportationInputField(
                label = "Vehicle License Number",
                value = licenseNumber,
                onValueChange = {
                    licenseNumber = it
                },
                placeholder = "Enter license / permit number"
            )

            Spacer(modifier = Modifier.height(14.dp))

            TransportationInputField(
                label = "Driving Experience",
                value = experienceYears,
                onValueChange = {
                    experienceYears = it
                },
                placeholder = "Example: 5 years"
            )

            Spacer(modifier = Modifier.height(14.dp))

            TransportationInputField(
                label = "Vehicle Per KM Cost",
                value = perKmCost,
                onValueChange = {
                    perKmCost = it
                },
                placeholder = "Example: ₹25/km"
            )

            Spacer(modifier = Modifier.height(14.dp))

            TransportationInputField(
                label = "Load Capacity",
                value = loadCapacity,
                onValueChange = {
                    loadCapacity = it
                },
                placeholder = "Example: 1.5 Ton / 1500 kg"
            )

            Spacer(modifier = Modifier.height(18.dp))

            TransportationVehiclePhotoCard(
                onClick = {
                    selectedInfo = TransportationProfileInfo(
                        title = "Vehicle Photo",
                        emoji = "📷",
                        message = "Vehicle photo upload will be connected later using Firebase Storage. For now this is a UI placeholder."
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TransportationSubmitProfileButton(
                onClick = {
                    selectedInfo = TransportationProfileInfo(
                        title = "Profile Submitted",
                        emoji = "✅",
                        message = "Your transport partner profile has been submitted for Agro Node Manager approval. Backend approval will be connected later."
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransportationProfileActionCard(
                icon = "🛟",
                title = "Help & Support",
                subtitle = "Contact Agro Node support",
                onClick = {
                    selectedInfo = TransportationProfileInfo(
                        title = "Help & Support",
                        emoji = "🛟",
                        message = "Need help? Contact Krishi Sangam support or your nearest Agro Node Manager for transporter approval and trip support."
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TransportationProfileActionCard(
                icon = "🚪",
                title = "Logout",
                subtitle = "Sign out from Transport Partner account",
                onClick = {
                    showLogoutDialog = true
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun TransportationVerificationStatusCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            ),
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(SoftYellow)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = AccentYellow.copy(alpha = 0.24f)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🕒",
                    fontSize = 25.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = "Verification Pending",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Agro Node Manager will approve your vehicle profile before assigning trips.",
                    fontSize = 13.sp,
                    color = TextMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun TransportationProfileHeaderCard(
    email: String
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
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
                    text = "🚚",
                    fontSize = 38.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Transport Partner",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = email,
                    fontSize = 13.sp,
                    color = TextMuted,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Vehicle Partner Account",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentYellow,
                    modifier = Modifier
                        .background(
                            AccentYellow.copy(alpha = 0.16f),
                            RoundedCornerShape(18.dp)
                        )
                        .border(
                            border = BorderStroke(
                                width = 1.dp,
                                color = AccentYellow.copy(alpha = 0.24f)
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
fun TransportationInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Text(
        text = label,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = TextLight,
        modifier = Modifier.padding(bottom = 7.dp)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = TextMuted,
                fontSize = 14.sp
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        textStyle = TextStyle(
            color = TextLight,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextLight,
            unfocusedTextColor = TextLight,
            focusedBorderColor = AccentYellow,
            unfocusedBorderColor = BorderGlass,
            focusedContainerColor = GlassCard,
            unfocusedContainerColor = GlassCard,
            cursorColor = AccentYellow
        )
    )
}

@Composable
fun TransportationVehiclePhotoCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📷",
                fontSize = 38.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Vehicle Photo",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tap to upload vehicle photo later",
                fontSize = 13.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun TransportationSubmitProfileButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(PrimaryGreen)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Submit For Approval",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

@Composable
fun TransportationProfileActionCard(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
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
                    text = icon,
                    fontSize = 20.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextMuted,
                    maxLines = 1
                )
            }

            Text(
                text = "›",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )
        }
    }
}

@Composable
fun TransportationProfileInfoDialog(
    info: TransportationProfileInfo,
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
                text = info.title,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = info.emoji,
                    fontSize = 36.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = info.message,
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    )
}