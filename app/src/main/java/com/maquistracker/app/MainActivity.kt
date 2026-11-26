package com.maquistracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import com.maquistracker.app.model.AppLanguage
import com.maquistracker.app.model.MaquisPatrolDeck
import com.maquistracker.app.model.PatrolCard
import com.maquistracker.app.ui.screens.LanguageScreen
import com.maquistracker.app.ui.screens.TrackerScreen
import com.maquistracker.app.ui.theme.MaquisTrackerTheme

private const val PREFS_NAME = "maquis_prefs"
private const val KEY_LANGUAGE = "selected_language"
private const val KEY_DARK_THEME = "is_dark_theme"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val prefs = remember { context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }

            var selectedLanguage by remember {
                val savedLang = prefs.getString(KEY_LANGUAGE, null)
                mutableStateOf(savedLang?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() })
            }
            var showLanguagePicker by rememberSaveable {
                mutableStateOf(selectedLanguage == null)
            }
            var isDarkTheme by remember {
                mutableStateOf(prefs.getBoolean(KEY_DARK_THEME, false))
            }
            var activeCardIds by rememberSaveable {
                mutableStateOf(MaquisPatrolDeck.cards.asSequence().map(PatrolCard::id).toSet())
            }

            fun updateTheme(dark: Boolean) {
                isDarkTheme = dark
                prefs.edit { putBoolean(KEY_DARK_THEME, dark) }
            }

            fun selectLanguage(lang: AppLanguage) {
                selectedLanguage = lang
                showLanguagePicker = false
                prefs.edit { putString(KEY_LANGUAGE, lang.name) }
            }

            MaquisTrackerTheme(darkTheme = isDarkTheme) {
                val currentLang = selectedLanguage
                if (showLanguagePicker || (currentLang == null)) {
                    LanguageScreen(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { updateTheme(!isDarkTheme) },
                        onSelectLanguage = ::selectLanguage,
                    )
                } else {
                    TrackerScreen(
                        language = currentLang,
                        activeCardIds = activeCardIds,
                        isDarkTheme = isDarkTheme,
                        onToggleCard = { id ->
                            activeCardIds = if (activeCardIds.contains(id)) {
                                activeCardIds - id
                            } else {
                                activeCardIds + id
                            }
                        },
                        onResetAll = {
                            activeCardIds = MaquisPatrolDeck.cards.asSequence().map(PatrolCard::id).toSet()
                        },
                        onToggleTheme = { updateTheme(!isDarkTheme) },
                        onChangeLanguage = { showLanguagePicker = true },
                    )
                }
            }
        }
    }
}
