package com.example.krishisangam.farmer

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
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

private val ProfilePrimaryGreen = Color(0xFF01AC66)
private val ProfileBackgroundColor = Color(0xFF003D22)
private val ProfileDeepGreen = Color(0xFF002514)
private val ProfileDarkGreen = Color(0xFF005C32)
private val ProfileAccentYellow = Color(0xFFFFC107)
private val ProfileTextLight = Color(0xFFF5FFF9)
private val ProfileTextMuted = Color(0xFFB9D8C7)
private val ProfileGlassCard = Color.White.copy(alpha = 0.105f)
private val ProfileBorderGlass = Color.White.copy(alpha = 0.16f)
private val ProfileDialogGreen = Color(0xFF123D2B)
private val ProfileDialogText = Color(0xFFD8EDE3)
private val ProfileSoftRed = Color(0xFFFF6B6B).copy(alpha = 0.14f)
private val ProfileRedText = Color(0xFFFF8A8A)

@Composable
fun FarmerProfileScreen(
    onEditProfileClick: () -> Unit,
    onFarmDetailsClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onLogoutConfirm: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val farmerName = currentUser?.displayName ?: "Farmer"
    val farmerEmail = currentUser?.email ?: "No email found"

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    if (showLogoutDialog) {
        FarmerLogoutDialog(
            onDismiss = {
                showLogoutDialog = false
            },
            onConfirmLogout = {
                showLogoutDialog = false
                FirebaseAuth.getInstance().signOut()
                onLogoutConfirm()
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ProfileDarkGreen,
                        ProfileBackgroundColor,
                        ProfileDeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(ProfileAccentYellow.copy(alpha = 0.07f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomStart)
                .background(ProfilePrimaryGreen.copy(alpha = 0.10f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 22.dp)
                .padding(bottom = 110.dp)
        ) {
            Text(
                text = "Profile",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ProfileTextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Manage your farmer account",
                fontSize = 14.sp,
                color = ProfileTextMuted
            )

            Spacer(modifier = Modifier.height(22.dp))

            FarmerProfileHeaderCard(
                farmerName = farmerName,
                farmerEmail = farmerEmail
            )

            Spacer(modifier = Modifier.height(18.dp))

            FarmerProfileTrustCard()

            Spacer(modifier = Modifier.height(18.dp))

            FarmerProfileActionCard(
                icon = "🌐",
                title = "App Language",
                subtitle = "Change app language",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "✏️",
                title = "Edit Profile",
                subtitle = "Update name, location and contact",
                onClick = onEditProfileClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "🌾",
                title = "Farm Details",
                subtitle = "Land size, crops and farming type",
                onClick = onFarmDetailsClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "📜",
                title = "Order History",
                subtitle = "View all completed orders",
                onClick = onOrdersClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "❓",
                title = "Help Center",
                subtitle = "Get support from Krishi Sangam",
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "🚪",
                title = "Sign Out",
                subtitle = "Logout from farmer account",
                isLogout = true,
                onClick = {
                    showLogoutDialog = true
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun FarmerProfileHeaderCard(
    farmerName: String,
    farmerEmail: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(26.dp)
            ),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProfileGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = ProfileBorderGlass
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
                    .size(66.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = ProfileBorderGlass
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👨‍🌾",
                    fontSize = 34.sp
                )
            }

            Column(
                modifier = Modifier.padding(start = 14.dp)
            ) {
                Text(
                    text = farmerName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ProfileTextLight,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = farmerEmail,
                    fontSize = 13.sp,
                    color = ProfileTextMuted,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Verified Farmer",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ProfileAccentYellow,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ProfileAccentYellow.copy(alpha = 0.16f))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun FarmerProfileTrustCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = ProfileGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = ProfileBorderGlass
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Trust Score",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = ProfileTextLight
                    )

                    Text(
                        text = "Based on quality and delivery reliability",
                        fontSize = 12.sp,
                        color = ProfileTextMuted
                    )
                }

                Text(
                    text = "70/100",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ProfileAccentYellow
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { 0.70f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = ProfileAccentYellow,
                trackColor = Color.White.copy(alpha = 0.14f)
            )
        }
    }
}

@Composable
private fun FarmerProfileActionCard(
    icon: String,
    title: String,
    subtitle: String,
    isLogout: Boolean = false,
    onClick: () -> Unit
) {
    val titleColor = if (isLogout) {
        ProfileRedText
    } else {
        ProfileTextLight
    }

    val subtitleColor = if (isLogout) {
        ProfileRedText.copy(alpha = 0.78f)
    } else {
        ProfileTextMuted
    }

    val cardColor = if (isLogout) {
        ProfileSoftRed
    } else {
        ProfileGlassCard
    }

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
            containerColor = cardColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isLogout) {
                ProfileRedText.copy(alpha = 0.22f)
            } else {
                ProfileBorderGlass
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = ProfileBorderGlass
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 25.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = titleColor
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = subtitleColor
                )
            }

            Text(
                text = "›",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ProfileAccentYellow
            )
        }
    }
}

@Composable
private fun FarmerLogoutDialog(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ProfileDialogGreen,
        titleContentColor = ProfileTextLight,
        textContentColor = ProfileDialogText,
        confirmButton = {
            TextButton(
                onClick = onConfirmLogout
            ) {
                Text(
                    text = "Logout",
                    color = ProfileRedText,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    color = ProfileAccentYellow,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        title = {
            Text(
                text = "Logout",
                color = ProfileTextLight,
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
                    text = "Are you sure you want to log out from your farmer account?",
                    color = ProfileDialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    )
}