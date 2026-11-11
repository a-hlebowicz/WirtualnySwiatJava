import java.util.function.Consumer;

public interface StanTarczy {
    int licznik();
    boolean chroni();
    StanTarczy aktywuj();
    StanTarczy poTurze(Consumer<String> komunikat);

    // licznik w pliku zapisu: 6..10 aktywna, 2..5 odnawianie, 0..1 gotowa
    static StanTarczy zLicznika(int licznik) {
        if (licznik >= 6) return new TarczaAktywna(licznik);
        if (licznik >= 2) return new TarczaOdnawianie(licznik);
        return new TarczaGotowa(licznik);
    }
}
