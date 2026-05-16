package com.example.krishisangam.farmer

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
import androidx.compose.material3.HorizontalDivider
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
import com.google.firebase.auth.FirebaseAuth

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftYellow = Color(0xFFFFF4D6)
private val YellowText = Color(0xFFB8860B)

@Composable
fun FarmerProfileScreen() {
    val currentUser = FirebaseAuth.getInstance().currentUser

    val farmerName = currentUser?.displayName ?: stringResource(R.string.farmer)
    val farmerEmail = currentUser?.email ?: stringResource(R.string.no_email_found)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(LightGreen),
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
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Text(
            text = farmerEmail,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.verified_farmer),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen,
            modifier = Modifier
                .background(LightGreen, RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        FarmerProfileTrustCard()

        Spacer(modifier = Modifier.height(18.dp))

        FarmerProfileInfoCard()

        Spacer(modifier = Modifier.height(18.dp))

        FarmerProfileActionCard(
            icon = "✏️",
            title = stringResource(R.string.edit_profile),
            subtitle = stringResource(R.string.update_name_location_contact)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FarmerProfileActionCard(
            icon = "🌾",
            title = stringResource(R.string.farm_details),
            subtitle = stringResource(R.string.land_size_crops_farming_type)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FarmerProfileActionCard(
            icon = "💳",
            title = stringResource(R.string.payment_method),
            subtitle = stringResource(R.string.bank_account_payout_details)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FarmerProfileActionCard(
            icon = "📜",
            title = stringResource(R.string.order_history),
            subtitle = stringResource(R.string.view_all_completed_orders)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FarmerProfileActionCard(
            icon = "❓",
            title = stringResource(R.string.help_center),
            subtitle = stringResource(R.string.get_support_from_krishi_sangam)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FarmerProfileActionCard(
            icon = "🚪",
            title = stringResource(R.string.sign_out),
            subtitle = stringResource(R.string.logout_from_farmer_account)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun FarmerProfileTrustCard() {
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
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.trust_score),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.based_on_quality_delivery_reliability),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Text(
                    text = stringResource(R.string.score_out_of_100, 70),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = YellowText
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { 0.70f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = PrimaryGreen,
                trackColor = LightGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.good),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = YellowText,
                modifier = Modifier
                    .background(SoftYellow, RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun FarmerProfileInfoCard() {
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
            FarmerProfileRow(
                title = stringResource(R.string.farmer_id),
                value = "FRM-BPL-001"
            )

            HorizontalDivider(color = Color(0xFFEDEDED))

            FarmerProfileRow(
                title = stringResource(R.string.location),
                value = "Bhopal, Madhya Pradesh"
            )

            HorizontalDivider(color = Color(0xFFEDEDED))

            FarmerProfileRow(
                title = stringResource(R.string.main_crops),
                value = stringResource(R.string.wheat_rice_tomato)
            )

            HorizontalDivider(color = Color(0xFFEDEDED))

            FarmerProfileRow(
                title = stringResource(R.string.products_listed),
                value = stringResource(R.string.products_count, 10)
            )

            HorizontalDivider(color = Color(0xFFEDEDED))

            FarmerProfileRow(
                title = stringResource(R.string.approved_products),
                value = stringResource(R.string.approved_count, 3)
            )

            HorizontalDivider(color = Color(0xFFEDEDED))

            FarmerProfileRow(
                title = stringResource(R.string.completed_orders),
                value = stringResource(R.string.orders_count, 6)
            )

            HorizontalDivider(color = Color(0xFFEDEDED))

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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )
    }
}

@Composable
fun FarmerProfileActionCard(
    icon: String,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
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
                    .background(LightGreen),
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
                fontSize = 28.sp,
                color = Color.Gray
            )
        }
    }
}