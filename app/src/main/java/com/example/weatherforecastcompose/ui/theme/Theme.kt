package com.example.compose
import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.ui.theme.AppTypography
import com.example.weatherforecastcompose.ui.theme.backgroundDark
import com.example.weatherforecastcompose.ui.theme.backgroundDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.backgroundDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.backgroundLight
import com.example.weatherforecastcompose.ui.theme.backgroundLightHighContrast
import com.example.weatherforecastcompose.ui.theme.backgroundLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.errorContainerDark
import com.example.weatherforecastcompose.ui.theme.errorContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.errorContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.errorContainerLight
import com.example.weatherforecastcompose.ui.theme.errorContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.errorContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.errorDark
import com.example.weatherforecastcompose.ui.theme.errorDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.errorDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.errorLight
import com.example.weatherforecastcompose.ui.theme.errorLightHighContrast
import com.example.weatherforecastcompose.ui.theme.errorLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.inverseOnSurfaceDark
import com.example.weatherforecastcompose.ui.theme.inverseOnSurfaceDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.inverseOnSurfaceDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.inverseOnSurfaceLight
import com.example.weatherforecastcompose.ui.theme.inverseOnSurfaceLightHighContrast
import com.example.weatherforecastcompose.ui.theme.inverseOnSurfaceLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.inversePrimaryDark
import com.example.weatherforecastcompose.ui.theme.inversePrimaryDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.inversePrimaryDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.inversePrimaryLight
import com.example.weatherforecastcompose.ui.theme.inversePrimaryLightHighContrast
import com.example.weatherforecastcompose.ui.theme.inversePrimaryLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.inverseSurfaceDark
import com.example.weatherforecastcompose.ui.theme.inverseSurfaceDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.inverseSurfaceDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.inverseSurfaceLight
import com.example.weatherforecastcompose.ui.theme.inverseSurfaceLightHighContrast
import com.example.weatherforecastcompose.ui.theme.inverseSurfaceLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onBackgroundDark
import com.example.weatherforecastcompose.ui.theme.onBackgroundDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onBackgroundDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onBackgroundLight
import com.example.weatherforecastcompose.ui.theme.onBackgroundLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onBackgroundLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onErrorContainerDark
import com.example.weatherforecastcompose.ui.theme.onErrorContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onErrorContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onErrorContainerLight
import com.example.weatherforecastcompose.ui.theme.onErrorContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onErrorContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onErrorDark
import com.example.weatherforecastcompose.ui.theme.onErrorDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onErrorDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onErrorLight
import com.example.weatherforecastcompose.ui.theme.onErrorLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onErrorLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onPrimaryContainerDark
import com.example.weatherforecastcompose.ui.theme.onPrimaryContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onPrimaryContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onPrimaryContainerLight
import com.example.weatherforecastcompose.ui.theme.onPrimaryContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onPrimaryContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onPrimaryDark
import com.example.weatherforecastcompose.ui.theme.onPrimaryDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onPrimaryDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onPrimaryLight
import com.example.weatherforecastcompose.ui.theme.onPrimaryLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onPrimaryLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onSecondaryContainerDark
import com.example.weatherforecastcompose.ui.theme.onSecondaryContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onSecondaryContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onSecondaryContainerLight
import com.example.weatherforecastcompose.ui.theme.onSecondaryContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onSecondaryContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onSecondaryDark
import com.example.weatherforecastcompose.ui.theme.onSecondaryDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onSecondaryDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onSecondaryLight
import com.example.weatherforecastcompose.ui.theme.onSecondaryLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onSecondaryLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onSurfaceDark
import com.example.weatherforecastcompose.ui.theme.onSurfaceDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onSurfaceDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onSurfaceLight
import com.example.weatherforecastcompose.ui.theme.onSurfaceLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onSurfaceLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onSurfaceVariantDark
import com.example.weatherforecastcompose.ui.theme.onSurfaceVariantDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onSurfaceVariantDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onSurfaceVariantLight
import com.example.weatherforecastcompose.ui.theme.onSurfaceVariantLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onSurfaceVariantLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onTertiaryContainerDark
import com.example.weatherforecastcompose.ui.theme.onTertiaryContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onTertiaryContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onTertiaryContainerLight
import com.example.weatherforecastcompose.ui.theme.onTertiaryContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onTertiaryContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.onTertiaryDark
import com.example.weatherforecastcompose.ui.theme.onTertiaryDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.onTertiaryDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.onTertiaryLight
import com.example.weatherforecastcompose.ui.theme.onTertiaryLightHighContrast
import com.example.weatherforecastcompose.ui.theme.onTertiaryLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.outlineDark
import com.example.weatherforecastcompose.ui.theme.outlineDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.outlineDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.outlineLight
import com.example.weatherforecastcompose.ui.theme.outlineLightHighContrast
import com.example.weatherforecastcompose.ui.theme.outlineLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.outlineVariantDark
import com.example.weatherforecastcompose.ui.theme.outlineVariantDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.outlineVariantDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.outlineVariantLight
import com.example.weatherforecastcompose.ui.theme.outlineVariantLightHighContrast
import com.example.weatherforecastcompose.ui.theme.outlineVariantLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.primaryContainerDark
import com.example.weatherforecastcompose.ui.theme.primaryContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.primaryContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.primaryContainerLight
import com.example.weatherforecastcompose.ui.theme.primaryContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.primaryContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.primaryDark
import com.example.weatherforecastcompose.ui.theme.primaryDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.primaryDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.primaryLight
import com.example.weatherforecastcompose.ui.theme.primaryLightHighContrast
import com.example.weatherforecastcompose.ui.theme.primaryLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.scrimDark
import com.example.weatherforecastcompose.ui.theme.scrimDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.scrimDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.scrimLight
import com.example.weatherforecastcompose.ui.theme.scrimLightHighContrast
import com.example.weatherforecastcompose.ui.theme.scrimLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.secondaryContainerDark
import com.example.weatherforecastcompose.ui.theme.secondaryContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.secondaryContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.secondaryContainerLight
import com.example.weatherforecastcompose.ui.theme.secondaryContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.secondaryContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.secondaryDark
import com.example.weatherforecastcompose.ui.theme.secondaryDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.secondaryDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.secondaryLight
import com.example.weatherforecastcompose.ui.theme.secondaryLightHighContrast
import com.example.weatherforecastcompose.ui.theme.secondaryLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceBrightDark
import com.example.weatherforecastcompose.ui.theme.surfaceBrightDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceBrightDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceBrightLight
import com.example.weatherforecastcompose.ui.theme.surfaceBrightLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceBrightLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerDark
import com.example.weatherforecastcompose.ui.theme.surfaceContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighDark
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighLight
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighestDark
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighestDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighestDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighestLight
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighestLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerHighestLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLight
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowDark
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowLight
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowestDark
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowestDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowestDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowestLight
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowestLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceContainerLowestLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceDark
import com.example.weatherforecastcompose.ui.theme.surfaceDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceDimDark
import com.example.weatherforecastcompose.ui.theme.surfaceDimDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceDimDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceDimLight
import com.example.weatherforecastcompose.ui.theme.surfaceDimLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceDimLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceLight
import com.example.weatherforecastcompose.ui.theme.surfaceLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceVariantDark
import com.example.weatherforecastcompose.ui.theme.surfaceVariantDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceVariantDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.surfaceVariantLight
import com.example.weatherforecastcompose.ui.theme.surfaceVariantLightHighContrast
import com.example.weatherforecastcompose.ui.theme.surfaceVariantLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.tertiaryContainerDark
import com.example.weatherforecastcompose.ui.theme.tertiaryContainerDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.tertiaryContainerDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.tertiaryContainerLight
import com.example.weatherforecastcompose.ui.theme.tertiaryContainerLightHighContrast
import com.example.weatherforecastcompose.ui.theme.tertiaryContainerLightMediumContrast
import com.example.weatherforecastcompose.ui.theme.tertiaryDark
import com.example.weatherforecastcompose.ui.theme.tertiaryDarkHighContrast
import com.example.weatherforecastcompose.ui.theme.tertiaryDarkMediumContrast
import com.example.weatherforecastcompose.ui.theme.tertiaryLight
import com.example.weatherforecastcompose.ui.theme.tertiaryLightHighContrast
import com.example.weatherforecastcompose.ui.theme.tertiaryLightMediumContrast

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
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
fun WeatherForecastComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colorScheme = when {


        darkTheme -> darkScheme
        else -> lightScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

