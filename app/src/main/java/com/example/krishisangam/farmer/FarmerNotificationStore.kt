package com.example.krishisangam.farmer

import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class FarmerNotification(
    val title: String,
    val message: String,
    val icon: String = "🔔",
    val time: String = getCurrentNotificationTime()
)

object FarmerNotificationStore {

    val notifications = mutableStateListOf<FarmerNotification>()

    fun addNotification(
        title: String,
        message: String,
        icon: String = "🔔"
    ) {
        notifications.add(
            index = 0,
            element = FarmerNotification(
                title = title,
                message = message,
                icon = icon
            )
        )
    }

    fun clearNotifications() {
        notifications.clear()
    }
}

private fun getCurrentNotificationTime(): String {
    return try {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        formatter.format(Date())
    } catch (exception: Exception) {
        "Just now"
    }
}