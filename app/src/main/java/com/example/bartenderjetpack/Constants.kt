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
                    " -  Wtedy możesz zlać piwo do butelek przy pomocy karnika. Butelki powinny być czyste i zdezynfekowane. Do każdej butelki dodaj jedną pastylkę Coopers lub 4 gramy glukozy krystalicznej albo cukru. Następnie zakapslujemy je i zostaw na około 4 tygodnie",
                "https://images.unsplash.com/photo-1618183479302-1e0aa382c36b")
        )
    ),
    DrinkCategory(
        name = "Bezalkoholowe",
        description = "Koktajle bezalkoholowe dla każdego!",
        drinks = listOf(
            Drink("Woda", "woda", "Wlać wodę do szklanki.","https://images.unsplash.com/photo-1612375066516-11d6d394bd2a"),
            Drink(
                "Woda gazowana",
                "woda mineralna, gaz",
                "Dodać do wody gaz, następnie wymieszać wszystko razem do uzyskania płynnej konsystencji.",
                "https://images.unsplash.com/photo-1594048099873-fb507f81a9ed"
            ),
            Drink(
                "Sok pomarańczowy",
                "pomarańcze, woda mineralna, cukier",
                "Wycisnąć pomarańcze za pomocą wyciskarki. Następnie przelać zawartość do szklanki i posłodzić. Uzupełnić brakującą przestrzeń w szklance wodą, następnie wymieszać, aby nikt się nie kapnął.",
                "https://images.unsplash.com/photo-1689066117649-0ca9762fc92c"
            ),
            Drink(
                "Mleko",
                "krowa",
                "Zmiksuj wszystkie składniki z lodem i podawaj w wysokiej szklance.",
                "https://images.unsplash.com/photo-1517448931760-9bf4414148c5"
            ),
            Drink(
                "Lemoniada",
                "cytryny, woda, cukier, mięta",
                "Wycisnąć sok z cytryn, dodać wodę i cukier do smaku. Wymieszać, a na koniec wrzucić kilka listków mięty.",
                "https://images.unsplash.com/photo-1575596510825-f748919a2bf7"
            ),
            Drink(
                "Herbata mrożona",
                "herbata, cytryna, cukier, kostki lodu",
                "Zaparzyć mocną herbatę, ostudzić, dodać cukier i sok z cytryny. Przelać do szklanki z lodem.",
                "https://images.unsplash.com/photo-1556679343-c7306c1976bc"
            ),
            Drink(
                "Koktajl bananowy",
                "banany, mleko, miód",
                "Zmiksować banany z mlekiem i odrobiną miodu. Podawać schłodzone.",
                "https://images.unsplash.com/photo-1707219811295-0f283760668b"
            ),
            Drink(
                "Kompot z jabłek",
                "jabłka, cukier, woda, cynamon",
                "Pokroić jabłka i gotować z cukrem, wodą i szczyptą cynamonu. Po ostudzeniu podawać na zimno.",
                "https://images.unsplash.com/photo-1534336810865-0beae4c81278"
            ),
            Drink(
                "Smoothie truskawkowe",
                "truskawki, jogurt naturalny, miód",
                "Zmiksować truskawki z jogurtem i miodem. Przelać do szklanki i udekorować świeżą truskawką.",
                "https://images.unsplash.com/photo-1579954115545-a95591f28bfc"
            ),
            Drink("Woda1", "woda", "Wlać wodę do szklanki.","https://images.unsplash.com/photo-1612375066516-11d6d394bd2a"),
            Drink(
                "Woda gazowana1",
                "woda mineralna, gaz",
                "Dodać do wody gaz, następnie wymieszać wszystko razem do uzyskania płynnej konsystencji.",
                "https://images.unsplash.com/photo-1594048099873-fb507f81a9ed"
            ),
            Drink(
                "Sok pomarańczowy1",
                "pomarańcze, woda mineralna, cukier",
                "Wycisnąć pomarańcze za pomocą wyciskarki. Następnie przelać zawartość do szklanki i posłodzić. Uzupełnić brakującą przestrzeń w szklance wodą, następnie wymieszać, aby nikt się nie kapnął.",
                "https://images.unsplash.com/photo-1689066117649-0ca9762fc92c"
            ),
            Drink(
                "Mleko1",
                "krowa",
                "Zmiksuj wszystkie składniki z lodem i podawaj w wysokiej szklance.",
                "https://images.unsplash.com/photo-1517448931760-9bf4414148c5"
            ),
            Drink(
                "Lemoniada1",
                "cytryny, woda, cukier, mięta",
                "Wycisnąć sok z cytryn, dodać wodę i cukier do smaku. Wymieszać, a na koniec wrzucić kilka listków mięty.",
                "https://images.unsplash.com/photo-1575596510825-f748919a2bf7"
            ),
            Drink(
                "Herbata mrożona1",
                "herbata, cytryna, cukier, kostki lodu",
                "Zaparzyć mocną herbatę, ostudzić, dodać cukier i sok z cytryny. Przelać do szklanki z lodem.",
                "https://images.unsplash.com/photo-1556679343-c7306c1976bc"
            ),
            Drink(
                "Koktajl bananowy1",
                "banany, mleko, miód",
                "Zmiksować banany z mlekiem i odrobiną miodu. Podawać schłodzone.",
                "https://images.unsplash.com/photo-1707219811295-0f283760668b"
            ),
            Drink(
                "Kompot z jabłek1",
                "jabłka, cukier, woda, cynamon",
                "Pokroić jabłka i gotować z cukrem, wodą i szczyptą cynamonu. Po ostudzeniu podawać na zimno.",
                "https://images.unsplash.com/photo-1534336810865-0beae4c81278"
            ),
            Drink(
                "Smoothie truskawkowe1",
                "truskawki, jogurt naturalny, miód",
                "Zmiksować truskawki z jogurtem i miodem. Przelać do szklanki i udekorować świeżą truskawką.",
                "https://images.unsplash.com/photo-1579954115545-a95591f28bfc"
            )
        )
    )
)