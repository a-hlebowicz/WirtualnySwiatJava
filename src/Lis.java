public class Lis extends Zwierze{
    public Lis(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.LIS, 3, 7, "src/ikony/lis.png");
    }


    @Override
    public void akcja() {
        if (getWiek() == 0) {
            postarzWiek();
            return;
        }
        postarzWiek();
        Kierunek kierunek = wybierzKierunekLis();

        if (kierunek == Kierunek.brak) {
            swiat.komunikat(this, "nie moze sie ruszyc ");
            return;
        }

        koordynaty pole;
        pole = Losuj.ustaw_pozycje(kierunek,x,y,predkosc);

        if (swiat.getMapaOrganizmow()[pole.x][pole.y] == null)swiat.przestawOrganizm(this, pole.x, pole.y);
        else swiat.getMapaOrganizmow()[pole.x][pole.y].kolizja(this);
    }
    public Kierunek wybierzKierunekLis()
    {
        Kierunek kierunek;
        //if (!swiat->czySasiadujaceWolne(getX(), getY()))return kierunek;
        int i=0;
        while (true) {
            if (i >= 20)return Kierunek.brak;
            kierunek = Losuj.wybierz_kierunek(swiat.getRand());
            koordynaty pole = Losuj.ustaw_pozycje(kierunek,x,y,predkosc) ;
            if (swiat.czyRuchMozliwy(pole.x, pole.y))
                if(swiat.getMapaOrganizmow()[pole.x][pole.y]==null || swiat.getMapaOrganizmow()[pole.x][pole.y].getSila()<=sila) break;
            i++;
        }
        return kierunek;
    }

}
