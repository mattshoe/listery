package com.listery.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "measurement_unit")
data class MeasurementUnitEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String
)