package com.example.krishisangam.manager

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
fun ManagerProfileScreen(
    onVerificationClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onFarmersClick: () -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    var showLanguageSheet by remember {
        mutableStateOf(false)
    }

    var selectedLanguage by remember {
        mutableStateOf(LanguageManager.getSavedLanguage(context))
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    val managerName = currentUser?.displayName ?: stringResource(R.string.node_manager)
    val managerEmail = currentUser?.email ?: "manager@krishisangam.com"

    val logoutTitle = stringResource(R.string.logout)

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
                        text = "Are you sure you want to logout from your manager account?",
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
                text = stringResource(R.string.profile),
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.manage_manager_account),
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            ManagerProfileHeaderCard(
                name = managerName,
                email = managerEmail
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = stringResource(R.string.account_options),
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            ManagerProfileOptionCard(
                icon = "🌐",
                title = stringResource(R.string.app_language),
                subtitle = stringResource(R.string.change_app_language),
                onClick = {
                    showLanguageSheet = true
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ManagerProfileOptionCard(
                icon = "✅",
                title = stringResource(R.string.verifications),
                subtitle = stringResource(R.string.review_farmer_listings),
                onClick = {
                    onVerificationClick()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ManagerProfileOptionCard(
                icon = "📦",
                title = stringResource(R.string.orders),
                subtitle = stringResource(R.string.manage_orders_dispatch),
                onClick = {
                    onOrdersClick()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ManagerProfileOptionCard(
                icon = "💳",
                title = stringResource(R.string.payments),
                subtitle = stringResource(R.string.track_payments_payouts),
                onClick = {
                    onPaymentsClick()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ManagerProfileOptionCard(
                icon = "🧑‍🌾",
                title = stringResource(R.string.farmers),
                subtitle = stringResource(R.string.view_farmer_directory),
                onClick = {
                    onFarmersClick()
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            ManagerLogoutCard(
                onClick = {
                    showLogoutDialog = true
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
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
fun ManagerProfileHeaderCard(
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
                    text = "👨‍💼",
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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.manager_account),
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
fun ManagerProfileOptionCard(
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
fun ManagerLogoutCard(
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
            color = RedText.copy(alpha = 0.24f)
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