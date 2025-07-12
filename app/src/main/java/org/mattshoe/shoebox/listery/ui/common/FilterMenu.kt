package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.cookbook.viewmodel.FilterOption


@Composable
fun FilterMenu(
    filterOptions: List<FilterOption<*>>,
    modifier: Modifier = Modifier,
    onUpdated: (List<FilterOption<*>>, FilterOption<*>) -> Unit
) {
    fun updateFilter(filterOption: FilterOption<*>) {
        val newList = filterOptions.replaceFirst(filterOption) {
            it.title == filterOption.title
        }
        onUpdated(newList, filterOption)
    }

    Column(modifier) {
        filterOptions.forEach { filterOption ->
            when (filterOption) {
                is FilterOption.Enumeration<*> -> EnumerationFilterOption(filterOption as FilterOption.Enumeration<Any?>) {
                    updateFilter(it)
                }
                is FilterOption.Toggle -> ToggleFilterOption(filterOption) { updateFilter(it) }
                is FilterOption.DecimalMin -> DecimalMinFilterOption(filterOption) { updateFilter(it) }
                is FilterOption.IntMin -> IntMinFilterOption(filterOption) { updateFilter(it) }
                is FilterOption.DecimalMax -> DecimalMaxFilterOption(filterOption) { updateFilter(it) }
                is FilterOption.IntMax -> IntMaxFilterOption(filterOption) { updateFilter(it) }
                is FilterOption.DecimalRange -> DecimalRangeFilterOption(filterOption) { updateFilter(it) }
                is FilterOption.IntRange -> IntRangeFilterOption(filterOption) { updateFilter(it) }
            }
        }
    }
}

