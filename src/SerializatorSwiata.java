import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public class SerializatorSwiata {

    public void zapisz(Swiat swiat, Czlowiek czlowiek, Writer plik) throws IOException {
        plik.write(swiat.getTura() + "\n");
        plik.write(czlowiek.getSymbol() + " ");
        plik.write(czlowiek.getX() + " " + czlowiek.getY() + " ");
        plik.write(czlowiek.getSila() + " ");
        plik.write(czlowiek.getWiek() + " ");
        plik.write(czlowiek.getUmiejetnosc() + "\n");
        Organizm[][] mapa = swiat.getMapaOrganizmow();
        for (int i = 1; i <= swiat.getSzerokosc(); i++) {
            for (int j = 1; j <= swiat.getWysokosc(); j++) {
                Organizm organizm = mapa[i][j];
                if (organizm == null || organizm == czlowiek) continue;
                plik.write(organizm.getSymbol() + " ");
                plik.write(organizm.getX() + " " + organizm.getY() + " ");
                plik.write(organizm.getSila() + " ");
                plik.write(organizm.getWiek() + "\n");
            }
        }
        plik.write("K\n");
    }

    public void wczytaj(Swiat swiat, Czlowiek czlowiek, Reader plik) {
        Scanner skaner = new Scanner(plik);
        swiat.wyczysc(skaner.nextInt());
        while (skaner.hasNextLine()) {
            char symbol = skaner.next().charAt(0);
            if (symbol == 'K') break;
            int x = skaner.nextInt();
            int y = skaner.nextInt();
            int sila = skaner.nextInt();
            int wiek = skaner.nextInt();
            if (symbol == czlowiek.getSymbol()) {
                czlowiek.setX(x);
                czlowiek.setY(y);
                czlowiek.setSila(sila);
                czlowiek.setWiek(wiek);
                czlowiek.setUmiejetnosc(skaner.nextInt());
                swiat.dodajCzlowieka(czlowiek);
            } else {
                Organizm organizm = swiat.dodajOrganizm(symbol, x, y);
                organizm.setSila(sila);
                organizm.setWiek(wiek);
            }
        }
    }
}
