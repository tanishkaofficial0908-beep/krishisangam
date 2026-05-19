package com.example.krishisangam.buyer

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

@Composable
fun BuyerProfileScreen(
    onEditProfileClick: () -> Unit,
    onDeliveryLocationClick: () -> Unit,
    onMyOrdersClick: () -> Unit,
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    var showLanguageSheet by remember {
        mutableStateOf(false)
    }

    var selectedLanguage by remember {
        mutableStateOf(LanguageManager.getSavedLanguage(context))
    }

    var showHelpDialog by remember {
        mutableStateOf(false)
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    val buyerName = currentUser?.displayName ?: stringResource(R.string.buyer)
    val buyerEmail = currentUser?.email ?: "buyer@krishisangam.com"

    if (showHelpDialog) {
        BuyerProfileInfoDialog(
            title = stringResource(R.string.help_support),
            emoji = "🛟",
            message = "Need help? You can contact Krishi Sangam support or visit your nearest Agro Node Manager.",
            onDismiss = {
                showHelpDialog = false
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
                        onLogout()
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
                    text = stringResource(R.string.logout),
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
                        text = "Are you sure you want to sign out from your buyer account?",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 22.dp)
                .padding(bottom = 110.dp)
        ) {
            Text(
                text = stringResource(R.string.profile),
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.manage_buyer_account),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            BuyerProfileHeaderCard(
                name = buyerName,
                email = buyerEmail
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = stringResource(R.string.account_options),
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            BuyerProfileOptionCard(
                icon = "✏️",
                title = "Edit Profile",
                subtitle = "Update name and delivery details",
                onClick = {
                    onEditProfileClick()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            BuyerProfileOptionCard(
                icon = "📦",
                title = stringResource(R.string.my_orders),
                subtitle = stringResource(R.string.track_product_orders),
                onClick = {
                    onMyOrdersClick()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            BuyerProfileOptionCard(
                icon = "📍",
                title = stringResource(R.string.delivery_location),
                subtitle = stringResource(R.string.manage_delivery_address),
                onClick = {
                    onDeliveryLocationClick()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            BuyerProfileOptionCard(
                icon = "🛟",
                title = stringResource(R.string.help_support),
                subtitle = stringResource(R.string.contact_support),
                onClick = {
                    showHelpDialog = true
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            BuyerLogoutCard(
                onClick = {
                    showLogoutDialog = true
                }
            )
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
fun BuyerProfileHeaderCard(
    name: String,
    email: String
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
                    text = "👤",
                    fontSize = 38.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = name,
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
            }
        }
    }
}

@Composable
fun BuyerProfileOptionCard(
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
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
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
                    fontSize = 23.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
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
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )
        }
    }
}

@Composable
fun BuyerLogoutCard(
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
            containerColor = SoftRed
        ),
        border = BorderStroke(
            width = 1.dp,
            color = RedText.copy(alpha = 0.22f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🚪",
                fontSize = 24.sp
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = stringResource(R.string.logout),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = RedText
                )

                Text(
                    text = stringResource(R.string.sign_out_account),
                    fontSize = 12.sp,
                    color = RedText
                )
            }
        }
    }
}

@Composable
fun BuyerProfileInfoDialog(
    title: String,
    emoji: String,
    message: String,
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
                text = title,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = emoji,
                    fontSize = 36.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    )
}