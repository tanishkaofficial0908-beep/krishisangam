package com.example.krishisangam.manager

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

private val ManagerEditPrimaryGreen = Color(0xFF01AC66)
private val ManagerEditBackgroundColor = Color(0xFF003D22)
private val ManagerEditDeepGreen = Color(0xFF002514)
private val ManagerEditDarkGreen = Color(0xFF005C32)
private val ManagerEditAccentYellow = Color(0xFFFFC107)
private val ManagerEditTextLight = Color(0xFFF5FFF9)
private val ManagerEditTextMuted = Color(0xFFB9D8C7)
private val ManagerEditGlassCard = Color.White.copy(alpha = 0.105f)
private val ManagerEditBorderGlass = Color.White.copy(alpha = 0.16f)
private val ManagerEditError = Color(0xFFFF6B6B)

@Composable
fun ManagerEditProfileScreen(onBackClick: () -> Unit) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = remember { FirebaseFirestore.getInstance() }
    val managerId = currentUser?.uid.orEmpty()

    var fullName by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var phoneNumber by remember { mutableStateOf(currentUser?.phoneNumber ?: "") }
    var location by remember { mutableStateOf("") }
    var nodeName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    DisposableEffect(managerId) {
        if (managerId.isBlank()) return@DisposableEffect onDispose { }

        val listener = firestore.collection("managerProfiles")
            .document(managerId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    fullName = snapshot.getString("fullName") ?: fullName
                    email = snapshot.getString("email") ?: email
                    phoneNumber = snapshot.getString("phoneNumber") ?: phoneNumber
                    location = snapshot.getString("location") ?: location
                    nodeName = snapshot.getString("nodeName") ?: nodeName
                }
            }

        onDispose { listener.remove() }
    }

    ManagerEditScaffold(
        title = "Edit Profile",
        subtitle = "Update manager name, contact and location",
        onBackClick = onBackClick
    ) {
        ManagerEditTextField(fullName, { fullName = it }, "Full Name")
        ManagerEditTextField(email, { email = it }, "Email", readOnly = true)
        ManagerEditTextField(phoneNumber, { phoneNumber = it }, "Phone Number")
        ManagerEditTextField(location, { location = it }, "Location / City")
        ManagerEditTextField(nodeName, { nodeName = it }, "Agro Node Name")

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = if (message.contains("failed", ignoreCase = true)) ManagerEditError else ManagerEditAccentYellow,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        ManagerEditPrimaryButton(
            text = if (isSaving) "Saving..." else "Save Profile",
            enabled = !isSaving,
            onClick = {
                if (managerId.isBlank()) {
                    message = "User not found."
                    return@ManagerEditPrimaryButton
                }

                isSaving = true
                message = ""

                currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName.trim())
                        .build()
                )

                val managerProfile = hashMapOf(
                    "managerId" to managerId,
                    "fullName" to fullName.trim(),
                    "email" to email.trim(),
                    "phoneNumber" to phoneNumber.trim(),
                    "location" to location.trim(),
                    "nodeName" to nodeName.trim()
                )

                firestore.collection("managerProfiles")
                    .document(managerId)
                    .set(managerProfile)
                    .addOnSuccessListener {
                        isSaving = false
                        message = "Profile updated successfully."
                    }
                    .addOnFailureListener { exception ->
                        isSaving = false
                        message = exception.message ?: "Profile update failed."
                    }
            }
        )
    }
}

@Composable
private fun ManagerEditScaffold(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(ManagerEditDarkGreen, ManagerEditBackgroundColor, ManagerEditDeepGreen)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(ManagerEditAccentYellow.copy(alpha = 0.07f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomStart)
                .background(ManagerEditPrimaryGreen.copy(alpha = 0.10f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 18.dp)
                .padding(bottom = 110.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.10f))
                        .border(BorderStroke(1.dp, ManagerEditBorderGlass), CircleShape)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("‹", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = ManagerEditTextLight)
                }

                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = ManagerEditTextLight)
                    Text(subtitle, fontSize = 13.sp, color = ManagerEditTextMuted, lineHeight = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(7.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ManagerEditGlassCard),
                border = BorderStroke(1.dp, ManagerEditBorderGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    content = content
                )
            }
        }
    }
}

@Composable
private fun ManagerEditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        label = { Text(label, color = ManagerEditTextMuted) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ManagerEditTextLight,
            unfocusedTextColor = ManagerEditTextLight,
            focusedBorderColor = ManagerEditPrimaryGreen,
            unfocusedBorderColor = ManagerEditBorderGlass,
            focusedContainerColor = ManagerEditGlassCard,
            unfocusedContainerColor = ManagerEditGlassCard,
            cursorColor = ManagerEditAccentYellow
        )
    )
}

@Composable
private fun ManagerEditPrimaryButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ManagerEditPrimaryGreen,
            contentColor = Color.White,
            disabledContainerColor = ManagerEditPrimaryGreen.copy(alpha = 0.45f),
            disabledContentColor = Color.White.copy(alpha = 0.75f)
        )
    ) {
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
    }
}
