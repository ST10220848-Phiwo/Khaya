package com.example.khaya.domain.model

import android.telephony.SignalStrength
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

// Enums from the design document from schema tables

enum class PositioningSource { GPS, WIFI, CELL_ID, BLE }
enum class AlertType { GEOFENCE, DEVIATION, DEVICE, ENGAGEMENT }

// Entities

data class User(
    val userId: String,
    val email: String,
    val displayName: String,
    val photoUrl: String? = null,
    val biometricEnabled: Boolean = false,
)

data class Child(
    val childId: String,
    val parentId: String,
    val name: String,
    val dateOfBirth: LocalDate? = null,
)

data class Adult(
    val adultId: String,
    val number: Number,
    val displayName: String,
    val photoUrl: String? = null,
)

data class Wristband(
    val deviceId: String,
    val childId: String,
    val serialNumber: String,
    val positioningMode: PositioningSource,
    val batteryLevel: Int,
    val lastSyncAt: ZonedDateTime,
) {
    init {
        // Fails if the battery level is out of range
        require(batteryLevel in 0..100) { "Battery level is out of range: $batteryLevel" }
    }

    /**
     * Alert for when the band has not responded in 15 mins, the screen warns the user.
     */
    fun isStale(
        now: Instant = Instant.now(),
        staleThreshold: Duration = Duration.ofMinutes(15),
    ): Boolean = lastSyncAt.toInstant().isBefore(now.minus(staleThreshold))
}

data class GeoPoint(val latitude: Double, val longitude: Double) {
    init {
        // Fails if the coordinates are impossible
        require(latitude in -90.0..90.0 && longitude in -180.0..180.0) {
            "Invalid coordinates: $latitude, $longitude"
        }
    }
}

/** JSONB active_schedule column, e.g. school schedule */
data class ActiveSchedule(
    val days: Set<DayOfWeek>,
    val start: LocalTime,
    val end: LocalTime,
) {
    /**
     * Checks whether the schedule is active at a given [moment] in the specified [timeZone].
     */
    fun isActiveAt(
        moment: Instant = Instant.now(),
        timeZone: ZoneId = ZoneId.systemDefault(),
    ): Boolean {
        val zdt = moment.atZone(timeZone)
        return isActiveAt(zdt)
    }

    /**
     * Checks whether the schedule is active at a given [dateTime].
     */
    fun isActiveAt(dateTime: ZonedDateTime): Boolean {
        if (dateTime.dayOfWeek !in days) return false

        val time = dateTime.toLocalTime()
        return if (start <= end) {
            // Standard range e.g. 09:00 to 17:00
            time in start..end
        } else {
            // Overnight range e.g. 22:00 to 06:00
            time >= start || time <= end
        }
    }

    companion object {
        val SCHOOL_DAYS_AFTERNOON = ActiveSchedule(
            days = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
            start = LocalTime.of(13, 0),
            end = LocalTime.of(18, 0),
        )

        val SCHOOL_DAY_MORNING = ActiveSchedule(
            days = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
            start = LocalTime.of(7, 30),
            end = LocalTime.of(14, 30),
        )

        val WORK_DAY_MORNING = ActiveSchedule(
            days = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY),
            start = LocalTime.of(9, 0),
            end = LocalTime.of(17, 0),
        )
    }
}

data class GeofenceZone(
    val zoneId: String,
    val childId: String,
    val label: String,
    val center: GeoPoint,
    val radiusMeters: Int,
    val schedule: ActiveSchedule,
) {
    init {
        require(label.isNotBlank() && label.length <= 60) {
            "Label must be non-blank and at most 60 characters: '$label'"
        }
        require(radiusMeters in 1..20000) {
            "Invalid radius: $radiusMeters meters (must be between 1 and 20,000m)"
        }
    }

    fun isActiveNow(
        now: Instant = Instant.now(),
        timeZone: ZoneId = ZoneId.systemDefault(),
    ): Boolean = schedule.isActiveAt(now, timeZone)
}

data class LocationPing(
    val pingId: String,
    val deviceId: String,
    val position: GeoPoint,
    val source: PositioningSource,
    val capturedAt: Instant,
)

/** Response to the GET/child/{childId}/location endpoint */
data class ChildLocation(
    val child: String,
    val position: GeoPoint,
    val source: PositioningSource,
    val capturedAt: Instant,
    val withinGeofence: Boolean,
)

data class Alert(
    val alertId: ZoneId,
    val childId: String,
    val type: AlertType,
    val message: String,
    val triggeredAt: Instant,
    val acknowledge: Boolean,
)

/** Response to the GET/devices/{id}/health endpoint */
data class DeviceHealth(
    val batteryLevel: Int,
    val source: PositioningSource,
    val signalStrength: SignalStrength,
    val lastSyncAt: Instant?,
)

/**Response to the GET/adult/{adultId}/location endpoint */
data class AdultLocation(
    val adult: Adult,
    val position: GeoPoint,
    val source: PositioningSource,
    val capturedAt: Instant,
    val withinGeofence: Boolean
)

enum class SignalStrength { NONE, WEAK, FAIR, STRONG }

/** Parent-facing gamification*/
data class EngagementRecord(
    val safeArrivalStreak: Int,
    val setupJourneyPercent: Int,
)