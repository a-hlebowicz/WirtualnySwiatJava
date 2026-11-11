public class Lis extends Zwierze{
    public Lis(Swiat swiat, int x, int y) {
        super(swiat, x, y, Gatunek.LIS, 3, 7, "src/ikony/lis.png");
    }


    // nie wchodzi na pole silniejszego
    @Override
    protected Kierunek wybierzKierunek()
    {
        Kierunek kierunek;
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
