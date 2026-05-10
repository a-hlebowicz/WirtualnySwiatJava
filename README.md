# Wirtualny Świat (Java)

Turowa symulacja ekosystemu z dziedziczeniem wirtualnym. Java + Swing, GUI z ikonami PNG.

Każde pole siatki to organizm (zwierzę albo roślina) z siłą, inicjatywą i wiekiem. W każdej turze organizmy wykonują swoją akcję: zwierzęta poruszają się i walczą, rośliny się rozsiewają. Człowiek jest sterowany z klawiatury, reszta działa autonomicznie. Plansza rysowana w siatce `JLabel` z ikonami, komunikaty wypisywane w `JTextArea` pod planszą.

## organizmy

Wszystkie dziedziczą po klasie abstrakcyjnej `Organizm` przez `Zwierze` albo `Roslina`. Kluczowe metody abstrakcyjne: `akcja()`, `kolizja()`. Każdy organizm trzyma referencję na `Swiat`, swoje współrzędne, siłę, inicjatywę, wiek oraz ścieżkę do pliku ikony (`String ikona`).

zwierzęta:

* `Wilk` (S=9, I=5); porusza się losowo
* `Owca` (S=4, I=4); porusza się losowo
* `Lis` (S=3, I=7); nie wchodzi na pole z silniejszym przeciwnikiem
* `Antylopa` (S=4, I=4); rusza się o 2 pola; 50% szansy na ucieczkę przed atakiem
* `Zolw` (S=2, I=1); 75% szansy że nie wykona ruchu; odpiera atak gdy atakujący ma siłę < 5
* `Czlowiek` (S=5, I=5); sterowany z klawiatury; umiejętność specjalna (Tarcza)

rośliny:

* `Trawa` (S=0, I=0); 5% szansy na rozsianie się na sąsiednie pole
* `Mlecz` (S=0, I=0); jak trawa, ale do trzech prób rozsiania w jednej turze
* `Guarana` (S=0, I=0); zwiększa siłę zjadającego o 3
* `BarszczSosnowskiego` (S=90, I=0); zabija sąsiednie zwierzęta i swojego zjadającego
* `WilczeJagody` (S=99, I=0); zabijają zjadającego

## tura

`Swiat.wykonajTure(Okno okno)` wykonuje cztery fazy:

1. Inkrementacja licznika tur i `usunOrganizmyZListy()`; usuwa martwych z poprzedniej tury i dołącza nowo narodzonych do `listaOrganizmow`
2. Sortowanie listy malejąco po inicjatywie (przy remisie po wieku) komparatorem `ktoPierwszy`
3. Wywołanie `akcja()` na każdym żywym organizmie w kolejności; żywotność sprawdzana przez `mapaOrganizmow[x][y] == organizm`
4. Wypisanie komunikatów do `JTextArea` przez `wyswietlKomunikaty(okno)` i przerysowanie planszy (`updateBoard()`)

Akcja zwierzęcia: wybór losowego kierunku (do 20 prób, żeby trafić w pole na planszy), sprawdzenie czy cel jest pusty albo zajęty. Jeśli pusty, przesunięcie; jeśli zajęty, wywołanie `kolizja()` na obrońcy. Obrońca decyduje o wyniku: śmierć słabszego, rozmnożenie gdy ten sam gatunek, ewentualnie zachowanie specjalne (ucieczka antylopy, odbicie żółwia, tarcza Człowieka).

Akcja rośliny: 5% szansy na rozsianie potomka na losowe wolne sąsiednie pole.

## człowiek i sterowanie

Sterowanie: `w` / `a` / `s` / `d` (góra, lewo, dół, prawo). Każde wciśnięcie klawisza ruchu wywołuje pełną turę i przerysowuje planszę.

Tarcza (`r`): aktywna przez 5 tur (`umiejetnosc` od 10 do 6), w tym czasie każdy atakujący jest odpychany na losowe wolne pole przez `tarczaPrzegon`. Po wygaśnięciu 5 tur cooldownu (`umiejetnosc` od 5 do 1), dopiero potem można aktywować ponownie. Ikona Człowieka zmienia się na `gracz_tarcza.png` w trakcie działania tarczy.

inne wejścia:

* menu `Plik > Zapisz`; zapis stanu świata do pliku (nazwę wpisuje się w konsoli)
* menu `Plik > Wczytaj`; wczytanie zapisu (czyści świat, ustawia turę, dodaje organizmy)
* menu `Następna tura`; pełna tura bez ruchu Człowieka
* prawy klik na komórce; menu kontekstowe dodające wybrany organizm w tym polu

## architektura

GUI w `Okno extends JFrame implements ActionListener, KeyListener`. Plansza to `JPanel` z `GridLayout(20, 20)` zawierający 400 `JLabel`, każdy z `ImageIcon`. Pod planszą `JTextArea` w `JScrollPane` na komunikaty. `JMenuBar` z menu `Plik` (Wczytaj, Zapisz) i przyciskiem `Następna tura`.

Po każdej turze `updateBoard()` woła `ustawSciezki()` (czyta `ikona` z każdego pola mapy), usuwa wszystkie etykiety z panelu i tworzy je od nowa.

Stan świata:

* `Organizm[][] mapaOrganizmow` rozmiaru `(szerokosc+1) x (wysokosc+1)`, indeksowana od 1
* `List<Organizm> listaOrganizmow`; lista do sortowania i iteracji
* `Queue<Organizm> doUsuniecia`; martwe organizmy, kasowane na początku kolejnej tury
* `Queue<Organizm> doDodania`; nowo narodzone, dołączane do listy przed kolejną turą (nie biorą udziału w turze swoich narodzin)
* `Queue<String> komunikaty`; bufor zdarzeń, opróżniany przy końcu tury

Polimorfizm przez abstrakcyjne `akcja()` i `kolizja()`; każdy gatunek to osobna klasa dziedzicząca po `Zwierze` albo `Roslina`. Konwersja symbolu na nazwę (na komunikaty) i nazwy na symbol (na menu kontekstowe) w klasie pomocniczej `Zamiana`. Losowanie kierunków i sąsiednich pól wydzielone do klasy `Losuj`, kierunki w `enum Kierunek`.

Zapis do pliku: pierwsza linia to numer tury, druga to Człowiek (`symbol x y sila wiek umiejetnosc`), kolejne to pozostałe organizmy (`symbol x y sila wiek`), zakończone linią `K`. Wczytanie czyści mapę przez `wyczyscSwiat()`, ustawia turę, czyta linijka po linijce; gdy trafi na symbol `X`, ustawia parametry istniejącego obiektu Człowieka zamiast tworzyć nowy.
