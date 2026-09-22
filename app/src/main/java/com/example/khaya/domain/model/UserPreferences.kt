package com.example.khaya.domain.model

data class UserPreferences(
    val themeMode: String = "SYSTEM",
    val notificationsEnabled: Boolean = true
)
