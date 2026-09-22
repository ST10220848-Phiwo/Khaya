package com.example.khaya.domain.model

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalTime
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime


//Enums from the design document from schema tables

enum class PositioningSource {GPS, WIFI, CELL_ID, BLE}
enum class AlertType {GEOFORCE, DEVIATION, DEVICE, ENGAGEMENT}

//Entities

data class Models(
    val id: String = ""
)
