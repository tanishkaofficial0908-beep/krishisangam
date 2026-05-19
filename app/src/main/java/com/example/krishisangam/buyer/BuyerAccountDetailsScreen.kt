package com.example.krishisangam.buyer

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishisangam.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

enum class BuyerAccountScreenMode {
    EDIT_PROFILE,
    DELIVERY_LOCATION_ONLY
}

private val AccountPrimaryGreen = Color(0xFF01AC66)
private val AccountBackgroundColor = Color(0xFF003D22)
private val AccountDeepGreen = Color(0xFF002514)
private val AccountDarkGreen = Color(0xFF005C32)
private val AccountAccentYellow = Color(0xFFFFC107)
private val AccountTextLight = Color(0xFFF5FFF9)
private val AccountTextMuted = Color(0xFFB9D8C7)
private val AccountGlassCard = Color.White.copy(alpha = 0.105f)
private val AccountBorderGlass = Color.White.copy(alpha = 0.16f)
private val AccountDialogGreen = Color(0xFF123D2B)
private val AccountDialogText = Color(0xFFD8EDE3)
private val AccountSoftRed = Color(0xFFFF6B6B).copy(alpha = 0.16f)
private val AccountRedText = Color(0xFFFF6B6B)

data class BuyerSavedAddress(
    val name: String,
    val phone: String,
    val address: String,
    val villageCity: String,
    val district: String,
    val pincode: String,
    val landmark: String
)

