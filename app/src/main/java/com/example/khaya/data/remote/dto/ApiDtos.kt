package com.example.khaya.data.remote.dto

import kotlinx.serialization.Serializable
import java.time.LocalDate

/** Auth
 *  POST/khaya/auth/session. Bearer = Firebase ID token, creates users row on first sign-in  */
@Serializable data class SessionRequestDto(val displayName: String? = null, val fcmToken: String? = null)
@Serializable data class SessionResponseDto(val user: UserDto, val isNewUser: Boolean)

@Serializable data class UserDto(
    val userId: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null,
    val biometricEnabled: Boolean = false,
)

@Serializable data class ChildDto(
    val childId: String,
    val parentId: String,
    val name: String,
    val dateOfBirth: LocalDate? = null,
)

//GET/khaya/children/{childId}/location/latest
@Serializable data class LatestLocationDto(
    val lat: Double,
    val lng: Double,
    val source: String,
    val capturedAt: String,
    val withinGeofence: Boolean,
)

@Serializable data class ScheduleDto(val days: List<String>, val start: String, val end: String)

//POST/khaya/children/{childId}/geofences
@Serializable data class CreateGeofenceRequestDto(
    val label: String,
    val lat: Double,
    val lng: Double,
    val radiusMeters: Int,
    val schedule: ScheduleDto,
)

@Serializable data class GeofenceDto(
    val zoneId: String,
    val childId: String,
    val label: String,
    val lat: Double,
    val lng: Double,
    val radiusMeters: Int,
    val schedule: ScheduleDto,
)

@Serializable data class GeofenceListDto(val zones: List<GeofenceDto>)

//GET/Khaya/childern/{childId}/alerts?since=
@Serializable data class AlertDto(
    val alertId: String,
    val childId: String,
    val type: String,
    val message: String,
    val triggeredAt: String,
    val acknowledge: Boolean,
)

@Serializable data class AlertListDto(val alerts: List<AlertDto>)
@Serializable data class AcknowledgeAlertRequestDto(val alertId: String, val acknowledged: Boolean)

//GET/khaya/devices/{deviceId}/health
@Serializable data class DeviceHealthDto(
    val batteryLevel: Int,
    val source: String,
    val signalStrength: String,
    val lastSyncAt: String? = null,
)

//PUT/khaya/parent/{parentId}/engagement
@Serializable data class EngagementDto(
    val safeArrivalStreak: Int,
    val setupJourneyPercent: Int,
)

//PUT/khaya/users/me/preferences
@Serializable data class PreferencesDto(
    val themeMode: String,
    val pushAlertsEnabled: Boolean,
    val deviationAlertsEnabled: Boolean,
    val deviceAlertsEnabled: Boolean,
    val showSafeArrivalStreak: Boolean,
    val mapStyle: String,
    val biometricLockEnabled: Boolean,
    val updatedAt: String? = null, //server stamp this to show changes
)
