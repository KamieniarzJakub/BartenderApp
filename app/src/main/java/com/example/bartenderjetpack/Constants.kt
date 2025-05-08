package com.example.bartenderjetpack

import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.model.DrinkCategory


val drinkCategories = listOf(
    DrinkCategory(
        name = "Alkoholowe",
        description = "Zawierają %",
        drinks = listOf(
            Drink("Piwo","nachmielony ekstrakt słodowy wraz z drożdżami, cukier, woda"," -  Cały sprzęt potrzebny do przygotowania piwa dokładnie umyj i odkaź za pomocą pirosiarczynu potasu lub sodu.\n" +
                    " -  Spod wieczka wyjmij drożdże oraz instrukcję. Puszkę ekstraktu nachmielonego umieść w gorącej wodzie na około 10 minut, dzięki czemu łatwiej Ci będzie wydostać zawartość.\n" +
                    " -  Drożdże należy uwodnić - w tym celu umieść je w szklance z wodą o temperaturze 25-28°C i pozostaw na około 30 minut.\n" +
                    " -  Zawartość puszki wlej do garnka z około 2–3 litrami wody. Dodaj cukier – 1 kg glukozy krystalicznej lub 1,5 kg płynnego ekstraktu słodowego. Całość podgrzewaj i mieszaj aż do rozpuszczenia (nie gotuj).\n" +
                    " -  Do pojemnika wlej około 10 litrów wody, dodaj rozpuszczone ekstrakty tak, aby uzyskać 23 litry brzeczki. Zapewnij temperaturę około 20 – 22 °C brzeczki.\n" +
                    " -  Dodaj drożdże i całość energicznie wymieszaj. \n" +
                    " -  Pojemnik zamknij pokrywką z umieszczoną w niej rurką fermentacyjną z woodą.\n" +
                    " -  Fermentacja trwa zwykle około 7- 10 dni. Po około 5 dniach możesz sprawdzić zawartość cukru za pomocą cukromierza. Fermentację możesz zakończyć na poziomie 0 – 1°BLG.\n" +
                    " -  Wtedy możesz zlać piwo do butelek przy pomocy karnika. Butelki powinny być czyste i zdezynfekowane. Do każdej butelki dodaj jedną pastylkę Coopers lub 4 gramy glukozy krystalicznej albo cukru. Następnie zakapslujemy je i zostaw na około 4 tygodnie")
        )
    ),
    DrinkCategory(
        name = "Bezalkoholowe",
        description = "Koktajle bezalkoholowe dla każdego!",
        drinks = listOf(
            Drink("Woda", "woda", "Wlać wodę do szklanki."),
            Drink(
                "Woda gazowana",
                "woda mineralna, gaz",
                "Dodać do wody gaz, następnie wymieszać wszystko razem do uzyskania płynnej konsystencji."
            ),
            Drink(
                "Sok pomarańczowy",
                "pomarańcze, woda mineralna, cukier",
                "Wycisnąć pomarańcze za pomocą wyciskarki. Następnie przelać zawartość do szklanki i posłodzić. Uzupełnić brakującą przestrzeń w szklance wodą, następnie wymieszać, aby nikt się nie kapnął."
            ),
            Drink(
                "Mleko",
                "krowa",
                "Zmiksuj wszystkie składniki z lodem i podawaj w wysokiej szklance."
            ),
        )
    )
)