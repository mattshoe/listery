package com.listery.text

import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Provider

class NumberFormatter @Inject constructor(
) {
    private val decimalFormat = DecimalFormat("#,###.##")
    fun concise(double: Double): String {
        return when {
            double.rem(1) == 0.0 -> decimalFormat.format(double.toLong())
            else -> decimalFormat.format(double)
        }
    }
}