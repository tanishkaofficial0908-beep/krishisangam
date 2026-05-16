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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val FieldColor = Color(0xFFF4F4F4)
private val TextDark = Color(0xFF111111)

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "‹",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.clickable {
                    onBackClick()
                }
            )

            Text(
                text = stringResource(R.string.search),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = FieldColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
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
                    fontSize = 18.sp,
                    color = PrimaryGreen
                )

                TextField(
                    value = searchText,
                    onValueChange = { newValue ->
                        searchText = newValue
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_wheat_rice_vegetables),
                            color = Color.Gray
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = FieldColor,
                        unfocusedContainerColor = FieldColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = PrimaryGreen,
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                )

                Text(
                    text = "🎙️",
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.recent_searches),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        SearchRow(
            text = stringResource(R.string.wheat)
        )

        SearchRow(
            text = stringResource(R.string.rice)
        )

        SearchRow(
            text = stringResource(R.string.fresh_vegetables)
        )

        SearchRow(
            text = stringResource(R.string.pulses)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.similar_matches),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        SearchRow(
            text = stringResource(R.string.grade_a_wheat),
            arrow = true
        )

        SearchRow(
            text = stringResource(R.string.basmati_rice),
            arrow = true
        )

        SearchRow(
            text = stringResource(R.string.bulk_onion),
            arrow = true
        )
    }
}

@Composable
fun SearchRow(
    text: String,
    arrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "↺",
            color = Color.Gray,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            color = TextDark,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = if (arrow) "↗" else "×",
            color = TextDark,
            fontSize = 18.sp
        )
    }
}