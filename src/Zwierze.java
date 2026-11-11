public abstract class Zwierze extends Organizm{
    int predkosc;
    public Zwierze(Swiat swiat, int x, int y, Gatunek gatunek, int sila, int inicjatywa, String ikona) {
        super(swiat, x, y, gatunek, sila, inicjatywa, ikona);
        this.predkosc=1;
    }

    @Override
    public void akcja() {
        przedRuchem();
        if (getWiek() == 0) {
            postarzWiek();
            return;
        }
        postarzWiek();
        koordynaty pole = wybierzPole();
        if (pole == null) return;
        Organizm cel = swiat.getMapaOrganizmow()[pole.x][pole.y];
        if (cel == null) swiat.przestawOrganizm(this, pole.x, pole.y);
        else cel.kolizja(this);
    }

    protected void przedRuchem() {
    }

    // null = brak ruchu w tej turze (komunikat wysyla hook)
    protected koordynaty wybierzPole() {
        Kierunek kierunek = wybierzKierunek();
        if (kierunek == Kierunek.brak) {
            swiat.komunikat(this, "nie moze sie ruszyc ");
            return null;
        }
        return Losuj.ustaw_pozycje(kierunek, x, y, predkosc);
    }

    protected Kierunek wybierzKierunek() {
        int i = 0;
        Kierunek kierunek=Kierunek.brak;
        while (true) {
            if (i >= 20) return kierunek;
            kierunek = Losuj.wybierz_kierunek(swiat.getRand());
            koordynaty pole = Losuj.ustaw_pozycje(kierunek,x,y,predkosc) ;
            if (swiat.czyRuchMozliwy(pole.x, pole.y)) break;
            i++;
        }
        return kierunek;
    }

    @Override
    public void kolizja(Organizm atakujacy) {
        if (this.getSymbol() == atakujacy.getSymbol()) {
            //dziecko
            if (swiat.czySasiadujaceWolne(this.getX(), this.getY())) {
                koordynaty pole = swiat.znajdzWolnePole(this);
                int x1 = pole.x, y1 = pole.y;
                swiat.dodajOrganizm(getSymbol(), x1,  y1);
                swiat.komunikat(this, "sie rozmnozyl");
            }
            //else swiat->komunikat(this, "nie ma miejsca na rozmnazanie");


        }
	else if (atakujacy.getSila() >= this.getSila()) {
            swiat.komunikat(atakujacy, "atakuje ");
            swiat.komunikat(this, "przegrywa ");
            swiat.zastapOrganizm(atakujacy, this);
        }
	else {
            swiat.komunikat(atakujacy, "atakuje ");
            swiat.komunikat(this, "wygrywa ");
            swiat.usunOrganizm(atakujacy);
        }
    }
}
