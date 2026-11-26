package com.maquistracker.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class MaquisPalette(
    val background: Color,
    val headerBackground: Color,
    val headerText: Color,
    val cardActiveBg: Color,
    val cardActiveBorder: Color,
    val cardInactiveBg: Color,
    val cardInactiveBorder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val resistanceRed: Color,
    val miliceBlue: Color,
    val isDark: Boolean
)

// Vibe du jeu de plateau : parchemin 1940, encre sépia, rouge Résistance et bleu Milice
val VintageBoardgamePalette = MaquisPalette(
    background = Color(0xFFEBE1C8),
    headerBackground = Color(0xFF2B241E),
    headerText = Color(0xFFF7F0DC),
    cardActiveBg = Color(0xFFFAF4E4),
    cardActiveBorder = Color(0xFF4A3E33),
    cardInactiveBg = Color(0xFFD6CCB4),
    cardInactiveBorder = Color(0xFFB3A78E),
    textPrimary = Color(0xFF1F1914),
    textSecondary = Color(0xFF5A4E42),
    textMuted = Color(0xFF8C806D),
    resistanceRed = Color(0xFFA82420),
    miliceBlue = Color(0xFF224268),
    isDark = false
)

// Option thème sombre moderne
val ModernDarkPalette = MaquisPalette(
    background = Color(0xFF0F1216),
    headerBackground = Color(0xFF171C22),
    headerText = Color(0xFFF2EDE2),
    cardActiveBg = Color(0xFF1C222B),
    cardActiveBorder = Color(0xFF333D4B),
    cardInactiveBg = Color(0xFF13171D),
    cardInactiveBorder = Color(0xFF222831),
    textPrimary = Color(0xFFF2EDE2),
    textSecondary = Color(0xFF9AA5B4),
    textMuted = Color(0xFF535D6B),
    resistanceRed = Color(0xFFE04843),
    miliceBlue = Color(0xFF4E8EE0),
    isDark = true
)

val LocalMaquisPalette = staticCompositionLocalOf { VintageBoardgamePalette }

private val MaquisTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 2.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        letterSpacing = 1.sp
    )
)

@Composable
fun MaquisTrackerTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val palette = if (darkTheme) ModernDarkPalette else VintageBoardgamePalette
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = palette.resistanceRed,
            secondary = palette.miliceBlue,
            background = palette.background,
            surface = palette.cardActiveBg,
            onPrimary = Color.White,
            onBackground = palette.textPrimary,
            onSurface = palette.textPrimary
        )
    } else {
        lightColorScheme(
            primary = palette.resistanceRed,
            secondary = palette.miliceBlue,
            background = palette.background,
            surface = palette.cardActiveBg,
            onPrimary = Color.White,
            onBackground = palette.textPrimary,
            onSurface = palette.textPrimary
        )
    }

    CompositionLocalProvider(LocalMaquisPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MaquisTypography,
            content = content
        )
    }
}
