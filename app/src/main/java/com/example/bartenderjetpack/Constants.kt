package com.example.bartenderjetpack

import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory


val drinkCategories = listOf(
    DrinkCategory(
        name = "Główna",
        description = "Witaj w aplikacji Bartender! Tutaj znajdziesz przepisy na różne napoje.",
        drinks = emptyList()
    ),
    DrinkCategory(
        name = "Bezalkoholowe",
        description = "Koktajle bezalkoholowe dla każdego!",
        drinks = listOf(
            Drink("Woda", "Woda", "Wlać wodę do szklanki."),
            Drink(
                "Woda gazowana",
                "Woda mineralna, gaz",
                "Dodać do wody gaz, następnie wymieszać wszystko razem do uzyskania płynnej konsystencji."
            ),
            Drink(
                "Sok pomarańczowy",
                "Pomarańcze, woda mineralna, cukier",
                "Wycisnąć pomarańcze za pomocą wyciskarki. Następnie przelać zawartość do szklanki i posłodzić. Uzupełnić brakującą przestrzeń w szklance wodą, następnie wymieszać, aby nikt się nie kapnął."
            ),
            Drink(
                "Mleko",
                "Krowa",
                "Zmiksuj wszystkie składniki z lodem i podawaj w wysokiej szklance."
            ),
        )
    ),
    DrinkCategory(
        name = "Template",
        description = "Do uzupełnienia",
        drinks = listOf(
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-"),
            Drink("Template", "-", "-")
        )
    )
)