@Composable
fun <T> EnumerationFilterOption(
    option: FilterOption.Enumeration<T?>,
    modifier: Modifier = Modifier,
    onOptionUpdated: (FilterOption.Enumeration<T?>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val label = option.title
    val selected = option.selectedValue
    val values = option.values

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val maxWidth = maxWidth

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .dimWhen { option.selectedValue == null },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Box(
                modifier = Modifier
                    .widthIn(max = maxWidth / 2)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.shapes.small
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = selected?.toString() ?: "Any",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("None") },
                        onClick = {
                            expanded = false
                            onOptionUpdated(
                                option.copy(
                                    selectedValue = null
                                )
                            )
                        }
                    )
                    values.forEach { value ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = value.toString(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            onClick = {
                                expanded = false
                                onOptionUpdated(
                                    option.copy(
                                        selectedValue = value
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToggleFilterOption(
    option: FilterOption.Toggle,
    onOptionUpdated: (FilterOption.Toggle) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .dimWhen { option.selectedValue == null }
            .clickable {
                val newOption = option.copy(
                    selectedValue = when (option.selectedValue) {
                        null -> true
                        true -> false
                        false -> null
                    }
                )
                onOptionUpdated(
                    newOption
                )
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = option.title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
        Checkbox(
            checked = option.selectedValue ?: false,
            onCheckedChange = null // row handles click
        )
    }
}

@Composable
fun DecimalMinFilterOption(
    option: FilterOption.DecimalMin,
    onOptionUpdated: (FilterOption.DecimalMin) -> Unit
) {
    MinFilterOption(
        title = option.title,
        value = option.selectedValue
    ) {
        onOptionUpdated(
            option.copy(
                selectedValue = it.text.toFloatOrNull()
            )
        )
    }
}

@Composable
fun IntMinFilterOption(
    option: FilterOption.IntMin,
    onOptionUpdated: (FilterOption.IntMin) -> Unit
) {
    MinFilterOption(
        title = option.title,
        value = option.selectedValue
    ) {
        onOptionUpdated(
            option.copy(
                selectedValue = it.text.toIntOrNull()
            )
        )
    }
}

@Composable
fun MinFilterOption(
    title: String,
    value: Number?,
    onUpdated: (TextFieldValue) -> Unit
) {
    SingleTextInputOptionRow(
        title = title,
        value = value?.toString(),
        placeholder = "Min",
        keyboardType = KeyboardType.Number
    ) {
        onUpdated(it)
    }
}

@Composable
fun DecimalMaxFilterOption(
    option: FilterOption.DecimalMax,
    onOptionUpdated: (FilterOption.DecimalMax) -> Unit
) {
    MaxFilterOption(
        option.title,
        option.selectedValue
    ) {
        onOptionUpdated(
            option.copy(
                selectedValue = it.text.toFloatOrNull()
            )
        )
    }
}
@Composable
fun IntMaxFilterOption(
    option: FilterOption.IntMax,
    onOptionUpdated: (FilterOption.IntMax) -> Unit
) {
    MaxFilterOption(
        option.title,
        option.selectedValue
    ) {
        onOptionUpdated(
            option.copy(
                selectedValue = it.text.toIntOrNull()
            )
        )
    }
}

@Composable
fun MaxFilterOption(
    title: String,
    value: Number?,
    onUpdated: (TextFieldValue) -> Unit
) {
    SingleTextInputOptionRow(
        title = title,
        value = value?.toString(),
        placeholder = "Max",
        keyboardType = KeyboardType.Number
    ) {
        onUpdated(it)
    }
}

@Composable
fun DecimalRangeFilterOption(
    option: FilterOption.DecimalRange,
    onOptionUpdated: (FilterOption.DecimalRange) -> Unit
) {
    RangeFilterOption(
        title = option.title,
        minValue = option.selectedValue.min,
        maxValue = option.selectedValue.max,
        onMinUpdated = {
            onOptionUpdated(
                option.copy(
                    selectedValue = option.selectedValue.copy(
                        min = it.text.toFloatOrNull()
                    )
                )
            )
        },
        onMaxUpdated = {
            onOptionUpdated(
                option.copy(
                    selectedValue = option.selectedValue.copy(
                        max = it.text.toFloatOrNull()
                    )
                )
            )
        }
    )
}

@Composable
fun IntRangeFilterOption(
    option: FilterOption.IntRange,
    onOptionUpdated: (FilterOption.IntRange) -> Unit
) {
    RangeFilterOption(
        title = option.title,
        minValue = option.selectedValue.min,
        maxValue = option.selectedValue.max,
        onMinUpdated = {
            onOptionUpdated(
                option.copy(
                    selectedValue = option.selectedValue.copy(
                        min = it.text.toIntOrNull()
                    )
                )
            )
        },
        onMaxUpdated = {
            onOptionUpdated(
                option.copy(
                    selectedValue = option.selectedValue.copy(
                        max = it.text.toIntOrNull()
                    )
                )
            )
        }
    )
}

@Composable
fun RangeFilterOption(
    title: String,
    minValue: Number?,
    maxValue: Number?,
    onMinUpdated: (TextFieldValue) -> Unit,
    onMaxUpdated: (TextFieldValue) -> Unit
) {
    var minText by remember {
        mutableStateOf(
            TextFieldValue(text = minValue?.toString() ?: "")
        )
    }
    var maxText by remember {
        mutableStateOf(
            TextFieldValue(text = maxValue?.toString() ?: "")
        )
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .dimWhen {
                    minValue == null && maxValue == null
                }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )

            ListeryTextInput(
                value = minText,
                placeholder = "Min",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.width(50.dp)
            ) {
                minText = it
                onMinUpdated(it)
            }
            Text(
                text = "—",
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            ListeryTextInput(
                value = maxText,
                placeholder = "Max",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.width(50.dp)
            ) {
                maxText = it
                onMaxUpdated(it)
            }
        }
    }
}

@Composable
fun SingleTextInputOptionRow(
    title: String,
    value: String?,
    placeholder: String,
    keyboardType: KeyboardType,
    inputWidth: Dp = 50.dp,
    onUpdated: (TextFieldValue) -> Unit
) {
    var text by remember {
        mutableStateOf(
            TextFieldValue(text = value ?: "")
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .dimWhen { value == null }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )

        ListeryTextInput(
            value = text,
            placeholder = placeholder,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            ),
            modifier = Modifier.width(inputWidth)
        ) {
            text = it
            onUpdated(it)
        }
    }
}

inline fun <T> List<T>.replaceFirst(newItem: T, predicate: (T) -> Boolean): List<T> {
    val index = indexOfFirst(predicate)
    if (index == -1) return this
    return toMutableList().apply { this[index] = newItem }
}

fun Modifier.dimWhen(predicate: () -> Boolean): Modifier {
    return if (predicate()) {
        this.alpha(0.5f)
    } else {
        this
    }
}