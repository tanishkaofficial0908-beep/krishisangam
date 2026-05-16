package com.example.krishisangam.farmer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.abs
import kotlin.math.roundToInt

private val PrimaryGreen = Color(0xFF01AC66)
private val BackgroundColor = Color(0xFFE8FAF6)
private val TextDark = Color(0xFF111111)
private val FieldColor = Color(0xFFFFFFFF)
private val LightGreen = Color(0xFFDFF8EF)
private val SoftRed = Color(0xFFFFE3E3)
private val RedText = Color(0xFFD64B4B)
private val SoftYellow = Color(0xFFFFF4D6)
private val YellowText = Color(0xFFB8860B)

private const val PRICE_FLAG_UNDERPRICED = "underpriced"
private const val PRICE_FLAG_OVERPRICED = "overpriced"
private const val PRICE_FLAG_ABOVE_SUGGESTED = "above_suggested"
private const val PRICE_FLAG_FAIR = "fair"

data class PricingResult(
    val mandiPrice: Int,
    val middlemanMarginRatio: Double,
    val krishiSangamPrice: Int,
    val minAllowedPrice: Int,
    val maxAllowedPrice: Int,
    val differenceFromMandi: Int,
    val differenceFromSuggested: Int,
    val profitGainPercent: Int,
    val fairnessScore: Int,
    val priceFlag: String,
    val unit: String = "Kg"
)

