import java.util.function.Consumer;

public record TarczaGotowa(int licznik) implements StanTarczy {
    private static final int CZAS_DZIALANIA = 10;

    @Override
    public boolean chroni() { return false; }

    @Override
    public StanTarczy aktywuj() { return new TarczaAktywna(CZAS_DZIALANIA); }

    @Override
    public StanTarczy poTurze(Consumer<String> komunikat) { return this; }
}
