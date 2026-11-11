import java.util.function.Consumer;

public record TarczaOdnawianie(int licznik) implements StanTarczy {

    @Override
    public boolean chroni() { return false; }

    @Override
    public StanTarczy aktywuj() { return this; }

    @Override
    public StanTarczy poTurze(Consumer<String> komunikat) {
        int nowy = licznik - 1;
        komunikat.accept("pozostalo " + nowy + " do odnowienia umiejetnosci");
        return StanTarczy.zLicznika(nowy);
    }
}
