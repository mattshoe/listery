package org.mattshoe.shoebox.listery.ui.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val lightScheme = lightColorScheme(
    primary = org.mattshoe.shoebox.listery.ui.theme.primaryLight,
    onPrimary = org.mattshoe.shoebox.listery.ui.theme.onPrimaryLight,
    primaryContainer = org.mattshoe.shoebox.listery.ui.theme.primaryContainerLight,
    onPrimaryContainer = org.mattshoe.shoebox.listery.ui.theme.onPrimaryContainerLight,
    secondary = org.mattshoe.shoebox.listery.ui.theme.secondaryLight,
    onSecondary = org.mattshoe.shoebox.listery.ui.theme.onSecondaryLight,
    secondaryContainer = org.mattshoe.shoebox.listery.ui.theme.secondaryContainerLight,
    onSecondaryContainer = org.mattshoe.shoebox.listery.ui.theme.onSecondaryContainerLight,
    tertiary = org.mattshoe.shoebox.listery.ui.theme.tertiaryLight,
    onTertiary = org.mattshoe.shoebox.listery.ui.theme.onTertiaryLight,
    tertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.tertiaryContainerLight,
    onTertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.onTertiaryContainerLight,
    error = org.mattshoe.shoebox.listery.ui.theme.errorLight,
    onError = org.mattshoe.shoebox.listery.ui.theme.onErrorLight,
    errorContainer = org.mattshoe.shoebox.listery.ui.theme.errorContainerLight,
    onErrorContainer = org.mattshoe.shoebox.listery.ui.theme.onErrorContainerLight,
    background = org.mattshoe.shoebox.listery.ui.theme.backgroundLight,
    onBackground = org.mattshoe.shoebox.listery.ui.theme.onBackgroundLight,
    surface = org.mattshoe.shoebox.listery.ui.theme.surfaceLight,
    onSurface = org.mattshoe.shoebox.listery.ui.theme.onSurfaceLight,
    surfaceVariant = org.mattshoe.shoebox.listery.ui.theme.surfaceVariantLight,
    onSurfaceVariant = org.mattshoe.shoebox.listery.ui.theme.onSurfaceVariantLight,
    outline = org.mattshoe.shoebox.listery.ui.theme.outlineLight,
    outlineVariant = org.mattshoe.shoebox.listery.ui.theme.outlineVariantLight,
    scrim = org.mattshoe.shoebox.listery.ui.theme.scrimLight,
    inverseSurface = org.mattshoe.shoebox.listery.ui.theme.inverseSurfaceLight,
    inverseOnSurface = org.mattshoe.shoebox.listery.ui.theme.inverseOnSurfaceLight,
    inversePrimary = org.mattshoe.shoebox.listery.ui.theme.inversePrimaryLight,
    surfaceDim = org.mattshoe.shoebox.listery.ui.theme.surfaceDimLight,
    surfaceBright = org.mattshoe.shoebox.listery.ui.theme.surfaceBrightLight,
    surfaceContainerLowest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowestLight,
    surfaceContainerLow = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowLight,
    surfaceContainer = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLight,
    surfaceContainerHigh = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighLight,
    surfaceContainerHighest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = org.mattshoe.shoebox.listery.ui.theme.primaryDark,
    onPrimary = org.mattshoe.shoebox.listery.ui.theme.onPrimaryDark,
    primaryContainer = org.mattshoe.shoebox.listery.ui.theme.primaryContainerDark,
    onPrimaryContainer = org.mattshoe.shoebox.listery.ui.theme.onPrimaryContainerDark,
    secondary = org.mattshoe.shoebox.listery.ui.theme.secondaryDark,
    onSecondary = org.mattshoe.shoebox.listery.ui.theme.onSecondaryDark,
    secondaryContainer = org.mattshoe.shoebox.listery.ui.theme.secondaryContainerDark,
    onSecondaryContainer = org.mattshoe.shoebox.listery.ui.theme.onSecondaryContainerDark,
    tertiary = org.mattshoe.shoebox.listery.ui.theme.tertiaryDark,
    onTertiary = org.mattshoe.shoebox.listery.ui.theme.onTertiaryDark,
    tertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.tertiaryContainerDark,
    onTertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.onTertiaryContainerDark,
    error = org.mattshoe.shoebox.listery.ui.theme.errorDark,
    onError = org.mattshoe.shoebox.listery.ui.theme.onErrorDark,
    errorContainer = org.mattshoe.shoebox.listery.ui.theme.errorContainerDark,
    onErrorContainer = org.mattshoe.shoebox.listery.ui.theme.onErrorContainerDark,
    background = org.mattshoe.shoebox.listery.ui.theme.backgroundDark,
    onBackground = org.mattshoe.shoebox.listery.ui.theme.onBackgroundDark,
    surface = org.mattshoe.shoebox.listery.ui.theme.surfaceDark,
    onSurface = org.mattshoe.shoebox.listery.ui.theme.onSurfaceDark,
    surfaceVariant = org.mattshoe.shoebox.listery.ui.theme.surfaceVariantDark,
    onSurfaceVariant = org.mattshoe.shoebox.listery.ui.theme.onSurfaceVariantDark,
    outline = org.mattshoe.shoebox.listery.ui.theme.outlineDark,
    outlineVariant = org.mattshoe.shoebox.listery.ui.theme.outlineVariantDark,
    scrim = org.mattshoe.shoebox.listery.ui.theme.scrimDark,
    inverseSurface = org.mattshoe.shoebox.listery.ui.theme.inverseSurfaceDark,
    inverseOnSurface = org.mattshoe.shoebox.listery.ui.theme.inverseOnSurfaceDark,
    inversePrimary = org.mattshoe.shoebox.listery.ui.theme.inversePrimaryDark,
    surfaceDim = org.mattshoe.shoebox.listery.ui.theme.surfaceDimDark,
    surfaceBright = org.mattshoe.shoebox.listery.ui.theme.surfaceBrightDark,
    surfaceContainerLowest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowestDark,
    surfaceContainerLow = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowDark,
    surfaceContainer = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerDark,
    surfaceContainerHigh = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighDark,
    surfaceContainerHighest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = org.mattshoe.shoebox.listery.ui.theme.primaryLightMediumContrast,
    onPrimary = org.mattshoe.shoebox.listery.ui.theme.onPrimaryLightMediumContrast,
    primaryContainer = org.mattshoe.shoebox.listery.ui.theme.primaryContainerLightMediumContrast,
    onPrimaryContainer = org.mattshoe.shoebox.listery.ui.theme.onPrimaryContainerLightMediumContrast,
    secondary = org.mattshoe.shoebox.listery.ui.theme.secondaryLightMediumContrast,
    onSecondary = org.mattshoe.shoebox.listery.ui.theme.onSecondaryLightMediumContrast,
    secondaryContainer = org.mattshoe.shoebox.listery.ui.theme.secondaryContainerLightMediumContrast,
    onSecondaryContainer = org.mattshoe.shoebox.listery.ui.theme.onSecondaryContainerLightMediumContrast,
    tertiary = org.mattshoe.shoebox.listery.ui.theme.tertiaryLightMediumContrast,
    onTertiary = org.mattshoe.shoebox.listery.ui.theme.onTertiaryLightMediumContrast,
    tertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.tertiaryContainerLightMediumContrast,
    onTertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.onTertiaryContainerLightMediumContrast,
    error = org.mattshoe.shoebox.listery.ui.theme.errorLightMediumContrast,
    onError = org.mattshoe.shoebox.listery.ui.theme.onErrorLightMediumContrast,
    errorContainer = org.mattshoe.shoebox.listery.ui.theme.errorContainerLightMediumContrast,
    onErrorContainer = org.mattshoe.shoebox.listery.ui.theme.onErrorContainerLightMediumContrast,
    background = org.mattshoe.shoebox.listery.ui.theme.backgroundLightMediumContrast,
    onBackground = org.mattshoe.shoebox.listery.ui.theme.onBackgroundLightMediumContrast,
    surface = org.mattshoe.shoebox.listery.ui.theme.surfaceLightMediumContrast,
    onSurface = org.mattshoe.shoebox.listery.ui.theme.onSurfaceLightMediumContrast,
    surfaceVariant = org.mattshoe.shoebox.listery.ui.theme.surfaceVariantLightMediumContrast,
    onSurfaceVariant = org.mattshoe.shoebox.listery.ui.theme.onSurfaceVariantLightMediumContrast,
    outline = org.mattshoe.shoebox.listery.ui.theme.outlineLightMediumContrast,
    outlineVariant = org.mattshoe.shoebox.listery.ui.theme.outlineVariantLightMediumContrast,
    scrim = org.mattshoe.shoebox.listery.ui.theme.scrimLightMediumContrast,
    inverseSurface = org.mattshoe.shoebox.listery.ui.theme.inverseSurfaceLightMediumContrast,
    inverseOnSurface = org.mattshoe.shoebox.listery.ui.theme.inverseOnSurfaceLightMediumContrast,
    inversePrimary = org.mattshoe.shoebox.listery.ui.theme.inversePrimaryLightMediumContrast,
    surfaceDim = org.mattshoe.shoebox.listery.ui.theme.surfaceDimLightMediumContrast,
    surfaceBright = org.mattshoe.shoebox.listery.ui.theme.surfaceBrightLightMediumContrast,
    surfaceContainerLowest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowLightMediumContrast,
    surfaceContainer = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLightMediumContrast,
    surfaceContainerHigh = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = org.mattshoe.shoebox.listery.ui.theme.primaryLightHighContrast,
    onPrimary = org.mattshoe.shoebox.listery.ui.theme.onPrimaryLightHighContrast,
    primaryContainer = org.mattshoe.shoebox.listery.ui.theme.primaryContainerLightHighContrast,
    onPrimaryContainer = org.mattshoe.shoebox.listery.ui.theme.onPrimaryContainerLightHighContrast,
    secondary = org.mattshoe.shoebox.listery.ui.theme.secondaryLightHighContrast,
    onSecondary = org.mattshoe.shoebox.listery.ui.theme.onSecondaryLightHighContrast,
    secondaryContainer = org.mattshoe.shoebox.listery.ui.theme.secondaryContainerLightHighContrast,
    onSecondaryContainer = org.mattshoe.shoebox.listery.ui.theme.onSecondaryContainerLightHighContrast,
    tertiary = org.mattshoe.shoebox.listery.ui.theme.tertiaryLightHighContrast,
    onTertiary = org.mattshoe.shoebox.listery.ui.theme.onTertiaryLightHighContrast,
    tertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.tertiaryContainerLightHighContrast,
    onTertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.onTertiaryContainerLightHighContrast,
    error = org.mattshoe.shoebox.listery.ui.theme.errorLightHighContrast,
    onError = org.mattshoe.shoebox.listery.ui.theme.onErrorLightHighContrast,
    errorContainer = org.mattshoe.shoebox.listery.ui.theme.errorContainerLightHighContrast,
    onErrorContainer = org.mattshoe.shoebox.listery.ui.theme.onErrorContainerLightHighContrast,
    background = org.mattshoe.shoebox.listery.ui.theme.backgroundLightHighContrast,
    onBackground = org.mattshoe.shoebox.listery.ui.theme.onBackgroundLightHighContrast,
    surface = org.mattshoe.shoebox.listery.ui.theme.surfaceLightHighContrast,
    onSurface = org.mattshoe.shoebox.listery.ui.theme.onSurfaceLightHighContrast,
    surfaceVariant = org.mattshoe.shoebox.listery.ui.theme.surfaceVariantLightHighContrast,
    onSurfaceVariant = org.mattshoe.shoebox.listery.ui.theme.onSurfaceVariantLightHighContrast,
    outline = org.mattshoe.shoebox.listery.ui.theme.outlineLightHighContrast,
    outlineVariant = org.mattshoe.shoebox.listery.ui.theme.outlineVariantLightHighContrast,
    scrim = org.mattshoe.shoebox.listery.ui.theme.scrimLightHighContrast,
    inverseSurface = org.mattshoe.shoebox.listery.ui.theme.inverseSurfaceLightHighContrast,
    inverseOnSurface = org.mattshoe.shoebox.listery.ui.theme.inverseOnSurfaceLightHighContrast,
    inversePrimary = org.mattshoe.shoebox.listery.ui.theme.inversePrimaryLightHighContrast,
    surfaceDim = org.mattshoe.shoebox.listery.ui.theme.surfaceDimLightHighContrast,
    surfaceBright = org.mattshoe.shoebox.listery.ui.theme.surfaceBrightLightHighContrast,
    surfaceContainerLowest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowLightHighContrast,
    surfaceContainer = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLightHighContrast,
    surfaceContainerHigh = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = org.mattshoe.shoebox.listery.ui.theme.primaryDarkMediumContrast,
    onPrimary = org.mattshoe.shoebox.listery.ui.theme.onPrimaryDarkMediumContrast,
    primaryContainer = org.mattshoe.shoebox.listery.ui.theme.primaryContainerDarkMediumContrast,
    onPrimaryContainer = org.mattshoe.shoebox.listery.ui.theme.onPrimaryContainerDarkMediumContrast,
    secondary = org.mattshoe.shoebox.listery.ui.theme.secondaryDarkMediumContrast,
    onSecondary = org.mattshoe.shoebox.listery.ui.theme.onSecondaryDarkMediumContrast,
    secondaryContainer = org.mattshoe.shoebox.listery.ui.theme.secondaryContainerDarkMediumContrast,
    onSecondaryContainer = org.mattshoe.shoebox.listery.ui.theme.onSecondaryContainerDarkMediumContrast,
    tertiary = org.mattshoe.shoebox.listery.ui.theme.tertiaryDarkMediumContrast,
    onTertiary = org.mattshoe.shoebox.listery.ui.theme.onTertiaryDarkMediumContrast,
    tertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.onTertiaryContainerDarkMediumContrast,
    error = org.mattshoe.shoebox.listery.ui.theme.errorDarkMediumContrast,
    onError = org.mattshoe.shoebox.listery.ui.theme.onErrorDarkMediumContrast,
    errorContainer = org.mattshoe.shoebox.listery.ui.theme.errorContainerDarkMediumContrast,
    onErrorContainer = org.mattshoe.shoebox.listery.ui.theme.onErrorContainerDarkMediumContrast,
    background = org.mattshoe.shoebox.listery.ui.theme.backgroundDarkMediumContrast,
    onBackground = org.mattshoe.shoebox.listery.ui.theme.onBackgroundDarkMediumContrast,
    surface = org.mattshoe.shoebox.listery.ui.theme.surfaceDarkMediumContrast,
    onSurface = org.mattshoe.shoebox.listery.ui.theme.onSurfaceDarkMediumContrast,
    surfaceVariant = org.mattshoe.shoebox.listery.ui.theme.surfaceVariantDarkMediumContrast,
    onSurfaceVariant = org.mattshoe.shoebox.listery.ui.theme.onSurfaceVariantDarkMediumContrast,
    outline = org.mattshoe.shoebox.listery.ui.theme.outlineDarkMediumContrast,
    outlineVariant = org.mattshoe.shoebox.listery.ui.theme.outlineVariantDarkMediumContrast,
    scrim = org.mattshoe.shoebox.listery.ui.theme.scrimDarkMediumContrast,
    inverseSurface = org.mattshoe.shoebox.listery.ui.theme.inverseSurfaceDarkMediumContrast,
    inverseOnSurface = org.mattshoe.shoebox.listery.ui.theme.inverseOnSurfaceDarkMediumContrast,
    inversePrimary = org.mattshoe.shoebox.listery.ui.theme.inversePrimaryDarkMediumContrast,
    surfaceDim = org.mattshoe.shoebox.listery.ui.theme.surfaceDimDarkMediumContrast,
    surfaceBright = org.mattshoe.shoebox.listery.ui.theme.surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowDarkMediumContrast,
    surfaceContainer = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = org.mattshoe.shoebox.listery.ui.theme.primaryDarkHighContrast,
    onPrimary = org.mattshoe.shoebox.listery.ui.theme.onPrimaryDarkHighContrast,
    primaryContainer = org.mattshoe.shoebox.listery.ui.theme.primaryContainerDarkHighContrast,
    onPrimaryContainer = org.mattshoe.shoebox.listery.ui.theme.onPrimaryContainerDarkHighContrast,
    secondary = org.mattshoe.shoebox.listery.ui.theme.secondaryDarkHighContrast,
    onSecondary = org.mattshoe.shoebox.listery.ui.theme.onSecondaryDarkHighContrast,
    secondaryContainer = org.mattshoe.shoebox.listery.ui.theme.secondaryContainerDarkHighContrast,
    onSecondaryContainer = org.mattshoe.shoebox.listery.ui.theme.onSecondaryContainerDarkHighContrast,
    tertiary = org.mattshoe.shoebox.listery.ui.theme.tertiaryDarkHighContrast,
    onTertiary = org.mattshoe.shoebox.listery.ui.theme.onTertiaryDarkHighContrast,
    tertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.tertiaryContainerDarkHighContrast,
    onTertiaryContainer = org.mattshoe.shoebox.listery.ui.theme.onTertiaryContainerDarkHighContrast,
    error = org.mattshoe.shoebox.listery.ui.theme.errorDarkHighContrast,
    onError = org.mattshoe.shoebox.listery.ui.theme.onErrorDarkHighContrast,
    errorContainer = org.mattshoe.shoebox.listery.ui.theme.errorContainerDarkHighContrast,
    onErrorContainer = org.mattshoe.shoebox.listery.ui.theme.onErrorContainerDarkHighContrast,
    background = org.mattshoe.shoebox.listery.ui.theme.backgroundDarkHighContrast,
    onBackground = org.mattshoe.shoebox.listery.ui.theme.onBackgroundDarkHighContrast,
    surface = org.mattshoe.shoebox.listery.ui.theme.surfaceDarkHighContrast,
    onSurface = org.mattshoe.shoebox.listery.ui.theme.onSurfaceDarkHighContrast,
    surfaceVariant = org.mattshoe.shoebox.listery.ui.theme.surfaceVariantDarkHighContrast,
    onSurfaceVariant = org.mattshoe.shoebox.listery.ui.theme.onSurfaceVariantDarkHighContrast,
    outline = org.mattshoe.shoebox.listery.ui.theme.outlineDarkHighContrast,
    outlineVariant = org.mattshoe.shoebox.listery.ui.theme.outlineVariantDarkHighContrast,
    scrim = org.mattshoe.shoebox.listery.ui.theme.scrimDarkHighContrast,
    inverseSurface = org.mattshoe.shoebox.listery.ui.theme.inverseSurfaceDarkHighContrast,
    inverseOnSurface = org.mattshoe.shoebox.listery.ui.theme.inverseOnSurfaceDarkHighContrast,
    inversePrimary = org.mattshoe.shoebox.listery.ui.theme.inversePrimaryDarkHighContrast,
    surfaceDim = org.mattshoe.shoebox.listery.ui.theme.surfaceDimDarkHighContrast,
    surfaceBright = org.mattshoe.shoebox.listery.ui.theme.surfaceBrightDarkHighContrast,
    surfaceContainerLowest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerLowDarkHighContrast,
    surfaceContainer = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerDarkHighContrast,
    surfaceContainerHigh = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = org.mattshoe.shoebox.listery.ui.theme.surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun ListeryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      
      darkTheme -> darkScheme
      else -> lightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

