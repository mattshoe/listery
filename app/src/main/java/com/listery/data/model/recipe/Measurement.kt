package com.listery.data.model.recipe

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.listery.data.model.MeasurementUnit

@Entity
data class Measurement(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @Embedded()
    val unit: MeasurementUnit,

    @ColumnInfo(name = "measurementQuantity")
    val quantity: Double
)