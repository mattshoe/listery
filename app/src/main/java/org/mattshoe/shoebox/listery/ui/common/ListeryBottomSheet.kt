package org.mattshoe.shoebox.listery.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.navigation.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListeryBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: (() -> Unit)? = null,
    canDismiss: () -> Boolean = { true },
    content: @Composable () -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(true) }

    if (!showBottomSheet) {
        LocalNavController.current.popBackStack()
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest ?: {
            showBottomSheet = false
        },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { state ->
                if (state == SheetValue.Hidden) {
                    canDismiss()
                } else {
                    true
                }
            }
        )
    ) {
        Column(
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                content()
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}