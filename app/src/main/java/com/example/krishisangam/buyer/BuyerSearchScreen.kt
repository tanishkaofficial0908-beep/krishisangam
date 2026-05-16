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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFF003D22)
private val DeepGreen = Color(0xFF002514)
private val DarkGreen = Color(0xFF005C32)
private val AccentYellow = Color(0xFFFFC107)
private val TextLight = Color(0xFFF5FFF9)
private val TextMuted = Color(0xFFB9D8C7)
private val GlassDark = Color.White.copy(alpha = 0.095f)
private val GlassCard = Color.White.copy(alpha = 0.105f)
private val BorderGlass = Color.White.copy(alpha = 0.16f)

@Composable
fun BuyerSearchScreen(
    onBackClick: () -> Unit
) {
    var searchText by remember {
        mutableStateOf("")
    }

    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
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
                .size(260.dp)
                .align(Alignment.TopEnd)
                .background(AccentYellow.copy(alpha = 0.08f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.BottomStart)
                .background(PrimaryGreen.copy(alpha = 0.12f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 22.dp)
                .padding(bottom = 110.dp)
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
                                color = BorderGlass
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
                        fontWeight = FontWeight.Bold,
                        color = TextLight
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.search),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextLight
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GlassDark
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
                        .fillMaxSize()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔍",
                        fontSize = 19.sp
                    )

                    TextField(
                        value = searchText,
                        onValueChange = { newValue ->
                            searchText = newValue
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_wheat_rice_vegetables),
                                color = TextMuted
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = AccentYellow,
                            focusedTextColor = TextLight,
                            unfocusedTextColor = TextLight,
                            focusedPlaceholderColor = TextMuted,
                            unfocusedPlaceholderColor = TextMuted
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                    )

                    Text(
                        text = "🎙️",
                        fontSize = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.recent_searches),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            SearchRow(
                text = stringResource(R.string.wheat),
                onClick = {
                    searchText = "Wheat"
                }
            )

            SearchRow(
                text = stringResource(R.string.rice),
                onClick = {
                    searchText = "Rice"
                }
            )

            SearchRow(
                text = stringResource(R.string.fresh_vegetables),
                onClick = {
                    searchText = "Fresh Vegetables"
                }
            )

            SearchRow(
                text = stringResource(R.string.pulses),
                onClick = {
                    searchText = "Pulses"
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.similar_matches),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(14.dp))

            SearchRow(
                text = stringResource(R.string.grade_a_wheat),
                arrow = true,
                onClick = {
                    searchText = "Grade A Wheat"
                }
            )

            SearchRow(
                text = stringResource(R.string.basmati_rice),
                arrow = true,
                onClick = {
                    searchText = "Basmati Rice"
                }
            )

            SearchRow(
                text = stringResource(R.string.bulk_onion),
                arrow = true,
                onClick = {
                    searchText = "Bulk Onion"
                }
            )
        }
    }
}

@Composable
fun SearchRow(
    text: String,
    arrow: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
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
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "↺",
                color = TextMuted,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                color = TextLight,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = if (arrow) "↗" else "×",
                color = if (arrow) AccentYellow else TextMuted,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}