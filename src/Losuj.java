import java.util.Random;

public class Losuj  {
    public static Kierunek wybierz_kierunek(Random rand){
        int a = rand.nextInt(4);
        if (a == 0) { return Kierunek.prawo; }
        if (a == 1) { return Kierunek.lewo; }
        if (a == 2) { return Kierunek.gora; }
        if (a == 3) { return Kierunek.dol; }
        return Kierunek.brak;
    }
    public static koordynaty ustaw_pozycje(Kierunek kierunek,int x,int y, int predkosc){
        koordynaty koordynaty = new koordynaty();
        if (kierunek==Kierunek.prawo){
            koordynaty.x=x+predkosc;
            koordynaty.y=y;
        }
        if (kierunek==Kierunek.lewo){
            koordynaty.x=x-predkosc;
            koordynaty.y=y;
        }
        if (kierunek==Kierunek.gora){
            koordynaty.x=x;
            koordynaty.y=y-predkosc;
        }
        if (kierunek==Kierunek.dol){
            koordynaty.x=x;
            koordynaty.y=y+predkosc;
        }
        return koordynaty;
    }
}
