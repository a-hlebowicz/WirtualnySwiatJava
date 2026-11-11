public class Czlowiek extends Zwierze{
    int umiejetnosc;
    Kierunek kierunek;
    char wejscie;
    public Czlowiek(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.CZLOWIEK, 5, 5, "src/ikony/gracz.png");
        this.kierunek=Kierunek.brak;
        this.umiejetnosc=0;
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
        if (umiejetnosc<2) {
            umiejetnosc = 10;
            ikona = "src/ikony/gracz_tarcza.png";
        }
    }
   public int getUmiejetnosc() {
        return umiejetnosc;
    }
@Override
   public void kolizja(Organizm atakujacy) {
        if (umiejetnosc < 6) super.kolizja(atakujacy);
	else {
            swiat.tarczaPrzegon(atakujacy);
            swiat.komunikat(this, "przegania tarcza ");
        }
    }

    @Override
    public void akcja() {
        if (umiejetnosc > 1) {
            umiejetnosc--;
            String napis = "pozostalo ";
            if (umiejetnosc > 5) { napis = napis + (umiejetnosc-5); napis = napis + " tur umiejetnosci";  ikona = "src/ikony/gracz_tarcza.png";}
            else { napis = napis + (umiejetnosc); napis = napis + " do odnowienia umiejetnosci"; ikona="src/ikony/gracz.png";}

            swiat.komunikat(this, napis);
        }
        if (getWiek() == 0) {
            postarzWiek();
            return;
        }
        postarzWiek();
        kierunek = wybierzKierunekCzlowiek(wejscie);
        int x1 = 0, y1 = 0;
        if (kierunek == Kierunek.brak) {
            swiat.komunikat(this, "nie moze sie ruszyc ");
            return;
        }
        if (kierunek == Kierunek.gora) x1 = -1;
        if (kierunek == Kierunek.dol)x1 = 1;
        if (kierunek == Kierunek.lewo)y1 = -1;
        if (kierunek == Kierunek.prawo)y1 = 1;
        if (!swiat.czyRuchMozliwy(getX()+x1, getY()+y1)){swiat.komunikat(this,"Czlowiek chce wyjsc poza plansze ");return; }
        if (swiat.getMapaOrganizmow()[getX() + x1][getY() + y1] == null)swiat.przestawOrganizm(this, getX() + x1, getY() + y1);
        else {
            Organizm inny = swiat.getMapaOrganizmow()[getX() + x1][getY() + y1];
            inny.kolizja(this);
        }
    }

    public void setUmiejetnosc(int umiejetnosc) {
        this.umiejetnosc=umiejetnosc;
    }
}
