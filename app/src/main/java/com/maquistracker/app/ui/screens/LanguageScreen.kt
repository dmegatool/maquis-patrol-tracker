package com.maquistracker.app.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maquistracker.app.model.AppLanguage
import com.maquistracker.app.ui.theme.LocalMaquisPalette

// Ajustez cette variable pour modifier la largeur des boutons de langue en portrait (ex: 0.5f = 50%, 0.66f = 66%, 1.0f = 100%)
private const val LANGUAGE_BUTTON_WIDTH_FRACTION = 0.66f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
) {
    val palette = LocalMaquisPalette.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        containerColor = palette.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    actionIconContentColor = palette.textPrimary
                ),
                title = {},
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Theme"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .then(if (isLandscape) Modifier else Modifier.navigationBarsPadding())
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = if (isLandscape) 4.dp else 12.dp,
                    bottom = if (isLandscape) 2.dp else 12.dp,
                )
        ) {
            if (isLandscape) {
                // Mode paysage : logo centré à gauche, 2 boutons de langue centrés à droite
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        LogoBlock()
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        LanguageButtonsBlock(
                            widthFraction = 0.72f,
                            onSelectLanguage = onSelectLanguage
                        )
                    }
                }
            } else {
                // Mode portrait : empilé au centre
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    LogoBlock()
                    LanguageButtonsBlock(
                        widthFraction = LANGUAGE_BUTTON_WIDTH_FRACTION,
                        onSelectLanguage = onSelectLanguage
                    )
                }
            }

            Text(
                text = "This is a fan made app\nAll rights to Maquis belong to their respective owners",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FontFamily.Serif,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                ),
                color = palette.textSecondary.copy(alpha = 0.78f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = if (isLandscape) 0.dp else 4.dp)
            )
        }
    }
}

@Composable
private fun LogoBlock() {
    val palette = LocalMaquisPalette.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(palette.resistanceRed, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFAF5E6),
                modifier = Modifier.size(40.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "MAQUIS",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 36.sp,
                    letterSpacing = 6.sp
                ),
                color = palette.textPrimary
            )
            Text(
                text = "PATROL TRACKER",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 14.sp,
                    letterSpacing = 4.sp
                ),
                color = palette.resistanceRed
            )
        }
    }
}

@Composable
private fun LanguageButtonsBlock(
    widthFraction: Float,
    onSelectLanguage: (AppLanguage) -> Unit
) {
    val palette = LocalMaquisPalette.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Button(
            onClick = { onSelectLanguage(AppLanguage.EN) },
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .height(60.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.resistanceRed,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "ENGLISH",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 2.sp
            )
        }

        Button(
            onClick = { onSelectLanguage(AppLanguage.FR) },
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .height(60.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.resistanceRed,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "FRANÇAIS",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                letterSpacing = 2.sp
            )
        }
    }
}
