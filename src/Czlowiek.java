public class Czlowiek extends Zwierze{
    private static final String IKONA = "src/ikony/gracz.png";
    private static final String IKONA_TARCZA = "src/ikony/gracz_tarcza.png";
    private StanTarczy tarcza;
    Kierunek kierunek;
    char wejscie;
    public Czlowiek(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.CZLOWIEK, 5, 5, IKONA);
        this.kierunek=Kierunek.brak;
        this.tarcza = new TarczaGotowa(0);
        this.wejscie='1';
    }
    public Kierunek wybierzKierunekCzlowiek(char wejscie)
    {
        if (wejscie == 'a')return Kierunek.lewo;
        if (wejscie == 'd')return Kierunek.prawo;
        if (wejscie == 'w')return Kierunek.gora;
        if (wejscie == 's')return Kierunek.dol;
        return Kierunek.brak;
    }
   public void setWejscie(char c)
   {
       wejscie = c;
   }
    public void aktywujUmietnosc() {
        zmienStan(tarcza.aktywuj());
    }
    public int getUmiejetnosc() {
        return tarcza.licznik();
    }
    public void setUmiejetnosc(int umiejetnosc) {
        this.tarcza = StanTarczy.zLicznika(umiejetnosc);
    }

    private void zmienStan(StanTarczy nowy) {
        if (nowy == tarcza) return;
        tarcza = nowy;
        ikona = tarcza.chroni() ? IKONA_TARCZA : IKONA;
    }

    @Override
    public void kolizja(Organizm atakujacy) {
        if (!tarcza.chroni()) super.kolizja(atakujacy);
        else {
            swiat.tarczaPrzegon(atakujacy);
            swiat.komunikat(this, "przegania tarcza ");
        }
    }

    @Override
    protected void przedRuchem() {
        zmienStan(tarcza.poTurze(napis -> swiat.komunikat(this, napis)));
    }

    @Override
    protected koordynaty wybierzPole() {
        kierunek = wybierzKierunekCzlowiek(wejscie);
        int x1 = 0, y1 = 0;
        if (kierunek == Kierunek.brak) {
            swiat.komunikat(this, "nie moze sie ruszyc ");
            return null;
        }
        if (kierunek == Kierunek.gora) x1 = -1;
        if (kierunek == Kierunek.dol)x1 = 1;
        if (kierunek == Kierunek.lewo)y1 = -1;
        if (kierunek == Kierunek.prawo)y1 = 1;
        if (!swiat.czyRuchMozliwy(getX()+x1, getY()+y1)){swiat.komunikat(this,"Czlowiek chce wyjsc poza plansze ");return null; }
        koordynaty pole = new koordynaty();
        pole.x = getX() + x1;
        pole.y = getY() + y1;
        return pole;
    }
}
