package com.example.krishisangam.buyer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.krishisangam.RazorpayConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.razorpay.Checkout
import org.json.JSONObject
import kotlin.math.roundToInt

private val CheckoutPrimaryGreen = Color(0xFF01AC66)
private val CheckoutBackground = Color(0xFF003D22)
private val CheckoutDeepGreen = Color(0xFF002514)
private val CheckoutDarkGreen = Color(0xFF005C32)
private val CheckoutAccentYellow = Color(0xFFFFC107)
private val CheckoutTextLight = Color(0xFFF5FFF9)
private val CheckoutTextMuted = Color(0xFFB9D8C7)
private val CheckoutGlassCardColor = Color.White.copy(alpha = 0.105f)
private val CheckoutBorderGlass = Color.White.copy(alpha = 0.16f)
private val CheckoutErrorRed = Color(0xFFFF6B6B)

@Composable
fun BuyerCheckoutScreen(
    onBackClick: () -> Unit,
    onOrderCompleted: () -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val checkoutItems = remember {
        BuyerOrderStore.orders.toList()
    }

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var completeAddress by remember { mutableStateOf("") }
    var cityVillage by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }

    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var isFetchingLocation by remember { mutableStateOf(false) }

    var validationMessage by remember { mutableStateOf("") }
    var showSuccessPopup by remember { mutableStateOf(false) }

    val isExactLocationCaptured = latitude != null && longitude != null

    val paymentSuccessId = BuyerPaymentStore.paymentSuccessId.value
    val paymentErrorMessage = BuyerPaymentStore.paymentErrorMessage.value

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted || coarseLocationGranted) {
            isFetchingLocation = true

            fetchCurrentLocation(
                fusedLocationClient = fusedLocationClient,
                onSuccess = { lat, lng ->
                    latitude = lat
                    longitude = lng
                    isFetchingLocation = false
                    validationMessage = "Exact location captured successfully."
                },
                onFailure = { error ->
                    isFetchingLocation = false
                    validationMessage = error
                }
            )
        } else {
            validationMessage = "Location permission denied. Please allow location or fill address manually."
        }
    }

    val subtotal by remember {
        derivedStateOf {
            checkoutItems.fold(0.0) { total, order ->
                total + (extractOrderPrice(order.price) * order.quantity)
            }
        }
    }

    val packagingCharge = if (checkoutItems.isEmpty()) 0.0 else 35.0
    val logisticsCharge = if (checkoutItems.isEmpty()) 0.0 else 80.0
    val platformFee = if (checkoutItems.isEmpty()) 0.0 else 15.0
    val tax = subtotal * 0.05
    val totalAmount = subtotal + packagingCharge + logisticsCharge + platformFee + tax
    val advanceAmount = totalAmount * 0.50
    val remainingAmount = totalAmount - advanceAmount

    LaunchedEffect(paymentSuccessId) {
        if (paymentSuccessId != null) {

            BuyerConfirmedOrderStore.addConfirmedOrder(
                items = checkoutItems,
                totalAmount = totalAmount,
                advanceAmount = advanceAmount,
                remainingAmount = remainingAmount,
                paymentId = paymentSuccessId
            )

            BuyerNotificationStore.addNotification(
                title = "Advance Payment Successful",
                message = "Your 50% advance payment is successful. Order has been placed. Estimated delivery within 7 days after Agro Node verification.",
                icon = "✅"
            )

            BuyerOrderStore.clearOrders()

            showSuccessPopup = true

            BuyerPaymentStore.clear()

            onOrderCompleted()
        }
    }

    LaunchedEffect(paymentErrorMessage) {
        if (paymentErrorMessage != null) {
            validationMessage = "Payment failed. Please try again."
            BuyerPaymentStore.clear()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CheckoutDarkGreen,
                        CheckoutBackground,
                        CheckoutDeepGreen
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.TopEnd)
                .background(CheckoutAccentYellow.copy(alpha = 0.07f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.BottomStart)
                .background(CheckoutPrimaryGreen.copy(alpha = 0.12f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
                .padding(top = 18.dp, bottom = 130.dp)
        ) {
            CheckoutTopBar(
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            DeliveryDetailsCard(
                fullName = fullName,
                onFullNameChange = { fullName = it },
                phoneNumber = phoneNumber,
                onPhoneNumberChange = { phoneNumber = it },
                completeAddress = completeAddress,
                onCompleteAddressChange = { completeAddress = it },
                cityVillage = cityVillage,
                onCityVillageChange = { cityVillage = it },
                district = district,
                onDistrictChange = { district = it },
                pincode = pincode,
                onPincodeChange = { pincode = it },
                landmark = landmark,
                onLandmarkChange = { landmark = it },
                isExactLocationCaptured = isExactLocationCaptured,
                latitude = latitude,
                longitude = longitude,
                isFetchingLocation = isFetchingLocation,
                onUseExactLocationClick = {
                    val finePermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    val coarsePermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (finePermission || coarsePermission) {
                        isFetchingLocation = true

                        fetchCurrentLocation(
                            fusedLocationClient = fusedLocationClient,
                            onSuccess = { lat, lng ->
                                latitude = lat
                                longitude = lng
                                isFetchingLocation = false
                                validationMessage = "Exact location captured successfully."
                            },
                            onFailure = { error ->
                                isFetchingLocation = false
                                validationMessage = error
                            }
                        )
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            CheckoutBillSplitCard(
                subtotal = subtotal,
                packagingCharge = packagingCharge,
                logisticsCharge = logisticsCharge,
                platformFee = platformFee,
                tax = tax,
                totalAmount = totalAmount
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaymentSplitCard(
                totalAmount = totalAmount,
                advanceAmount = advanceAmount,
                remainingAmount = remainingAmount
            )

            if (validationMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = validationMessage,
                    color = if (
                        validationMessage.contains("successfully", ignoreCase = true)
                    ) {
                        CheckoutAccentYellow
                    } else {
                        CheckoutErrorRed
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ConfirmAdvanceButton(
                onClick = {
                    val isValid = validateCheckoutForm(
                        fullName = fullName,
                        phoneNumber = phoneNumber,
                        completeAddress = completeAddress,
                        cityVillage = cityVillage,
                        district = district,
                        pincode = pincode,
                        isExactLocationCaptured = isExactLocationCaptured
                    )

                    if (!isValid) {
                        validationMessage = if (fullName.isBlank() || phoneNumber.isBlank()) {
                            "Please fill full name and phone number."
                        } else {
                            "Please use exact location or fill complete address, city/village, district and pincode."
                        }
                    } else {
                        validationMessage = ""

                        val activity = context.findActivity()

                        if (activity == null) {
                            validationMessage = "Unable to start payment. Please try again."
                        } else {
                            startRazorpayPayment(
                                activity = activity,
                                advanceAmount = advanceAmount,
                                fullName = fullName,
                                phoneNumber = phoneNumber,
                                onError = { error ->
                                    validationMessage = error
                                }
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Razorpay test payment will open after validation. Final order record will be saved permanently in Firebase in the next step.",
                color = CheckoutTextMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }

        AnimatedVisibility(
            visible = showSuccessPopup,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 350)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 280)
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 18.dp)
                .padding(top = 18.dp)
        ) {
            CheckoutSuccessPopup(
                advanceAmount = advanceAmount,
                remainingAmount = remainingAmount
            )
        }
    }
}

@Composable
fun CheckoutTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = CheckoutBorderGlass
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
                color = CheckoutTextLight
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Checkout",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CheckoutTextLight
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Delivery location and 50% advance payment details.",
                fontSize = 13.sp,
                color = CheckoutTextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun DeliveryDetailsCard(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    completeAddress: String,
    onCompleteAddressChange: (String) -> Unit,
    cityVillage: String,
    onCityVillageChange: (String) -> Unit,
    district: String,
    onDistrictChange: (String) -> Unit,
    pincode: String,
    onPincodeChange: (String) -> Unit,
    landmark: String,
    onLandmarkChange: (String) -> Unit,
    isExactLocationCaptured: Boolean,
    latitude: Double?,
    longitude: Double?,
    isFetchingLocation: Boolean,
    onUseExactLocationClick: () -> Unit
) {
    CheckoutGlassCard {
        Text(
            text = "Delivery Details",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CheckoutTextLight
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Estimated delivery within 7 days after Agro Node verification and dispatch planning.",
            fontSize = 13.sp,
            color = CheckoutTextMuted,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        CheckoutTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = "Full Name"
        )

        Spacer(modifier = Modifier.height(10.dp))

        CheckoutTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = "Phone Number"
        )

        Spacer(modifier = Modifier.height(14.dp))

        UseExactLocationButton(
            isExactLocationCaptured = isExactLocationCaptured,
            isFetchingLocation = isFetchingLocation,
            onClick = onUseExactLocationClick
        )

        if (isExactLocationCaptured) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Location captured: ${latitude?.formatCoordinate()}, ${longitude?.formatCoordinate()}",
                color = CheckoutAccentYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Address fields below are optional because exact coordinates are captured.",
                color = CheckoutTextMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        } else {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "If you do not use exact location, manual address details are required.",
                color = CheckoutAccentYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        CheckoutTextField(
            value = completeAddress,
            onValueChange = onCompleteAddressChange,
            label = if (isExactLocationCaptured) {
                "Complete Delivery Address optional"
            } else {
                "Complete Delivery Address"
            },
            minLines = 2
        )

        Spacer(modifier = Modifier.height(10.dp))

        CheckoutTextField(
            value = cityVillage,
            onValueChange = onCityVillageChange,
            label = if (isExactLocationCaptured) {
                "Village / City optional"
            } else {
                "Village / City"
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        CheckoutTextField(
            value = district,
            onValueChange = onDistrictChange,
            label = if (isExactLocationCaptured) {
                "District optional"
            } else {
                "District"
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        CheckoutTextField(
            value = pincode,
            onValueChange = onPincodeChange,
            label = if (isExactLocationCaptured) {
                "Pincode optional"
            } else {
                "Pincode"
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        CheckoutTextField(
            value = landmark,
            onValueChange = onLandmarkChange,
            label = "Nearest Landmark optional"
        )
    }
}

@Composable
fun UseExactLocationButton(
    isExactLocationCaptured: Boolean,
    isFetchingLocation: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(
                if (isExactLocationCaptured) {
                    CheckoutPrimaryGreen.copy(alpha = 0.30f)
                } else {
                    CheckoutAccentYellow.copy(alpha = 0.16f)
                }
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isExactLocationCaptured) {
                        CheckoutPrimaryGreen.copy(alpha = 0.55f)
                    } else {
                        CheckoutAccentYellow.copy(alpha = 0.40f)
                    }
                ),
                shape = RoundedCornerShape(17.dp)
            )
            .clickable {
                if (!isFetchingLocation) {
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when {
                isFetchingLocation -> "Fetching exact location..."
                isExactLocationCaptured -> "Exact Location Captured ✅"
                else -> "Use Exact Location 📍"
            },
            color = if (isExactLocationCaptured) CheckoutTextLight else CheckoutAccentYellow,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun CheckoutBillSplitCard(
    subtotal: Double,
    packagingCharge: Double,
    logisticsCharge: Double,
    platformFee: Double,
    tax: Double,
    totalAmount: Double
) {
    CheckoutGlassCard {
        Text(
            text = "Bill Split",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CheckoutTextLight
        )

        Spacer(modifier = Modifier.height(14.dp))

        CheckoutAmountRow("Product subtotal", subtotal)
        CheckoutAmountRow("Packaging Charge", packagingCharge)
        CheckoutAmountRow("Logistics Charge", logisticsCharge)
        CheckoutAmountRow("Platform Fee", platformFee)
        CheckoutAmountRow("Tax 5%", tax)

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            color = Color.White.copy(alpha = 0.16f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CheckoutTextLight,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "₹${totalAmount.roundToInt()}",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CheckoutAccentYellow
            )
        }
    }
}

@Composable
fun PaymentSplitCard(
    totalAmount: Double,
    advanceAmount: Double,
    remainingAmount: Double
) {
    CheckoutGlassCard {
        Text(
            text = "Payment Split",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = CheckoutTextLight
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Buyer pays 50% advance now and remaining 50% after delivery.",
            fontSize = 13.sp,
            color = CheckoutTextMuted,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        PaymentBox(
            title = "50% Advance Now",
            amount = advanceAmount,
            emoji = "💳",
            highlight = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        PaymentBox(
            title = "50% After Delivery",
            amount = remainingAmount,
            emoji = "📦",
            highlight = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Total payable amount: ₹${totalAmount.roundToInt()}",
            fontSize = 13.sp,
            color = CheckoutAccentYellow,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PaymentBox(
    title: String,
    amount: Double,
    emoji: String,
    highlight: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (highlight) {
                    CheckoutPrimaryGreen.copy(alpha = 0.28f)
                } else {
                    Color.White.copy(alpha = 0.07f)
                }
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (highlight) {
                        CheckoutPrimaryGreen.copy(alpha = 0.45f)
                    } else {
                        CheckoutBorderGlass
                    }
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 25.sp
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                color = CheckoutTextLight,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = if (highlight) {
                    "Paid before Agro Node processing"
                } else {
                    "Paid after successful delivery"
                },
                color = CheckoutTextMuted,
                fontSize = 11.sp
            )
        }

        Text(
            text = "₹${amount.roundToInt()}",
            color = CheckoutAccentYellow,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun CheckoutAmountRow(
    title: String,
    amount: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = CheckoutTextMuted,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "₹${amount.roundToInt()}",
            fontSize = 14.sp,
            color = CheckoutTextLight,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ConfirmAdvanceButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(18.dp)
            )
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        CheckoutPrimaryGreen,
                        Color(0xFF00985B),
                        Color(0xFF007A49)
                    )
                )
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.18f)
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Confirm Order & Pay 50% Advance",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun CheckoutTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = CheckoutTextMuted,
                fontSize = 13.sp
            )
        },
        minLines = minLines,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = CheckoutTextLight,
            unfocusedTextColor = CheckoutTextLight,
            focusedLabelColor = CheckoutAccentYellow,
            unfocusedLabelColor = CheckoutTextMuted,
            cursorColor = CheckoutAccentYellow,
            focusedBorderColor = CheckoutAccentYellow.copy(alpha = 0.65f),
            unfocusedBorderColor = CheckoutBorderGlass,
            focusedContainerColor = Color.White.copy(alpha = 0.06f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.06f)
        )
    )
}

@Composable
fun CheckoutGlassCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = CheckoutGlassCardColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = CheckoutBorderGlass
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            content()
        }
    }
}

@Composable
fun CheckoutSuccessPopup(
    advanceAmount: Double,
    remainingAmount: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(22.dp)
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = CheckoutPrimaryGreen
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color.White.copy(alpha = 0.22f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            CheckoutPrimaryGreen,
                            Color(0xFF00985B),
                            Color(0xFF007A49)
                        )
                    )
                )
                .padding(horizontal = 15.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✅",
                    fontSize = 22.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = "Payment Successful",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Advance ₹${advanceAmount.roundToInt()} paid. Remaining ₹${remainingAmount.roundToInt()} after delivery.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.82f),
                    lineHeight = 17.sp
                )
            }
        }
    }
}

fun validateCheckoutForm(
    fullName: String,
    phoneNumber: String,
    completeAddress: String,
    cityVillage: String,
    district: String,
    pincode: String,
    isExactLocationCaptured: Boolean
): Boolean {
    val basicDetailsFilled = fullName.isNotBlank() && phoneNumber.isNotBlank()

    val manualAddressFilled = completeAddress.isNotBlank() &&
            cityVillage.isNotBlank() &&
            district.isNotBlank() &&
            pincode.isNotBlank()

    return basicDetailsFilled && (isExactLocationCaptured || manualAddressFilled)
}

private fun startRazorpayPayment(
    activity: Activity,
    advanceAmount: Double,
    fullName: String,
    phoneNumber: String,
    onError: (String) -> Unit
) {
    try {
        val checkout = Checkout()
        checkout.setKeyID(RazorpayConfig.KEY_ID)

        val amountInPaise = advanceAmount.roundToInt() * 100

        val options = JSONObject().apply {
            put("name", "Krishi Sangam")
            put("description", "50% Advance Payment")
            put("currency", "INR")
            put("amount", amountInPaise)

            val prefill = JSONObject().apply {
                put("name", fullName)
                put("contact", phoneNumber)
            }

            put("prefill", prefill)
        }

        checkout.open(activity, options)
    } catch (exception: Exception) {
        onError("Unable to open Razorpay payment. Please try again.")
    }
}

private fun Context.findActivity(): Activity? {
    var currentContext = this

    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }

        currentContext = currentContext.baseContext
    }

    return null
}

@SuppressLint("MissingPermission")
private fun fetchCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onSuccess: (Double, Double) -> Unit,
    onFailure: (String) -> Unit
) {
    val cancellationTokenSource = CancellationTokenSource()

    fusedLocationClient
        .getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )
        .addOnSuccessListener { location ->
            if (location != null) {
                onSuccess(location.latitude, location.longitude)
            } else {
                onFailure("Unable to fetch exact location. Please turn on location/GPS or fill address manually.")
            }
        }
        .addOnFailureListener {
            onFailure("Unable to fetch exact location. Please try again or fill address manually.")
        }
}

private fun Double.formatCoordinate(): String {
    return "%.5f".format(this)
}