import java.util.Random;

public class Main {
    public static void main(String[] args) {

        Random rand = args.length > 0 ? new Random(Long.parseLong(args[0])) : new Random();
        Swiat swiat = new Swiat(20,20, rand);
        Czlowiek czlowiek = new Czlowiek(swiat,rand.nextInt(20), rand.nextInt(20));
        swiat.dodajCzlowieka(czlowiek);
        swiat.ustawSwiat();
        new Okno(swiat, czlowiek);
    }
}