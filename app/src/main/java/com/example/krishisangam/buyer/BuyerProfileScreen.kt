package com.example.krishisangam.buyer

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftRed = Color(0xFFFFE3E3)
private val RedText = Color(0xFFD64B4B)

@Composable
fun BuyerProfileScreen() {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    var showLanguageSheet by remember {
        mutableStateOf(false)
    }

    var selectedLanguage by remember {
        mutableStateOf(LanguageManager.getSavedLanguage(context))
    }

    val buyerName = currentUser?.displayName ?: stringResource(R.string.buyer)
    val buyerEmail = currentUser?.email ?: "buyer@krishisangam.com"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp)
    ) {
        Text(
            text = stringResource(R.string.profile),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.manage_buyer_account),
            fontSize = 14.sp,
            color = Color.Gray
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
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        BuyerProfileOptionCard(
            icon = "🌐",
            title = stringResource(R.string.app_language),
            subtitle = stringResource(R.string.change_app_language),
            onClick = {
                showLanguageSheet = true
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        BuyerProfileOptionCard(
            icon = "❤️",
            title = stringResource(R.string.wishlist),
            subtitle = stringResource(R.string.view_saved_products),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        BuyerProfileOptionCard(
            icon = "📦",
            title = stringResource(R.string.my_orders),
            subtitle = stringResource(R.string.track_product_orders),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        BuyerProfileOptionCard(
            icon = "📍",
            title = stringResource(R.string.delivery_location),
            subtitle = stringResource(R.string.manage_delivery_address),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        BuyerProfileOptionCard(
            icon = "🔔",
            title = stringResource(R.string.notifications),
            subtitle = stringResource(R.string.manage_alerts),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(12.dp))

        BuyerProfileOptionCard(
            icon = "🛟",
            title = stringResource(R.string.help_support),
            subtitle = stringResource(R.string.contact_support),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(18.dp))

        BuyerLogoutCard(
            onClick = {
                FirebaseAuth.getInstance().signOut()
            }
        )

        Spacer(modifier = Modifier.height(80.dp))
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
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
                    .background(LightGreen),
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
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = email,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.buyer_account),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen,
                    modifier = Modifier
                        .background(LightGreen, RoundedCornerShape(18.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
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
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
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
                    .background(LightGreen),
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
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "›",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
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
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftRed
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
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
                    fontWeight = FontWeight.Bold,
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