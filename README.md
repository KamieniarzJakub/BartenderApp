TODO:

Lab 5, 6, 7:

Cel: Jest to kontynuacja aplikacji z poprzednich zajęć i zmiana jej strony wizualnej oraz niektórych mechanizmów nawigacyjnych. Ekran listy ma się składać z co najmniej trzech kart. Pierwsza karta ma być kartą główną aplikacji, a pozostałe mają być kartami kategorii. Karta główna będzie informować o  przeznaczeniu aplikacji, a karty kategorii mogą np. dzielić koktajle według dowolnego kryterium np. short drinki long drinki, koktajle bezalkoholowe i alkoholowe lub jakiegokolwiek inny sensowny podział, który przyjdzie Wam do głowy.

Wymagania minimalne:
1. ~~Karty kategorii zamiast listy nazw koktajlów mają używać widoku RecyclerView z układem siatki (grid), w którym poszczególne pozycje (koktajle) będą prezentowane w postaci obrazka i nazwy, dla których użyto widoku CardView. Kliknięcie wybranej pozycji (szlaku) powoduje wyświetlenie szczegółów, czyli nazwy koktajlu, większego obrazka, listy składników i sposobu przygotowania.~~
2. ~~Na ekranie szczegółów ma się pojawić przycisk FAB (floating action button), który będzie odpowiedzialny za wysłanie SMSa ze składnikami  (w uproszczonej wersji działanie przycisku może prowadzić jedynie do wyświetlenia odpowiedniego komunikatu).~~
3. ~~W aplikacji należy zastosować motywy.~~
4. ~~W aplikacji należy korzystać z fragmentów (w przypadku stosowania Jetpack Compose nie wymagane)~~
5. ~~Aplikacja ma działać poprawnie przy zmianie orientacji urządzenia.~~
6. ~~Każda aktywność ma mieć pasek aplikacji w postaci paska narzędzi.~~
7. ~~Ekran szczegółów ma być przewijany w pionie razem z paskiem aplikacji.~~
8. ~~Na ekranie szczegółów obrazek ma się pojawić na pasku aplikacji, ale ma się razem z nim zwijać.~~
9. ~~Przechodzenie pomiędzy kartami ma się odbywać także za pomocą gestu przeciągnięcia.~~
10. ~~Do aplikacji należy dodać szufladę nawigacyjną~~

Elementy dodatkowe:
1. ~~Kod w Kotlinie~~
2. ~~UI w Jetpack Compose~~
3. ~~Dane umieszczone poza urządzeniem~~
4. ~~Wykorzystanie motywów z biblioteki wzornictwa (material design)~~
5. ~~Dodanie do paska aplikacji akcji.~~
6. Dodanie do paska aplikacji opcji wyszukiwania przepisu zawierającego w nazwie i/lub opisie podany tekst.
7. ~~Opracowanie własnych ikon związanych z akcją.~~
8. ~~Opracowanie własnej ikony dla aplikacji~~


Lab 8:
Zadanie - Animacje

Cel: Jest to kontynuacja aplikacji z poprzednich zajęć i dodanie do niej krótkiej animacji, która będzie się pojawiać w trakcie uruchamiania aplikacji. Animacja powinna być związana tematycznie z realizowanym zadaniem.

Wymagania minimalne

1. Animacja ma się opierać na systemie animacji właściwości, czyli korzystać z obiektu ObjectAnimator

Elementy dodatkowe:

1. Kod w Kotlinie,
2. Uruchomienie kilku animatorów równocześnie
3. Animacja reagująca na działania wybranego sensora lub sensorów
4. Ruchy obiektów zgodne z regułami biblioteki wzornictwa Material Design
5. Ruchy obiektów zgodne z prawami fizyki
6. Inne wg uznania

TODO:
- podświetlanie aktywnego itema na widoku horyzontalnym
- testy tabletu
- wyszukiwanie
- swipe z prawej na lewą na ostatnim ekranie szczegółów daje ??? w top app bar

Bugi do naprawienia:
- na telefonie, widok horyzontalny, scrollowanie się buguje, trzeba przeciągnąć w odpowiednim miejscu, lista drinków z danej kategorii (widać tylko 2 pierwsze) -> odpalenie aplikacji na widoku pionowym, wybór kategorii, zmiana orientacji, wybór drinka = crash
- na widoku pionowym, jak jest więcej drinków, to wgl nie działa scroll
