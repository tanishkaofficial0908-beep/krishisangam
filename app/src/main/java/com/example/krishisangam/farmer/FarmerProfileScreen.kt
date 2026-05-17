package com.example.krishisangam.farmer

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.LanguageManager
import com.example.krishisangam.LanguageSelectionBottomSheet
import com.example.krishisangam.R
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
private val SoftRed = Color(0xFFFF6B6B).copy(alpha = 0.16f)
private val RedText = Color(0xFFFF6B6B)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)

data class FarmerProfileInfo(
    val title: String,
    val emoji: String,
    val message: String
)

@Composable
fun FarmerProfileScreen(
    onOrdersClick: () -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    var showLanguageSheet by remember {
        mutableStateOf(false)
    }

    var selectedLanguage by remember {
        mutableStateOf(LanguageManager.getSavedLanguage(context))
    }

    var selectedInfo by remember {
        mutableStateOf<FarmerProfileInfo?>(null)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    val farmerName = currentUser?.displayName ?: stringResource(R.string.farmer)
    val farmerEmail = currentUser?.email ?: stringResource(R.string.no_email_found)
    val logoutTitle = stringResource(R.string.logout)

    if (selectedInfo != null) {
        FarmerProfileInfoDialog(
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
                        text = logoutTitle,
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
                    text = logoutTitle,
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
                        text = "Are you sure you want to logout from your farmer account?",
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
                .padding(bottom = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.profile),
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Manage farmer account",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(96.dp)
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
                    text = "👨‍🌾",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = farmerName,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight,
                maxLines = 1
            )

            Text(
                text = farmerEmail,
                fontSize = 14.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.verified_farmer),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimaryGreen,
                modifier = Modifier
                    .background(PrimaryGreen.copy(alpha = 0.16f), RoundedCornerShape(20.dp))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = PrimaryGreen.copy(alpha = 0.24f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(26.dp))

            FarmerProfileTrustCard()

            Spacer(modifier = Modifier.height(18.dp))

            FarmerProfileInfoCard()

            Spacer(modifier = Modifier.height(18.dp))

            FarmerProfileActionCard(
                icon = "🌐",
                title = stringResource(R.string.app_language),
                subtitle = stringResource(R.string.change_app_language),
                onClick = {
                    showLanguageSheet = true
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "✏️",
                title = "Edit Profile",
                subtitle = "Update name, location and contact",
                onClick = {
                    selectedInfo = FarmerProfileInfo(
                        title = "Edit Profile",
                        emoji = "✏️",
                        message = "Edit profile feature is coming soon. Later, farmers will be able to update name, location and contact details here."
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "🌾",
                title = "Farm Details",
                subtitle = "Land size, crops and farming type",
                onClick = {
                    selectedInfo = FarmerProfileInfo(
                        title = "Farm Details",
                        emoji = "🌾",
                        message = "Farm details feature is coming soon. Later, farmers will be able to add land size, crops and farming type here."
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "💳",
                title = "Payment Method",
                subtitle = "Bank account and payout details",
                onClick = {
                    selectedInfo = FarmerProfileInfo(
                        title = "Payment Method",
                        emoji = "💳",
                        message = "Payment method setup is coming soon. Later, farmers will be able to add bank account and payout details here."
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "📜",
                title = stringResource(R.string.order_history),
                subtitle = stringResource(R.string.view_all_completed_orders),
                onClick = {
                    onOrdersClick()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "❓",
                title = stringResource(R.string.help_center),
                subtitle = stringResource(R.string.get_support_from_krishi_sangam),
                onClick = {
                    selectedInfo = FarmerProfileInfo(
                        title = "Help Center",
                        emoji = "❓",
                        message = "Need help? You can contact Krishi Sangam support or visit your nearest Agro Node Manager for support."
                    )
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            FarmerProfileActionCard(
                icon = "🚪",
                title = stringResource(R.string.sign_out),
                subtitle = stringResource(R.string.logout_from_farmer_account),
                onClick = {
                    showLogoutDialog = true
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (showLanguageSheet) {
        LanguageSelectionBottomSheet(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = { languageTag ->
                selectedLanguage = languageTag
                showLanguageSheet = false

                LanguageManager.changeLanguage(
                    context = context,
                    languageTag = languageTag
                )
            },
            onDismiss = {
                showLanguageSheet = false
            }
        )
    }
}

@Composable
fun FarmerProfileTrustCard() {
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
                        text = stringResource(R.string.trust_score),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.based_on_quality_delivery_reliability),
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Text(
                    text = stringResource(R.string.score_out_of_100, 70),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentYellow
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { 0.70f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = AccentYellow,
                trackColor = Color.White.copy(alpha = 0.14f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.good),
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow,
                modifier = Modifier
                    .background(SoftYellow, RoundedCornerShape(20.dp))
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = AccentYellow.copy(alpha = 0.24f)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun FarmerProfileInfoCard() {
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
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            FarmerProfileRow(
                title = stringResource(R.string.farmer_id),
                value = "FRM-BPL-001"
            )

            FarmerProfileRow(
                title = stringResource(R.string.location),
                value = "Bhopal, Madhya Pradesh"
            )

            FarmerProfileRow(
                title = stringResource(R.string.main_crops),
                value = stringResource(R.string.wheat_rice_tomato)
            )

            FarmerProfileRow(
                title = stringResource(R.string.products_listed),
                value = stringResource(R.string.products_count, 10)
            )

            FarmerProfileRow(
                title = stringResource(R.string.approved_products),
                value = stringResource(R.string.approved_count, 3)
            )

            FarmerProfileRow(
                title = stringResource(R.string.completed_orders),
                value = stringResource(R.string.orders_count, 6)
            )

            FarmerProfileRow(
                title = stringResource(R.string.total_earnings),
                value = "₹7,230"
            )
        }
    }
}

@Composable
fun FarmerProfileRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = TextMuted,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentYellow,
            maxLines = 1
        )
    }
}

@Composable
fun FarmerProfileActionCard(
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
fun FarmerProfileInfoDialog(
    info: FarmerProfileInfo,
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