public enum Gatunek {
    WILK('W', "Wilk", Wilk::new),
    OWCA('O', "Owca", Owca::new),
    ZOLW('Z', "Zolw", Zolw::new),
    ANTYLOPA('A', "Antylopa", Antylopa::new),
    LIS('L', "Lis", Lis::new),
    TRAWA('T', "Trawa", Trawa::new),
    MLECZ('M', "Mlecz", Mlecz::new),
    WILCZE_JAGODY('J', "WilczeJagody", WilczeJagody::new),
    BARSZCZ_SOSNOWSKIEGO('B', "BarszczSosnowskiego", BarszczSosnowskiego::new),
    GUARANA('G', "Guarana", Guarana::new),
    CZLOWIEK('X', "Czlowiek", Czlowiek::new);

    private final char symbol;
    private final String nazwa;
    private final FabrykaOrganizmu fabryka;

    Gatunek(char symbol, String nazwa, FabrykaOrganizmu fabryka) {
        this.symbol = symbol;
        this.nazwa = nazwa;
        this.fabryka = fabryka;
    }

    public char symbol() { return symbol; }
    public String nazwa() { return nazwa; }

    // Czlowiek jest jeden i tworzy go Main, nie menu ani rozstawienie startowe
    public boolean dodawalny() { return this != CZLOWIEK; }

    public Organizm utworz(Swiat swiat, int x, int y) {
        return fabryka.utworz(swiat, x, y);
    }

    public static Gatunek zSymbolu(char symbol) {
        for (Gatunek gatunek : values()) {
            if (gatunek.symbol == symbol) return gatunek;
        }
        return null;
    }
}
