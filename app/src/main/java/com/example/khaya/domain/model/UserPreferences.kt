package com.example.khaya.domain.model

enum class ThemeMode { SYSTEM, LIGHT, DARK }
enum class MapStyle { NORMAL, SATELLITE, HYBRID, TERRAIN }

/**
 * Everything the settings screen can change, DataStore persists these changes.
 * Also putting it into the API so the change is visible in Postgres*/
data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val pushAlertsEnabled: Boolean = true,
    val deviationAlertsEnabled: Boolean = true,
    val deviceAlertsEnabled: Boolean = true,  //low battery and signal loss alerts
    val showSafeArrivalStreak: Boolean = true,
    val mapStyle: MapStyle = MapStyle.NORMAL,
    val biometricLockEnabled: Boolean = false,
)
