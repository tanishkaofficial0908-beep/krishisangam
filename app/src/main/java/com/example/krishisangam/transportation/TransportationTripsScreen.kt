package com.example.krishisangam.transportation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
private val SoftGreen = Color(0xFF01AC66).copy(alpha = 0.16f)
private val SoftYellow = Color(0xFFFFC107).copy(alpha = 0.16f)
private val SoftBlue = Color(0xFF2F6FED).copy(alpha = 0.18f)
private val BlueText = Color(0xFF7FB2FF)
private val DividerGlass = Color.White.copy(alpha = 0.14f)

private const val TRIP_FILTER_AVAILABLE = "Available"
private const val TRIP_FILTER_ONGOING = "Ongoing"
private const val TRIP_FILTER_COMPLETED = "Completed"

private const val TRIP_STATUS_AVAILABLE = "Available"
private const val TRIP_STATUS_ACCEPTED = "Accepted"
private const val TRIP_STATUS_PICKED_UP = "Picked Up"
private const val TRIP_STATUS_IN_TRANSIT = "In Transit"
private const val TRIP_STATUS_DELIVERED = "Delivered"

data class TransportationTrip(
    val tripId: String,
    val orderId: String,
    val productName: String,
    val productEmoji: String,
    val orderType: String,
    val orderWeight: String,
    val pickupPoint: String,
    val dropPoint: String,
    val distance: String,
    val earning: String,
    val status: String
)

