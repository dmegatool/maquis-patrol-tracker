package com.maquistracker.app.model

import java.text.Collator
import java.util.Locale

enum class AppLanguage {
    FR,
    EN
}

enum class MaquisLocation(val frName: String, val enName: String) {
    PONT_LEVEQUE("Pont Lévêque", "Pont Leveque"),
    PONT_DU_NORD("Pont du Nord", "Pont du Nord"),
    BLACK_MARKET("Marché Noir", "Black Market"),
    DOCTOR("Médecin", "Doctor"),
    RADIO_A("Radio A", "Radio A"),
    RADIO_B("Radio B", "Radio B"),
    RUE_BARADAT("Rue Baradat", "Rue Baradat"),
    GROCER("Épicerie", "Grocer"),
    FENCE("Receleur", "Fence"),
    POOR_DISTRICT("Quartier Ouvrier", "Poor District");

    fun label(language: AppLanguage): String = when (language) {
        AppLanguage.FR -> frName
        AppLanguage.EN -> enName
    }
}

data class PatrolCard(
    val id: Int,
    val locations: List<MaquisLocation> // Ordre exact sur la carte : 1 -> 2 -> 3
)

object MaquisPatrolDeck {
    val cards: List<PatrolCard> = listOf(
        PatrolCard(1, listOf(MaquisLocation.PONT_LEVEQUE, MaquisLocation.BLACK_MARKET, MaquisLocation.DOCTOR)),
        PatrolCard(2, listOf(MaquisLocation.RADIO_A, MaquisLocation.RUE_BARADAT, MaquisLocation.GROCER)),
        PatrolCard(3, listOf(MaquisLocation.GROCER, MaquisLocation.PONT_DU_NORD, MaquisLocation.FENCE)),
        PatrolCard(4, listOf(MaquisLocation.FENCE, MaquisLocation.PONT_DU_NORD, MaquisLocation.PONT_LEVEQUE)),
        PatrolCard(5, listOf(MaquisLocation.PONT_LEVEQUE, MaquisLocation.PONT_DU_NORD, MaquisLocation.DOCTOR)),
        PatrolCard(6, listOf(MaquisLocation.FENCE, MaquisLocation.RUE_BARADAT, MaquisLocation.POOR_DISTRICT)),
        PatrolCard(7, listOf(MaquisLocation.RADIO_B, MaquisLocation.RUE_BARADAT, MaquisLocation.PONT_LEVEQUE)),
        PatrolCard(8, listOf(MaquisLocation.RADIO_A, MaquisLocation.PONT_LEVEQUE, MaquisLocation.BLACK_MARKET)),
        PatrolCard(9, listOf(MaquisLocation.GROCER, MaquisLocation.POOR_DISTRICT, MaquisLocation.DOCTOR)),
        PatrolCard(10, listOf(MaquisLocation.RADIO_B, MaquisLocation.GROCER, MaquisLocation.BLACK_MARKET))
    )

    fun sortedFor(language: AppLanguage): List<PatrolCard> {
        val locale = if (language == AppLanguage.FR) Locale.FRENCH else Locale.ENGLISH
        val collator = Collator.getInstance(locale).apply {
            strength = Collator.PRIMARY // Ignore les accents pour trier Épicerie sous E
        }
        return cards.sortedWith { a, b ->
            val cmp1 = collator.compare(a.locations[0].label(language), b.locations[0].label(language))
            if (cmp1 != 0) cmp1
            else {
                val cmp2 = collator.compare(a.locations[1].label(language), b.locations[1].label(language))
                if (cmp2 != 0) cmp2
                else collator.compare(a.locations[2].label(language), b.locations[2].label(language))
            }
        }
    }
}
