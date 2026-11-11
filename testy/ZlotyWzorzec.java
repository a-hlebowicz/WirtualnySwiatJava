import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Random;

// Uruchamiac z katalogu glownego repo: java -cp out ZlotyWzorzec [sprawdz|zapisz]
public class ZlotyWzorzec {

    // w/a/s/d = ruch + tura, '-' = tura bez nowego klawisza (jak "Nastepna tura"), r = tarcza (bez tury)
    record Scenariusz(String nazwa, long ziarno, String plikStartowy, String ruchy) {}

    private static final Path KATALOG = Path.of("testy", "golden");

    private static final List<Scenariusz> SCENARIUSZE = List.of(
            new Scenariusz("ziarno1", 1, null, "wwddssaar" + "wdsa--wd".repeat(7)),
            new Scenariusz("ziarno2", 2, null, "rddddssss" + "aaw-r-wwdd".repeat(5) + "s".repeat(10)),
            new Scenariusz("ziarno3_dlugi", 3, null, "wasd-".repeat(12) + "r" + "dsaw".repeat(5)),
            new Scenariusz("test_txt", 4, "test.txt", "-----" + "r" + "wwaassdd".repeat(3)),
            new Scenariusz("tlum", 5, "testy/start_tlum.txt", "r" + "-".repeat(15) + "wasd".repeat(5))
    );

    public static void main(String[] args) throws IOException {
        boolean zapisz = args.length > 0 && args[0].equals("zapisz");
        int bledy = 0;
        for (Scenariusz s : SCENARIUSZE) {
            String wynik = uruchom(s);
            if (!wynik.equals(uruchom(s))) {
                System.out.println("NIEPOWTARZALNE " + s.nazwa() + " (dwa przebiegi z tym samym ziarnem sie roznia)");
                bledy++;
                continue;
            }
            Path plik = KATALOG.resolve(s.nazwa() + ".txt");
            if (zapisz) {
                Files.createDirectories(KATALOG);
                Files.writeString(plik, wynik, StandardCharsets.UTF_8);
                System.out.println("ZAPISANO " + plik);
            } else if (!porownaj(s.nazwa(), plik, wynik)) {
                bledy++;
            }
        }
        if (bledy > 0) {
            System.out.println("BLEDY: " + bledy);
            System.exit(1);
        }
        System.out.println(zapisz ? "Baza zapisana." : "Wszystko zgodne ze zlotym wzorcem.");
    }

    private static String uruchom(Scenariusz s) throws IOException {
        StringWriter wyjscie = new StringWriter();
        Random rand = new Random(s.ziarno());
        Swiat swiat = new Swiat(20, 20, rand);
        Czlowiek czlowiek = new Czlowiek(swiat, rand.nextInt(20), rand.nextInt(20));
        swiat.dodajCzlowieka(czlowiek);
        swiat.ustawSwiat();
        SerializatorSwiata serializator = new SerializatorSwiata();
        if (s.plikStartowy() != null) {
            try (Reader plik = Files.newBufferedReader(Path.of(s.plikStartowy()))) {
                serializator.wczytaj(swiat, czlowiek, plik);
            }
        }
        swiat.dodajObserwatora((tura, komunikaty) -> {
            wyjscie.write("== tura " + tura + "\n");
            for (String komunikat : komunikaty) wyjscie.write(komunikat + "\n");
        });
        wyjscie.write("== start\n");
        serializator.zapisz(swiat, czlowiek, wyjscie);
        for (char znak : s.ruchy().toCharArray()) {
            if (znak == 'r') {
                czlowiek.aktywujUmietnosc();
                wyjscie.write("[r]\n");
                continue;
            }
            if (znak != '-') czlowiek.setWejscie(znak);
            swiat.wykonajTure();
            serializator.zapisz(swiat, czlowiek, wyjscie);
        }
        return wyjscie.toString();
    }

    private static boolean porownaj(String nazwa, Path plik, String wynik) throws IOException {
        if (!Files.exists(plik)) {
            System.out.println("BRAK BAZY " + plik + " (uruchom z argumentem: zapisz)");
            return false;
        }
        String[] oczekiwane = Files.readString(plik, StandardCharsets.UTF_8).replace("\r\n", "\n").split("\n", -1);
        String[] aktualne = wynik.split("\n", -1);
        for (int i = 0; i < Math.max(oczekiwane.length, aktualne.length); i++) {
            String o = i < oczekiwane.length ? oczekiwane[i] : "<koniec pliku>";
            String a = i < aktualne.length ? aktualne[i] : "<koniec wyniku>";
            if (!o.equals(a)) {
                System.out.println("ROZNICA " + nazwa + " w linii " + (i + 1) + ":");
                System.out.println("  oczekiwano: " + o);
                System.out.println("  jest:       " + a);
                return false;
            }
        }
        System.out.println("OK " + nazwa);
        return true;
    }
}
