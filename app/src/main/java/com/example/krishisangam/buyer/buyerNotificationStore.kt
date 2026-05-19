package com.example.krishisangam.buyer

import androidx.compose.runtime.mutableStateListOf

data class BuyerNotificationItem(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val message: String,
    val time: String,
    val icon: String = "🔔"
)

object BuyerNotificationStore {

    val notifications = mutableStateListOf<BuyerNotificationItem>()

    fun addNotification(
        title: String,
        message: String,
        icon: String = "🔔"
    ) {
        notifications.add(
            index = 0,
            element = BuyerNotificationItem(
                title = title,
                message = message,
                time = "Just now",
                icon = icon
            )
        )
    }

    fun clearNotifications() {
        notifications.clear()
    }
}