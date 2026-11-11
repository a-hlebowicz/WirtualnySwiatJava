import java.util.function.Consumer;

public record TarczaAktywna(int licznik) implements StanTarczy {

    @Override
    public boolean chroni() { return true; }

    @Override
    public StanTarczy aktywuj() { return this; }

    @Override
    public StanTarczy poTurze(Consumer<String> komunikat) {
        int nowy = licznik - 1;
        if (nowy > 5) komunikat.accept("pozostalo " + (nowy - 5) + " tur umiejetnosci");
        else komunikat.accept("pozostalo " + nowy + " do odnowienia umiejetnosci");
        return StanTarczy.zLicznika(nowy);
    }
}