@Composable
fun FarmerAddProductScreen(
    onBackClick: () -> Unit,
    onProductSubmitted: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(1) }

    var selectedCategory by remember { mutableStateOf("Grains") }
    var selectedEmoji by remember { mutableStateOf("🌾") }
    var productName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("Kg") }
    var cropYear by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var farmerExpectedPrice by remember { mutableStateOf("") }

    var showSuccessMessage by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val pricingResult = calculateFairPrice(
        category = selectedCategory,
        productName = productName,
        farmerExpectedPriceText = farmerExpectedPrice
    )

    val pleaseEnterProductName = stringResource(R.string.please_enter_product_name)
    val pleaseEnterQuantity = stringResource(R.string.please_enter_quantity)
    val pleaseEnterValidFarmerExpectedPrice =
        stringResource(R.string.please_enter_valid_farmer_expected_price)
    val userNotLoggedIn = stringResource(R.string.user_not_logged_in)
    val failedToListProduct = stringResource(R.string.failed_to_list_product)
    val defaultFarmerName = stringResource(R.string.farmer)

    val overpricedDemandRiskLimitMessage = stringResource(
        R.string.overpriced_demand_risk_limit,
        pricingResult.maxAllowedPrice,
        pricingResult.unit
    )

    val tooLowPriceLimitMessage = stringResource(
        R.string.too_low_price_limit,
        pricingResult.minAllowedPrice,
        pricingResult.unit
    )

    val priceMustBeBetweenMessage = stringResource(
        R.string.price_must_be_between,
        pricingResult.minAllowedPrice,
        pricingResult.maxAllowedPrice,
        pricingResult.unit
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 18.dp, vertical = 18.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AddProductTopBar(
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(18.dp))

        StepIndicator(
            currentStep = currentStep
        )

        Spacer(modifier = Modifier.height(28.dp))

        when (currentStep) {
            1 -> {
                ProductInfoStep(
                    selectedCategory = selectedCategory,
                    productName = productName,
                    quantity = quantity,
                    unit = unit,
                    cropYear = cropYear,
                    description = description,
                    location = location,
                    onCategorySelected = { category, emoji ->
                        selectedCategory = category
                        selectedEmoji = emoji
                    },
                    onProductNameChange = { productName = it },
                    onQuantityChange = { quantity = it },
                    onUnitChange = { unit = it },
                    onCropYearChange = { cropYear = it },
                    onDescriptionChange = { description = it },
                    onLocationChange = { location = it }
                )

                Spacer(modifier = Modifier.height(22.dp))

                PrimaryActionButton(
                    text = stringResource(R.string.next),
                    onClick = {
                        if (productName.isBlank()) {
                            errorMessage = pleaseEnterProductName
                        } else if (quantity.isBlank()) {
                            errorMessage = pleaseEnterQuantity
                        } else {
                            errorMessage = ""
                            currentStep = 2
                        }
                    }
                )

                if (errorMessage.isNotBlank()) {
                    ErrorBox(errorMessage)
                }
            }

            2 -> {
                PricingStep(
                    pricingResult = pricingResult,
                    farmerExpectedPrice = farmerExpectedPrice,
                    onFarmerExpectedPriceChange = { farmerExpectedPrice = it }
                )

                if (errorMessage.isNotBlank()) {
                    ErrorBox(errorMessage)
                }

                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SecondaryActionButton(
                        text = stringResource(R.string.back),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            errorMessage = ""
                            currentStep = 1
                        }
                    )

                    PrimaryActionButton(
                        text = stringResource(R.string.next),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val enteredPrice = cleanPriceToInt(farmerExpectedPrice)

                            if (enteredPrice == null) {
                                errorMessage = pleaseEnterValidFarmerExpectedPrice
                            } else if (enteredPrice > pricingResult.maxAllowedPrice) {
                                errorMessage = overpricedDemandRiskLimitMessage
                            } else if (enteredPrice < pricingResult.minAllowedPrice) {
                                errorMessage = tooLowPriceLimitMessage
                            } else {
                                errorMessage = ""
                                currentStep = 3
                            }
                        }
                    )
                }
            }

            3 -> {
                PhotosStep(
                    selectedEmoji = selectedEmoji,
                    productName = productName,
                    pricingResult = pricingResult
                )

                if (errorMessage.isNotBlank()) {
                    ErrorBox(errorMessage)
                }

                if (showSuccessMessage) {
                    Spacer(modifier = Modifier.height(16.dp))

                    SuccessListedCard()

                    Spacer(modifier = Modifier.height(16.dp))

                    PrimaryActionButton(
                        text = stringResource(R.string.go_to_my_products),
                        onClick = {
                            onProductSubmitted()
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.height(22.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SecondaryActionButton(
                            text = stringResource(R.string.back),
                            modifier = Modifier.weight(1f),
                            onClick = {
                                currentStep = 2
                            }
                        )

                        PrimaryActionButton(
                            text = if (isSubmitting) {
                                stringResource(R.string.submitting)
                            } else {
                                stringResource(R.string.submit)
                            },
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (isSubmitting) return@PrimaryActionButton

                                val enteredPrice = cleanPriceToInt(farmerExpectedPrice)

                                if (enteredPrice == null) {
                                    errorMessage = pleaseEnterValidFarmerExpectedPrice
                                    currentStep = 2
                                    return@PrimaryActionButton
                                }

                                if (enteredPrice !in pricingResult.minAllowedPrice..pricingResult.maxAllowedPrice) {
                                    errorMessage = priceMustBeBetweenMessage
                                    currentStep = 2
                                    return@PrimaryActionButton
                                }

                                isSubmitting = true
                                errorMessage = ""

                                val finalQuantity = if (quantity.isBlank()) {
                                    ""
                                } else {
                                    "$quantity $unit"
                                }

                                saveFarmerProductToFirestore(
                                    name = productName,
                                    category = selectedCategory,
                                    emoji = selectedEmoji,
                                    quantity = finalQuantity,
                                    cropYear = cropYear,
                                    description = description,
                                    location = location,
                                    pricingResult = pricingResult,
                                    userNotLoggedInMessage = userNotLoggedIn,
                                    productNameRequiredMessage = pleaseEnterProductName,
                                    quantityRequiredMessage = pleaseEnterQuantity,
                                    failedToListProductMessage = failedToListProduct,
                                    defaultFarmerName = defaultFarmerName,
                                    onSuccess = {
                                        isSubmitting = false
                                        showSuccessMessage = true
                                    },
                                    onError = { error ->
                                        isSubmitting = false
                                        errorMessage = error
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

fun calculateFairPrice(
    category: String,
    productName: String,
    farmerExpectedPriceText: String
): PricingResult {
    val mandiPrice = getMandiPricePerKg(category, productName)

    val middlemanMarginRatio = when {
        category.equals("Fruits", ignoreCase = true) -> 0.18
        category.equals("Vegetables", ignoreCase = true) -> 0.15
        category.equals("Pulses", ignoreCase = true) -> 0.12
        else -> 0.10
    }

    val farmerUpliftRatio = 0.30

    val krishiSangamPrice =
        (mandiPrice + (farmerUpliftRatio * mandiPrice) + (middlemanMarginRatio * mandiPrice))
            .roundToInt()

    val minAllowedPrice = (mandiPrice * 1.05).roundToInt()
    val maxAllowedPrice = (krishiSangamPrice * 1.12).roundToInt()

    val farmerExpectedPrice = cleanPriceToInt(farmerExpectedPriceText) ?: krishiSangamPrice

    val differenceFromMandi = farmerExpectedPrice - mandiPrice
    val differenceFromSuggested = farmerExpectedPrice - krishiSangamPrice

    val profitGainPercent = if (mandiPrice == 0) {
        0
    } else {
        (((farmerExpectedPrice - mandiPrice).toDouble() / mandiPrice.toDouble()) * 100).roundToInt()
    }

    val deviationPercent = if (krishiSangamPrice == 0) {
        100.0
    } else {
        (abs(farmerExpectedPrice - krishiSangamPrice).toDouble() / krishiSangamPrice.toDouble()) * 100
    }

    val fairnessScore = (100 - (deviationPercent * 2)).roundToInt().coerceIn(0, 100)

    val priceFlag = when {
        farmerExpectedPrice < krishiSangamPrice -> PRICE_FLAG_UNDERPRICED
        farmerExpectedPrice > maxAllowedPrice -> PRICE_FLAG_OVERPRICED
        farmerExpectedPrice > krishiSangamPrice -> PRICE_FLAG_ABOVE_SUGGESTED
        else -> PRICE_FLAG_FAIR
    }

    return PricingResult(
        mandiPrice = mandiPrice,
        middlemanMarginRatio = middlemanMarginRatio,
        krishiSangamPrice = krishiSangamPrice,
        minAllowedPrice = minAllowedPrice,
        maxAllowedPrice = maxAllowedPrice,
        differenceFromMandi = differenceFromMandi,
        differenceFromSuggested = differenceFromSuggested,
        profitGainPercent = profitGainPercent,
        fairnessScore = fairnessScore,
        priceFlag = priceFlag
    )
}

fun getMandiPricePerKg(
    category: String,
    productName: String
): Int {
    val crop = productName.lowercase()

    return when {
        crop.contains("wheat") || crop.contains("sharbati") -> 22
        crop.contains("rice") -> 32
        crop.contains("maize") || crop.contains("corn") -> 18
        crop.contains("barley") -> 20
        crop.contains("tomato") -> 28
        crop.contains("onion") -> 25
        crop.contains("potato") -> 18
        crop.contains("mango") -> 55
        crop.contains("apple") -> 80
        crop.contains("gram") || crop.contains("chana") -> 65
        crop.contains("masoor") -> 75
        category.equals("Grains", ignoreCase = true) -> 22
        category.equals("Rice", ignoreCase = true) -> 32
        category.equals("Vegetables", ignoreCase = true) -> 25
        category.equals("Fruits", ignoreCase = true) -> 55
        category.equals("Pulses", ignoreCase = true) -> 65
        else -> 40
    }
}

fun cleanPriceToInt(priceText: String): Int? {
    return priceText
        .replace("₹", "")
        .replace(",", "")
        .replace("/kg", "", ignoreCase = true)
        .replace("/Kg", "", ignoreCase = true)
        .replace("kg", "", ignoreCase = true)
        .replace("per kg", "", ignoreCase = true)
        .trim()
        .toIntOrNull()
}

@Composable
fun PricingStep(
    pricingResult: PricingResult,
    farmerExpectedPrice: String,
    onFarmerExpectedPriceChange: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.pricing_intelligence),
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.set_fair_price_using_mandi_rate),
        fontSize = 14.sp,
        color = Color.Gray
    )

    Spacer(modifier = Modifier.height(20.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PriceInfoCard(
            title = stringResource(R.string.mandi_price),
            value = stringResource(
                R.string.rupee_per_unit,
                pricingResult.mandiPrice,
                pricingResult.unit
            ),
            subtitle = stringResource(R.string.current_wholesale_rate),
            modifier = Modifier.weight(1f)
        )

        PriceInfoCard(
            title = stringResource(R.string.krishi_sangam),
            value = stringResource(
                R.string.rupee_per_unit,
                pricingResult.krishiSangamPrice,
                pricingResult.unit
            ),
            subtitle = stringResource(R.string.suggested_fair_price),
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    FieldLabel(
        text = stringResource(R.string.farmer_expected_price)
    )

    AppInputField(
        value = farmerExpectedPrice,
        onValueChange = onFarmerExpectedPriceChange,
        placeholder = stringResource(
            R.string.rupee_per_unit,
            pricingResult.krishiSangamPrice,
            pricingResult.unit
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGreen
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = stringResource(R.string.allowed_range),
                fontSize = 13.sp,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(
                    R.string.rupee_range_per_unit,
                    pricingResult.minAllowedPrice,
                    pricingResult.maxAllowedPrice,
                    pricingResult.unit
                ),
                fontSize = 18.sp,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    PriceAnalysisCard(
        pricingResult = pricingResult
    )

    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = stringResource(R.string.price_display_only_message),
        fontSize = 12.sp,
        color = Color.Gray
    )
}

@Composable
fun PriceInfoCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(115.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(13.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = value,
                fontSize = 20.sp,
                color = PrimaryGreen,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PriceAnalysisCard(
    pricingResult: PricingResult
) {
    val flagColor = when (pricingResult.priceFlag) {
        PRICE_FLAG_UNDERPRICED -> YellowText
        PRICE_FLAG_OVERPRICED -> RedText
        PRICE_FLAG_ABOVE_SUGGESTED -> YellowText
        else -> PrimaryGreen
    }

    val flagBg = when (pricingResult.priceFlag) {
        PRICE_FLAG_OVERPRICED -> SoftRed
        PRICE_FLAG_UNDERPRICED -> SoftYellow
        PRICE_FLAG_ABOVE_SUGGESTED -> SoftYellow
        else -> LightGreen
    }

    val flagText = when (pricingResult.priceFlag) {
        PRICE_FLAG_UNDERPRICED -> stringResource(R.string.underpriced_by_farmer)
        PRICE_FLAG_OVERPRICED -> stringResource(R.string.overpriced_demand_risk)
        PRICE_FLAG_ABOVE_SUGGESTED -> stringResource(R.string.above_suggested_but_acceptable)
        else -> stringResource(R.string.fair_price)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.price_analysis),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            AnalysisRow(
                title = stringResource(R.string.difference_from_mandi),
                value = signedRupee(pricingResult.differenceFromMandi)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnalysisRow(
                title = stringResource(R.string.difference_from_suggested),
                value = signedRupee(pricingResult.differenceFromSuggested)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnalysisRow(
                title = stringResource(R.string.farmer_profit_gain),
                value = stringResource(
                    R.string.percentage_value,
                    pricingResult.profitGainPercent
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnalysisRow(
                title = stringResource(R.string.fairness_score),
                value = stringResource(
                    R.string.score_out_of_100,
                    pricingResult.fairnessScore
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = flagText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = flagColor,
                modifier = Modifier
                    .background(flagBg, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

fun signedRupee(value: Int): String {
    return if (value >= 0) {
        "+₹$value"
    } else {
        "-₹${abs(value)}"
    }
}

@Composable
fun AnalysisRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
    }
}

@Composable
fun AddProductTopBar(
    onBackClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "‹",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.clickable {
                onBackClick()
            }
        )

        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.list_new_product),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = stringResource(R.string.list_your_produce_reach_buyers),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun StepIndicator(
    currentStep: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepItem(
            number = 1,
            title = stringResource(R.string.product_info),
            active = currentStep >= 1
        )

        Text(
            text = "-",
            color = PrimaryGreen,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        StepItem(
            number = 2,
            title = stringResource(R.string.pricing),
            active = currentStep >= 2
        )

        Text(
            text = "-",
            color = PrimaryGreen,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        StepItem(
            number = 3,
            title = stringResource(R.string.photos),
            active = currentStep >= 3
        )
    }
}

@Composable
fun StepItem(
    number: Int,
    title: String,
    active: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (active) PrimaryGreen else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                color = if (active) Color.White else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (active) PrimaryGreen else Color.Gray
        )
    }
}

@Composable
fun ProductInfoStep(
    selectedCategory: String,
    productName: String,
    quantity: String,
    unit: String,
    cropYear: String,
    description: String,
    location: String,
    onCategorySelected: (String, String) -> Unit,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onCropYearChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onLocationChange: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.what_are_you_listing),
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark
    )

    Spacer(modifier = Modifier.height(14.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CategoryOption(
            title = stringResource(R.string.grains),
            emoji = "🌾",
            selected = selectedCategory == "Grains",
            modifier = Modifier.weight(1f),
            onClick = {
                onCategorySelected("Grains", "🌾")
            }
        )

        CategoryOption(
            title = stringResource(R.string.rice),
            emoji = "🍚",
            selected = selectedCategory == "Rice",
            modifier = Modifier.weight(1f),
            onClick = {
                onCategorySelected("Rice", "🍚")
            }
        )

        CategoryOption(
            title = stringResource(R.string.vegetables),
            emoji = "🥦",
            selected = selectedCategory == "Vegetables",
            modifier = Modifier.weight(1f),
            onClick = {
                onCategorySelected("Vegetables", "🥦")
            }
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CategoryOption(
            title = stringResource(R.string.fruits),
            emoji = "🍎",
            selected = selectedCategory == "Fruits",
            modifier = Modifier.weight(1f),
            onClick = {
                onCategorySelected("Fruits", "🍎")
            }
        )

        CategoryOption(
            title = stringResource(R.string.pulses),
            emoji = "🫘",
            selected = selectedCategory == "Pulses",
            modifier = Modifier.weight(1f),
            onClick = {
                onCategorySelected("Pulses", "🫘")
            }
        )

        CategoryOption(
            title = stringResource(R.string.other),
            emoji = "📦",
            selected = selectedCategory == "Other",
            modifier = Modifier.weight(1f),
            onClick = {
                onCategorySelected("Other", "📦")
            }
        )
    }

    Spacer(modifier = Modifier.height(22.dp))

    FieldLabel(
        text = stringResource(R.string.product_name)
    )

    AppInputField(
        value = productName,
        onValueChange = onProductNameChange,
        placeholder = stringResource(R.string.product_name_placeholder)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            FieldLabel(
                text = stringResource(R.string.quantity)
            )

            AppInputField(
                value = quantity,
                onValueChange = onQuantityChange,
                placeholder = stringResource(R.string.quantity_placeholder)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            FieldLabel(
                text = stringResource(R.string.unit)
            )

            AppInputField(
                value = unit,
                onValueChange = onUnitChange,
                placeholder = stringResource(R.string.unit_placeholder)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    FieldLabel(
        text = stringResource(R.string.crop_year)
    )

    AppInputField(
        value = cropYear,
        onValueChange = onCropYearChange,
        placeholder = stringResource(R.string.crop_year_placeholder)
    )

    Spacer(modifier = Modifier.height(16.dp))

    FieldLabel(
        text = stringResource(R.string.product_description)
    )

    AppInputField(
        value = description,
        onValueChange = onDescriptionChange,
        placeholder = stringResource(R.string.product_description_placeholder),
        minLines = 3
    )

    Spacer(modifier = Modifier.height(16.dp))

    FieldLabel(
        text = stringResource(R.string.location)
    )

    AppInputField(
        value = location,
        onValueChange = onLocationChange,
        placeholder = stringResource(R.string.location_placeholder)
    )
}

@Composable
fun PhotosStep(
    selectedEmoji: String,
    productName: String,
    pricingResult: PricingResult
) {
    Text(
        text = stringResource(R.string.product_photos),
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.photo_placeholders_message),
        fontSize = 14.sp,
        color = Color.Gray
    )

    Spacer(modifier = Modifier.height(22.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PhotoBox(
            emoji = selectedEmoji,
            title = stringResource(R.string.main_photo),
            modifier = Modifier.weight(1f)
        )

        PhotoBox(
            emoji = "📷",
            title = stringResource(R.string.add_photo),
            modifier = Modifier.weight(1f)
        )

        PhotoBox(
            emoji = "+",
            title = stringResource(R.string.more),
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(26.dp))

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
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.preview),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(LightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedEmoji,
                        fontSize = 34.sp
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = productName.ifBlank {
                            stringResource(R.string.product_name)
                        },
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = stringResource(
                            R.string.price_with_value,
                            pricingResult.krishiSangamPrice,
                            pricingResult.unit
                        ),
                        fontSize = 14.sp,
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = stringResource(R.string.status_pending_verification),
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun SuccessListedCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGreen
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✅",
                fontSize = 38.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.successfully_listed),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.product_sent_to_agro_node_manager),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CategoryOption(
    title: String,
    emoji: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(86.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) LightGreen else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) PrimaryGreen else TextDark,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PhotoBox(
    emoji: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(112.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emoji,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
    }
}

@Composable
fun FieldLabel(
    text: String
) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark,
        modifier = Modifier.padding(bottom = 7.dp)
    )
}

@Composable
fun AppInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = Color(0xFFB0B0B0),
                fontSize = 14.sp
            )
        },
        modifier = Modifier.fillMaxWidth(),
        minLines = minLines,
        shape = RoundedCornerShape(18.dp),
        textStyle = TextStyle(
            color = TextDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextDark,
            unfocusedTextColor = TextDark,
            focusedBorderColor = PrimaryGreen,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = FieldColor,
            unfocusedContainerColor = FieldColor,
            cursorColor = PrimaryGreen
        )
    )
}

@Composable
fun PrimaryActionButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(PrimaryGreen)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SecondaryActionButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(Color.White)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ErrorBox(
    message: String
) {
    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = message,
        color = RedText,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftRed, RoundedCornerShape(14.dp))
            .padding(12.dp)
    )
}

fun saveFarmerProductToFirestore(
    name: String,
    category: String,
    emoji: String,
    quantity: String,
    cropYear: String,
    description: String,
    location: String,
    pricingResult: PricingResult,
    userNotLoggedInMessage: String,
    productNameRequiredMessage: String,
    quantityRequiredMessage: String,
    failedToListProductMessage: String,
    defaultFarmerName: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser

    if (user == null) {
        onError(userNotLoggedInMessage)
        return
    }

    if (name.isBlank()) {
        onError(productNameRequiredMessage)
        return
    }

    if (quantity.isBlank()) {
        onError(quantityRequiredMessage)
        return
    }

    val productRef = db.collection("products").document()

    val finalPrice = "₹${pricingResult.krishiSangamPrice}/${pricingResult.unit}"

    val product = hashMapOf(
        "productId" to productRef.id,
        "name" to name.trim(),
        "category" to category.trim(),
        "emoji" to emoji,
        "quantity" to quantity.trim(),
        "cropYear" to cropYear.trim(),
        "description" to description.trim(),
        "location" to location.trim(),

        "price" to finalPrice,

        "packagingCharge" to "",
        "logisticsCharge" to "",

        "mandiPrice" to "₹${pricingResult.mandiPrice}/${pricingResult.unit}",
        "farmerExpectedPrice" to finalPrice,
        "krishiSangamPrice" to "₹${pricingResult.krishiSangamPrice}/${pricingResult.unit}",
        "priceDifferenceFromMandi" to signedRupee(pricingResult.differenceFromMandi),
        "priceDifferenceFromSuggested" to signedRupee(pricingResult.differenceFromSuggested),
        "profitGainPercent" to "${pricingResult.profitGainPercent}%",
        "fairnessScore" to pricingResult.fairnessScore,
        "priceFlag" to pricingResult.priceFlag,

        "farmerId" to user.uid,
        "farmerName" to (user.displayName ?: defaultFarmerName),
        "farmerEmail" to (user.email ?: ""),
        "trustScore" to 70,
        "status" to "pending",
        "createdAt" to System.currentTimeMillis(),
        "approvedAt" to null,
        "approvedBy" to ""
    )

    productRef.set(product)
        .addOnSuccessListener {
            onSuccess()
        }
        .addOnFailureListener { exception: Exception ->
            onError(exception.message ?: failedToListProductMessage)
        }
}