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
import com.google.firebase.firestore.FirebaseFirestore

private val ManagerNodePrimaryGreen = Color(0xFF01AC66)
private val ManagerNodeBackgroundColor = Color(0xFF003D22)
private val ManagerNodeDeepGreen = Color(0xFF002514)
private val ManagerNodeDarkGreen = Color(0xFF005C32)
private val ManagerNodeAccentYellow = Color(0xFFFFC107)
private val ManagerNodeTextLight = Color(0xFFF5FFF9)
private val ManagerNodeTextMuted = Color(0xFFB9D8C7)
private val ManagerNodeGlassCard = Color.White.copy(alpha = 0.105f)
private val ManagerNodeBorderGlass = Color.White.copy(alpha = 0.16f)
private val ManagerNodeError = Color(0xFFFF6B6B)

@Composable
fun ManagerNodeDetailsScreen(onBackClick: () -> Unit) {
    val managerId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    val firestore = remember { FirebaseFirestore.getInstance() }

    var nodeName by remember { mutableStateOf("") }
    var nodeCode by remember { mutableStateOf("") }
    var nodeLocation by remember { mutableStateOf("") }
    var nodeDistrict by remember { mutableStateOf("") }
    var nodeState by remember { mutableStateOf("") }
    var storageCapacity by remember { mutableStateOf("") }
    var contactPerson by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var pickupArea by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    DisposableEffect(managerId) {
        if (managerId.isBlank()) return@DisposableEffect onDispose { }

        val listener = firestore.collection("agroNodes")
            .document(managerId)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    nodeName = snapshot.getString("nodeName") ?: nodeName
                    nodeCode = snapshot.getString("nodeCode") ?: nodeCode
                    nodeLocation = snapshot.getString("nodeLocation") ?: nodeLocation
                    nodeDistrict = snapshot.getString("nodeDistrict") ?: nodeDistrict
                    nodeState = snapshot.getString("nodeState") ?: nodeState
                    storageCapacity = snapshot.getString("storageCapacity") ?: storageCapacity
                    contactPerson = snapshot.getString("contactPerson") ?: contactPerson
                    contactNumber = snapshot.getString("contactNumber") ?: contactNumber
                    pickupArea = snapshot.getString("pickupArea") ?: pickupArea
                }
            }

        onDispose { listener.remove() }
    }

    ManagerNodeScaffold(
        title = "Node Details",
        subtitle = "Manage Agro Node storage, location and contact",
        onBackClick = onBackClick
    ) {
        ManagerNodeTextField(nodeName, { nodeName = it }, "Agro Node Name")
        ManagerNodeTextField(nodeCode, { nodeCode = it }, "Node Code e.g. BPL-AGRO-01")
        ManagerNodeTextField(nodeLocation, { nodeLocation = it }, "Node Location / Area")
        ManagerNodeTextField(nodeDistrict, { nodeDistrict = it }, "District")
        ManagerNodeTextField(nodeState, { nodeState = it }, "State")
        ManagerNodeTextField(storageCapacity, { storageCapacity = it }, "Storage Capacity e.g. 500 Quintal")
        ManagerNodeTextField(contactPerson, { contactPerson = it }, "Contact Person")
        ManagerNodeTextField(contactNumber, { contactNumber = it }, "Contact Number")
        ManagerNodeTextField(pickupArea, { pickupArea = it }, "Covered Pickup Areas")

        if (message.isNotBlank()) {
            Text(
                text = message,
                color = if (message.contains("failed", ignoreCase = true)) ManagerNodeError else ManagerNodeAccentYellow,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        ManagerNodePrimaryButton(
            text = if (isSaving) "Saving..." else "Save Node Details",
            enabled = !isSaving,
            onClick = {
                if (managerId.isBlank()) {
                    message = "User not found."
                    return@ManagerNodePrimaryButton
                }

                isSaving = true
                message = ""

                val nodeData = hashMapOf(
                    "managerId" to managerId,
                    "nodeName" to nodeName.trim(),
                    "nodeCode" to nodeCode.trim(),
                    "nodeLocation" to nodeLocation.trim(),
                    "nodeDistrict" to nodeDistrict.trim(),
                    "nodeState" to nodeState.trim(),
                    "storageCapacity" to storageCapacity.trim(),
                    "contactPerson" to contactPerson.trim(),
                    "contactNumber" to contactNumber.trim(),
                    "pickupArea" to pickupArea.trim()
                )

                firestore.collection("agroNodes")
                    .document(managerId)
                    .set(nodeData)
                    .addOnSuccessListener {
                        isSaving = false
                        message = "Node details saved successfully."
                    }
                    .addOnFailureListener { exception ->
                        isSaving = false
                        message = exception.message ?: "Node details save failed."
                    }
            }
        )
    }
}

@Composable
private fun ManagerNodeScaffold(
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
                    listOf(ManagerNodeDarkGreen, ManagerNodeBackgroundColor, ManagerNodeDeepGreen)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(ManagerNodeAccentYellow.copy(alpha = 0.07f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(230.dp)
                .align(Alignment.BottomStart)
                .background(ManagerNodePrimaryGreen.copy(alpha = 0.10f), CircleShape)
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
                        .border(BorderStroke(1.dp, ManagerNodeBorderGlass), CircleShape)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("‹", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = ManagerNodeTextLight)
                }

                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = ManagerNodeTextLight)
                    Text(subtitle, fontSize = 13.sp, color = ManagerNodeTextMuted, lineHeight = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(7.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ManagerNodeGlassCard),
                border = BorderStroke(1.dp, ManagerNodeBorderGlass)
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
private fun ManagerNodeTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        label = { Text(label, color = ManagerNodeTextMuted) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ManagerNodeTextLight,
            unfocusedTextColor = ManagerNodeTextLight,
            focusedBorderColor = ManagerNodePrimaryGreen,
            unfocusedBorderColor = ManagerNodeBorderGlass,
            focusedContainerColor = ManagerNodeGlassCard,
            unfocusedContainerColor = ManagerNodeGlassCard,
            cursorColor = ManagerNodeAccentYellow
        )
    )
}

@Composable
private fun ManagerNodePrimaryButton(
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
            containerColor = ManagerNodePrimaryGreen,
            contentColor = Color.White,
            disabledContainerColor = ManagerNodePrimaryGreen.copy(alpha = 0.45f),
            disabledContentColor = Color.White.copy(alpha = 0.75f)
        )
    ) {
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
    }
}