@Composable
fun TransportationTripsScreen() {
    var selectedFilter by remember {
        mutableStateOf(TRIP_FILTER_AVAILABLE)
    }

    var selectedTrip by remember {
        mutableStateOf<TransportationTrip?>(null)
    }

    val trips = remember {
        mutableStateListOf(
            TransportationTrip(
                tripId = "#TRP1021",
                orderId = "#ORD2201",
                productName = "Sharbati Wheat",
                productEmoji = "🌾",
                orderType = "Bulk Grain Order",
                orderWeight = "12 Quintal",
                pickupPoint = "Bhopal Agro Node 01",
                dropPoint = "Bhopal Market Hub",
                distance = "18 km",
                earning = "₹650",
                status = TRIP_STATUS_AVAILABLE
            ),
            TransportationTrip(
                tripId = "#TRP1022",
                orderId = "#ORD2202",
                productName = "Rice",
                productEmoji = "🍚",
                orderType = "Community Order",
                orderWeight = "9 Quintal",
                pickupPoint = "Sehore Agro Node",
                dropPoint = "Community Drop Point",
                distance = "32 km",
                earning = "₹920",
                status = TRIP_STATUS_AVAILABLE
            ),
            TransportationTrip(
                tripId = "#TRP1023",
                orderId = "#ORD2203",
                productName = "Tomato",
                productEmoji = "🍅",
                orderType = "Fresh Produce",
                orderWeight = "750 kg",
                pickupPoint = "Bhopal Agro Node 01",
                dropPoint = "Raisen Drop Point",
                distance = "41 km",
                earning = "₹1,150",
                status = TRIP_STATUS_ACCEPTED
            ),
            TransportationTrip(
                tripId = "#TRP1024",
                orderId = "#ORD2204",
                productName = "Onion",
                productEmoji = "🧅",
                orderType = "Vegetable Supply",
                orderWeight = "600 kg",
                pickupPoint = "Vidisha Agro Node",
                dropPoint = "Bhopal Market Hub",
                distance = "55 km",
                earning = "₹1,400",
                status = TRIP_STATUS_IN_TRANSIT
            ),
            TransportationTrip(
                tripId = "#TRP1019",
                orderId = "#ORD2197",
                productName = "Potato",
                productEmoji = "🥔",
                orderType = "Completed Delivery",
                orderWeight = "800 kg",
                pickupPoint = "Raisen Agro Node",
                dropPoint = "Bhopal Community Hub",
                distance = "37 km",
                earning = "₹1,050",
                status = TRIP_STATUS_DELIVERED
            )
        )
    }

    val visibleTrips = trips.filter { trip ->
        when (selectedFilter) {
            TRIP_FILTER_AVAILABLE -> trip.status == TRIP_STATUS_AVAILABLE
            TRIP_FILTER_ONGOING -> trip.status == TRIP_STATUS_ACCEPTED ||
                    trip.status == TRIP_STATUS_PICKED_UP ||
                    trip.status == TRIP_STATUS_IN_TRANSIT

            TRIP_FILTER_COMPLETED -> trip.status == TRIP_STATUS_DELIVERED
            else -> true
        }
    }

    if (selectedTrip != null) {
        TransportationTripDetailDialog(
            trip = selectedTrip!!,
            onDismiss = {
                selectedTrip = null
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
                .padding(horizontal = 20.dp, vertical = 22.dp)
        ) {
            Text(
                text = "Trip Management",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Accept nearby orders and update delivery status",
                fontSize = 14.sp,
                color = TextMuted,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            TransportationTripSummaryRow(
                available = trips.count { it.status == TRIP_STATUS_AVAILABLE },
                ongoing = trips.count {
                    it.status == TRIP_STATUS_ACCEPTED ||
                            it.status == TRIP_STATUS_PICKED_UP ||
                            it.status == TRIP_STATUS_IN_TRANSIT
                },
                completed = trips.count { it.status == TRIP_STATUS_DELIVERED }
            )

            Spacer(modifier = Modifier.height(18.dp))

            TransportationTripFilterRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { filter ->
                    selectedFilter = filter
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "${visibleTrips.size} trips",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (visibleTrips.isEmpty()) {
                EmptyTransportationTripsState(
                    selectedFilter = selectedFilter
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = visibleTrips,
                        key = { trip ->
                            trip.tripId
                        }
                    ) { trip ->
                        TransportationTripCard(
                            trip = trip,
                            onCardClick = {
                                selectedTrip = trip
                            },
                            onAcceptClick = {
                                updateTripStatus(
                                    trips = trips,
                                    tripId = trip.tripId,
                                    newStatus = TRIP_STATUS_ACCEPTED
                                )
                                selectedFilter = TRIP_FILTER_ONGOING
                            },
                            onPickedUpClick = {
                                updateTripStatus(
                                    trips = trips,
                                    tripId = trip.tripId,
                                    newStatus = TRIP_STATUS_PICKED_UP
                                )
                            },
                            onInTransitClick = {
                                updateTripStatus(
                                    trips = trips,
                                    tripId = trip.tripId,
                                    newStatus = TRIP_STATUS_IN_TRANSIT
                                )
                            },
                            onDeliveredClick = {
                                updateTripStatus(
                                    trips = trips,
                                    tripId = trip.tripId,
                                    newStatus = TRIP_STATUS_DELIVERED
                                )
                                selectedFilter = TRIP_FILTER_COMPLETED
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(130.dp))
                    }
                }
            }
        }
    }
}

fun updateTripStatus(
    trips: MutableList<TransportationTrip>,
    tripId: String,
    newStatus: String
) {
    val index = trips.indexOfFirst { trip ->
        trip.tripId == tripId
    }

    if (index != -1) {
        trips[index] = trips[index].copy(
            status = newStatus
        )
    }
}

@Composable
fun TransportationTripSummaryRow(
    available: Int,
    ongoing: Int,
    completed: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TransportationTripSummaryCard(
            title = "Available",
            value = available.toString(),
            icon = "📍",
            modifier = Modifier.weight(1f)
        )

        TransportationTripSummaryCard(
            title = "Ongoing",
            value = ongoing.toString(),
            icon = "🚚",
            modifier = Modifier.weight(1f)
        )

        TransportationTripSummaryCard(
            title = "Completed",
            value = completed.toString(),
            icon = "✅",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TransportationTripSummaryCard(
    title: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(90.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(9.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = icon,
                fontSize = 19.sp
            )

            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccentYellow
            )

            Text(
                text = title,
                fontSize = 10.sp,
                color = TextMuted,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TransportationTripFilterRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TransportationTripFilterChip(
            title = TRIP_FILTER_AVAILABLE,
            selectedFilter = selectedFilter,
            onClick = {
                onFilterSelected(TRIP_FILTER_AVAILABLE)
            },
            modifier = Modifier.weight(1f)
        )

        TransportationTripFilterChip(
            title = TRIP_FILTER_ONGOING,
            selectedFilter = selectedFilter,
            onClick = {
                onFilterSelected(TRIP_FILTER_ONGOING)
            },
            modifier = Modifier.weight(1f)
        )

        TransportationTripFilterChip(
            title = TRIP_FILTER_COMPLETED,
            selectedFilter = selectedFilter,
            onClick = {
                onFilterSelected(TRIP_FILTER_COMPLETED)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TransportationTripFilterChip(
    title: String,
    selectedFilter: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selected = title == selectedFilter

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (selected) {
                    PrimaryGreen
                } else {
                    GlassCard
                },
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (selected) {
                        Color.White.copy(alpha = 0.18f)
                    } else {
                        BorderGlass
                    }
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable {
                onClick()
            }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (selected) Color.White else TextLight,
            maxLines = 1
        )
    }
}

@Composable
fun TransportationTripCard(
    trip: TransportationTrip,
    onCardClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onPickedUpClick: () -> Unit,
    onInTransitClick: () -> Unit,
    onDeliveredClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable {
                onCardClick()
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
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
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
                        text = trip.productEmoji,
                        fontSize = 30.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 13.dp)
                ) {
                    Text(
                        text = "${trip.tripId} • ${trip.productName}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextLight,
                        maxLines = 1
                    )

                    Text(
                        text = trip.orderType,
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )

                    Text(
                        text = "Order: ${trip.orderId}",
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                TransportationTripStatusBadge(
                    status = trip.status
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = DividerGlass)

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TransportationTripInfoBox(
                    title = "Weight",
                    value = trip.orderWeight
                )

                TransportationTripInfoBox(
                    title = "Distance",
                    value = trip.distance
                )

                TransportationTripInfoBox(
                    title = "Earning",
                    value = trip.earning
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Pickup: ${trip.pickupPoint}",
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Drop: ${trip.dropPoint}",
                fontSize = 12.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(15.dp))

            when (trip.status) {
                TRIP_STATUS_AVAILABLE -> {
                    Button(
                        onClick = onAcceptClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Accept Trip",
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                TRIP_STATUS_ACCEPTED -> {
                    Button(
                        onClick = onPickedUpClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentYellow,
                            contentColor = Color(0xFF142617)
                        )
                    ) {
                        Text(
                            text = "Mark Picked Up",
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                TRIP_STATUS_PICKED_UP -> {
                    Button(
                        onClick = onInTransitClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentYellow,
                            contentColor = Color(0xFF142617)
                        )
                    ) {
                        Text(
                            text = "Start Delivery",
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                TRIP_STATUS_IN_TRANSIT -> {
                    Button(
                        onClick = onDeliveredClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Mark Delivered",
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                TRIP_STATUS_DELIVERED -> {
                    Text(
                        text = "Delivery completed successfully.",
                        fontSize = 13.sp,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SoftGreen, RoundedCornerShape(15.dp))
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = PrimaryGreen.copy(alpha = 0.24f)
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TransportationTripInfoBox(
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextLight,
            maxLines = 1
        )

        Text(
            text = title,
            fontSize = 10.sp,
            color = TextMuted,
            maxLines = 1
        )
    }
}

@Composable
fun TransportationTripStatusBadge(
    status: String
) {
    val bgColor = when (status) {
        TRIP_STATUS_DELIVERED -> SoftGreen
        TRIP_STATUS_IN_TRANSIT -> SoftBlue
        TRIP_STATUS_AVAILABLE -> SoftYellow
        else -> SoftYellow
    }

    val textColor = when (status) {
        TRIP_STATUS_DELIVERED -> PrimaryGreen
        TRIP_STATUS_IN_TRANSIT -> BlueText
        TRIP_STATUS_AVAILABLE -> AccentYellow
        else -> AccentYellow
    }

    Text(
        text = status,
        fontSize = 10.sp,
        fontWeight = FontWeight.ExtraBold,
        color = textColor,
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = textColor.copy(alpha = 0.24f)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 9.dp, vertical = 5.dp),
        maxLines = 1
    )
}

@Composable
fun EmptyTransportationTripsState(
    selectedFilter: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                .fillMaxWidth()
                .padding(vertical = 45.dp, horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🚚",
                fontSize = 45.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "No $selectedFilter trips",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Trips will appear here after orders are assigned or available nearby.",
                fontSize = 13.sp,
                color = TextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun TransportationTripDetailDialog(
    trip: TransportationTrip,
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
                text = trip.tripId,
                color = TextLight,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp
            )
        },
        text = {
            Column {
                Text(
                    text = trip.productEmoji,
                    fontSize = 38.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Product: ${trip.productName}",
                    color = TextLight,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Order ID: ${trip.orderId}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Type: ${trip.orderType}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Weight: ${trip.orderWeight}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Distance: ${trip.distance}",
                    color = DialogText,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Earning: ${trip.earning}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Status: ${trip.status}",
                    color = AccentYellow,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pickup: ${trip.pickupPoint}",
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Drop: ${trip.dropPoint}",
                    color = DialogText,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                TransportationMapPlaceholderCard(
                    pickupPoint = trip.pickupPoint,
                    dropPoint = trip.dropPoint,
                    distance = trip.distance
                )
            }
        }
    )
}

@Composable
fun TransportationMapPlaceholderCard(
    pickupPoint: String,
    dropPoint: String,
    distance: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.10f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = BorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🗺️",
                    fontSize = 26.sp
                )

                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = "Live Route Preview",
                        color = TextLight,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Google Maps integration pending",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Pickup",
                color = AccentYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = pickupPoint,
                color = DialogText,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "↓",
                color = AccentYellow,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Drop",
                color = AccentYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = dropPoint,
                color = DialogText,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Estimated distance: $distance",
                color = PrimaryGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .background(
                        PrimaryGreen.copy(alpha = 0.14f),
                        RoundedCornerShape(14.dp)
                    )
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            color = PrimaryGreen.copy(alpha = 0.24f)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
    }
}