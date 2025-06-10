package org.mattshoe.shoebox.listery.cookbook.viewmodel

sealed interface FilterOption<T> {
    val title: String
    val selectedValue: T

    data class Enumeration<T>(override val title: String, override val selectedValue: T, val values: List<T>): FilterOption<T>
    data class Toggle(override val title: String, override val selectedValue: Boolean?): FilterOption<Boolean?>
    data class DecimalRange(override val title: String, override val selectedValue: FilterRange<Float>): FilterOption<FilterRange<Float>>
    data class IntRange(override val title: String, override val selectedValue: FilterRange<Int>): FilterOption<FilterRange<Int>>
    data class DecimalMin(override val title: String, override val selectedValue: Float?): FilterOption<Float?>
    data class IntMin(override val title: String, override val selectedValue: Int?): FilterOption<Int?>
    data class DecimalMax(override val title: String, override val selectedValue: Float?): FilterOption<Float?>
    data class IntMax(override val title: String, override val selectedValue: Int?): FilterOption<Int?>
}

data class FilterRange<T: Number>(val min: T?, val max: T?)