package com.listery.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MeasurementUnit(
    @PrimaryKey
    @ColumnInfo(name = "measurementUnit")
    val measurementUnit: String
)