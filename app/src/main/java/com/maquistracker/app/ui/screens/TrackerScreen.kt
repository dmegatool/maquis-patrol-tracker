package com.maquistracker.app.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maquistracker.app.model.AppLanguage
import com.maquistracker.app.model.MaquisPatrolDeck
import com.maquistracker.app.model.PatrolCard
import com.maquistracker.app.ui.theme.LocalMaquisPalette

// Ajustez cette variable pour modifier l'espacement entre les 3 lieux d'une carte (ex: 0.dp, 2.dp, 4.dp, 6.dp)
private val LOCATION_SPACING = 2.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(
    language: AppLanguage,
    activeCardIds: Set<Int>,
    isDarkTheme: Boolean,
    onToggleCard: (Int) -> Unit,
    onResetAll: () -> Unit,
    onToggleTheme: () -> Unit,
    onChangeLanguage: () -> Unit,
) {
    val palette = LocalMaquisPalette.current
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val cards = remember(language) { MaquisPatrolDeck.sortedFor(language) }
    val rows = remember(cards, isLandscape) {
        if (isLandscape) {
            // Paysage (5 colonnes x 2 lignes) : ordre par colonne (1er haut-gauche, 2e bas-gauche, 3e haut-col2, etc.)
            val rowCount = 2
            List(rowCount) { rowIndex ->
                cards.filterIndexed { index, _ -> (index % rowCount) == rowIndex }
            }
        } else {
            // Portrait (2 colonnes x 5 lignes) : ordre par ligne (gauche à droite, puis ligne suivante)
            cards.chunked(2)
        }
    }
    val activeCount = activeCardIds.size

    Scaffold(
        containerColor = palette.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = palette.headerBackground,
                    titleContentColor = palette.headerText,
                    navigationIconContentColor = palette.headerText,
                    actionIconContentColor = palette.headerText,
                ),
                navigationIcon = {
                    IconButton(onClick = onChangeLanguage) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (language == AppLanguage.FR) "Langue" else "Language"
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "MAQUIS PATROL TRACKER",
                            style = MaterialTheme.typography.titleMedium.copy(
                                letterSpacing = 1.5.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (language == AppLanguage.FR) {
                                "Patrouilles actives : $activeCount / 10"
                            } else {
                                "Active patrols: $activeCount / 10"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = 11.sp
                            ),
                            color = palette.headerText.copy(alpha = 0.78f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Theme"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = palette.headerBackground,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(
                            horizontal = 14.dp,
                            vertical = if (isLandscape) 6.dp else 10.dp
                        )
                ) {
                    Button(
                        onClick = onResetAll,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isLandscape) 42.dp else 50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = palette.resistanceRed,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.FR) "RÉINITIALISER" else "RESET ALL",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Portrait : 2 colonnes x 5 lignes | Paysage : 5 colonnes x 2 lignes
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp, vertical = if (isLandscape) 8.dp else 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rows.forEach { rowCards ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowCards.forEach { card ->
                        val isLit = activeCardIds.contains(card.id)
                        PatrolCardItem(
                            card = card,
                            language = language,
                            isLit = isLit,
                            isLandscape = isLandscape,
                            onClick = { onToggleCard(card.id) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PatrolCardItem(
    card: PatrolCard,
    language: AppLanguage,
    isLit: Boolean,
    isLandscape: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val palette = LocalMaquisPalette.current

    val containerColor by animateColorAsState(
        targetValue = if (isLit) palette.cardActiveBg else palette.cardInactiveBg,
        label = "cardBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isLit) palette.cardActiveBorder else palette.cardInactiveBorder,
        label = "cardBorder"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (isLit) 1f else 0.42f,
        label = "cardAlpha"
    )

    val horizontalPad = if (isLandscape) 8.dp else 15.dp

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isLit) 1.5.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isLit) 3.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha)
                .padding(horizontal = horizontalPad, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(LOCATION_SPACING, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start
        ) {
            card.locations.forEachIndexed { index, location ->
                Text(
                    text = location.label(language),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp,
                        lineHeight = 20.sp,
                        fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Medium,
                        textDecoration = if (isLit) TextDecoration.None else TextDecoration.LineThrough
                    ),
                    color = if (isLit) {
                        if (index == 0) palette.resistanceRed else palette.textPrimary
                    } else {
                        palette.textMuted
                    },
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
