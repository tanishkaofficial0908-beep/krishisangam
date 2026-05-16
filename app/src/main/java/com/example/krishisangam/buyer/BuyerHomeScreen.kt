package com.example.krishisangam.buyer

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)

@Composable
fun BuyerHomeScreen(
    onSearchClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        BuyerTopHeader()

        Spacer(modifier = Modifier.height(20.dp))

        BuyerSearchBar(
            onSearchClick = onSearchClick
        )

        Spacer(modifier = Modifier.height(22.dp))

        BuyerSectionHeader(
            title = stringResource(R.string.categories),
            action = stringResource(R.string.see_all)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BuyerCategoryItem(
                emoji = "🌾",
                title = stringResource(R.string.grain),
                onClick = {
                    onCategoryClick("Grain")
                }
            )

            BuyerCategoryItem(
                emoji = "🍎",
                title = stringResource(R.string.fruits),
                onClick = {
                    onCategoryClick("Fruits")
                }
            )

            BuyerCategoryItem(
                emoji = "🥦",
                title = stringResource(R.string.vegetables),
                onClick = {
                    onCategoryClick("Vegetables")
                }
            )

            BuyerCategoryItem(
                emoji = "🫘",
                title = stringResource(R.string.pulses),
                onClick = {
                    onCategoryClick("Pulses")
                }
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        BuyerSectionHeader(
            title = stringResource(R.string.todays_offers),
            action = stringResource(R.string.see_all)
        )

        Spacer(modifier = Modifier.height(12.dp))

        BuyerOfferCard()

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = stringResource(R.string.tap_category_to_view_listings),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun BuyerTopHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "👤",
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = stringResource(R.string.buyer),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        Text(
            text = "❤",
            fontSize = 22.sp,
            color = PrimaryGreen,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "🔔",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "⋯",
            fontSize = 26.sp,
            color = TextDark
        )
    }
}

@Composable
fun BuyerSearchBar(
    onSearchClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable {
                onSearchClick()
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔍",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.search_wheat_rice_vegetables),
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            Text(
                text = "🎙️",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun BuyerSectionHeader(
    title: String,
    action: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = TextDark,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = action,
            color = PrimaryGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BuyerCategoryItem(
    emoji: String,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Card(
            modifier = Modifier.size(58.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    fontSize = 28.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark,
            maxLines = 1
        )
    }
}

@Composable
fun BuyerOfferCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryGreen
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.bulk_crop_deal),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.up_to_lower_logistics_cost),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Text(
                    text = stringResource(R.string.for_community_orders),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "🚚🌾",
                fontSize = 42.sp
            )
        }
    }
}