@Composable
fun BuyerAccountDetailsScreen(
    mode: BuyerAccountScreenMode,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    var fullName by remember {
        mutableStateOf(currentUser?.displayName ?: "")
    }

    var email by remember {
        mutableStateOf(currentUser?.email ?: "")
    }

    var phoneNumber by remember {
        mutableStateOf(currentUser?.phoneNumber ?: "")
    }

    var deliveryFullName by remember {
        mutableStateOf(currentUser?.displayName ?: "")
    }

    var deliveryPhoneNumber by remember {
        mutableStateOf(currentUser?.phoneNumber ?: "")
    }

    var completeAddress by remember { mutableStateOf("") }
    var villageCity by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var landmark by remember { mutableStateOf("") }


    var message by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    val savedAddresses = remember {
        mutableStateListOf<BuyerSavedAddress>()
    }

    val title = when (mode) {
        BuyerAccountScreenMode.EDIT_PROFILE -> "Edit Profile"
        BuyerAccountScreenMode.DELIVERY_LOCATION_ONLY -> "Delivery Locations"
    }

    val subtitle = when (mode) {
        BuyerAccountScreenMode.EDIT_PROFILE -> "Manage your profile and delivery details"
        BuyerAccountScreenMode.DELIVERY_LOCATION_ONLY -> "Manage your delivery addresses"
    }

    val showProfileSection = mode == BuyerAccountScreenMode.EDIT_PROFILE

    val profilePhoneInfo = if (phoneNumber.isNotBlank()) phoneNumber else "Phone number not available"



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AccountDarkGreen,
                        AccountBackgroundColor,
                        AccountDeepGreen
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .padding(bottom = 120.dp)
        ) {
            AccountTopBar(
                title = title,
                subtitle = subtitle,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (showProfileSection) {
                ProfileDetailsCard(
                    fullName = fullName,
                    onFullNameChange = { fullName = it },
                    email = email,
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { phoneNumber = it },
                    onSaveClick = {
                        isSaving = true
                        message = ""

                        val user = FirebaseAuth.getInstance().currentUser
                        if (user == null) {
                            message = "User not available."
                            isSaving = false
                        } else {
                            if (fullName.isNotBlank() && fullName != user.displayName) {
                                val request = UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName)
                                    .build()

                                user.updateProfile(request)
                                    .addOnSuccessListener {
                                        message = "Profile updated successfully."
                                        isSaving = false
                                    }
                                    .addOnFailureListener { error ->
                                        message = error.message ?: "Unable to update profile."
                                        isSaving = false
                                    }
                            } else {
                                message = "Profile data saved locally."
                                isSaving = false
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))
            } else {
                CompactProfileCard(
                    name = currentUser?.displayName ?: "Buyer",
                    email = currentUser?.email ?: "buyer@krishisangam.com"
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            DeliveryAddressSection(
                mode = mode,
                deliveryFullName = deliveryFullName,
                onDeliveryFullNameChange = { deliveryFullName = it },
                deliveryPhoneNumber = deliveryPhoneNumber,
                onDeliveryPhoneNumberChange = { deliveryPhoneNumber = it },
                completeAddress = completeAddress,
                onCompleteAddressChange = { completeAddress = it },
                villageCity = villageCity,
                onVillageCityChange = { villageCity = it },
                district = district,
                onDistrictChange = { district = it },
                pincode = pincode,
                onPincodeChange = { pincode = it },
                landmark = landmark,
                onLandmarkChange = { landmark = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            SavedAddressesSection(
                addresses = savedAddresses.toList(),
                onAddAnotherAddress = {
                    if (savedAddresses.size >= 2) {
                        message = "You can save up to 2 delivery addresses for now."
                    } else {
                        val address = BuyerSavedAddress(
                            name = deliveryFullName.ifBlank { fullName.ifBlank { "Buyer" } },
                            phone = deliveryPhoneNumber.ifBlank { phoneNumber },
                            address = completeAddress,
                            villageCity = villageCity,
                            district = district,
                            pincode = pincode,
                            landmark = landmark
                        )

                        savedAddresses.add(address)
                        message = "Address saved successfully."
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SaveAddressButton(
                text = "Save Address",
                onClick = {
                    if (savedAddresses.size >= 2) {
                        message = "You can save up to 2 delivery addresses for now."
                    } else {
                        val address = BuyerSavedAddress(
                            name = deliveryFullName.ifBlank { fullName.ifBlank { "Buyer" } },
                            phone = deliveryPhoneNumber.ifBlank { phoneNumber },
                            address = completeAddress,
                            villageCity = villageCity,
                            district = district,
                            pincode = pincode,
                            landmark = landmark
                        )

                        savedAddresses.add(address)
                        message = "Address saved successfully."
                    }
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            SaveAddressButton(
                text = "Add Another Address",
                onClick = {
                    if (savedAddresses.size >= 2) {
                        message = "You can save up to 2 delivery addresses for now."
                    } else {
                        val address = BuyerSavedAddress(
                            name = deliveryFullName.ifBlank { fullName.ifBlank { "Buyer" } },
                            phone = deliveryPhoneNumber.ifBlank { phoneNumber },
                            address = completeAddress,
                            villageCity = villageCity,
                            district = district,
                            pincode = pincode,
                            landmark = landmark
                        )

                        savedAddresses.add(address)
                        message = "Address added successfully."
                    }
                }
            )



            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = message,
                    color = if (message.contains("success", ignoreCase = true)) {
                        AccountAccentYellow
                    } else {
                        AccountRedText
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun AccountTopBar(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.10f))
                .border(
                    border = BorderStroke(1.dp, AccountBorderGlass),
                    shape = CircleShape
                )
                .clickable { onBackClick() }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "‹",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = AccountTextLight
            )
        }

        Spacer(modifier = Modifier.height(0.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccountTextLight
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = AccountTextMuted,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun ProfileDetailsCard(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    AccountCard {
        SectionTitle("Profile Details")

        Spacer(modifier = Modifier.height(12.dp))

        AccountTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            label = "Full Name"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccountTextField(
            value = email,
            onValueChange = {},
            label = "Email",
            readOnly = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccountTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            label = "Phone Number"
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Phone number update will be connected with Firestore later.",
            fontSize = 12.sp,
            color = AccountTextMuted,
            lineHeight = 17.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        SaveAddressButton(
            text = "Save Profile",
            onClick = onSaveClick
        )
    }
}

@Composable
private fun CompactProfileCard(
    name: String,
    email: String
) {
    AccountCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(
                        border = BorderStroke(1.dp, AccountBorderGlass),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    fontSize = 28.sp
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccountTextLight
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = email,
                    fontSize = 12.sp,
                    color = AccountTextMuted
                )
            }
        }
    }
}

@Composable
private fun DeliveryAddressSection(
    mode: BuyerAccountScreenMode,
    deliveryFullName: String,
    onDeliveryFullNameChange: (String) -> Unit,
    deliveryPhoneNumber: String,
    onDeliveryPhoneNumberChange: (String) -> Unit,
    completeAddress: String,
    onCompleteAddressChange: (String) -> Unit,
    villageCity: String,
    onVillageCityChange: (String) -> Unit,
    district: String,
    onDistrictChange: (String) -> Unit,
    pincode: String,
    onPincodeChange: (String) -> Unit,
    landmark: String,
    onLandmarkChange: (String) -> Unit
) {
    AccountCard {
        SectionTitle("Delivery Address")

        Spacer(modifier = Modifier.height(12.dp))

        AccountTextField(
            value = deliveryFullName,
            onValueChange = onDeliveryFullNameChange,
            label = "Delivery Full Name"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccountTextField(
            value = deliveryPhoneNumber,
            onValueChange = onDeliveryPhoneNumberChange,
            label = "Delivery Phone Number"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccountTextField(
            value = completeAddress,
            onValueChange = onCompleteAddressChange,
            label = "Complete Address",
            minLines = 2
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccountTextField(
            value = villageCity,
            onValueChange = onVillageCityChange,
            label = "Village / City"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccountTextField(
            value = district,
            onValueChange = onDistrictChange,
            label = "District"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccountTextField(
            value = pincode,
            onValueChange = onPincodeChange,
            label = "Pincode"
        )

        Spacer(modifier = Modifier.height(10.dp))

        AccountTextField(
            value = landmark,
            onValueChange = onLandmarkChange,
            label = "Nearest Landmark optional"
        )

        if (mode == BuyerAccountScreenMode.DELIVERY_LOCATION_ONLY) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "You can save up to 2 delivery addresses for now.",
                fontSize = 12.sp,
                color = AccountTextMuted,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun SavedAddressesSection(
    addresses: List<BuyerSavedAddress>,
    onAddAnotherAddress: () -> Unit
) {
    AccountCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle("Saved Addresses")
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${addresses.size}/2",
                color = AccountAccentYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (addresses.isEmpty()) {
            Text(
                text = "No saved delivery addresses yet.",
                fontSize = 13.sp,
                color = AccountTextMuted
            )
        } else {
            addresses.take(2).forEachIndexed { index, address ->
                SavedAddressCard(
                    index = index + 1,
                    address = address
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        SaveAddressButton(
            text = "Add Another Address",
            onClick = onAddAnotherAddress
        )
    }
}

@Composable
private fun SavedAddressCard(
    index: Int,
    address: BuyerSavedAddress
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AccountBorderGlass
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = "Address $index",
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AccountAccentYellow
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = address.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = AccountTextLight
            )

            Text(
                text = address.phone,
                fontSize = 12.sp,
                color = AccountTextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = address.address,
                fontSize = 12.sp,
                color = AccountTextMuted,
                lineHeight = 18.sp
            )

            Text(
                text = "${address.villageCity}, ${address.district}, ${address.pincode}",
                fontSize = 12.sp,
                color = AccountTextMuted,
                lineHeight = 18.sp
            )

            if (address.landmark.isNotBlank()) {
                Text(
                    text = "Landmark: ${address.landmark}",
                    fontSize = 12.sp,
                    color = AccountTextMuted,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun SaveAddressButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        AccountPrimaryGreen,
                        Color(0xFF00985B),
                        Color(0xFF007A49)
                    )
                )
            )
            .border(
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun AccountTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        label = {
            Text(
                text = label,
                color = AccountTextMuted,
                fontSize = 13.sp
            )
        },
        minLines = minLines,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AccountTextLight,
            unfocusedTextColor = AccountTextLight,
            focusedLabelColor = AccountAccentYellow,
            unfocusedLabelColor = AccountTextMuted,
            cursorColor = AccountAccentYellow,
            focusedBorderColor = AccountAccentYellow.copy(alpha = 0.65f),
            unfocusedBorderColor = AccountBorderGlass,
            focusedContainerColor = Color.White.copy(alpha = 0.06f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.06f),
            disabledTextColor = AccountTextMuted,
            disabledBorderColor = AccountBorderGlass,
            disabledContainerColor = Color.White.copy(alpha = 0.05f),
            disabledLabelColor = AccountTextMuted
        )
    )
}

@Composable
private fun AccountCard(
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
            containerColor = AccountGlassCard
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AccountBorderGlass
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
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 19.sp,
        fontWeight = FontWeight.ExtraBold,
        color = AccountTextLight
    )
}