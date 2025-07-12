package org.mattshoe.shoebox.listery.recipe.create.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import org.mattshoe.shoebox.listery.R
import org.mattshoe.shoebox.listery.navigation.LocalNavController
import org.mattshoe.shoebox.listery.navigation.Routes
import org.mattshoe.shoebox.listery.ui.common.ListeryBottomSheet

@Composable
fun ChooseRecipeCreationMethodBottomSheet() {
    val navController = LocalNavController.current

    ListeryBottomSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CreateRecipeOption(
                title = "Generate with AI",
                icon = {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_sparkle),
                        contentDescription = "AI Symbol",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            ) {
                navController.navigate(Routes.GenerateRecipe)
            }

            CreateRecipeOption(
                title = "Read web page",
                icon = {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_globe),
                        contentDescription = "World wide web symbol",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            ) {
                navController.navigate(Routes.ParseWebsite)
            }

            CreateRecipeOption(
                title = "Build your own",
                icon = {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_cookbook),
                        contentDescription = "Recipe symbol",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            ) {
                navController.navigate(Routes.CreateRecipeManuallyBottomSheet)
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun CreateRecipeOption(
    title: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    HorizontalDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.weight(1f)
        )
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
            contentDescription = "Navigate symbol",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}